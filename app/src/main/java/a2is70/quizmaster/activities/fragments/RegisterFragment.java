package a2is70.quizmaster.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import a2is70.quizmaster.R;
import a2is70.quizmaster.activities.LoginFormHandler;
import a2is70.quizmaster.data.Account;

public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    // TODO: Rename and change types of parameters
    private String email;

    private String password;

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
    // TODO: Rename and change types and number of parameters
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

        mRegisterEmail = (AutoCompleteTextView) view.findViewById(R.id.register_email);
        mRegisterPassword = (EditText) view.findViewById(R.id.register_password);
        mRegisterStudentType = (RadioButton) view.findViewById(R.id.register_type_student);

        Button button = (Button) view.findViewById(R.id.register_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });

        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
            password = getArguments().getString(PASSWORD);


            // fill email field with value
            mRegisterEmail.setText(email);
            mRegisterPassword.setText(password);
        }

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void doRegister() {
        if (mListener != null) {
            String email = mRegisterEmail.getText().toString();
            String password = mRegisterPassword.getText().toString();
            Account.Type type = mRegisterStudentType.isChecked() ? Account.Type.STUDENT : Account.Type.TEACHER;
            mListener.onRegister(email, password, type);
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
