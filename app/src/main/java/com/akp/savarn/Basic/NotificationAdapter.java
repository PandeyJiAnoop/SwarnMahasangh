package com.akp.savarn.Basic;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.savarn.R;

import java.util.HashMap;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public NotificationAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public NotificationAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_noti, viewGroup, false);
        NotificationAdapter.VH viewHolder = new NotificationAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.VH vh, int i) {
        vh.tv1.setText(arrayList.get(i).get("NotificationDis"));
//        vh.tv2.setText(arrayList.get(i).get("notification"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tv4;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
//            tv2 = itemView.findViewById(R.id.tv2);
//            tv3= itemView.findViewById(R.id.tv3);
//            tv4= itemView.findViewById(R.id.tv4);

        }
    }
}



