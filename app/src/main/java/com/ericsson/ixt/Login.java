package com.ericsson.ixt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = Login.class.getSimpleName();
    private EditText email, password;
    private Button login,activate,Resetpassword;
    private TextView signup;
    private ProgressDialog progressDialog;
    private UserSession session;
    private UserInfo userInfo;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_n);

        double density = getResources().getDisplayMetrics().density;

        String imageUri ="https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers.png";
        if(density==0.75){
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers.png";
        }else if(density==1.0){
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers.png";
        }else if(density==1.5){
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers1.png";
        }else if(density==2.0){
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers2.png";
        }else if(density==3.0){
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers3.png";
        }else if(density==4.0){
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers4.png";
        }else {
            imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/theme/skyscrapers.png";
        }

        imageView = (ImageView) findViewById(R.id.imageback);
        Picasso.with(getApplicationContext()).load(imageUri).fit().into(imageView );
        Picasso.with(getApplicationContext()).invalidate(imageUri);
        Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);



        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");

        email        = (EditText)findViewById(R.id.email);
        password        = (EditText)findViewById(R.id.password);
        login           = (Button)findViewById(R.id.login);
        activate        = (Button)findViewById(R.id.Activate);
        Resetpassword   = (Button) findViewById(R.id.Resetpassword);
        progressDialog  = new ProgressDialog(this);
        session         = new UserSession(this);
        userInfo        = new UserInfo(this);


        if(session.isUserLoggedin()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        login.setOnClickListener(this);
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ActivateUser.class));
            }
        });
        Resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("https://ixt.eid-tools.com/login/reset_password");
            }
        });


    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }



    private void login(final String email, final String password){
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Utils.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite

                        JSONObject user = jObj.getJSONObject("user");

                            String user_id = user.getString("user_id");
                            String user_fname = user.getString("user_fname");
                            String user_lname = user.getString("user_lname");
                            String user_contact = user.getString("user_contact");
                            String user_cu_id = user.getString("user_cu_id");
                            String user_cu_name = user.getString("user_cu_name");
                            String user_comp_id = user.getString("user_asp_id");
                            String user_comp_name = user.getString("user_asp_name");
                            String user_cust_id = user.getString("user_cust_id");
                            String user_project_id = user.getString("user_project_id");
                            String user_join_date = user.getString("user_join_date");
                            String user_last_activity = user.getString("user_last_activity");
                            String user_status = user.getString("user_status");
                            String user_type = user.getString("user_type");
                            String user_prev = user.getString("user_prev");


                            // Inserting row in users table
                            userInfo.setUserid(user_id);
                            userInfo.setFname(user_fname);
                            userInfo.setLname(user_lname);
                            userInfo.setContact(user_contact);
                            userInfo.setCuid(user_cu_id);
                            userInfo.setCuname(user_cu_name);
                            userInfo.setCompid(user_comp_id);
                            userInfo.setCompname(user_comp_name);
                            userInfo.setCustid(user_cust_id);
                            userInfo.setProjid(user_project_id);
                            userInfo.setJoindate(user_join_date);
                            userInfo.setLastact(user_last_activity);
                            userInfo.setStatus(user_status);
                            userInfo.setType(user_type);
                            userInfo.setPrev(user_prev);
                            session.setLoggedin(true);

                            String token="";
                            try
                            {
                                Log.d(TAG, FirebaseInstanceId.getInstance().getToken());
                                token =FirebaseInstanceId.getInstance().getToken();
                                sendRegistrationToServer(token,user_id);
                            }
                            catch (Exception e) {
                                //error handling code
                               // Toast.makeText(getApplicationContext(), "try to find notification id", Toast.LENGTH_LONG).show();
                            }



                            startActivity(new Intent(Login.this, ProjectSelector.class));
                            finish();




                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast(errorMsg);
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    toast("Json error: " + e.getMessage());
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                toast("Failed To Login");
                progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void toast(String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String uName = email.getText().toString().trim();
                String pass  = password.getText().toString().trim();
                login(uName, pass);
                break;
            case R.id.open_signup:
                startActivity(new Intent(this, SignUp.class));
                break;
        }
    }

    private void sendRegistrationToServer(final String token,final String user_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://android.eid-tools.com/ixt_20_UAT/uploadToken.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                      //  Toast.makeText(getApplicationContext(), "Getting token respond from server", Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                          //  Toast.makeText(getApplicationContext(), "checking token generator", Toast.LENGTH_LONG).show();
                            if (success) {
                            //    Toast.makeText(getApplicationContext(), "token Assigned for your id", Toast.LENGTH_LONG).show();
                            }else{
                             //   Toast.makeText(getApplicationContext(), "failed to upload token", Toast.LENGTH_LONG).show();
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
                params.put("email", user_id);
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
