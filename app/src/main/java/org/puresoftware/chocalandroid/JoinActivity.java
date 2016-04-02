package org.puresoftware.chocalandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class JoinActivity extends AppCompatActivity {

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

        Button mJoinButton = (Button) findViewById(R.id.join_button);
        mJoinButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptJoin();
            }
        });

        mJoinFormView = findViewById(R.id.join_form);
        mProgressView = findViewById(R.id.join_progress);
        Chocal.setActivity(JoinActivity.this);
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

        // TODO : Select an Avatar picture

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
    private void showProgress(final boolean show) {
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
            // TODO: Set Avatar
            Chocal.initWebSocket();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

