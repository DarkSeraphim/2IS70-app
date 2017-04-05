package a2is70.quizmaster.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.LoginFormHandler;
import a2is70.quizmaster.data.Account;

public class RegisterFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    // TODO: Rename and change types of parameters
    private String email;
    private String password;
    private EditText mRegisterName;
    private AutoCompleteTextView mRegisterEmail;
    private EditText mRegisterPassword;
    private LoginFormHandler mListener;
    private RadioButton mRegisterStudentType;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @param password Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance(String email, String password) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        args.putString(PASSWORD, password);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
            password = getArguments().getString(PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mRegisterName = (EditText) view.findViewById(R.id.register_name);
        mRegisterEmail = (AutoCompleteTextView) view.findViewById(R.id.register_email);
        mRegisterPassword = (EditText) view.findViewById(R.id.register_password);
        mRegisterStudentType = (RadioButton) view.findViewById(R.id.register_type_student);

        // Set focus on name field and show keyboard
        mRegisterName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        Button registerButton = (Button) view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        // After password view, focus on radio buttons and hide keyboard
        mRegisterPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_NEXT) {
                    mRegisterStudentType.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mRegisterPassword.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
            password = getArguments().getString(PASSWORD);

            // fill email field with values from LoginActivity
            mRegisterEmail.setText(email);
            mRegisterPassword.setText(password);
        }

        return view;

    }

    public void attemptRegister() {
        // Reset errors
        mRegisterName.setError(null);
        mRegisterEmail.setError(null);
        mRegisterPassword.setError(null);
        mRegisterStudentType.setError(null);

        // Store values at the time of the login attempt.
        String name = mRegisterName.getText().toString();
        String email = mRegisterEmail.getText().toString();
        String password = mRegisterPassword.getText().toString();
        Account.Type type = mRegisterStudentType.isChecked() ? Account.Type.STUDENT : Account.Type.TEACHER;

        boolean cancel = false;
        View focusView = null;

        // Check if text fields are valid
        if(TextUtils.isEmpty(name)) {
            mRegisterName.setError(getString(R.string.error_field_required));
            focusView = mRegisterName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mRegisterEmail.setError(getString(R.string.error_field_required));
            focusView = mRegisterEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mRegisterPassword.setError(getString(R.string.error_field_required));
            focusView = mRegisterPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Pass the submitted values to the listener to check them
            if (mListener != null) {
                mListener.onRegister(email, password, type);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFormHandler) {
            mListener = (LoginFormHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
