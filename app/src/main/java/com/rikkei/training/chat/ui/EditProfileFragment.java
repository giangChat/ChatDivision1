package com.rikkei.training.chat.ui;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rikkei.training.chat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    MainActivity mainActivity;
    ImageView butBackProfile;
    TextView tvSaveInfo;
    CircleImageView imgUpdateUser;
    ImageView imgCamera;
    EditText edName;
    EditText edNumber;
    EditText edBirthday;
    private final int REQUEST_CODE_GALLERY = 123;
    private final int REQUEST_CODE_CAMERA = 125;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference riversRef;
    Uri imgUri;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        riversRef = storageReference.child("imagesAvt/").child(user.getUid() + ".jpg");
        databaseReference = firebaseDatabase.getReference().child("user").child(user.getUid());
        databaseReference.child("imgUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("default")) {
                    Glide.with(mainActivity).load(snapshot.getValue()).into(imgUpdateUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainActivity, "Error Loading...", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference.child("birthDay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("default")) {
                    edBirthday.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("fullName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edName.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("numberPhone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue().toString().equals("default")) {
                    edNumber.setText(snapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        butBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setFragment(new ProfileFragment(), true);
                mainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(mainActivity).create();
                alertDialog.setTitle("Allow access to camera or photo gallery");
                alertDialog.setIcon(R.drawable.camera);
                alertDialog.setButton2("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aksPermissionAndCapture();
                        openGallery();
                    }
                });
                alertDialog.setButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aksPermissionAndCamera();
                        openCamera();
                    }
                });
                alertDialog.show();
            }
        });
        tvSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri != null) {
                    updateAvt(imgUri);
                    databaseReference.child("fullName").setValue(edName.getText().toString().trim());
                    databaseReference.child("birthDay").setValue(edBirthday.getText().toString().trim());
                    databaseReference.child("numberPhone").setValue(edNumber.getText().toString().trim());
                } else {
                    databaseReference.child("fullName").setValue(edName.getText().toString().trim());
                    databaseReference.child("birthDay").setValue(edBirthday.getText().toString().trim());
                    databaseReference.child("numberPhone").setValue(edNumber.getText().toString().trim());
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imgUpdateUser.setImageBitmap(bitmap);
            imgUri = data.getData();
        }
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            imgUpdateUser.setImageURI(imgUri);
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(mainActivity, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case REQUEST_CODE_GALLERY: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(mainActivity, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void aksPermissionAndCapture() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        } else {
            int readPermission = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
                mainActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        }
    }

    public void aksPermissionAndCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        } else {
            int camera = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA);
            if (camera != PackageManager.PERMISSION_GRANTED) {
                mainActivity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA);
            }
        }
    }

    public void updateAvt(Uri uri) {
        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child("imgUrl").setValue(uri.toString());
                        Toast.makeText(mainActivity, "Updated!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
//        Uri file=Uri.fromFile(new File("imagesAvt/"+user.getUid()+".jpg"));
//        uploadTask = riversRef.putFile(file);
//
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                Log.d("Thuc", riversRef.getDownloadUrl().toString());
//                // Continue with the task to get the download URL
//                return riversRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                } else {
//                    // Handle failures
//                    // ...
//                }
//            }
//        });

    }

    public void init(View view) {
        mainActivity = (MainActivity) getActivity();
        butBackProfile = view.findViewById(R.id.imgBack);
        tvSaveInfo = view.findViewById(R.id.tvSaveInfoUser);
        imgUpdateUser = view.findViewById(R.id.imgUpdateUser);
        imgCamera = view.findViewById(R.id.imgCamera);
        edName = view.findViewById(R.id.edFullName);
        edNumber = view.findViewById(R.id.edPhoneNumber);
        edBirthday = view.findViewById(R.id.edBirthday);
    }
}
