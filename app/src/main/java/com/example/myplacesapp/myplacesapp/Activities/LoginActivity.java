package com.example.myplacesapp.myplacesapp.Activities;

import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myplacesapp.myplacesapp.R;
import com.example.myplacesapp.myplacesapp.SharedClasses.AlertDialogManager;
import com.example.myplacesapp.myplacesapp.SharedClasses.ControllerConstants;
import com.example.myplacesapp.myplacesapp.SharedClasses.ControllerFunctions;
import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

public class LoginActivity extends AppCompatActivity {
    final int REQUEST_CODE_FSQ_CONNECT = 100;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 101;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ControllerFunctions.isNetworkAvailable(LoginActivity.this)) {
                    Intent intent = FoursquareOAuth.getConnectIntent(LoginActivity.this, ControllerConstants.FoursquareClintID);
                    startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
                }
                else
                {
                    new AlertDialogManager() {
                        @Override
                        public void onPositiveButtonClickListener() {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }

                        @Override
                        public void onNegativeButtonClickListener() {

                        }
                    }.showConfirmationDialog(LoginActivity.this,"Network Settings","Network is not enabled. Do you want to go to settings menu?",getString(R.string.settings),getString(R.string.cancel));

                }
            }
        });
        // getting GPS status
        LocationManager locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isGPSEnabled&&!isNetworkEnabled)
        {
           new AlertDialogManager() {
               @Override
               public void onPositiveButtonClickListener() {
                   Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                   startActivity(intent);
               }

               @Override
               public void onNegativeButtonClickListener() {

               }
           }.showConfirmationDialog(this,getString(R.string.GPS_settings),getString(R.string.do_you_want_enable_gps),getString(R.string.settings),getString(R.string.cancel));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                onCompleteConnect(resultCode, data);
                break;
            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                onCompleteTokenExchange(resultCode, data);
                break;
        }
    }

    private void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            // Success.
            String code = codeResponse.getCode();
            performTokenExchange(code);

        } else {
            if (exception instanceof FoursquareCancelException) {
                // Cancel.
                Snackbar.make(btnLogin, R.string.login_canceled, Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();

            } else if (exception instanceof FoursquareDenyException) {
                // Deny.
                Snackbar.make(btnLogin, R.string.login_denied, Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();

            } else if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = exception.getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                Snackbar.make(btnLogin, errorMessage + " " + errorCode, Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();

            } else {
                // Error.
                Snackbar.make(btnLogin, exception.getMessage(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        }
    }

    private void onCompleteTokenExchange(int resultCode, Intent data) {
        AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
        Exception exception = tokenResponse.getException();

        if (exception == null) {
            String accessToken = tokenResponse.getAccessToken();
            // Success.
            // Persist the token for later use. In this example, we save
            // it to shared prefs.
            ControllerFunctions.persistPreferences(LoginActivity.this, ControllerConstants.TOKEN, accessToken);
            Intent intent = new Intent(LoginActivity.this, NearbyPlacesActivity.class);
            startActivity(intent);
        } else {
            if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = ((FoursquareOAuthException) exception).getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                Snackbar.make(btnLogin, errorMessage + " " + errorCode, Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            } else {
                // Other exception type.
                Snackbar.make(btnLogin, exception.getMessage(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        }
    }

    private void performTokenExchange(String code) {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(this, ControllerConstants.FoursquareClintID, ControllerConstants.FoursquareClintSecret, code);
        startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);

    }
}
