package com.akp.savarn.footerpage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.savarn.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public ProfileListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public ProfileListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_profile_post, viewGroup, false);
        ProfileListAdapter.VH viewHolder = new ProfileListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.VH vh, int i) {
        vh.tv1.setText(arrayList.get(i).get("ViewCount"));

        Picasso.get().load(arrayList.get(i).get("PostImage")).into(vh.ivImage);

//        vh.tv2.setText(arrayList.get(i).get("notification"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView tv1;
        ImageView ivImage;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            ivImage = itemView.findViewById(R.id.ivImage);
//            tv3= itemView.findViewById(R.id.tv3);
//            tv4= itemView.findViewById(R.id.tv4);

        }
    }
}

