package com.leomarx.whereareyou.account.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.leomarx.whereareyou.R;
import com.leomarx.whereareyou.menu.MenuActivity;
import com.leomarx.whereareyou.utils.InputManager;

/**
 * Allows the user to register a new account.
 * Created by Ricardo Mota on 18/12/2016.
 */
public class RegisterAccountActivity extends Activity {

    // Layout inputs.
    private TextInputLayout _layoutUserName;
    private TextInputLayout _layoutUserEmail;
    private TextInputLayout _layoutUserPassword;
    private TextInputLayout _layoutUserConfirmPassword;

    // Input values.
    private String _emailAddress;
    private String _userName;
    private String _password;
    private String _confirmPassword;

    private ProgressDialog _progressDialog;

    /**
     * The activity creation callback.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        Button registerAccountButton = (Button) findViewById(R.id.register_account_register_button);
        Button cancelButton = (Button) findViewById(R.id.register_account_cancel_button);

        _layoutUserName = (TextInputLayout) findViewById(R.id.text_name_input_layout);
        _layoutUserEmail = (TextInputLayout) findViewById(R.id.text_email_input_layout);
        _layoutUserPassword = (TextInputLayout) findViewById(R.id.text_password_input_layout);
        _layoutUserConfirmPassword = (TextInputLayout) findViewById(R.id.text_confirm_password_input_layout);

        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Creating Account...");
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressDialog.setIndeterminate(true);

        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRegisterAccount();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

    }

    /**
     * Sets the user account input values.
     */
    private void setInputValues() {

        EditText nameText = (EditText) findViewById(R.id.register_account_text_name);
        EditText emailText = (EditText) findViewById(R.id.register_account_text_email);
        EditText passwordText = (EditText) findViewById(R.id.register_account_text_password);
        EditText confirmPasswordText = (EditText) findViewById(R.id.register_account_text_confirm_password);

        _userName = nameText.getText().toString();
        _emailAddress = emailText.getText().toString();
        _password = passwordText.getText().toString();
        _confirmPassword = confirmPasswordText.getText().toString();
    }

    /**
     * Check if user register inputs are valid.
     */
    private boolean validUserInputs() {

        boolean isValid = true;

        if (!InputManager.isValidString(_userName)) {
            _layoutUserName.setError("Invalid user name");
            _layoutUserName.setErrorEnabled(true);
            isValid = false;
        } else {
            _layoutUserName.setErrorEnabled(false);
        }

        if (!InputManager.isValidEmailAddress(_emailAddress)) {
            _layoutUserEmail.setError("Invalid user email");
            _layoutUserEmail.setErrorEnabled(true);
            isValid = false;
        } else {
            _layoutUserEmail.setErrorEnabled(false);
        }

        if (!InputManager.isValidString(_password, 6)) {
            _layoutUserPassword.setError("Invalid password (min. 6 char)");
            _layoutUserPassword.setErrorEnabled(true);
            isValid = false;
        } else {
            _layoutUserPassword.setErrorEnabled(false);
        }

        if (!InputManager.isValidString(_confirmPassword, 6)) {
            _layoutUserConfirmPassword.setError("Invalid password (min. 6 char)");
            _layoutUserConfirmPassword.setErrorEnabled(true);
            isValid = false;
        } else {
            _layoutUserConfirmPassword.setErrorEnabled(false);
        }

        if (InputManager.isValidString(_password, 6) && InputManager.isValidString(_confirmPassword, 6)) {
            if (!_password.equals(_confirmPassword)) {
                _layoutUserConfirmPassword.setError("Passwords do not match");
                _layoutUserConfirmPassword.setErrorEnabled(true);
                isValid = false;
            } else {
                _layoutUserConfirmPassword.setErrorEnabled(false);
            }
        }

        return isValid;

    }

    /**
     * Tries to register new user account.
     */
    private void tryRegisterAccount(){

        setInputValues();
        boolean validInputs = validUserInputs();

        if (validInputs) {

            _progressDialog.show();

            try {
                final FirebaseAuth _auth = FirebaseAuth.getInstance();

                if (_auth != null) {
                    _auth.createUserWithEmailAndPassword(_emailAddress, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser _user = _auth.getCurrentUser();

                            if (_user != null) {

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(_userName)
                                        //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                        .build();

                                _user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    _auth.signInWithEmailAndPassword(_emailAddress,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if(task.isSuccessful()){
                                                                handleAccountCreated();
                                                            }else{
                                                                handleAccountFail();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            handleAccountFail();
                                                        }
                                                    });

                                                } else {
                                                    try {
                                                        _auth.getCurrentUser().delete();
                                                    } catch (Exception Ex) {
                                                        handleAccountFail();
                                                    }
                                                }
                                            }
                                        });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleAccountFail();
                        }
                    });
                }

            } catch (Exception Ex) {
                handleAccountFail();
            }
        }
    }

    private void handleAccountFail(){
        _progressDialog.hide();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,
                "Failed to create account.", duration);
        toast.show();
    }

    private void handleAccountCreated(){
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,
                "Account created!", duration);
        toast.show();

        Intent menuActivity = new Intent(
                RegisterAccountActivity.this, MenuActivity.class);
        startActivity(menuActivity);
        _progressDialog.hide();
    }

}
