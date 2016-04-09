package org.puresoftware.chocalandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A login screen that offers login via email/password.
 */
public class JoinActivity extends AppCompatActivity {

    private static final int PICK_PHOTO_FOR_AVATAR = 5;
    private static final int CAPTURE_PHOTO_FOR_AVATAR = 14;

    /**
     * Keep track of the join task to ensure we can cancel it if requested.
     */
    private UserJoinTask mAuthTask = null;

    // UI references.
    private EditText mNameView;
    private EditText mIpView;
    private EditText mPortView;
    private View mProgressView;
    private View mJoinFormView;
    private Bitmap mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        // Set up the join form.
        mNameView = (EditText) findViewById(R.id.name);
        mIpView = (EditText) findViewById(R.id.ip);
        mPortView = (EditText) findViewById(R.id.port);

        mPortView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.join || id == EditorInfo.IME_NULL) {
                    attemptJoin();
                    return true;
                }
                return false;
            }
        });

        Button joinButton = (Button) findViewById(R.id.join_button);
        joinButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptJoin();
            }
        });

        Button avatarButton = (Button) findViewById(R.id.choose_avatar_button);
        avatarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseAvatar();
            }
        });

        Button captureButton = (Button) findViewById(R.id.capture_avatar_button);
        captureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                captureAvatar();
            }
        });

        mJoinFormView = findViewById(R.id.join_form);
        mProgressView = findViewById(R.id.join_progress);
        Chocal.setActivity(JoinActivity.this);
    }

    private void chooseAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)),
                PICK_PHOTO_FOR_AVATAR);
    }

    private void captureAvatar() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.take_photo)),
                CAPTURE_PHOTO_FOR_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            // User selected photo from gallery

            try {
                // Get the returned data
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                // Crop Avatar picture
                mAvatar = performCrop(BitmapFactory.decodeStream(inputStream));
                // Preview Avatar on image view
                refreshAvatar();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAPTURE_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            // User captured photo from camera

            // Get the returned data
            Bundle extras = data.getExtras();
            // Get the cropped bitmap
            mAvatar = performCrop((Bitmap)extras.getParcelable("data"));
            // Preview Avatar on image view
            refreshAvatar();
        }
    }

    private Bitmap performCrop(Bitmap bitmap) {
        int width = 128, height = 128;

        Bitmap croppedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
        Canvas canvas = new Canvas(croppedBitmap);
        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, transformation, paint);

        return croppedBitmap;
    }

    private void refreshAvatar() {
        // Show Avatar in image view as a rounded image
        ImageView avatarImage = (ImageView) findViewById(R.id.avatar_image);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory
                .create(getResources(), mAvatar);
        roundedBitmapDrawable.setCircular(true);
        avatarImage.setImageDrawable(roundedBitmapDrawable);
    }

    /**
     * Attempts to join to Chocal Chat.
     * If there are form errors (invalid name, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptJoin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mNameView.setError(null);
        mIpView.setError(null);
        mPortView.setError(null);

        // Store values at the time of the join attempt.
        String name = mNameView.getText().toString();
        String ip = mIpView.getText().toString();
        int port = -1;
        boolean cancel = false;
        View focusView = null;

        try {
            port = Integer.parseInt(mPortView.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            cancel = true;
        }

        String uri = "ws://" + ip + ":" + port;

        // Check for a valid port number.
        if (!isPortValid(port)) {
            mPortView.setError(getString(R.string.error_invalid_port));
            focusView = mPortView;
            cancel = true;
        }

        // Check for a valid IP.
        if (!isIpValid(ip)) {
            mIpView.setError(getString(R.string.error_invalid_ip));
            focusView = mIpView;
            cancel = true;
        }

        // Check for a valid name.
        if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt join and focus the first
            // form field with an error.
            assert focusView != null;
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user join attempt.
            showProgress(true);
            mAuthTask = new UserJoinTask(name, uri);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name) {
        return !name.isEmpty();
    }

    private boolean isIpValid(String ip) {
        return !ip.isEmpty();
    }

    private boolean isPortValid(int port) {
        return port > 1 && port < 65534;
    }

    /**
     * Shows the progress UI and hides the join form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

            mJoinFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mJoinFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mJoinFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mJoinFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Represents an asynchronous join task used to join
     * Chocal Chat.
     */
    public class UserJoinTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mUri;

        UserJoinTask(String name, String uri) {
            mName = name;
            mUri = uri;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Chocal.setUri(mUri);
            Chocal.setName(mName);
            Chocal.setAvatar(mAvatar);
            return Chocal.initWebSocket();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if(!success) {
                Snackbar.make(findViewById(R.id.name), R.string.error_cant_connect_to_chocal_server,
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

