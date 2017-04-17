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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.List;
import java.util.zip.Inflater;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.fragments.LoginFragment;
import a2is70.quizmaster.activities.fragments.RegisterFragment;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.database.DBInterface;
import a2is70.quizmaster.utils.function.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            "Stan@student.tue.nl:student4:s", "Tom@student.tue.nl:student6:s",
            "a:a:t"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private DBInterface dbi;
    private View mProgressView;
    private View mLoginFormView;
    private LayoutInflater inflater;

    public LoginActivity() {
        // Empty constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbi=AppContext.getInstance().getDBI();

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
        mAuthTask = new LoginActivity.UserLoginTask(email, password,passwordView);
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onRegister(final String email, final String password, Account.Type type) {
        if (mAuthTask != null) {
            return;
        }
        showProgress(true);

        dbi.addAccount(email,password).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                mAuthTask = new LoginActivity.UserLoginTask(email, password,null);
                mAuthTask.execute((Void) null);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                final View view = inflater.inflate(R.layout.fragment_register,null);
                EditText v;
                v = (EditText)view.findViewById(R.id.register_name);
                v.setError("Could not register");
            }
        });


    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private EditText mPasswordView;

        UserLoginTask(String email, String password, EditText e) {
            mEmail = email;
            mPassword = password;
            mPasswordView = e;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final Responseboolean rb = new Responseboolean();
            final Object lock = new Object();
            // TODO: attempt authentication against a network service.
            dbi.loadAccount(mEmail,mPassword).enqueue(new Callback<Account>(){

                @Override
                public void onResponse(Call<Account> c,Response<Account> r ){
                    AppContext.getInstance().setAccount(r.body());
                    rb.result = true;
                    synchronized(lock){
                        lock.notify();
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    //Stuff went wrong.
                    rb.result = false;
                    lock.notify();
                }

            });

            try {
                synchronized(lock){
                    lock.wait();
                }
            }catch (Exception e){

            }

            return rb.result;

            /*
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
            */

            // TODO: register the new account here.


        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startActivity(new Intent(LoginActivity.this, OverviewActivity.class));
            } else if(mPasswordView!=null){
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }else if(mPasswordView==null){
                final View view = inflater.inflate(R.layout.fragment_register,null);
                mPasswordView = (EditText)view.findViewById(R.id.register_name);
                mPasswordView.setError("Could not login");
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

    public class Responseboolean{
        public boolean result;

    }

}

