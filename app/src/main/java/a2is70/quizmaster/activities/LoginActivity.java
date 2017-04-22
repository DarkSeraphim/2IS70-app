package a2is70.quizmaster.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.fragments.LoginFragment;
import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.database.DBInterface;
import a2is70.quizmaster.utils.JsonConverter;
import a2is70.quizmaster.utils.function.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginFormHandler {

    private DBInterface dbi;

    private View mProgressView;

    private View mLoginFormView;

    private final Semaphore loginLock = new Semaphore(1);

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
    public void onLogin(String email, String password, final Consumer<Account> consumer) {

        boolean failed = true;
        try {
            if (!loginLock.tryAcquire()) {
                failed = false;
                return;
            }
            showProgress(true);
            dbi.loadAccount(email, password).enqueue(new Callback<Account>(){

                @Override
                public void onResponse(Call<Account> c, Response<Account> r ){
                    loginLock.release();
                    switch (r.code()) {
                        case 200:
                            Account account = r.body();
                            AppContext.getInstance().setAccount(account);
                            consumer.accept(account);
                            break;
                        case 404:
                            consumer.accept(null);
                            break;
                        default:
                            throw new RuntimeException("Failed to log in: HTTP " + r.code() + " returned");
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    loginLock.release();
                    showProgress(false);
                    // TODO: handle this?
                    throw new RuntimeException(t);
                }
            });

            failed = false;
        } finally {
            if (failed) {
                showProgress(false);
                Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show();
                loginLock.release();
            }
        }
    }

    @Override
    public void onRegister(final String name, final String email, final String password, Account.Type type, final Consumer<Account> callback) {
        boolean failed = true;
        try {
            if (!loginLock.tryAcquire()) {
                failed = false;
                return;
            }
            showProgress(true);
            DBInterface dbi = AppContext.getInstance().getDBI();
            dbi.addAccount(name, email, password, type).enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    loginLock.release();
                    showProgress(false);
                    switch (response.code()) {
                        case 200:
                            break;
                        case 409:
                            callback.accept(null);
                            return;
                        default:
                            throw new RuntimeException("Invalid register request");
                    }
                    Account account = response.body();
                    AppContext.getInstance().setAccount(account);
                    callback.accept(account);
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    loginLock.release();
                    Toast.makeText(LoginActivity.this, "Could not register", Toast.LENGTH_LONG).show();
                }
            });
            failed = false;
        } finally {
            if (failed) {
                showProgress(false);
                loginLock.release();
            }
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

    public void goToOverview() {
        AppContext.getInstance().getDBI().getGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                showProgress(false);
                if (response.code() == 200) {
                    String groups = JsonConverter.toJson(response.body());
                    Intent intent = new Intent(LoginActivity.this, OverviewActivity.class);
                    intent.putExtra("groups", groups);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to fetch data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                // TODO: handle
                throw new RuntimeException("Unexpected error", t);

            }
        });

    }
}

