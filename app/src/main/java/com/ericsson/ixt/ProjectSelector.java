package com.ericsson.ixt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class ProjectSelector extends AppCompatActivity {
    private UserInfo userInfo;
    private UserSession userSession;
    private LinearLayout parentLinearLayout,buttonLayout;
    private TextView tvAccount, tvProject;
    private JSONArray resultArray;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_select);

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

        logout          = (Button)findViewById(R.id.logout);




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GETPROJLIST_URL,
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

                            parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
                            for(int i=0;i<resultArray.length();i++){
                                try {
                                    //Getting json object
                                    Typeface font = Typeface.createFromAsset(getAssets(),"fonts/ericssoncapitaltt-webfont.ttf");
                                    JSONObject json = resultArray.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    final String customerName=json.getString(Utils.TAG_USRCUST);
                                    final String projectName=json.getString(Utils.TAG_USRPROJ);

                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View rowView = inflater.inflate(R.layout.project_list, null);


                                        tvAccount = (TextView) rowView.findViewById(R.id.tvAccount);
                                        tvProject = (TextView) rowView.findViewById(R.id.tvProject);
                                        tvAccount.setText(customerName);
                                        tvProject.setText(projectName);
                                        tvAccount.setTypeface(font);
                                        tvProject.setTypeface(font);
                                        // Add the new row before the add field button.


                                    buttonLayout = (LinearLayout) rowView.findViewById(R.id.btnprojectlist);
                                    buttonLayout.setClickable(true);

                                    buttonLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            userInfo.setCustid(customerName);
                                            userInfo.setProjid(projectName);
                                            userSession.setLoggedin(true);
                                            startActivity(new Intent(ProjectSelector.this, MainActivity.class));
                                            finish();
                                        }
                                    });

                                    parentLinearLayout.addView(rowView);

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
                        // Toast.makeText(SentTicketDetail.this, ticketid + email + projid + custid, Toast.LENGTH_LONG).show();
                        // Toast.makeText(SentTicketDetail.this, "masuk 3", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Toast.makeText(SentTicketDetail.this, "masuk", Toast.LENGTH_LONG).show();
        //Adding request to the queue
        requestQueue.add(stringRequest);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder ab = new AlertDialog.Builder(ProjectSelector.this);
                ab.setTitle("IXT Application Message");
                ab.setMessage("Are you sure to Log Out from application?");
                ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //if you want to kill app . from other then your main avtivity.(Launcher)
                        //android.os.Process.killProcess(android.os.Process.myPid());
                        //System.exit(1);

                        //if you want to finish just current activity
                        sendRegistrationToServer("",email);
                        userSession.setLoggedin(false);
                        userInfo.clearUserInfo();
                        startActivity(new Intent(ProjectSelector.this, Login.class));
                        ActivityCompat.finishAffinity(ProjectSelector.this);
                        ProjectSelector.this.finish();
                        finish();
                    }
                });
                ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ab.show();



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

}
