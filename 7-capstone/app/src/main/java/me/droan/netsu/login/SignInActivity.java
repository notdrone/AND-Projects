package me.droan.netsu.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.droan.netsu.MainActivity;
import me.droan.netsu.R;
import me.droan.netsu.common.Constants;
import me.droan.netsu.common.Utils;
import me.droan.netsu.firstChild.AddFirstChild;
import me.droan.netsu.model.User;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    private ProgressDialog authProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        googleApiClient = new Authentication(this).create();

    }

    @OnClick(R.id.signInButton)
    public void onClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        authProgressDialog = createProgressDialog();
        authProgressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {

                            //TODO add to user node
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            addUserToFirebase(task);
                        }
                    }
                });
    }

    private void addUserToFirebase(@NonNull Task<AuthResult> task) {
        AuthResult result = task.getResult();
        String uid = result.getUser().getUid();
        String displayName = result.getUser().getDisplayName();
        String email = result.getUser().getEmail();
        Uri photoUrl = result.getUser().getPhotoUrl();
        String eid = Utils.convertEmailToUid(email);
        User user = new User(uid, displayName, email, photoUrl != null ? photoUrl.toString() : "");
        DatabaseReference usersRef = ref.child(Constants.PATH_USERS);
        usersRef.child(eid).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //user added to db
                //now check if first login
                checkIfZeroChildren();

            }
        });
    }

    private void checkIfZeroChildren() {
        String eid = Utils.getEid();
        if (eid != null) {
            ref.child(Constants.PATH_FAMILIES).child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object exists = dataSnapshot.getValue();
                    authProgressDialog.dismiss();
                    if (exists != null) {
                        openMainActivity();
                    } else {
                        openAddNewChildActivity();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void openAddNewChildActivity() {
        startActivity(new Intent(SignInActivity.this, AddFirstChild.class));
        finish();
    }

    private void openMainActivity() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public ProgressDialog createProgressDialog() {
        ProgressDialog authProgressDialog = new ProgressDialog(this);
        authProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        authProgressDialog.setMessage(getString(R.string.progress_dialog_signing_in));
        authProgressDialog.setCancelable(false);
        return authProgressDialog;
    }
}
