package com.ericsson.ixt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class Profile  extends AppCompatActivity {

    private TextView tvUsername,
            tvEmail, tvCompid,tvValidUnt,
            tvRegisterdate,tvPhonenum,tvWanum,
            tvTicketcreated,tvTicketclosed,tvprofilename,tvprofilecompany,tvprofilerole;
    private ImageView ivpicture;
    private Button logout,btsetprofile;
    private UserInfo userInfo;
    private UserSession userSession;
    private JSONArray resultUser;
    private JSONArray resultArray;
    private TextView txtName,txtAlamat,txtPt,textView4,textView,textView5,textView7,
            textView6,textView3,textView31,txtBrand,txtCreateTicket,txtTicketClose;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_n);

        userInfo        = new UserInfo(this);
        userSession     = new UserSession(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String email    = userInfo.getUserid();
        final String username = userInfo.getFname();
        final String lname    = userInfo.getLname();
        final String contact    = userInfo.getContact();
        final String cuid    = userInfo.getCuid();
        final String cuname    = userInfo.getCuname();
        final String compid   = userInfo.getCompid();
        final String compname   = userInfo.getCompname();
        final String custid   = userInfo.getCustid().toLowerCase();
        final String projid    = userInfo.getProjid().toLowerCase();
        final String joindate    = userInfo.getJoindate();
        final String lastact    = userInfo.getLastact();
        final String status   = userInfo.getStatus();
        final String usertype   = userInfo.getType();
        final String userprev    = userInfo.getPrev();
        final String userimg   = userInfo.getImg();

        //Toast.makeText(Profile.this, compid, Toast.LENGTH_LONG).show();
        btsetprofile=(Button) findViewById(R.id.btsetprofile);
        //tvUsername=(TextView) findViewById(R.id.tv) ;
        tvEmail=(TextView) findViewById(R.id.tvemail);
        tvCompid=(TextView) findViewById(R.id.tvcompid);
        tvValidUnt=(TextView) findViewById(R.id.tvvalidunt);
        tvRegisterdate=(TextView) findViewById(R.id.tvregistdate);
        tvPhonenum=(TextView) findViewById(R.id.tvphonenum);
        tvWanum=(TextView) findViewById(R.id.tvwanum);
        tvTicketcreated=(TextView) findViewById(R.id.tvticketcreated);
        tvTicketclosed=(TextView) findViewById(R.id.tvticketclosed);
        tvprofilename=(TextView) findViewById(R.id.tvprofilename);
        tvprofilecompany=(TextView) findViewById(R.id.tvprofilecompany);
        tvprofilerole=(TextView) findViewById(R.id.tvprofilerole);
        //ivpicture=(ImageView) findViewById(R.id.profilephoto);


        //txtName     = (TextView)findViewById(R.id.txtName);
        //txtAlamat   = (TextView)findViewById(R.id.txtAlamat);
        txtBrand    = (TextView)findViewById(R.id.txtBrand);
        //txtPt       = (TextView)findViewById(R.id.txtPt);
        textView     = (TextView)findViewById(R.id.textView);
        textView3     = (TextView)findViewById(R.id.textView3);
        textView4     = (TextView)findViewById(R.id.textView4);
        textView5     = (TextView)findViewById(R.id.textView5);
        textView6     = (TextView)findViewById(R.id.textView6);
        textView7     = (TextView)findViewById(R.id.textView7);
        textView31     = (TextView)findViewById(R.id.textView31);
        txtCreateTicket = (TextView)findViewById(R.id.txtCreateTicket);
        txtTicketClose  = (TextView) findViewById(R.id.txtTicketClose);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
        tvprofilename.setTypeface(face);
        tvprofilecompany.setTypeface(face);
        txtBrand.setTypeface(face);
        tvprofilecompany.setTypeface(face);
        textView.setTypeface(face);
        textView3.setTypeface(face);
        textView4.setTypeface(face);
        textView5.setTypeface(face);
        textView6.setTypeface(face);
        textView7.setTypeface(face);
        textView31.setTypeface(face);
        txtTicketClose.setTypeface(face);
        txtCreateTicket.setTypeface(face);
        tvEmail.setTypeface(face);
        tvCompid.setTypeface(face);
        tvValidUnt.setTypeface(face);
        tvRegisterdate.setTypeface(face);
        tvPhonenum.setTypeface(face);
        tvWanum.setTypeface(face);
        tvTicketclosed.setTypeface(face);
        tvTicketcreated.setTypeface(face);


        getticketcounter(email,projid,custid);
        tvprofilename.setText(username+" "+lname);
        tvprofilecompany.setText(compid +" - "+compname);
        tvprofilerole.setText(userprev);
        tvEmail.setText(email);
        tvCompid.setText(compid);
        tvValidUnt.setText(lastact);
        tvRegisterdate.setText(joindate);
        tvPhonenum.setText(contact);
        tvWanum.setText(contact);

        String imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/"+email+".jpg";
        Picasso.with(getApplicationContext()).invalidate(imageUri);
        Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
        ImageView myImage = (ImageView) findViewById(R.id.profilephoto);
        Picasso.with(getApplicationContext()).load(imageUri).into(myImage);
        Picasso.with(getApplicationContext()).invalidate(imageUri);
        Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);





