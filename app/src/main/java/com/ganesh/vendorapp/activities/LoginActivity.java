package com.ganesh.vendorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ganesh.vendorapp.R;
import com.ganesh.vendorapp.api.RetrofitClient;
import com.ganesh.vendorapp.models.LoadingDialog;
import com.ganesh.vendorapp.models.LoginResponse;
import com.ganesh.vendorapp.models.OtpResponse;
import com.ganesh.vendorapp.storage.UsersSharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNo, inputPin;
    private Button btnLogin, btnSendOtp;
    private LoadingDialog loadingDialog;

    private ImageView googleLogin, facebookLogin;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private String otpPin=null;
    private String uid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.main_layout_login).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    InputMethodManager inputMethodManager = (InputMethodManager) LoginActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        loadingDialog = new LoadingDialog(LoginActivity.this);

        inputPhoneNo=findViewById(R.id.inputPhoneNo);
        inputPin=findViewById(R.id.inputPin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLogin = findViewById(R.id.googleLogin);
        googleLogin.setOnClickListener(view -> {
            signInWithGoogle();
        });

        callbackManager = CallbackManager.Factory.create();

        facebookLogin = findViewById(R.id.facebookLogin);
        facebookLogin.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });
        });

        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener((View v)->{
            signInWithPhoneNumber();
        });

        btnSendOtp=findViewById(R.id.otpSend);
        btnSendOtp.setOnClickListener(view -> {
            sendOTP();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(UsersSharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void signInWithPhoneNumber(){
        String pin = inputPin.getText().toString().trim();

        if(pin.isEmpty()){
            inputPin.setError("Required Pin No.");
            inputPin.requestFocus();
            return;
        }
        // change it into not equals.
        if(pin.equals(otpPin)){

            inputPhoneNo.setError("Required Valid Pin");
            inputPhoneNo.requestFocus();
            return;

        }else{
            if(uid != null){

                loadingDialog.startLoadingDialog();
                Call<LoginResponse> call = RetrofitClient.getInstance().getApi().getUser(uid);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        loadingDialog.dismissDialog();
                        LoginResponse loginResponse = response.body();

                        if(!loginResponse.isErr()){

                            //save user in sheared preference.
                            UsersSharedPrefManager.getInstance(LoginActivity.this)
                                    .saveUser(loginResponse.getUser());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {
                            inputPhoneNo.setError(loginResponse.getMsg());
                            inputPhoneNo.requestFocus();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }else{
                String phone_no = inputPhoneNo.getText().toString().trim();
                UsersSharedPrefManager.getInstance(LoginActivity.this).setLoginWith("mobile",phone_no);
                Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        }
        return;
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithFacebook(AccessToken accessToken){
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try{

                    String fbId = object.getString("id");
                    String fbName = object.getString("first_name") +" "+ object.getString("last_name");
                    String fbMail = object.getString("email");

                    loadingDialog.startLoadingDialog();
                    Call<LoginResponse> call = RetrofitClient.getInstance().getApi().getUserbyemail(fbId,fbMail);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse loginResponse = response.body();
                            loadingDialog.dismissDialog();

                            if(loginResponse.isErr()){
                                UsersSharedPrefManager.getInstance(LoginActivity.this).setLoginWith(fbId,"facebook",fbMail,fbName);
                                Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                                startActivity(intent);
                            }else {

                                //save user in sheared preference.
                                UsersSharedPrefManager.getInstance(LoginActivity.this)
                                        .saveUser(loginResponse.getUser());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {

                        }
                    });

                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken!=null){
                signInWithFacebook(currentAccessToken);
            }
        }
    };



    private void sendOTP(){
        String phone_no = inputPhoneNo.getText().toString().trim();

        if(phone_no.isEmpty()){
            inputPhoneNo.setError("Required Phone Number");
            inputPhoneNo.requestFocus();
            return;
        }

        if(phone_no.length() != 10 ||
                !(Character.getNumericValue(phone_no.charAt(0)) < 10 && Character.getNumericValue(phone_no.charAt(0)) > 5) ){
            inputPhoneNo.setError("Required Valid Phone Number");
            inputPhoneNo.requestFocus();
            return;
        }
        inputPhoneNo.setEnabled(false);
        btnSendOtp.setEnabled(false);
        btnSendOtp.setText("Sending...");
        inputPin.requestFocus();



        Call<OtpResponse> call = RetrofitClient.getInstance().getApi().getOtp("+91"+phone_no);

        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                OtpResponse otpResponse = response.body();
                if(!otpResponse.isErr()){

                    otpPin = otpResponse.getOtp();
                    uid = otpResponse.getUid();

                }else{
                    inputPhoneNo.setEnabled(true);
                    btnSendOtp.setEnabled(true);
                    btnSendOtp.setText("Send OTP");
                    inputPhoneNo.setError("Sending Failed");
                    inputPhoneNo.requestFocus();
                    System.out.println(otpResponse.getMsg());
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                inputPhoneNo.setEnabled(true);
                btnSendOtp.setEnabled(true);
                btnSendOtp.setText("Send OTP");
                inputPhoneNo.setError("Sending Failed");
                inputPhoneNo.requestFocus();
                System.out.println(t.getMessage());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {

                loadingDialog.startLoadingDialog();
                Call<LoginResponse> call = RetrofitClient.getInstance().getApi().getUserbyemail(account.getId(),account.getEmail());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse loginResponse = response.body();
                        loadingDialog.dismissDialog();

                        if(loginResponse.isErr()){
                            UsersSharedPrefManager.getInstance(LoginActivity.this).setLoginWith("google",account.getEmail(),account.getDisplayName());
                            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                            startActivity(intent);
                        }else {

                            //save user in sheared preference.
                            UsersSharedPrefManager.getInstance(LoginActivity.this)
                                    .saveUser(loginResponse.getUser());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}