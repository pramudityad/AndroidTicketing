package com.ericsson.ixt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class ActivateUser extends AppCompatActivity {
    private UserSession session;
    private UserInfo userInfo;
    private Button btActivate;
    private EditText etHintuserid;
    private JSONArray resultArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_user);

        btActivate = (Button) findViewById(R.id.btnActivate);
        etHintuserid = (EditText) findViewById(R.id.etHintuserid);
        userInfo        =  new UserInfo(this);



        btActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, InputCode.class));



                if(!etHintuserid.getText().toString().isEmpty()){

                    if(userInfo.getUserid().equalsIgnoreCase(etHintuserid.getText().toString())){
                        startActivity(new Intent(ActivateUser.this, InputCode.class));
                        finish();
                    }else if(!userInfo.getUserid().equalsIgnoreCase(etHintuserid.getText().toString())){
                        sendTokenToSms(etHintuserid.getText().toString());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Input Your Registered Email", Toast.LENGTH_LONG).show();
                }




            }
        });
    }

    private void sendTokenToSms(final String email) {
        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_20_UAT/sendsms.php",
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
                            resultArray = j.getJSONArray("result");

                            for(int i=0;i<resultArray.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = resultArray.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    final String user_status=json.getString("user_status");
                                    final String user_contact=json.getString("user_contact");
                                    final String sms_token=json.getString("sms_token");
                                    //Toast.makeText(getApplicationContext(), sms_token, Toast.LENGTH_LONG).show();


                                    if(user_status.equalsIgnoreCase("")&&user_contact.equalsIgnoreCase("")){
                                        Toast.makeText(getApplicationContext(), "You Are Not Yet Registered", Toast.LENGTH_LONG).show();
                                    }else if(user_status.equalsIgnoreCase("NONASP")){
                                        Toast.makeText(getApplicationContext(), "You are not an ASP", Toast.LENGTH_LONG).show();
                                    }else if(user_status.equalsIgnoreCase("1")){
                                        Toast.makeText(getApplicationContext(), "You Already Registered And Active", Toast.LENGTH_LONG).show();
                                    }else if(user_status.equalsIgnoreCase("0")){
                                        userInfo.setTimetoken("86400000");
                                        userInfo.setToken(sms_token);
                                        userInfo.setUserid(email);
                                        Intent intent = new Intent(ActivateUser.this, InputCode.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("token", sms_token);
                                        intent.putExtra("user_status", user_status);
                                        ActivateUser.this.startActivity(intent);
                                        finish();

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
                // Posting parameters to ResetPassword url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);






    }
}
