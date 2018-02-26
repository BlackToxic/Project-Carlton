package com.projectcarlton.fbljk.projectcarlton.Activities.Core;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity implements APIUtilCallback, ActivityCallback {

    private ImageView userImageView;
    private Button logoutButton;
    private Button settingsButton;
    private TextView pushDesc;
    private TextView userNameTxt;
    private TextView userEmailTxt;
    private Switch pushSwitch;

    private APIUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profil");

        apiUtil = new APIUtil(getApplicationContext(), this);
        ActivityCallbacks.registerActivityCallback(this, ActivityCallbackType.RELOADPROFILE_CALLBACK);

        userImageView = (ImageView) findViewById(R.id.profile_userimage);
        logoutButton = (Button) findViewById(R.id.profile_logoutbutton);
        settingsButton = (Button) findViewById(R.id.profile_settings);
        pushDesc = (TextView) findViewById(R.id.profile_pushdesc);
        userNameTxt = (TextView) findViewById(R.id.profile_username);
        userEmailTxt = (TextView) findViewById(R.id.profile_useremail);
        pushSwitch = (Switch) findViewById(R.id.profile_pushtoggle);

        pushSwitch.setChecked(SettingsCache.SHOWPUSHNOTIFICATIONS);
        setPushDesc(SettingsCache.SHOWPUSHNOTIFICATIONS);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_POSITIVE:
                                apiUtil.getLogoutAsync(SettingsCache.CURRENTUSER.userId);
                                SettingsCache.logout();

                                SharedPreferences pref = getSharedPreferences("PROJECTCARLTON_PREF", MODE_PRIVATE);
                                pref.edit().clear().apply();

                                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                                intent.putExtra("finish", true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Wirklich abmelden?").setPositiveButton(R.string.button_yes, dialogClickListener).setNegativeButton(R.string.button_no, dialogClickListener);
                builder.create().show();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SettingsCache.SHOWPUSHNOTIFICATIONS = b;
                setPushDesc(b);
            }
        });

        if (SettingsCache.CURRENTUSER != null) {
            userNameTxt.setText(SettingsCache.CURRENTUSER.userName);
            userEmailTxt.setText(SettingsCache.CURRENTUSER.userEmail);

            apiUtil.getImageAsync(SettingsCache.CURRENTUSER.userPhoto);
        }
    }

    private void setPushDesc(boolean show) {
        if (show)
            pushDesc.setText("An");
        else
            pushDesc.setText("Aus");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SettingsCache.saveSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityCallbacks.deregisterActivityCallback(this, ActivityCallbackType.RELOADPROFILE_CALLBACK);
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.DOWNLOADIMAGE_CALLBACK) {
            if (result != null && result instanceof Bitmap) {
                userImageView.setImageBitmap((Bitmap)result);
            }
        }
    }

    @Override
    public void callbackActivity(int activityCallbackType, Object... options) {
        if (activityCallbackType == ActivityCallbackType.RELOADPROFILE_CALLBACK) {
            userNameTxt.setText(SettingsCache.CURRENTUSER.userName);
            userEmailTxt.setText(SettingsCache.CURRENTUSER.userEmail);

            apiUtil.getImageAsync(SettingsCache.CURRENTUSER.userPhoto);
        }
    }
}
