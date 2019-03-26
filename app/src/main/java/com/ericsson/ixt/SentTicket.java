package com.ericsson.ixt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

/**
 * Created by ericsson on 11/13/2017.
 */

public class SentTicket extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private ListView lvOutbox;
    private CustomAdapterOutbox adapter;
    private List<ticketout> mOutboxList;
    private UserInfo userInfo;
    private UserSession userSession;
    private Spinner spinnersearch;
    private String[] requestid;
    private JSONArray resultNotif;
    private TextView tvprojectname;
    private ImageButton btswapproject;

    //JSON Array
    private JSONArray resultOutbox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_outbox);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");

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

        userInfo        =  new UserInfo(this);
        userSession     =  new UserSession(this);
        lvOutbox = (ListView)findViewById(R.id.listviewticketsent);

        mOutboxList = new ArrayList<>();

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
        tvprojectname   = (TextView) findViewById(R.id.tvprojectname);
        btswapproject   = (ImageButton) findViewById(R.id.btswapproject);

        tvprojectname.setText(custid+" "+projid);
        tvprojectname.setTypeface(face);




        //Toast.makeText(SentTicket.this, email, Toast.LENGTH_LONG).show();

        //create spinner for filtering


        spinnersearch=(Spinner) findViewById(R.id.spsearch) ;
        final List<SpinnerDataHistory> custom = new ArrayList<>();
        custom.add(new SpinnerDataHistory(R.drawable.all,"ALL"));
        custom.add(new SpinnerDataHistory(R.drawable.submitted,"SUBMITTED"));
        custom.add(new SpinnerDataHistory(R.drawable.open,"OPEN"));
        custom.add(new SpinnerDataHistory(R.drawable.handover, "IN PAUSE"));



        /*
        this.requestid = new String[] {
                "ALL","SUBMITTED","OPEN","IN PAUSE"
        };
        */
        btswapproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  userInfo.setEmail(email);
                //  userInfo.setUsername(username);
                userSession.setLoggedin(true);
                startActivity(new Intent(SentTicket.this, ProjectSelector.class));
            }
        });

        CustomSpinnerOutbox customSpinnerAdapter = new CustomSpinnerOutbox(SentTicket.this,R.layout.spinner_layout,custom);
        spinnersearch.setAdapter(customSpinnerAdapter);
        /*
        ArrayAdapter<String> adapterrequestid = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, requestid);
        spinnersearch.setAdapter(adapterrequestid);
        */


        spinnersearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
               // Toast.makeText(SentTicket.this, custom.get(position).getIconName(), Toast.LENGTH_SHORT).show();
            String category = custom.get(position).getIconName().toString();
            if(!category.equalsIgnoreCase("ALL")){
                mOutboxList.clear();
               // Toast.makeText(SentTicket.this,category+projid+custid+email, Toast.LENGTH_LONG).show();
                addlistviewinbox(email,category,projid,custid);
            }
            else
            {
                mOutboxList.clear();
                category="ALL";
               // Toast.makeText(SentTicket.this,category+projid+custid+email, Toast.LENGTH_LONG).show();
                addlistviewinbox(email,category,projid,custid);
            }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        lvOutbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String ticketid = mOutboxList.get(position).getTicketid();
                String siteid = mOutboxList.get(position).getSiteid();
                String sitename= mOutboxList.get(position).getSitename();
                String picname = mOutboxList.get(position).getServedby();
                String requestoption = mOutboxList.get(position).getEvactivity();
                String submittime = mOutboxList.get(position).getInputtime();


                //Toast.makeText(SentTicket.this, ticketid, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SentTicket.this, SentTicketDetail.class);
                intent.putExtra("ticketid", ticketid);
                intent.putExtra("siteid", siteid);
                intent.putExtra("sitename", sitename);
                intent.putExtra("picname", picname);
                intent.putExtra("requestoption", requestoption);
                intent.putExtra("submittime", submittime);

                SentTicket.this.startActivity(intent);
            }
        });

    }

     private void addlistviewinbox(final String email,final String category,final String projid,final String custid) {
         //Creating a string request
         StringRequest stringRequest = new StringRequest(Request.Method.POST,Utils.TICKETOUT_URL,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         JSONObject j = null;
                         try {
                             //Parsing the fetched Json String to JSON Object
                             j = new JSONObject(response);

                             //Storing the Array of JSON String to our JSON Array
                             resultOutbox = j.getJSONArray(Utils.JSON_ARRAY);

                             //Calling method getStudents to get the students from the JSON Array
                             getOutboxBody(resultOutbox);
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
                 params.put("t_status", category);
                 params.put("projid", projid);
                 params.put("custid", custid);
                 params.put("email", email);
                 return params;
             }

         };

         //Creating a request queue
         RequestQueue requestQueue = Volley.newRequestQueue(this);

         //Adding request to the queue
         requestQueue.add(stringRequest);


    }


    private void getOutboxBody(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);


                //Adding the name of the student to array list


                String ticketid = json.getString(Utils.TAG_OTID);
                String siteid = json.getString(Utils.TAG_OSITEID);
                String sitename= json.getString(Utils.TAG_OSITENAME);
                String evactivity = json.getString(Utils.TAG_OEVACTIVITY);
                String inputtime = json.getString(Utils.TAG_OINPUTTIME);
                String servedby = json.getString(Utils.TAG_OSERVEDBY);
                String tstatus = json.getString(Utils.TAG_OTSTATUS);



                mOutboxList.add(new ticketout(ticketid, siteid,sitename,evactivity,inputtime,servedby,tstatus));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //Setting adapter to show the items in the spinner
        //Init adapter
        adapter = new CustomAdapterOutbox(getApplicationContext(), mOutboxList);
        lvOutbox.setAdapter(adapter);
    }

    /*
    @Override
    public void onBackPressed() {

        Intent setIntent = new Intent(SentTicket.this,MainActivity.class);
        startActivity(setIntent);
        SentTicket.this.finish();
    }
    */


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

                                            AlertDialog.Builder ab = new AlertDialog.Builder(SentTicket.this);
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
                                                    ActivityCompat.finishAffinity(SentTicket.this);
                                                    SentTicket.this.finish();
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent setIntent = new Intent(SentTicket.this,MainActivity.class);
            startActivity(setIntent);
            SentTicket.this.finish();
        }
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
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
            startActivity(new Intent(SentTicket.this, NewTicket.class));
            finish();
        } else if (id == R.id.nav_drop_in) {
            userSession.setLoggedin(true);
            startActivity(new Intent(SentTicket.this, Inbox.class));
            finish();
        } else if (id == R.id.nav_ongoing) {
            /*
            userSession.setLoggedin(true);
            startActivity(new Intent(SentTicket.this, SentTicket.class));
            finish();
            */
        }else if (id == R.id.nav_working_canvas) {
            userSession.setLoggedin(true);
            startActivity(new Intent(SentTicket.this, WcList.class));
            finish();
        } else if (id == R.id.nav_closed) {
            userSession.setLoggedin(true);
            startActivity(new Intent(SentTicket.this, History.class));
            finish();
        } else if (id == R.id.nav_about) {
            userSession.setLoggedin(true);
            startActivity(new Intent(SentTicket.this, About.class));
            finish();
        }else if (id == R.id.nav_logout) {

            AlertDialog.Builder ab = new AlertDialog.Builder(SentTicket.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("Are you sure to Log Out from application?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    sendRegistrationToServer("",email);
                    userSession.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(SentTicket.this, Login.class));
                    ActivityCompat.finishAffinity(SentTicket.this);
                    SentTicket.this.finish();
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
