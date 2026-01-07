package com.leomarx.whereareyou.account.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.leomarx.whereareyou.menu.MenuActivity;
import com.leomarx.whereareyou.R;

/**
 * A login screen that offers login through email, Facebook or Google.
 */
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getName().toString();
    private static final int RC_SIGN_IN = 1;

    // UI references.
    private View mLoginFormView;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken ;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog _progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Loading");
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressDialog.setIndeterminate(true);

        // Initialize the Firebase authentication.
        mAuth = FirebaseAuth.getInstance();

        // Firebase auth listener.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Initialize FB SDK
        //This line must be put here before the  setContentView(R.layout.activity_login);
        //Or else you will get null object error
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                // AccessToken is for us to check whether we have
                // previously logged in into this app, and this information
                // is save in shared preferences and sets it during SDK initialization
                accessToken = AccessToken.getCurrentAccessToken();

                if (accessToken == null)
                {
                    Log.d(TAG, "User is not logged in.");
                }
                else
                {
                    handleFacebookSignIn(accessToken);
                    Log.d(TAG, "Logged in already.");
                    Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
                    menuActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(menuActivity);
                }
            }
        });

        MultiDex.install(this);
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();

        // Register access token to check whether user logged in before
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;
            }
        };

        // Register the Facebook sign in button.
        Button buttonSignInFacebook = (Button) findViewById(R.id.button_sign_in_facebook);
        Button buttonSignInGoogle = (Button) findViewById(R.id.button_sign_in_google);
        Button registerAccountButton = (Button) findViewById(R.id.register_user_button);
        Button signInEmailButton = (Button) findViewById(R.id.button_sign_in_email);

        // Handle Register Account Action.
        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerAccountActivity = new Intent(LoginActivity.this, RegisterAccountActivity.class);
                startActivity(registerAccountActivity);
            }
        });

        // Handle Email Sign In.
        signInEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _progressDialog.show();
                handleEmailSignIn();
            }
        });

        // Handle Facebook Sign In.
        buttonSignInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _progressDialog.show();
                callbackFacebookButton();
            }
         });

        // Handle Google Sign In.
        buttonSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _progressDialog.show();
                callbackGoogleButton();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        //Facebook Login (Activity Result)
        callbackManager.onActivityResult(requestCode, responseCode, intent);

        // Google Login (Activity Result)
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                handleGoogleSignIn(account);
            } else {
                handleInvalidLogin();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void callbackFacebookButton(){

        accessTokenTracker.startTracking();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends", "email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Once authorized from facebook will directly go to MainActivity
                        accessToken = loginResult.getAccessToken();
                        handleFacebookSignIn(accessToken);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Canceled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        handleInvalidLogin();
                    }
                });
    }

    private void callbackGoogleButton(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookSignIn(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
                            menuActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(menuActivity);
                        }
                        else
                        {
                            try {
                                Exception ex = task.getException();
                                handleInvalidLogin(ex.getMessage());
                            }catch(Exception Ex){
                                handleInvalidLogin(Ex.getMessage());
                            }
                        }
                    }
                });
    }

    private void handleEmailSignIn(){

        _progressDialog.show();

        TextView emailView = (TextView) findViewById(R.id.text_email);
        TextView passwordView = (TextView) findViewById(R.id.text_password);

        String email;
        String password;

        if(emailView == null || passwordView == null)
        {
            handleInvalidLogin();
            return;
        }

        email = emailView.getText().toString();
        password = passwordView.getText().toString();

        if(email.length() == 0 || password.length() == 0)
        {
            handleInvalidCredentials();
            return;
        }

        if(mAuth != null)
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        Intent menuActivity = new Intent(
                                LoginActivity.this, MenuActivity.class);
                        menuActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(menuActivity);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    handleInvalidLogin();
                }
            });
        }


    }

    private void handleGoogleSignIn(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String msg2 = task.getResult().toString();
                            Intent menuActivity = new Intent(
                                    LoginActivity.this, MenuActivity.class);
                            menuActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(menuActivity);
                        }
                        else
                        {
                           handleInvalidLogin();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handleInvalidLogin("INVALIDDDDD!!");
            }
        });
    }

    private void handleInvalidLogin(){
        _progressDialog.hide();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,
                "Failed to login.", duration);
        toast.show();
    }

    private void handleInvalidLogin(String errorMessage){
        _progressDialog.hide();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,
                errorMessage, duration);
        toast.show();
    }

    private void handleInvalidCredentials(){
        _progressDialog.hide();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,
                "Invalid credentials.", duration);
        toast.show();
    }

}

