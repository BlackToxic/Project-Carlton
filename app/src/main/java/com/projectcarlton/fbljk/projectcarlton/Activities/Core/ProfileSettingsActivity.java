package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIExceptionType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIPostImageRequest;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.io.File;

public class ProfileSettingsActivity extends AppCompatActivity implements APIUtilCallback {

    private LinearLayout progressbarLayout;
    private ConstraintLayout generalLayout;
    private ImageView userImg;
    private EditText userNameTxt;
    private EditText userEmailTxt;
    private Button saveButton;

    private APIUtil apiUtil;
    private Bitmap newUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        setTitle("Profileinstellungen");

        apiUtil = new APIUtil(getApplicationContext(), this);

        progressbarLayout = (LinearLayout) findViewById(R.id.profilesetting_progressbar_layout);
        generalLayout = (ConstraintLayout) findViewById(R.id.profilesetting_generallayout);
        userImg = (ImageView)findViewById(R.id.profilesetting_userimage);
        userNameTxt = (EditText) findViewById(R.id.profilesetting_usernameTxt);
        userEmailTxt = (EditText) findViewById(R.id.profilesetting_emailTxt);
        saveButton = (Button) findViewById(R.id.profilesetting_saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingsActivity.this);
                builder.setMessage("Bild aufnehmen oder auswählen?").setPositiveButton("Auswählen", dialogClickListener).setNegativeButton("Aufnehmen", dialogClickListener);
                builder.create().show();
            }
        });

        if (SettingsCache.CURRENTUSER != null) {
            userNameTxt.setText(SettingsCache.CURRENTUSER.userName);
            userEmailTxt.setText(SettingsCache.CURRENTUSER.userEmail);

            apiUtil.getImageAsync(SettingsCache.CURRENTUSER.userPhoto);
        }
    }

    private void save() {
        if (newUserImage != null && userNameTxt.getText() != null && !userNameTxt.getText().toString().equals("") && userEmailTxt.getText() != null && !userEmailTxt.getText().toString().equals("")) {
            apiUtil.uploadProfileImage(newUserImage, SettingsCache.CURRENTUSER.userId, userNameTxt.getText().toString(), userEmailTxt.getText().toString());
        } else if (userNameTxt.getText() != null && !userNameTxt.getText().toString().equals("") && userEmailTxt.getText() != null && !userEmailTxt.getText().toString().equals("")) {
            apiUtil.updateUserAsync(SettingsCache.CURRENTUSER.userId, userNameTxt.getText().toString(), userEmailTxt.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SettingsCache.saveSettings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    userImg.setImageURI(selectedImage);
                }

                break;

            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        newUserImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        userImg.setImageBitmap(newUserImage);
                    } catch (Exception e) {

                    }
                }

                break;
        }
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.DOWNLOADIMAGE_CALLBACK) {
            if (result != null && result instanceof Bitmap) {
                userImg.setImageBitmap((Bitmap)result);
            }
        } else if (callbackType == CallbackType.UPLOADIMAGE_CALLBACK) {
            if (result != null && result instanceof String) {
                SettingsCache.CURRENTUSER.userName = userNameTxt.getText().toString();
                SettingsCache.CURRENTUSER.userEmail = userEmailTxt.getText().toString();

                ActivityCallbacks.request(ActivityCallbackType.RELOADPROFILE_CALLBACK);
                onBackPressed();
            } else if (result != null && result instanceof APIException) {
                APIException exception = (APIException)result;
                int errorCode = exception.getType();

                if (errorCode == APIExceptionType.EXISTING_EMAIL) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_existing_email, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else if (errorCode == APIExceptionType.EXISTING_USERNAME) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_existing_username, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else if (errorCode == APIExceptionType.INVALID_EMAIL) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registerresponse_invalid_email, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.API_ERROR, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    }
}