package com.ericsson.ixt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

/**
 * Created by ericsson on 11/15/2017.
 */

public class SentTicketDetail extends AppCompatActivity {

    private Button btchat,bthistory;
    private UserInfo userInfo;
    private UserSession userSession;
    private TextView tvticketid,
            tvsiteid, tvsitename,tvpicname,
            tvrequestoption,tvsubmittime;
    private EditText etmsg;
    private List<tickettimeline> mTimelineList;
    private JSONArray resultNotif;

    private TextView tvtflowuserid,tvtimestamp,tveventcomment;

    private JSONArray resultArray;

    private LinearLayout parentLinearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail_out_b);
        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
/*
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        for(int i =0;i<20;i++){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.fieldn, null);
            // Add the new row before the add field button.
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);

        }
*/

        userInfo        = new UserInfo(this);
        userSession     = new UserSession(this);

        if(!userSession.isUserLoggedin()){
            startActivity(new Intent(this, Login.class));
            finish();
        }


        btchat=(Button) findViewById(R.id.btchat);
       // bthistory=(Button) findViewById(R.id.bthistory);
       // tvpicname=(TextView) findViewById(R.id.tvpicname);
       // final String picname=tvpicname.getText().toString();

        Intent repeating_intent = getIntent();
        final String account_id  = repeating_intent.getStringExtra("account_id");
        final String project_id = repeating_intent.getStringExtra("project_id");
        final String t_status = repeating_intent.getStringExtra("t_status");
        if(account_id!=null&&project_id!=null){
            userInfo.setCustid(account_id);
            userInfo.setProjid(project_id);
            if(t_status!=null){
                int t_stat=Integer.parseInt(t_status);
                if(t_stat>8){
                    Toast.makeText(SentTicketDetail.this, "This Ticket No longer on this status", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SentTicketDetail.this, MainActivity.class));
                }
            }
        }


        mTimelineList = new ArrayList<>();
        final String username = userInfo.getFname();
        final String email    = userInfo.getUserid();
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
        checkUpdate(email);

        tvticketid=(TextView) findViewById(R.id.tvticketid);
        tvsiteid=(TextView) findViewById(R.id.tvsiteid);
        tvsitename=(TextView) findViewById(R.id.tvsitename);
        tvpicname=(TextView) findViewById(R.id.tvpicname);
        tvrequestoption=(TextView) findViewById(R.id.tvreqopt);
        tvsubmittime=(TextView) findViewById(R.id.tvsubmittime);
       // etmsg=(EditText) findViewById(R.id.etmsgout);

        tvtimestamp=(TextView) findViewById(R.id.tvTimestamp);
        tvtflowuserid=(TextView) findViewById(R.id.tvtflowuserid);
        tveventcomment=(TextView) findViewById(R.id.tvactivitycomment);




        Intent intent = getIntent();
        final String ticketid = intent.getStringExtra("ticketid");
        final String siteid = intent.getStringExtra("siteid");
        final String sitename = intent.getStringExtra("sitename");
        final String picname = intent.getStringExtra("picname");
        final String requestoption = intent.getStringExtra("requestoption");
        final String message = intent.getStringExtra("message");
        final String submittime = intent.getStringExtra("submittime");

        //addlistviewinbox(email,projid,custid,ticketid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GETTIMELINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(SentTicketDetail.this, "masuk 1", Toast.LENGTH_LONG).show();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                           // Toast.makeText(SentTicketDetail.this, "masuk 2", Toast.LENGTH_LONG).show();
                            //Storing the Array of JSON String to our JSON Array
                            resultArray = j.getJSONArray(Utils.JSON_ARRAY);

                            for(int i=0;i<resultArray.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = resultArray.getJSONObject(i);

                                    //Adding the name of the student to array list
                                    String timestamp=json.getString(Utils.TAG_TLTIMESTAMP);
                                    //Toast.makeText(SentTicketDetail.this, timestamp, Toast.LENGTH_LONG).show();
                                    String tflow=json.getString(Utils.TAG_TLTFLOW);
                                    String tid=json.getString(Utils.TAG_TLTID);
                                    String userid=json.getString(Utils.TAG_TLUSERID);
                                    String useract=json.getString(Utils.TAG_TLUSERACT);
                                    String usercom=json.getString(Utils.TAG_TLUSERCOM);

                                    // Toast.makeText(SentTicketDetail.this, timestamp, Toast.LENGTH_LONG).show();
                                    mTimelineList.add(new tickettimeline(timestamp,tflow,userid,useract,usercom,tid));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            //Calling method getStudents to get the students from the JSON Array
                            //getTimeline(resultArray);




                            parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
                            for(int i =0;i<mTimelineList.size();i++){
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View rowView = inflater.inflate(R.layout.fieldn, null);
                                tvtimestamp=(TextView)rowView.findViewById(R.id.tvTimestamp);
                                tvtflowuserid=(TextView)rowView. findViewById(R.id.tvtflowuserid);
                                tveventcomment=(TextView)rowView. findViewById(R.id.tvactivitycomment);
                                tvtimestamp.setText(mTimelineList.get(i).getTimestamp());
                                tvtflowuserid.setText(mTimelineList.get(i).getTid()+"\n"+mTimelineList.get(i).getUserid());
                                tveventcomment.setText(mTimelineList.get(i).getEvactivity()+"\n"+mTimelineList.get(i).getUsercomment());

                                // Add the new row before the add field button.
                                parentLinearLayout.addView(rowView);

                            }

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
                params.put("t_id", ticketid);
                params.put("email", email);
                params.put("projid", projid);
                params.put("custid", custid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Toast.makeText(SentTicketDetail.this, "masuk", Toast.LENGTH_LONG).show();
        //Adding request to the queue
        requestQueue.add(stringRequest);

        tvticketid.setText(ticketid);
        tvsiteid.setText(siteid);
        tvsitename.setText(sitename);
        tvpicname.setText(picname);
        tvrequestoption.setText(requestoption);
//        etmsg.setText(message);
        tvsubmittime.setText(submittime);




        //String asu=Integer.toString(mTimelineList.size());
        //Toast.makeText(SentTicketDetail.this, "isi list"+asu, Toast.LENGTH_LONG).show();






        btchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getNumber(picname,ticketid);
            }
        });
