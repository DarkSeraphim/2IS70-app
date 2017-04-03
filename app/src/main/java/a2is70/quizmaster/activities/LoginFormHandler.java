package a2is70.quizmaster.activities;

import a2is70.quizmaster.data.Account;
import android.widget.EditText;

/**
 * Created by s129977 on 26-3-2017.
 */
public interface LoginFormHandler {
    void onLogin(String email, String password, EditText passwordView);

    void onRegister(String email, String password, Account.Type type);
}
