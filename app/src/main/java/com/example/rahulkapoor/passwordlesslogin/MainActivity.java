package com.example.rahulkapoor.passwordlesslogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * created by Rahul Kapoor 22/07/17;
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1001;
    private ImageView ivUserImage;
    private Button btnGoogleSignout, btnRevokeAccess;
    private SignInButton btnGoogleSignin;
    private TextView tvUserName, tvUserEmail;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInResult googleSignInResult;
    private String userPhoto;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to request for users data and email;
        //use request Email to ask for user email;
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        buildGoogleApiClient();

        init();


    }

    /**
     * check for users cached credentials;
     * if valid google signinresult is made available instantly;
     */
    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        //for silent signin ;
        if (optionalPendingResult.isDone()) {

            Toast.makeText(this, "Cached Signin", Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = optionalPendingResult.get();
            signinIntentHandling(result);

        } else {

            showProgressDialog();
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull final GoogleSignInResult result) {
                    hideProgressDialog();
                    signinIntentHandling(result);
                }
            });


        }

    }

    /**
     * google+ signin
     */
    private void signIn() {
        //start activity for result intent passing google api client to it;
        Intent signinIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        //passed with request code;
        startActivityForResult(signinIntent, RC_SIGN_IN);


    }

    /**
     * google+ signout
     */
    private void signOut() {
//google plus signed out;
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull final Status status) {
                updateUI(false);
            }
        });

    }

    /**
     * completely revokes or cancels the access from google+
     */
    private void revokeAccess() {

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull final Status status) {
                updateUI(false);
            }
        });

    }


    /**
     * @param requestCode request code
     * @param resultCode  result code
     * @param data        intent;
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //result returned after launching the google+ signin intent;
        if (requestCode == RC_SIGN_IN) {
            //to get result from intent;
            googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signinIntentHandling(googleSignInResult);

        }

    }

    /**
     * handling google signin result
     *
     * @param result result from intent;
     */
    private void signinIntentHandling(final GoogleSignInResult result) {
        if (result.isSuccess()) {
            //signed in with success;
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            tvUserName.setText(googleSignInAccount.getDisplayName());
            tvUserEmail.setText(googleSignInAccount.getEmail());
            if (googleSignInAccount.getPhotoUrl() != null) {
                userPhoto = googleSignInAccount.getPhotoUrl().toString();

                //set user image using glide;
                Glide.with(getApplicationContext()).load(userPhoto).override(72, 72).centerCrop().into(ivUserImage);
            }

            updateUI(true);
        } else {
            //failure in signing in;
            updateUI(false);
        }

    }

    /**
     * initialised;
     */
    private void init() {
//view inits;
        ivUserImage = (ImageView) findViewById(R.id.iv_userImage);
        btnGoogleSignin = (SignInButton) findViewById(R.id.btn_sign_in);
        btnGoogleSignin.setSize(SignInButton.SIZE_STANDARD);
        btnGoogleSignout = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        tvUserEmail = (TextView) findViewById(R.id.tv_userEmail);
        tvUserName = (TextView) findViewById(R.id.tv_userName);
        //setting up onClickListeners;
        btnGoogleSignin.setOnClickListener(this);
        btnGoogleSignout.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);
    }

    /**
     * google api client build;
     */
    private void buildGoogleApiClient() {
        //enable auto manage requires context of activity or frag, on connection failed listener;
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }


    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();

    }

    /**
     * to update UI according to the signin result;
     *
     * @param val boolean
     */
    private void updateUI(boolean val) {

        if (val) {
            btnGoogleSignin.setVisibility(View.GONE);
            btnGoogleSignout.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
        } else {
            btnGoogleSignin.setVisibility(View.VISIBLE);
            btnGoogleSignout.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
        }
    }

    /**
     * handling on click;
     *
     * @param v view;
     */
    @Override
    public void onClick(final View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }

    }

    /**
     * show progress dialog;
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    /**
     * hide progress dialog;
     */
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

}
