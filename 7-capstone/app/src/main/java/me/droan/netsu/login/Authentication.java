package me.droan.netsu.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import me.droan.netsu.R;

/**
 * Created by Drone on 13/09/16.
 */

public class Authentication implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Authentication";

    private Context context;

    public Authentication(Context context) {

        this.context = context;
    }

    public GoogleApiClient create() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(
                        context.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .enableAutoManage((SignInActivity) context, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(context, R.string.play_service_error, Toast.LENGTH_SHORT).show();

    }
}
