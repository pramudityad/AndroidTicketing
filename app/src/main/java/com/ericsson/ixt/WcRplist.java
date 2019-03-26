package com.ericsson.ixt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class WcRplist extends AppCompatActivity {
    private UserInfo userInfo;
    private UserSession userSession;
    private LinearLayout parentLinearLayout;
    private RadioButton rbAccount;
    private JSONArray resultArray;
    private Button next;
    private RadioGroup rgrplist;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_select);

        userInfo        = new UserInfo(this);
        userSession     = new UserSession(this);
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

        Intent intent = getIntent();
        final String ticketid = intent.getStringExtra("ticketid");
        final String requestor = intent.getStringExtra("requestor");
        final String siteid = intent.getStringExtra("siteid");
        final String sitename = intent.getStringExtra("sitename");
        final String submittime = intent.getStringExtra("submittime");
        final String evactivity = intent.getStringExtra("evactivity");
        final String mid = intent.getStringExtra("mid");


        //Toast.makeText(WcRplist.this, evactivity, Toast.LENGTH_LONG).show();

        next = (Button)findViewById(R.id.next);

        rgrplist =(RadioGroup) findViewById(R.id.rgrplist);
        final ArrayList<Wc_radiobutton> list = new ArrayList<>();
        rgrplist.clearCheck();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_20_UAT/getreportlist.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(SentTicketDetail.this, "masuk 1", Toast.LENGTH_LONG).show();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            //list.clear();
                            j = new JSONObject(response);

                            resultArray = j.getJSONArray(Utils.JSON_ARRAY);

                            //list.clear();

                            for(int i=0;i<resultArray.length();i++){
                                try {
                                    //Getting json object
                                    Typeface font = Typeface.createFromAsset(getAssets(),"fonts/ericssoncapitaltt-webfont.ttf");
                                    JSONObject json = resultArray.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    //final String customerName=json.getString(Utils.TAG_USRCUST);
                                    //final String projectName=json.getString(Utils.TAG_USRPROJ);
                                    final String r_id=json.getString("r_id");
                                    final String r_name=json.getString("r_name");
                                    final String r_ev_type=json.getString("r_ev_type");
                                    final String r_ev_activity=json.getString("r_ev_activity");
                                    final String r_status=json.getString("r_status");
                                    final String r_desc=json.getString("r_desc");
                                        list.add(new Wc_radiobutton(r_id,r_name,r_ev_type,r_ev_activity,r_status,r_desc));

                                    Log.e("path",Integer.toString(list.size()) ); //
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            Log.e("path",Integer.toString(list.size()) ); // use selectedImagePath


                            rgrplist.clearCheck();
                            rgrplist.removeAllViews();
                            rgrplist.removeAllViewsInLayout();

                            RadioButton button;
                            for(int i = 0; i < list.size(); i++) {
                                button = new RadioButton(WcRplist.this);
                                button.setText(list.get(i).getR_name());
                                rgrplist.addView(button);
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
                params.put("email", email);
                params.put("projid", projid);
                params.put("custid", custid);
                params.put("ticketid",ticketid);
                params.put("evactivity",evactivity);

                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Toast.makeText(SentTicketDetail.this, "masuk", Toast.LENGTH_LONG).show();
        //Adding request to the queue
        requestQueue.add(stringRequest);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rgrplist.getCheckedRadioButtonId()>=0){
                    int radioButtonID = rgrplist.getCheckedRadioButtonId();
                    int selected =0;
                    if(radioButtonID%list.size()==0){
                        selected= list.size()-1;
                    }else{
                        selected=radioButtonID%list.size()-1;
                    }

                    //Toast.makeText(WcRplist.this, Integer.toString(selected), Toast.LENGTH_LONG).show();
                    Log.e("report",Integer.toString(selected) ); //
                    Log.e("report",list.get(selected).getR_id().toString() ); //
                    //


                    Intent intent = new Intent(WcRplist.this, WCanvas.class);
                    intent.putExtra("ticketid", ticketid);
                    intent.putExtra("requestor", requestor);
                    intent.putExtra("siteid", siteid);
                    intent.putExtra("sitename", sitename);
                    intent.putExtra("submittime", submittime);
                    intent.putExtra("evactivity", evactivity);
                    intent.putExtra("mid", mid);
                    intent.putExtra("r_id", list.get(selected).getR_id().toString());
                    WcRplist.this.startActivity(intent);
                    finish();
                    list.clear();

                }

                /*
                Intent intent = new Intent(WcRplist.this, WCanvas.class);
                WcRplist.this.startActivity(intent);
    */
            }
        });
    }


    private void sendRegistrationToServer(final String token,final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://android.eid-tools.com/ixt_20_UAT/uploadToken.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        // Toast.makeText(getApplicationContext(), "Respond token from server", Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            //Toast.makeText(getApplicationContext(), "Accessing token generator", Toast.LENGTH_LONG).show();
                            if (success) {
                                //  Toast.makeText(getApplicationContext(), "Deleting Token Notification For your ID", Toast.LENGTH_LONG).show();
                            }else{
                                //  Toast.makeText(getApplicationContext(), "failed to upload token to your id", Toast.LENGTH_LONG).show();
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
                }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token",token);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);





    }

    @Override
    public void onBackPressed() {

        Intent setIntent = new Intent(WcRplist.this,WcList.class);
        startActivity(setIntent);
        finish();

    }

}
