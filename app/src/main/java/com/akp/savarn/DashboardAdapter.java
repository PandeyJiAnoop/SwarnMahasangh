package com.akp.savarn;

/*

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    private AlertDialog alertDialog;
    String get_Userid,get_ProjectId,get_Projectsize;
    private int totalamount;
    private PopupWindow popupWindow;
    LinearLayout ll;
    ViewGroup viewGroup;
    String getVideoid,getUserid;
    String getdash_vname,getdash_vdes,getdash_vview,getdash_vdate,getdash_vid,getdash_vvimage,getdash_vtime,getdash_vvideoid,getdash_vcatid;
    private int like =0;

    public DashboardAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public DashboardAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_youtube_dash, viewGroup, false);
        DashboardAdapter.VH viewHolder = new DashboardAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.VH vh, final int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("MemberName"));
        vh.des_tv1.setText(arrayList.get(i).get("Dis"));
        vh.des_tv.setText(arrayList.get(i).get("PostDate"));
        vh.commnet_count_tv.setText(" View "+arrayList.get(i).get("CommentCount")+" Comments");
        vh.user_name_tv.setText(arrayList.get(i).get("CategoryName"));
        vh.like_tv.setText("Useful "+arrayList.get(i).get("LikeCount"));
        vh.view_tv.setText(arrayList.get(i).get("ViewCount")+" View");


        vh.pots_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPost(arrayList.get(i).get("user_id"),arrayList.get(i).get("PostID"),arrayList.get(i).get("UserID"));
                Intent intent=new Intent(context, FullImagePage.class);
                intent.putExtra("Path",arrayList.get(i).get("PostPhoto"));
                context.startActivity(intent);
            }
        });




        if(arrayList.get(i).get("IsLike").equalsIgnoreCase("1"))
        {
            vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_245));
        }
        else {
            vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24));
        }

        vh.like_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (like == 0) {
                    vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_24));
                    like++;
                    LikeDislikePost(arrayList.get(i).get("user_id"),arrayList.get(i).get("PostID"),arrayList.get(i).get("UserID"));
                } else if (like == 1) {
                    vh.like_img.setBackground(context.getResources().getDrawable(R.drawable.ic_baseline_thumb_up_245));
                    like = 0;
                    LikeDislikePost(arrayList.get(i).get("user_id"),arrayList.get(i).get("PostID"),arrayList.get(i).get("UserID"));
                } }
        });


        if (arrayList.get(i).get("PostPhoto").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("PostPhoto")).error(R.drawable.noimage).into(vh.pots_img);
        }

        if (arrayList.get(i).get("ProfilePhoto").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("ProfilePhoto")).error(R.drawable.noimage).into(vh.user_profile_img);
        }
        if (arrayList.get(i).get("ProfilePhoto").equalsIgnoreCase("")){
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("ProfilePhoto")).error(R.drawable.noimage).into(vh.comment_profile);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv, des_tv, commnet_count_tv,user_name_tv,like_tv,view_tv,des_tv1;
        ImageView pots_img,more_img,like_img;
        CircleImageView user_profile_img,comment_profile;
        LinearLayout like_ll;

        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            des_tv1= itemView.findViewById(R.id.des_tv1);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
            commnet_count_tv = itemView.findViewById(R.id.commnet_count_tv);
            like_tv=itemView.findViewById(R.id.like_tv);
            view_tv = itemView.findViewById(R.id.view_tv);
            user_profile_img = itemView.findViewById(R.id.user_profile_img);
            comment_profile = itemView.findViewById(R.id.comment_profile);
            pots_img = itemView.findViewById(R.id.pots_img);
//            video_time_tv=itemView.findViewById(R.id.video_time_tv);
            like_img=itemView.findViewById(R.id.like_img);
            like_ll=itemView.findViewById(R.id.like_ll);
//            viewGroup = itemView.findViewById(android.R.id.content);
        }
    }

   public void LikeDislikePost(String username,String postid,String friendno) {
       StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"LikeDislikePost", new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
               String jsonString = response;
               jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
               jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
               jsonString = jsonString.replace("</string>", " ");
               Log.d("test", jsonString);
               try {
                   JSONObject object = new JSONObject(jsonString);
                   String status = object.getString("Status");
                   if (status.equalsIgnoreCase("true")) {
//                       notifyDataSetChanged();
//                       Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(context, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String, String> params = new HashMap<>();
               params.put("UserName", username);
               params.put("PostID", postid);
               params.put("FriendID", friendno);
               return params;
           }
       };
       stringRequest.setRetryPolicy(new DefaultRetryPolicy(
               30000,
               DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
               DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       RequestQueue requestQueue = Volley.newRequestQueue(context);
       requestQueue.add(stringRequest);

   }




    public void ViewPost(String username,String postid,String friendno) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.BaseURL+"ViewPost", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
//                        Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, object.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserName", username);
                params.put("PostID", postid);
                params.put("FriendID", friendno);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

*/

//}