package a2is70.quizmaster.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.fragments.LoginFragment;
import a2is70.quizmaster.activities.fragments.RegisterFragment;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.utils.function.Consumer;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginFormHandler {
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "w@tue.nl:foo:s",
            "Jasper@tue.nl:teacher2:t", "Mark@student.tue.nl:student2:s",
            "Maurits@student.tue.nl:student1:s", "Thijs@student.tue.nl:student3:s",
            "Stan@student.tue.nl:student4:s", "Tom@student.tue.nl:student6:s"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;


    private View mProgressView;
    private View mLoginFormView;

    public LoginActivity() {
        // Empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Populate login frame with LoginFragment
        if (savedInstanceState == null) {
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.login_frame, loginFragment).commit();
        }

        // Set up the login form.
        mLoginFormView = findViewById(R.id.login_frame);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onLogin(String email, String password, EditText passwordView) {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);
        mAuthTask = new LoginActivity.UserLoginTask(email, password, passwordView);
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onRegister(String email, String password, Account.Type type) {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);

        // TODO: have an actual registration here
        return;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private EditText mPasswordView;

        UserLoginTask(String email, String password, EditText passwordView) {
            mEmail = email;
            mPassword = password;
            mPasswordView = passwordView;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    if(pieces[1].equals(mPassword)){

                        // @todo Replace dummy account with the real deal
                        switch (pieces[2]) {
                            case "s":
                                Account dummyAccountS = new Account(666, "John Doe The Student", Account.Type.STUDENT, pieces[0]);
                                AppContext.getInstance().setAccount(dummyAccountS);
                                break;
                            case "t":
                                Account dummyAccountT = new Account(666, "John Doe The Teacher", Account.Type.TEACHER, pieces[0]);
                                AppContext.getInstance().setAccount(dummyAccountT);
                                break;
                        }
                        return true;
                    }
                }
            }

            // TODO: register the new account here.

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startActivity(new Intent(LoginActivity.this, OverviewActivity.class));
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}

