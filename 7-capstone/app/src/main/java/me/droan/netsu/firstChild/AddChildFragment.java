package me.droan.netsu.firstChild;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.droan.netsu.MainActivity;
import me.droan.netsu.R;
import me.droan.netsu.common.Constants;
import me.droan.netsu.common.MappingHelper;
import me.droan.netsu.common.Utils;
import me.droan.netsu.model.Child;

/**
 * Created by Drone on 17/09/16.
 */

public class AddChildFragment extends BottomSheetDialogFragment {
    private static final int RC_PHOTO_PICKER = 2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    @BindView(R.id.childNameEditTxt)
    EditText childNameEditTxt;
    @BindView(R.id.addChildBtn)
    Button addChildBtn;
    private Uri photoUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_child, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.addProfilePicture, R.id.addChildBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addProfilePicture:
                launchPhotoPicker();
                break;
            case R.id.addChildBtn:
                addChild();
                break;
        }
    }

    private void addChild() {
        final String childName = childNameEditTxt.getText().toString().trim();
        if (!childName.equals("")) {
            if (photoUri != null) {
                updateStorage(childName);
            } else {
                updateDatabase(null, childName);
            }
        }
    }

    private void updateStorage(final String childName) {
        StorageReference photoRef = storageReference.child(Constants.PATH_PROFILE_PHOTOS).child(Utils.generatePushId(databaseReference));
        //TODO match photo name with child Id
        photoRef.putFile(photoUri)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        updateDatabase(downloadUrl, childName);
                    }
                });
    }

    private void updateDatabase(Uri downloadUrl, String childName) {
        final Child child = new Child(childName, databaseReference.push().getKey(), downloadUrl, null, null);
        HashMap<String, Object> addChildMap = MappingHelper.addNewChild(child, databaseReference);
        databaseReference.updateChildren(addChildMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                if (getActivity() instanceof AddFirstChild) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }

        });
    }

    private void launchPhotoPicker() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(i.createChooser(i, getString(R.string.create_action_using)), RC_PHOTO_PICKER);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {
                photoUri = data.getData();
            }
        }
    }
}