/*
    File imgFile = new  File(userimg);

    if(imgFile.exists()){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        Bitmap myBitmap = BitmapFactory.decodeFile(userimg,options);


        ImageView myImage = (ImageView) findViewById(R.id.profilephoto);


        myImage.setImageBitmap(myBitmap);


    }
*/






        //This method will fetch the data from the URL
      //  getUserProfile(email);




        btsetprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Profile.this,EditProfile.class);
                intent.putExtra("Compid", compid);
                intent.putExtra("ValidUnt", lastact);
                intent.putExtra("Registerdate", joindate);
                Profile.this.startActivity(intent);
                //startActivity(new Intent(Profile.this, EditProfile.class));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            //startActivity(new Intent(this, MainActivity.class));
            onBackPressed();
            //this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getticketcounter(final String email,final String projid,final String custid){
        //Traversing through all the items in the json array
        try {

            //addlistviewinbox(email,projid,custid,ticketid);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GETTICKETCOUNTER_URL ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Toast.makeText(SentTicketDetail.this, "masuk 1", Toast.LENGTH_LONG).show();
                            JSONObject j = null;
                            try {
                                //Parsing the fetched Json String to JSON Object
                                j = new JSONObject(response);
                                //Toast.makeText(SentTicketDetail.this, "masuk 2", Toast.LENGTH_LONG).show();
                                //Storing the Array of JSON String to our JSON Array
                                resultArray = j.getJSONArray(Utils.JSON_ARRAY);

                                for(int i=0;i<resultArray.length();i++){
                                    try {
                                        //Getting json object
                                        JSONObject json = resultArray.getJSONObject(i);


                                        //Adding the name of the student to array list
                                        String ttotal=json.getString(Utils.TAG_TOTAL);
                                        String tclosed=json.getString(Utils.TAG_CLOSED);
                                        if(!ttotal.equalsIgnoreCase("null")&&!tclosed.equalsIgnoreCase("null")) {
                                            tvTicketcreated.setText(ttotal);
                                            tvTicketclosed.setText(tclosed);
                                        }else{
                                            tvTicketcreated.setText("0");
                                            tvTicketclosed.setText("0");
                                        }
                                        // Toast.makeText(SentTicketDetail.this, timestamp, Toast.LENGTH_LONG).show();
                                        //mTimelineList.add(new tickettimeline(timestamp,tflow,userid,useract,usercom));


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                //Calling method getStudents to get the students from the JSON Array
                                //getTimeline(resultArray);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(SentTicketDetail.this, ticketid + email + projid + custid, Toast.LENGTH_LONG).show();
                            // Toast.makeText(SentTicketDetail.this, "masuk 3", Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
//                Toast.makeText(SentTicketDetail.this, ticketid + email + projid + custid, Toast.LENGTH_LONG).show();
                    params.put("projid", projid);
                    params.put("custid", custid);
                    params.put("email", email);
                    return params;
                }

            };

            //Creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            // Toast.makeText(SentTicketDetail.this, "masuk", Toast.LENGTH_LONG).show();
            //Adding request to the queue
            requestQueue.add(stringRequest);




        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void onBackPressed() {

        Intent setIntent = new Intent(Profile.this,MainActivity.class);
        startActivity(setIntent);

       Profile.this.finish();

    }





}
