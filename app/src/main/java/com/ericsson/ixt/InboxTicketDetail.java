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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

/**
 * Created by ericsson on 11/15/2017.
 */

public class InboxTicketDetail extends AppCompatActivity {
    private TextView tvreqid,
            tvpicname, tvsiteid,tvsitename,etmsgpbs,
            tvsubmittime;
    private Button btsubmit;
    private UserInfo userInfo;
    private UserSession userSession;
    //JSON Array
    private JSONArray resultArray;
    //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
    private JSONArray resultNotif;


    private List<tickettimeline> mTimelineList;

    private TextView tvtflowuserid,tvtimestamp,tveventcomment;

    private JSONArray resultArraytl;

    private LinearLayout parentLinearLayout;
    String rp_custom="";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail_in_n);

        userInfo        =  new UserInfo(this);
        userSession     =  new UserSession(this);
        mTimelineList = new ArrayList<>();
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
        checkUpdate(email);



        tvreqid=(TextView) findViewById(R.id.tvreqid);
        tvpicname=(TextView) findViewById(R.id.tvpicname);
        tvsiteid=(TextView) findViewById(R.id.tvsiteid);
        tvsitename=(TextView) findViewById(R.id.tvsitename);
        tvsubmittime=(TextView) findViewById(R.id.tvsubmittime);
        etmsgpbs=(TextView) findViewById(R.id.etmsgpbs);
        btsubmit=(Button) findViewById(R.id.btsubmit);

        /*
        tvreqid.setTypeface(face);
        tvpicname.setTypeface(face);
        tvsiteid.setTypeface(face);
        tvsitename.setTypeface(face);
        tvsubmittime.setTypeface(face);
        etmsgpbs.setTypeface(face);
        etactionmsg.setTypeface(face);
        btsubmit.setTypeface(face);

        */
        /*
        if(!usertype.equalsIgnoreCase("7")){
            btsubmit.setVisibility(Button.GONE);

        }
        if(usertype.equalsIgnoreCase("7")){
            btsubmit.setVisibility(Button.VISIBLE);
        }
        */



        Intent intent = getIntent();
        final String ticketid = intent.getStringExtra("ticketid");
        final String requestor = intent.getStringExtra("requestor");
        final String siteid = intent.getStringExtra("siteid");
        final String sitename = intent.getStringExtra("sitename");
        final String submittime = intent.getStringExtra("submittime");
        final String evactivity = intent.getStringExtra("evactivity");
        final String mid = intent.getStringExtra("mid");
        final String fromfragment = intent.getStringExtra("fromfragment");
        

        if(fromfragment.equalsIgnoreCase("two")||fromfragment.equalsIgnoreCase("three")){
            btsubmit.setVisibility(Button.GONE);
        }



       // Toast.makeText(InboxTicketDetail.this, ticketid+evactivity, Toast.LENGTH_LONG).show();

        //Toast.makeText(InboxTicketDetail.this, "MAU MASUK RQ", Toast.LENGTH_LONG).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.PBSACT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       //  Toast.makeText(InboxTicketDetail.this, "masuk response", Toast.LENGTH_LONG).show();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            resultArray = j.getJSONArray(Utils.JSON_ARRAY);
                            String pbs="problem";
                            for(int i=0;i<resultArray.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = resultArray.getJSONObject(i);


                                    //Adding the name of the student to array list
                                    pbs=json.getString(Utils.TAG_PBS);
                                    //action=json.getString(Utils.TAG_ACTION);
                                  //  Toast.makeText(InboxTicketDetail.this, pbs, Toast.LENGTH_LONG).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            etmsgpbs.setText(pbs);

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

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("ticketid", ticketid);
                params.put("evactivity", evactivity);
                params.put("projid", projid);
                params.put("custid", custid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);



        StringRequest stringRequest_rp_check = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_20_UAT/rp_check.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(InboxTicketDetail.this, "masuk response", Toast.LENGTH_LONG).show();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            resultArray = j.getJSONArray(Utils.JSON_ARRAY);
                            //String pbs="problem";
                            for(int i=0;i<resultArray.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = resultArray.getJSONObject(i);


                                    //Adding the name of the student to array list
                                   rp_custom=json.getString("rp_check");
                                    //action=json.getString(Utils.TAG_ACTION);
                                    //  Toast.makeText(InboxTicketDetail.this, pbs, Toast.LENGTH_LONG).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        if(rp_custom.equalsIgnoreCase("0")){
                                btsubmit.setText("CLOSE TICKET");
                        } //Calling method getStudents to get the students from the JSON Array
                            //getTimeline(resultArray);



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
                params.put("ticketid", ticketid);
                params.put("evactivity", evactivity);
                params.put("projid", projid);
                params.put("custid", custid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue_rp_check = Volley.newRequestQueue(this);

        requestQueue_rp_check.add(stringRequest_rp_check);



        StringRequest stringRequesttl = new StringRequest(Request.Method.POST, Utils.GETTIMELINE_URL,
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
                            resultArraytl = j.getJSONArray(Utils.JSON_ARRAY);

                            for(int i=0;i<resultArraytl.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = resultArraytl.getJSONObject(i);


                                    //Adding the name of the student to array list
                                    String timestamp=json.getString(Utils.TAG_TLTIMESTAMP);
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
                                parentLinearLayout.addView(rowView);;

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
                params.put("t_id", ticketid);
                params.put("email", email);
                params.put("projid", projid);
                params.put("custid", custid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueuetl = Volley.newRequestQueue(this);
        // Toast.makeText(SentTicketDetail.this, "masuk", Toast.LENGTH_LONG).show();
        //Adding request to the queue
        requestQueuetl.add(stringRequesttl);






        tvreqid.setText(ticketid);
        tvpicname.setText(requestor);
        tvsiteid.setText(siteid);
        tvsitename.setText(sitename);
        tvsubmittime.setText(submittime);



        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rp_custom.equalsIgnoreCase("1")){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.TICKETACC_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject j = null;
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(InboxTicketDetail.this, "Ticket Accepted By " + username + " " + lname, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(InboxTicketDetail.this, MainActivity.class);
                                        InboxTicketDetail.this.startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(InboxTicketDetail.this);
                                        builder.setMessage("Accepting Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
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
                        params.put("email", email);
                        params.put("projid", projid);
                        params.put("custid", custid);
                        params.put("t_id", ticketid);
                        params.put("mid", mid);
                        return params;
                    }

                };

                //Creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(InboxTicketDetail.this);

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }

                if(rp_custom.equalsIgnoreCase("0")){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_20_UAT/close.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject j = null;
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            Toast.makeText(InboxTicketDetail.this, "Ticket Accepted By " + username + " " + lname, Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(InboxTicketDetail.this, MainActivity.class);
                                            InboxTicketDetail.this.startActivity(intent);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(InboxTicketDetail.this);
                                            builder.setMessage("Accepting Failed")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
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
                            params.put("email", email);
                            params.put("projid", projid);
                            params.put("custid", custid);
                            params.put("t_id", ticketid);
                            params.put("mid", mid);
                            return params;
                        }

                    };

                    //Creating a request queue
                    RequestQueue requestQueue = Volley.newRequestQueue(InboxTicketDetail.this);

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }



            }
        });









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

                                            AlertDialog.Builder ab = new AlertDialog.Builder(InboxTicketDetail.this);
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
                                                    ActivityCompat.finishAffinity(InboxTicketDetail.this);
                                                    InboxTicketDetail.this.finish();
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
