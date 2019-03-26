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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prefs.UserInfo;
import prefs.UserSession;

/**
 * Created by ericsson on 3/7/2018.
 */

public class ResetPassword extends AppCompatActivity {


    private UserSession session;
    private UserInfo userInfo;
    private EditText etpassword,etpasswordagain;
    private Button btsetpassword;
    private JSONArray resultArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        userInfo        =  new UserInfo(this);
        final String email = userInfo.getUserid().toString();

        etpassword = (EditText) findViewById(R.id.etpassword);
        etpasswordagain=(EditText) findViewById(R.id.etpasswordagain);
        btsetpassword = (Button) findViewById(R.id.btsetpassword);

btsetpassword.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(!etpassword.getText().toString().equalsIgnoreCase("")&&!etpasswordagain.getText().toString().equalsIgnoreCase("")) {
            if (etpassword.getText().toString().equalsIgnoreCase(etpasswordagain.getText().toString())) {
                if(isValidPassword(etpassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password Valid", Toast.LENGTH_LONG).show();
                    activateAccount(email,etpassword.getText().toString());
                    startActivity(new Intent(ResetPassword.this, Login.class));
                    finish();
                    userInfo.setUserid("");
                    userInfo.setToken("");


                }else{

                    Toast.makeText(getApplicationContext(), "Password Invalid : Password At least one upper case English letter, (?=.*?[A-Z])\n" +
                            "At least one lower case English letter, (?=.*?[a-z])\n" +
                            "At least one digit, (?=.*?[0-9])  And Minimum 8 character", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "The password you entered is not the same", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please Input Your Same Password Twice", Toast.LENGTH_LONG).show();
        }



    }
});



    }

    public void activateAccount(final String email, final String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_20_UAT/activateaccount.php",
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
                                    final String status=json.getString("status");

                                    if(status.equalsIgnoreCase("success")){
                                        Toast.makeText(getApplicationContext(), "You are now Active User", Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Activation Failed", Toast.LENGTH_LONG).show();
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
                params.put("password", password);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
