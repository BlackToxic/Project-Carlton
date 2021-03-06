package com.projectcarlton.fbljk.projectcarlton.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.GroupActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.Helpers.APIUtil;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class InviteUsersAdapter extends ArrayAdapter<User> implements View.OnClickListener, APIUtilCallback {

    private ArrayList<User> data;
    private Context context;
    private int lastPosition = -1;
    private APIUtil apiUtil;

    private static class ViewHolder {
        int position;
        TextView txtName;
        Button inviteButton;
    }

   public InviteUsersAdapter(ArrayList<User> data, Context context) {
       super(context, R.layout.inviteuserlist_item, data);
       this.data = data;
       this.context = context;
       apiUtil = new APIUtil(context, this);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       User dataItem = getItem(position);
       ViewHolder viewHolder;

       final View result;

       if (convertView == null) {
           viewHolder = new ViewHolder();
           LayoutInflater inflater = LayoutInflater.from(getContext());
           convertView = inflater.inflate(R.layout.inviteuserlist_item, parent, false);
           viewHolder.txtName = (TextView) convertView.findViewById(R.id.inviteuserlistitem_name);
           viewHolder.inviteButton = (Button) convertView.findViewById(R.id.inviteuserlistitem_invitebutton);

           result = convertView;

           convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder)convertView.getTag();
           result = convertView;
       }

       Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
       result.startAnimation(animation);
       lastPosition = position;

       viewHolder.position = position;
       viewHolder.txtName.setText(dataItem.userName);
       viewHolder.inviteButton.setOnClickListener(this);
       viewHolder.inviteButton.setTag(viewHolder);

       return result;
   }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        User user = (User) getItem(viewHolder.position);

        switch (view.getId()) {
            case R.id.inviteuserlistitem_invitebutton:
                apiUtil.inviteUserAsync(SettingsCache.CURRENTGROUP.groupId, SettingsCache.CURRENTUSER.userId, user.userId);

                viewHolder.inviteButton.setText(R.string.inviteusers_alreadyinvited_text);
                viewHolder.inviteButton.setBackgroundColor(Color.WHITE);
                viewHolder.inviteButton.setEnabled(false);

                break;
        }
    }

    @Override
    public void callback(int callbackType, Object result) {
        if (callbackType == CallbackType.INVITEUSER_CALLBACK) {

        }
    }
}