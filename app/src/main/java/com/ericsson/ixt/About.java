package com.ericsson.ixt;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

/**
 * Created by ericsson on 11/13/2017.
 */

public class About extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener   {
    private UserInfo userInfo;
    private UserSession userSession;
    private Button btdownload;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_about);

        userInfo        =  new UserInfo(this);
        userSession     =  new UserSession(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        btdownload    = (Button) findViewById(R.id.btdownload);
        btdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl("https://drive.google.com/open?id=1JiPqzN5ur953S3IFdOnKkihxNX7BncOA");
            }
        });

    }
/*
    @Override
    public void onBackPressed() {

        Intent setIntent = new Intent(About.this,MainActivity.class);
        startActivity(setIntent);
       About.this.finish();
    }
    */

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent setIntent = new Intent(About.this,MainActivity.class);
            startActivity(setIntent);
            About.this.finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final String username = userInfo.getFname();
        final String email    = userInfo.getUserid();
        final String lname    = userInfo.getLname();
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        TextView profilename = (TextView) findViewById(R.id.profilename);
        TextView profileemail = (TextView) findViewById(R.id.profileemail);


        String imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/"+email+".jpg";
        Picasso.with(getApplicationContext()).invalidate(imageUri);
        Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
        ImageView myImage = (ImageView) findViewById(R.id.profilepic);
        Picasso.with(getApplicationContext()).load(imageUri).into(myImage);
        Picasso.with(getApplicationContext()).invalidate(imageUri);
        Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);


        profilename.setText(username+" "+lname);
        profileemail.setText(email);



        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        userInfo              =  new UserInfo(this);
        final String email    = userInfo.getUserid();

        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            userSession.setLoggedin(true);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_new_request) {
            userSession.setLoggedin(true);
            startActivity(new Intent(About.this, NewTicket.class));
            finish();
        } else if (id == R.id.nav_drop_in) {
            userSession.setLoggedin(true);
            startActivity(new Intent(About.this, Inbox.class));
            finish();
        } else if (id == R.id.nav_working_canvas) {
            userSession.setLoggedin(true);
            startActivity(new Intent(About.this, WcList.class));
            finish();
        }  else if (id == R.id.nav_ongoing) {

            userSession.setLoggedin(true);
            startActivity(new Intent(About.this, SentTicket.class));
            finish();

        } else if (id == R.id.nav_closed) {

            userSession.setLoggedin(true);
            startActivity(new Intent(About.this, History.class));
            finish();

        } else if (id == R.id.nav_about) {
            /*
            userSession.setLoggedin(true);
            startActivity(new Intent(About.this, About.class));
            finish();
            */
        }else if (id == R.id.nav_logout) {

            AlertDialog.Builder ab = new AlertDialog.Builder(About.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("Are you sure to Log Out from application?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    sendRegistrationToServer("",email);
                    userSession.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(About.this, Login.class));
                    ActivityCompat.finishAffinity(About.this);
                    About.this.finish();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
