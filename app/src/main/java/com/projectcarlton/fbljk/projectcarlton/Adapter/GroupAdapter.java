package com.projectcarlton.fbljk.projectcarlton.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {

    private ArrayList<Group> data;
    private Context context;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView txtName;
        TextView txtDescription;
    }

    public GroupAdapter(ArrayList<Group> data, Context context) {
        super(context, R.layout.grouplist_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group dataItem = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.grouplist_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.groupitem_name);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.groupitem_desc);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataItem.groupName);
        viewHolder.txtDescription.setText(dataItem.groupDescription);

        return result;
    }

}