/*
        bthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userInfo.setUserid(email);
                userInfo.setFname(username);
                userSession.setLoggedin(true);
                startActivity(new Intent(SentTicketDetail.this, HistoryTicketDetail.class));

            }
        });
*/

    }

    private void addlistviewinbox(final String email,final String projid,final String custid,final String ticketid) {



    }




    private void getNumber(final String picname,final String ticketid){
        //Traversing through all the items in the json array
        try {
//Toast.makeText(SentTicketDetail.this, "masukchat", Toast.LENGTH_LONG).show();
            //addlistviewinbox(email,projid,custid,ticketid);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GETNUMBER_URL,
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
                                        String servedby=json.getString(Utils.TAG_CONTACT);

                                        // Toast.makeText(SentTicketDetail.this, timestamp, Toast.LENGTH_LONG).show();
                                        //mTimelineList.add(new tickettimeline(timestamp,tflow,userid,useract,usercom));

                                        String text = "Chat With BO : "+ picname +",\nHere, i am calling for this \n*"+ ticketid +"*";// Replace with your message.
                                        String toNumber = servedby.replaceAll("[+]",""); // Replace with mobile phone number without +Sign or leading zeros.

                                       // Toast.makeText(SentTicketDetail.this, toNumber, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                                        startActivity(intent);


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
                    params.put("t_og_served_by", picname);
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

    @Override
    public void onBackPressed() {

        Intent setIntent = new Intent(SentTicketDetail.this,SentTicket.class);
        startActivity(setIntent);
    }

    public static List sortArray(List<tickettimeline> array) {
        List<tickettimeline> jsons = new ArrayList<>();

        Collections.sort(jsons, new Comparator<tickettimeline>() {
            @Override
            public int compare(tickettimeline lhs, tickettimeline rhs) {
                String lid = null;

                    lid = lhs.getTimestamp();

                String rid = null;

                    rid = lhs.getTimestamp();

                // Here you could parse string id to integer and then compare.
                return lid.compareToIgnoreCase(rid);
            }
        });
        return new ArrayList(jsons);
    }


    public void checkUpdate(final String email){
        final String versionCode = Integer.toString(BuildConfig.VERSION_CODE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.TICKETNOTIFUPD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultNotif = j.getJSONArray(Utils.JSON_ARRAY);


                            //clear()
                            for (int i = 0; i < resultNotif.length(); i++) {
                                try {

                                    JSONObject json = resultNotif.getJSONObject(i);

                                    String message = json.getString(Utils.TAG_UPDMSG);
                                    String updvers = json.getString(Utils.TAG_UPDVERS);

                                    if(!message.isEmpty()&&!updvers.isEmpty()){
                                        if(!updvers.equalsIgnoreCase(versionCode)){

                                            AlertDialog.Builder ab = new AlertDialog.Builder(SentTicketDetail.this);
                                            ab.setCancelable(false);
                                            ab.setTitle("IXT Application Message");
                                            ab.setMessage(message);
                                            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    goToUrl("https://play.google.com/store/apps/details?id=com.ericsson.androidlogin");

                                                }
                                            });
                                            ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    ActivityCompat.finishAffinity(SentTicketDetail.this);
                                                    SentTicketDetail.this.finish();
                                                }
                                            });

                                            ab.show();



                                        }

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("versionCode", versionCode);
                params.put("email", email);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);

        //    }

    }


    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }






}
