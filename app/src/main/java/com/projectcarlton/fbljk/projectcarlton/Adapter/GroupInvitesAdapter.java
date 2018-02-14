package com.projectcarlton.fbljk.projectcarlton.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Activities.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Data.Invite;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class GroupInvitesAdapter extends ArrayAdapter<Invite> implements View.OnClickListener, APICallback {

    private ArrayList<Invite> data;
    private Context context;
    private int lastPosition = -1;

    private static class ViewHolder {
        int position;
        TextView txtName;
        TextView txtSender;
        Button checkButton;
        Button uncheckButton;
    }

    public GroupInvitesAdapter(ArrayList<Invite> data, Context context) {
        super(context, R.layout.groupinvites_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Invite dataItem = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.groupinvites_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.inviteitem_name);
            viewHolder.txtSender = (TextView) convertView.findViewById(R.id.inviteitem_sender);
            viewHolder.checkButton = (Button) convertView.findViewById(R.id.inviteitem_check_button);
            viewHolder.uncheckButton = (Button) convertView.findViewById(R.id.inviteitem_uncheck_button);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.position = position;
        viewHolder.txtName.setText(dataItem.groupName);
        viewHolder.txtSender.setText(dataItem.senderName);
        viewHolder.checkButton.setOnClickListener(this);
        viewHolder.checkButton.setTag(viewHolder);
        viewHolder.uncheckButton.setOnClickListener(this);
        viewHolder.uncheckButton.setTag(viewHolder);

        return result;
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Invite invite = (Invite) getItem(viewHolder.position);

        String apiUrl = "";
        APIGetRequest request;
        switch (view.getId()) {
            case R.id.inviteitem_check_button:
                apiUrl = context.getString(R.string.API_URL) + "invite?inviteid=" + invite.inviteId + "&userid=" + GroupsActivity.currentUser.userId + "&accept=1";
                request = new APIGetRequest(this, CallbackType.ACCEPTINVITE_CALLBACK, 5000);
                request.execute(apiUrl);

                viewHolder.uncheckButton.setVisibility(View.GONE);
                viewHolder.checkButton.setEnabled(false);

                break;

            case R.id.inviteitem_uncheck_button:
                apiUrl = context.getString(R.string.API_URL) + "invite?inviteid=" + invite.inviteId + "&userid=" + GroupsActivity.currentUser.userId + "&accept=0";
                request = new APIGetRequest(this, CallbackType.REJECTINVITE_CALLBACK, 5000);
                request.execute(apiUrl);

                viewHolder.checkButton.setVisibility(View.GONE);
                viewHolder.uncheckButton.setEnabled(false);

                break;
        }
    }

    @Override
    public void callback(int callbackType, String resultString) {
        if (callbackType == CallbackType.ACCEPTINVITE_CALLBACK) {
            ActivityCallbacks.request(ActivityCallbackType.GROUPRELOAD_CALLBACK);
        } else if (callbackType == CallbackType.REJECTINVITE_CALLBACK) {

        }
    }
}