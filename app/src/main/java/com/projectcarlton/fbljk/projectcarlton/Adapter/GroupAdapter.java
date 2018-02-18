package com.projectcarlton.fbljk.projectcarlton.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.ActivityCallbacks.ActivityCallbacks;
import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private ArrayList<Group> data;
    private Context context;
    private int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtName;
        TextView txtDescription;
        ImageView imgGroup;

        ViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.grouplist_card);
            this.txtName = (TextView)itemView.findViewById(R.id.grouplist_groupname);
            this.txtDescription = (TextView)itemView.findViewById(R.id.grouplist_groupdesc);
            this.imgGroup = (ImageView) itemView.findViewById(R.id.grouplist_groupphoto);
        }
    }

    public GroupAdapter(ArrayList<Group> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouplist_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Group group = data.get(position);

        if (group != null) {
            holder.txtName.setText(group.groupName);
            holder.txtDescription.setText(group.groupDescription);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCallbacks.request(ActivityCallbackType.GROUPOPEN_CALLBACK, group);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}