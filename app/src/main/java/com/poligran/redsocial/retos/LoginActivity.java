package com.poligran.redsocial.retos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    /* Azure AD v2 Configs */
    final static String[] SCOPES = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* UI & Debugging Variables */
    private static final String TAG = LoginActivity.class.getSimpleName();
    Button callGraphButton;


    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private IAuthenticationResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        callGraphButton = findViewById(R.id.callGraph);

        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });


        /* Configure your sample app and save state for this activity */
        sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    R.raw.auth_config);


        /* Attempt to get a user and acquireTokenSilent
         * If this fails we do an interactive request
         */
        sampleApp.getAccounts(new PublicClientApplication.AccountsLoadedCallback() {
            @Override
            public void onAccountsLoaded(final List<IAccount> accounts) {

                if (!accounts.isEmpty()) {
                    /* This sample doesn't support multi-account scenarios, use the first account */
                    sampleApp.acquireTokenSilentAsync(SCOPES, accounts.get(0), getAuthSilentCallback());
                } else {
                    /* No accounts or >1 account */
                }
            }
        });
    }

    //
    // Core Identity methods used by MSAL
    // ==================================
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // onSignOutClicked() - Signs account out of the app & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    //

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
    }

    /* Clears an account's tokens from the cache.
     * Logically similar to "sign out" but only signs out of this app.
     * User will get interactive SSO if trying to sign back-in.
     */
    private void onSignOutClicked() {
        /* Attempt to get a user and acquireTokenSilent
         * If this fails we do an interactive request
         */
        sampleApp.getAccounts(new PublicClientApplication.AccountsLoadedCallback() {
            @Override
            public void onAccountsLoaded(final List<IAccount> accounts) {

                if (accounts.isEmpty()) {
                    /* No accounts to remove */

                } else {
                    for (final IAccount account : accounts) {
                        sampleApp.removeAccount(
                                account,
                                new PublicClientApplication.AccountsRemovedCallback() {
                            @Override
                            public void onAccountsRemoved(Boolean isSuccess) {
                                if (isSuccess) {
                                    /* successfully removed account */
                                } else {
                                    /* failed to remove account */
                                }
                            }
                        });
                    }
                }

                updateSignedOutUI();
            }
        });
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {return;}

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                updateGraphUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    //
    // Helper methods manage UI updates
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    /* Sets the graph response */
    private void updateGraphUI(JSONObject graphResponse) {

    }

    /* Set the UI for successful token acquisition data */
    private void updateSuccessUI() {
        callGraphButton.setVisibility(View.INVISIBLE);
        intent();
    }

    /* Set the UI for signed out account */
    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);



        Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                .show();
    }

    //
    // App callbacks for MSAL
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the authResult */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                updateSuccessUI();
                intent();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
                /* User cancelled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }


    public void intent () {
        Intent i = new Intent(LoginActivity.this, Home.class);
        startActivity(i);
        finish();
    }





}
