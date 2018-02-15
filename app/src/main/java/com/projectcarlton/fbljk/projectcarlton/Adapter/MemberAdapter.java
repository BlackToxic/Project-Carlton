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

import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.Activities.GroupActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter<User> implements View.OnClickListener {

    private ArrayList<User> data;
    private Context context;
    private int lastPosition = -1;

    private static class ViewHolder {
        int position;
        TextView txtName;
        Button deleteButton;
    }

    public MemberAdapter(ArrayList<User> data, Context context) {
        super(context, R.layout.memberlist_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User dataItem = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.memberlist_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.memberlistitem_name);
            viewHolder.deleteButton = (Button) convertView.findViewById(R.id.memberlistitem_deletebutton);

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
        viewHolder.deleteButton.setOnClickListener(this);
        viewHolder.deleteButton.setTag(viewHolder);

        if (!GroupActivity.isUserAdmin())
            viewHolder.deleteButton.setVisibility(View.GONE);

        return result;
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        User user = (User) getItem(viewHolder.position);

        String apiUrl = "";
        APIGetRequest request;
        switch (view.getId()) {
            case R.id.memberlistitem_deletebutton:

                break;
        }
    }
}