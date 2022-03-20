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
import android.util.Log;
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

import com.rikkei.training.chat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private View view;
    MainActivity mainActivity;
    ImageView butBackProfile;
    TextView tvSaveInfo;
    CircleImageView imgUpdateUser;
    ImageView imgCamera;
    EditText edName;
    EditText edNumber;
    EditText edBirthday;
    private final int REQUEST_CODE_GALLERY=123;
    private final int REQUEST_CODE_CAMERA=125;

    public static Fragment newInstance() {

        Bundle args = new Bundle();

        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.edit_profile_fragment,container,false);
        init();
        butBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getFragment(new ProfileFragment());
                mainActivity.getBottomNavigationView().setVisibility(View.VISIBLE);
            }
        });
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog=new AlertDialog.Builder(mainActivity).create();
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
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imgUpdateUser.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imgUpdateUser.setImageURI(imageUri);
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Giang",permissions.toString());
        switch (requestCode){
            case REQUEST_CODE_CAMERA:{
                if(grantResults.length>1
                &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else{
                    Toast.makeText(mainActivity,"Permission Denied",Toast.LENGTH_LONG).show();
                }
                break;
            }
            case REQUEST_CODE_GALLERY:{
                if(grantResults.length>1
                &&grantResults[0]==PackageManager.PERMISSION_GRANTED
                &&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }else{
                    Toast.makeText(mainActivity,"Permission Denied",Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public void aksPermissionAndCapture(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }else{
            int readPermission= ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission=ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(writePermission!= PackageManager.PERMISSION_GRANTED||readPermission!=PackageManager.PERMISSION_GRANTED){
                mainActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        }
    }

    public void aksPermissionAndCamera(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }else{
            int camera=ActivityCompat.checkSelfPermission(mainActivity,Manifest.permission.CAMERA);
            if(camera!= PackageManager.PERMISSION_GRANTED){
                mainActivity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA);
            }
        }
    }

    public void init(){
        mainActivity= (MainActivity) getActivity();
        butBackProfile=view.findViewById(R.id.butBackProfile);
        tvSaveInfo=view.findViewById(R.id.tvSaveInfoUser);
        imgUpdateUser=view.findViewById(R.id.imgUpdateUser);
        imgCamera=view.findViewById(R.id.imgCamera);
        edName=view.findViewById(R.id.edName);
        edNumber=view.findViewById(R.id.edNumber);
        edBirthday=view.findViewById(R.id.edBirthday);

    }
}
