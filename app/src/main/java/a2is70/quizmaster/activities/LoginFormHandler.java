package a2is70.quizmaster.activities;

import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.utils.function.Consumer;

import android.widget.EditText;

/**
 * Created by s129977 on 26-3-2017.
 */
public interface LoginFormHandler {
    /**
     * Authenticates an existing user against the provided email and password parameters
     * @param email Email of the user
     * @param password Password used by the user during the registration phase
     * @param callback Callback which will be called with the {@code Account} this email/password
     *                 combination belongs to. Will be null if the email/password combination does
     *                 not exist.
     */
    void onLogin(String email, String password, Consumer<Account> callback);

    /**
     * Creates a new user
     *
     * @param name Name of the user
     * @param email Email of the user (used for login &amp; password reset)
     * @param password Password of the user
     * @param type Account type, whether the user is a teacher or student
     * @param callback Callback which will be called with the created {@code Account}.
     *                  Null if the account could not be created (f.e. duplicate email)
     */
    void onRegister(String name, String email, String password, Account.Type type, Consumer<Account> callback);
}
