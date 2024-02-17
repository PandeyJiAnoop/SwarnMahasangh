package com.akp.savarn;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.VH> {
    private final SharedPreferences sharedPreferences;
    String userid;
    Context context;
    List<HashMap<String,String>> arrayList;
    String getcommetid,getUserid;

    public ChatListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        sharedPreferences = context.getSharedPreferences("login_preference", MODE_PRIVATE);
        userid = sharedPreferences.getString("username", "");

    }
    @NonNull
    @Override
    public ChatListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_chat, viewGroup, false);
        ChatListAdapter.VH viewHolder = new ChatListAdapter.VH(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.VH vh, int i) {
        vh.username.setText(arrayList.get(i).get("UserName"));
//        vh.username.setText(arrayList.get(i).get("UserName")+"("+arrayList.get(i).get("ondate")+")");
        vh.message.setText(arrayList.get(i).get("Comment"));

        getcommetid=arrayList.get(i).get("ChId");
        getUserid=arrayList.get(i).get("userId");


        if (arrayList.get(i).get("ProfilePic").equalsIgnoreCase("")) {
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        } else {
            Glide.with(context).load(arrayList.get(i).get("ProfilePic")).error(R.drawable.noimage).into(vh.user_pic);
        }


//        getVideoId=arrayList.get(i).get("TaskId");

     /*   if (getUserid.equalsIgnoreCase(userid)){
            vh.delet.setVisibility(View.VISIBLE);
            vh.delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"DeleteComment", new  Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            String jsonString = response;
                            jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
                            jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">"," ");
                            jsonString = jsonString.replace("</string>"," ");
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                String msg = jsonobject.getString("Msg");
                                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                                if (jsonObject.getString("status").equalsIgnoreCase("0")){
                                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    arrayList.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, arrayList.size());
                                }

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                            Log.d("myTag", "message:"+error);
                            Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("ChId",arrayList.get(i).get("ChId"));
//                            params.put("VideoId",getVideoId);
                            params.put("UserId",arrayList.get(i).get("userId"));
                            params.put("Type","ELEARNINGTASK");
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                }
            });
        }*/




    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView message,username;
        CircleImageView user_pic;
        ImageView delet;
        public VH(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message);
            user_pic=itemView.findViewById(R.id.user_pic);
            username=itemView.findViewById(R.id.username);
            delet=itemView.findViewById(R.id.delet);
        }}


}
