package com.ericsson.ixt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

/**
 *
 * Created by ericsson on 11/15/2017.
 */

public class HistoryTicketDetail extends AppCompatActivity {

    private TextView tvticketid,tvsiteid, tvsitename,
            tvreqtype,tvpicname,tvsubmittime,tvopentime,tvclosedtime,tvleadtime,tvslacompliment;
    private EditText etnote;
    private UserInfo userInfo;
    private UserSession userSession;
    private Button tolist;

    //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");

    private List<tickettimeline> mTimelineList;

    private TextView tvtflowuserid,tvtimestamp,tveventcomment;

    private JSONArray resultArray;


    private LinearLayout parentLinearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail_history_u);
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


        Intent repeating_intent = getIntent();
        final String account_id  = repeating_intent.getStringExtra("account_id");
        final String project_id = repeating_intent.getStringExtra("project_id");
        final String t_status = repeating_intent.getStringExtra("t_status");
        if(account_id!=null&&project_id!=null){
            userInfo.setCustid(account_id);
            userInfo.setProjid(project_id);
            if(t_status!=null){
                int t_stat=Integer.parseInt(t_status);
                if(t_stat<9){
                    Toast.makeText(HistoryTicketDetail.this, "This Ticket No longer on this status", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HistoryTicketDetail.this, MainActivity.class));
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


        tvticketid=(TextView) findViewById(R.id.tvreqid);
        tvpicname=(TextView) findViewById(R.id.tvpicname);
        tvsiteid=(TextView) findViewById(R.id.tvsiteid);
        tvsitename=(TextView) findViewById(R.id.tvsitename);
        tvreqtype=(TextView) findViewById(R.id.tvreqtype);
        tvsubmittime=(TextView) findViewById(R.id.tvsubmittime);
        tvopentime=(TextView) findViewById(R.id.tvopentime);
        tvclosedtime=(TextView) findViewById(R.id.tvclosedtime);
        tvleadtime=(TextView) findViewById(R.id.tvleadtime);
        tvslacompliment=(TextView) findViewById(R.id.tvslaCompliment);
        tolist=(Button) findViewById(R.id.bttolist);


        /*
        tvticketid.setTypeface(face);
        tvpicname.setTypeface(face);
        tvsiteid.setTypeface(face);
        tvsitename.setTypeface(face);
        tvreqtype.setTypeface(face);
        tvsubmittime.setTypeface(face);
        tvopentime.setTypeface(face);
        tvclosedtime.setTypeface(face);
        tvleadtime.setTypeface(face);
        tvslacompliment.setTypeface(face);
        tolist.setTypeface(face);
*/




        Intent intent = getIntent();
        final String ticketid = intent.getStringExtra("ticketid");
        final String picname = intent.getStringExtra("picname");
        final String siteid = intent.getStringExtra("siteid");
        final String sitename = intent.getStringExtra("sitename");
        final String requesttype = intent.getStringExtra("evactivity");
        final String submittime = intent.getStringExtra("submittime");
        final String opentime = intent.getStringExtra("opentime");
        final String closedtime = intent.getStringExtra("closedtime");
        final String leadtimetoclose = intent.getStringExtra("leadtimetoclose");
        final String slacompliment = intent.getStringExtra("slacompliment");
      //  final String note = intent.getStringExtra("note");


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
        tvpicname.setText(picname);
        tvsiteid.setText(siteid);
        tvsitename.setText(sitename);
        tvreqtype.setText(requesttype);
        tvsubmittime.setText(submittime);
        tvopentime.setText(opentime);
        tvclosedtime.setText(closedtime);
        tvleadtime.setText(leadtimetoclose);
        tvslacompliment.setText(slacompliment);
      //  etnote.setText(note);

        tolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setIntent = new Intent(HistoryTicketDetail.this,History.class);
                startActivity(setIntent);
                HistoryTicketDetail.this.finish();
                finish();

            }
        });


    }

}
