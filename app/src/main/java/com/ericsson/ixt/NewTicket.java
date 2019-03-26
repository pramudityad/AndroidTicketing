package com.ericsson.ixt;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import prefs.UserInfo;
import prefs.UserSession;

import static android.content.Intent.ACTION_PICK;

/**
 *
 * Created by ericsson on 11/13/2017.
 */

public class NewTicket extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserInfo userInfo;
    private UserSession userSession;
    private Button btsubmit;
    private ImageButton btswapproject;
    private TextView tvreqid,tvpicname,tvsitedetail,tvprojectname,tvtgtdate_g,tvactivity_g,tvtechnology_g,tvmodule_g;
    private Spinner spsiteid,spreqopt;
    private RadioGroup rdgroupsl;
    private RadioButton rbtnsl;
    private EditText etmsgnew;
    private ArrayList<sites> ARsiteid;
    private ArrayList<String> ARsiteString;
    private String[] requestoption;
    private JSONArray resultNotif;

    private ImageView iv_df,iv_cf,iv_rs,iv_in,iv_ss;
    private TextView tvsitename_g,tvmilestone_g,tvsow_g,tvprogid_g;

    private LinearLayout lhidden;
    private LinearLayout attachmentbox,attachmentlabel;
    private ArrayList<String> ARreqopt;

    /*image uploader variable*/
    private Button button,uriadd,uploadbulk,imageselect,clearpic,checkimage;
    private Switch imgSwitch;
    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;
    private ArrayList<String> ARimage;
    private ArrayList<String> ARimagename;
    private ArrayList<String> ARencoded;
    private ArrayList<Uri> ARuri;
    private TextView filename;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String imagepath=null;
    private ImageView img;
    private LinearLayout parentLinearLayout;
    /*image uploader variable*/


    private TextView tv_ms_item;
    private ImageView iv_ms;
    private List<String> mTraffictList;

    private TextView tvwiringitem;
    private HorizontalScrollView vwscrollwiring;


    //String stat="";
    String milestone    = "";
    String m_id   = "";
    String tgtGrpString="";
    String event_type="";
    //JSON Array
    private JSONArray resultSite;
    private JSONArray resultReqopt;
    private JSONArray resultSiteStat;

    List<String> midArray;


    ImageView mIcon;
    private LinearLayout parentLinearLayoutSow;

    private List<String> mSowImgList;
    private JSONArray resultArraySow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_new_ticket);
        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");

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


        tvreqid     = (TextView) findViewById(R.id.tvreqid);
        tvpicname   = (TextView) findViewById(R.id.tvpicname);


        tvsitename_g = (TextView) findViewById(R.id.tvsitename_g);
        tvmilestone_g = (TextView) findViewById(R.id.tvmilestone_g);
        tvsow_g = (TextView) findViewById(R.id.tvsow_g);
        tvprogid_g = (TextView) findViewById(R.id.tvprogid_g);

        tvactivity_g= (TextView) findViewById(R.id.tvactivity_g);
        tvtgtdate_g= (TextView) findViewById(R.id.tvtgtdate_g);
        tvtechnology_g= (TextView) findViewById(R.id.tvtechnology_g);
        tvmodule_g= (TextView) findViewById(R.id.tvmodule_g);

        spsiteid    = (Spinner) findViewById(R.id.tvsiteid);
        spreqopt    = (Spinner) findViewById(R.id.spreqopt);

        rdgroupsl =(RadioGroup) findViewById(R.id.radioGroupsl);

        etmsgnew    = (EditText) findViewById(R.id.etmsgnew);

        btsubmit    = (Button) findViewById(R.id.btsubmit);
        btswapproject = (ImageButton) findViewById(R.id.btswapproject);

        vwscrollwiring = (HorizontalScrollView) findViewById(R.id.vwscrollwiring);
        tvwiringitem = (TextView) findViewById(R.id.tvwiringitem);


        imgSwitch=(Switch) findViewById(R.id.imgSwitch);
        attachmentlabel=(LinearLayout) findViewById(R.id.attachmentlabel);
        lhidden =(LinearLayout) findViewById(R.id.layout_site_detail);
        lhidden.setVisibility(LinearLayout.GONE);

        attachmentbox=(LinearLayout) findViewById(R.id.attachmentbox);
        attachmentbox.setVisibility(LinearLayout.GONE);
        attachmentlabel.setVisibility(LinearLayout.GONE);



        ARsiteid = new ArrayList<sites>();
        ARsiteString = new ArrayList<String>();
        mTraffictList = new ArrayList<>();

        ARreqopt = new ArrayList<String>();

        midArray=new ArrayList<String>();

        mSowImgList = new ArrayList<>();


        userInfo        = new UserInfo(this);
        userSession     = new UserSession(this);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
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
        tvprojectname   = (TextView) findViewById(R.id.tvprojectname);
        tvprojectname.setText(custid+" "+projid);
        tvprojectname.setTypeface(face);
        checkUpdate(email);



        /**
         * image uploader initiate*/
        ARimage = new ArrayList<String>();
        ARimagename = new ArrayList<String>();
        ARencoded = new ArrayList<String>();
        ARuri = new ArrayList<Uri>();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        filename    =(TextView) findViewById(R.id.filename);
        uriadd      =(Button) findViewById(R.id.uriadd);
        imageselect =(Button) findViewById(R.id.imageselect);
        clearpic    =(Button) findViewById(R.id.clearpic);
        /**
         * end of image uploader initiate*/

        getDatasiteid(email,custid,projid);

        btswapproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.setLoggedin(true);
                startActivity(new Intent(NewTicket.this, ProjectSelector.class));
                finish();
            }
        });

        /**
         *start button for image uploader*/

        clearpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARuri.clear();
                ARimagename.clear();
                ARimage.clear();
                ARencoded.clear();
                filename.setText(ARimagename.toString());
            }
        });
        uriadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getFileUri();
                i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                startActivityForResult(i, 10);
            }
        });
        imageselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);//
                startActivityForResult(Intent.createChooser(i, "Select Image"),20);
            }
        });


        /**
         * end of button image uploader*/




        spreqopt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    event_type = getEventType(i - 1);
                    tgtGrpString = getTgtGrp(i - 1);
                }

                if (spreqopt.getSelectedItem().toString().equalsIgnoreCase("Request Integration")||
                        spreqopt.getSelectedItem().toString().equalsIgnoreCase("Request RSA")||
                        spreqopt.getSelectedItem().toString().equalsIgnoreCase("Revise Config Files")){
                    // atchhidden.setVisibility(LinearLayout.VISIBLE);
                    attachmentlabel.setVisibility(LinearLayout.VISIBLE);
                }else{
                    // atchhidden.setVisibility(LinearLayout.GONE);
                    attachmentlabel.setVisibility(TextView.GONE);
                    attachmentbox.setVisibility(LinearLayout.GONE);
                    imgSwitch.setChecked(false);

                    ARuri.clear();
                    ARimagename.clear();
                    ARimage.clear();
                    ARencoded.clear();
                    filename.setText(ARimagename.toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spsiteid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(spsiteid.getCount()>1&&!spsiteid.getSelectedItem().toString().equalsIgnoreCase("Select site id...")) {

                    tvsitename_g.setText(getSitename(position-1));
                    tvmilestone_g.setText(getMilestone(position-1));
                    tvsow_g.setText(getSitesow(position-1));
                    tvprogid_g.setText(getProgramid(position-1));
                    tvactivity_g.setText(getactid(position-1));
                    tvtgtdate_g.setText(getdatetgt(position-1));
                    tvtechnology_g.setText(gettech(position-1));
                    tvmodule_g.setText(getmodule(position-1));

                    milestone=getMilestone(position-1);
                    m_id=getmid(position-1);

                    getDatasitestat(m_id,custid,projid);

                    midArray.clear();
                    midArray.add(m_id);



                    String siteid=spsiteid.getItemAtPosition(position).toString();
                    final String sow=getSitesow(position-1);
                    //Toast.makeText(NewTicket.this, siteid+sow ,Toast.LENGTH_LONG).show();
                    ARreqopt.clear();
                    //This method will fetch the data from the URL
                    getReqopt(siteid,sow,email,custid,projid,usertype);

                    lhidden.setVisibility(LinearLayout.VISIBLE);
                    StringRequest stringRequesttl = new StringRequest(Request.Method.POST, "https://ixt.eid-tools.com/assets/sow/imageList.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject j = null;
                                    try {
                                        //Parsing the fetched Json String to JSON Object
                                        //Parsing the fetched Json String to JSON Object
                                        j = new JSONObject(response);

                                        //Storing the Array of JSON String to our JSON Array
                                        resultArraySow = j.getJSONArray("sow_img");
                                        int result=resultArraySow.length();

                                        mSowImgList.clear();
                                        for (int a= 0;a<result;a++){
                                            mSowImgList.add(resultArraySow.get(a).toString());

                                        }
                                        if(mSowImgList.size()==0){
                                            tvwiringitem.setVisibility(TextView.VISIBLE);
                                            vwscrollwiring.setVisibility(HorizontalScrollView.GONE);

                                        }else{
                                            tvwiringitem.setVisibility(TextView.GONE);
                                            vwscrollwiring.setVisibility(HorizontalScrollView.VISIBLE);

                                        }



                                        parentLinearLayoutSow = (LinearLayout) findViewById(R.id.parent_linear_picture);
                                        parentLinearLayoutSow.removeAllViews();
                                        for(int i =0;i<mSowImgList.size();i++){
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View rowView = inflater.inflate(R.layout.pict_sow, null);

                                            DisplayMetrics displayMetrics = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                            final int height = displayMetrics.heightPixels;
                                            final int width = displayMetrics.widthPixels;

                                            final String imageUri = mSowImgList.get(i).toString();
                                            Picasso.with(getApplicationContext()).invalidate(imageUri);
                                            Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
                                            mIcon = rowView.findViewById(R.id.ivIcon);
                                            Picasso.with(getApplicationContext()).load(imageUri).into(mIcon);
                                            Picasso.with(getApplicationContext()).invalidate(imageUri);
                                            Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);

                                            mIcon.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewTicket.this);
                                                    View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                                                    ZoomageView photoView = mView.findViewById(R.id.imageView);
                                                    Picasso.with(getApplicationContext()).load(imageUri).into(photoView);
                                                    //photoView.setImageResource(R.drawable.nature);
                                                    Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
                                                    mBuilder.setView(mView);
                                                    AlertDialog mDialog = mBuilder.create();
                                                    mDialog.show();
                                                    mDialog.getWindow().setLayout(width, height);
                                                }
                                            });
                                            parentLinearLayoutSow.addView(rowView);
                                            //parentLinearLayoutSow.removeAllViews();

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
                            params.put("projid", projid);
                            params.put("custid", custid);
                            params.put("sow_id", sow );
                            return params;
                        }

                    };

                    //Creating a request queue
                    RequestQueue requestQueuetl = Volley.newRequestQueue(NewTicket.this);
                    requestQueuetl.add(stringRequesttl);




                }else{
                    ARreqopt.clear();
                    String siteid=spsiteid.getItemAtPosition(position).toString();
                    String sow=getSitesow(position);
                    getReqopt(siteid,sow,email,custid,projid,usertype);
                    lhidden.setVisibility(LinearLayout.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        imgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    attachmentbox.setVisibility(LinearLayout.VISIBLE);
                }
                else {
                    attachmentbox.setVisibility(LinearLayout.GONE);
                    ARuri.clear();
                    ARimagename.clear();
                    ARimage.clear();
                    ARencoded.clear();
                    filename.setText(ARimagename.toString());
                }
            }
        });




        btsubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                //onBackPressed();
                if(rdgroupsl.getCheckedRadioButtonId()>0&&!spsiteid.getSelectedItem().toString().equalsIgnoreCase("Select site id...")&&!spreqopt.getSelectedItem().toString().equalsIgnoreCase("Select Request Type...")&&etmsgnew.getText().toString().trim().length() >= 10) {

                    Random rand = new Random();
                    final String reqid = Integer.toString(rand.nextInt(10000) + 1);
                    final String requestoption = spreqopt.getSelectedItem().toString();

                    final String newmessage = etmsgnew.getText().toString();

                    final String siteid = spsiteid.getSelectedItem().toString();
                    //final String sitedetail = tvsitedetail.getText().toString();
                    final String sitename = tvsitename_g.getText().toString(); // "Before"
                    final String sow = tvsow_g.getText().toString(); // "After"
                    final String mkdir = Integer.toString(ARuri.size());
                    final String t_target_asp_id="All";

                    //Toast.makeText(NewTicket.this, "Buat Folder"+mkdir, Toast.LENGTH_LONG).show();

                    //pilih radio button yang ada di radio button group
                    int selectedId = rdgroupsl.getCheckedRadioButtonId();

                    // mencari radio button
                    rbtnsl = (RadioButton) findViewById(selectedId);
                    String slyesornot = rbtnsl.getText().toString();
                    // Toast.makeText(NewTicket.this, siteid+sitename+sow+projid+custid+email+requestoption+slyesornot+newmessage,Toast.LENGTH_LONG).show();

                    HashMap<String, String> prodHashMap = new HashMap<String, String>();
                    prodHashMap.put("t_target_asp_id", t_target_asp_id);
                    prodHashMap.put("t_target_id", tgtGrpString);



                    // now here we convert this list array into json string

                    Gson gson=new Gson();

                    final String ar_mid=gson.toJson(midArray); // dataarray is list aaray
                    final String ar_target_grup_string=gson.toJson(prodHashMap);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        final ProgressDialog progressDialog1 = ProgressDialog.show(NewTicket.this, "Checking Status", "Please wait a moment", true);
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {

                                    JSONObject user = jsonResponse.getJSONObject("user");
                                    String path_ticket_id = user.getString("ticket_id");
                                    //Toast.makeText(NewTicket.this, path_ticket_id, Toast.LENGTH_LONG).show();

                                    if(!path_ticket_id.equalsIgnoreCase("null")){
                                        //upload gambar
                                        //Toast.makeText(NewTicket.this, "UPLOAD PICTURE", Toast.LENGTH_LONG).show();
                                        String urisize=Integer.toString(ARuri.size());
                                        imagepath = path_ticket_id;
                                        //Toast.makeText(NewTicket.this, urisize,Toast.LENGTH_LONG).show();
                                        //Toast.makeText(NewTicket.this, ARuri.toString(),Toast.LENGTH_LONG).show();
                                        new Encode_image().execute();

                                    }


                                    progressDialog1.dismiss();

                                    Intent intent = new Intent(NewTicket.this, MainActivity.class);
                                    Toast.makeText(NewTicket.this, "Ticket Sent", Toast.LENGTH_LONG).show();
                                    NewTicket.this.startActivity(intent);
                                    final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                                    final Intent submit_intent = new Intent(getApplicationContext(), SentTicket.class);

                                    submit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 501, submit_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    String CHANNEL_ID = "my_channel_03";// The id of the channel.
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        CharSequence name = "notif2";// The user-visible name of the channel.
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                        notificationManager.createNotificationChannel(mChannel);
                                    }
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                            .setContentIntent(pendingIntent)
                                            .setSmallIcon(R.drawable.eid)
                                            .setContentTitle("New Ticket For "+ requestoption+ " SUBMITTED")
                                            .setContentText("Please open IXT application to check the ticket")
                                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setDeleteIntent(pendingIntent)
                                            .setOngoing(true)
                                            .setChannelId(CHANNEL_ID)
                                            .setAutoCancel(true);

                                    Random rand = new Random();

                                    int n = rand.nextInt(1000) + 1;
                                    notificationManager.notify(n, builder.build());
                                    //progressDialog1.dismiss();
                                } else {
                                    JSONObject user = jsonResponse.getJSONObject("user");
                                    String message = user.getString("message");
                                    //String message = response.toString();

                                    progressDialog1.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(NewTicket.this);
                                    String alert="<font color='#FF0000'>"+message.replace("\n","<br />")+"</font>";
                                    builder.setTitle(Html.fromHtml("<font color='#FF0000'>"+"ALERT"+"</font>"))
                                            .setMessage(Html.fromHtml(alert))
                                            .setNegativeButton("Retry", null)
                                            .setIcon(R.drawable.sent_back)
                                            .create()
                                            .show();
                                    //progressDialog1.dismiss();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(NewTicket.this, "Undefined Error Found", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                progressDialog1.dismiss();
                            }
                        }
                    };
                    //Toast.makeText(NewTicket.this,  newmessage+  ar_mid+  event_type+  ar_target_grup_string+  custid+  projid+  email+  mkdir+  cuid+  slyesornot,Toast.LENGTH_LONG).show();
                    NewTicketRequest NewTicketRequest = new NewTicketRequest( newmessage,  ar_mid,  event_type,  ar_target_grup_string,  custid,  projid,  email,  mkdir,  cuid,  slyesornot,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(NewTicket.this);
                    queue.add(NewTicketRequest);

                }else{
                    if(spsiteid.getSelectedItem().toString().equalsIgnoreCase("Select site id...")){
                        Toast.makeText(NewTicket.this, "Select site id...", Toast.LENGTH_LONG).show();
                    }
                    if(spreqopt.getSelectedItem().toString().equalsIgnoreCase("Select Request Type...")){
                        Toast.makeText(NewTicket.this, "Select Request Type...", Toast.LENGTH_LONG).show();
                    }
                    if(!(rdgroupsl.getCheckedRadioButtonId()>0)) {
                        Toast.makeText(NewTicket.this, "Smart Laptop YES/NOT ?", Toast.LENGTH_LONG).show();
                    }
                    if(etmsgnew.getText().toString().trim().length() < 10) {
                        Toast.makeText(NewTicket.this, "Additional Information Minimum 10 Character!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void getReqopt(final String siteid,final String sow,final String email,final String custid,final String projid,final String usertype) {

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Utils.REQ_OPT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultReqopt = j.getJSONArray(Utils.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            /**
                             * closed for req option manual
                             * //fillreqopt(resultReqopt);
                             */
                            fillreqopt(resultReqopt);


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
                params.put("siteid", siteid);
                params.put("usertype", usertype);
                params.put("sow", sow);
                params.put("custid", custid);
                params.put("projid", projid);
                params.put("email", email);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void fillreqopt(JSONArray j) {
        //Traversing through all the items in the json array
        for(int i=-1;i<j.length();i++){
            try {
                if(i==-1){
                    //Adding the name of the student to array list
                    String reqopt="Select Request Type...";
                    ARreqopt.add(reqopt);

                }else {
                    //Getting json object
                    JSONObject json = j.getJSONObject(i);

                    //Adding the name of the student to array list
                    String reqopt=json.getString(Utils.TAG_REQ_OPT);
                    if(reqopt.equalsIgnoreCase("null")){

                    }else {
                        ARreqopt.add(reqopt);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Setting adapter to show the items in the spinner
        spreqopt.setAdapter(new ArrayAdapter<String>(NewTicket.this, android.R.layout.simple_spinner_dropdown_item, ARreqopt));
    }


    private String getEventType(int position) {
        String event="";
        try {
            //Getting object of given index
            JSONObject json = resultReqopt.getJSONObject(position);

            //Fetching name from that object
            event = json.getString(Utils.TAG_EV_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return event;
    }

    private String getTgtGrp(int position) {
        String group="";
        try {
            //Getting object of given index
            JSONObject json = resultReqopt.getJSONObject(position);

            //Fetching name from that object
            group = json.getString(Utils.TAG_TGT_GRP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return group;
    }



    private void getDatasiteid(final String email,final String custid,final String projid) {

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Utils.SITE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultSite = j.getJSONArray(Utils.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getSites(resultSite);
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
                params.put("custid", custid);
                params.put("projid", projid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }



    private void getDatasitestat(final String m_id,final String custid,final String projid) {

        final ProgressDialog progressDialog = ProgressDialog.show(NewTicket.this, "Checking Status", "Please wait a moment", true);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Utils.SITE_STAT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultSiteStat = j.getJSONArray(Utils.SITE_STAT);
                            //Toast.makeText(NewTicket.this, resultSiteStat.get(0).toString(), Toast.LENGTH_LONG).show();
                            int result=resultSiteStat.length();
                            //Toast.makeText(NewTicket.this,Integer.toString(result) , Toast.LENGTH_LONG).show();
                            mTraffictList.clear();
                            for (int a= 0;a<result;a++){
                                mTraffictList.add(resultSiteStat.get(a).toString());
                                //Toast.makeText(NewTicket.this, resultSiteStat.get(a).toString(), Toast.LENGTH_LONG).show();
                            }


                            //Toast.makeText(NewTicket.this, mTraffictList.toString(), Toast.LENGTH_LONG).show();

                            parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_traffic);
                            parentLinearLayout.removeAllViews();
                            for(int i =0;i<mTraffictList.size();i+=2){
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View rowView = inflater.inflate(R.layout.traffictlight, null);
                                // Add the new row before the add field button.

                                tv_ms_item=(TextView)rowView.findViewById(R.id.tv_ms_item);
                                iv_ms=(ImageView) rowView. findViewById(R.id.iv_ms);
                                tv_ms_item.setText(mTraffictList.get(i).toString().toUpperCase());
                                iv_ms.setVisibility(View.VISIBLE);

                                final String stat=mTraffictList.get(i+1).toString();
                                // Toast.makeText(NewTicket.this, stat, Toast.LENGTH_LONG).show();

                                if(stat.equalsIgnoreCase("INCOMPLT")){
                                    iv_ms.setImageResource(R.drawable.incomplete);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("COMPLETE")){
                                    iv_ms.setImageResource(R.drawable.complete);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("EXPIRED")){
                                    iv_ms.setImageResource(R.drawable.expired);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("FAILED")){
                                    iv_ms.setImageResource(R.drawable.failed);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("N-A")){
                                    iv_ms.setImageResource(R.drawable.na);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("NOT-REQ")){
                                    iv_ms.setImageResource(R.drawable.notreq);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("ON-GOING")){
                                    iv_ms.setImageResource(R.drawable.ongoing);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("PASSED")){
                                    iv_ms.setImageResource(R.drawable.passed);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("SUBMITTED")){
                                    iv_ms.setImageResource(R.drawable.submittedack);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("IN-REVIEW")){
                                    iv_ms.setImageResource(R.drawable.inreview);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("REJECTED")){
                                    iv_ms.setImageResource(R.drawable.rejected);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }else if (stat.equalsIgnoreCase("TIME-OUT")){
                                    iv_ms.setImageResource(R.drawable.timeout);
                                    iv_ms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                                                    .setCancelable(true)
                                                    .setDismissOnClick(false)
                                                    .setCornerRadius(20f)
                                                    .setGravity(Gravity.BOTTOM)
                                                    .setText(stat);
                                            builder.show();
                                        }
                                    });
                                }


                                parentLinearLayout.addView(rowView);
                                //parentLinearLayout.removeAllViews();

                            }

                            progressDialog.dismiss();

                            //Calling method getStudents to get the students from the JSON Array
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
                params.put("m_id", m_id);
                params.put("custid", custid);
                params.put("projid", projid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue.add(stringRequest);




    }






    private void getSites(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=-1;i<j.length();i++){
            try {
                if(i==-1){
                    //Adding the name of the student to array list
                    String siteid="Select site id...";
                    String sitename="Select site id...";
                    String sow="Select site id...";


                    ARsiteid.add(new sites(siteid,sitename,sow));

                }else {
                    //Getting json object
                    JSONObject json = j.getJSONObject(i);

                    //Adding the name of the student to array list
                    String siteid=json.getString(Utils.TAG_SITE_ID);
                    String sitename=json.getString(Utils.TAG_SITE_NAME);
                    String sow=json.getString(Utils.TAG_SOW);

                    ARsiteid.add(new sites(siteid,sitename,sow));
                    // ARsiteString.add(siteaddress);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        //Setting adapter to show the items in the spinner
        spsiteid.setAdapter(new ArrayAdapter<sites>(NewTicket.this, android.R.layout.simple_spinner_dropdown_item, ARsiteid));
    }


    //Method to get student name of a particular position
    private String getSitesow(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Utils.TAG_SOW);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }


    //Doing the same with this method as we did with getName()
    private String getSitename(int position){
        String course="";
        try {
            JSONObject json = resultSite.getJSONObject(position);
            course = json.getString(Utils.TAG_SITE_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }


    private String getMilestone(int position){
        String milestone="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            milestone = json.getString(Utils.TAG_MILESTONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return milestone;
    }

    private String getProgramid(int position){
        String progid="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            progid = json.getString(Utils.TAG_PROG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return progid;
    }

    private String getDF(int position){
        String df="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            df = json.getString(Utils.TAG_DF);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return df;
    }

    private String getDFexpstat(int position){
        String dfexpstat="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            dfexpstat = json.getString(Utils.TAG_DFEXP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return dfexpstat;
    }

    private String getCF(int position){
        String cf="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            cf = json.getString(Utils.TAG_CF);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return cf;
    }

    private String getRSA(int position){
        String rsa="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            rsa = json.getString(Utils.TAG_RSA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return rsa;
    }

    private String getINT(int position){
        String getint="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            getint = json.getString(Utils.TAG_INT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return getint;
    }

    private String getOV(int position){
        String getov="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            getov = json.getString(Utils.TAG_OV);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return getov;
    }

    private String getmid(int position){
        String mid="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            mid = json.getString(Utils.TAG_M_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return mid;
    }

    private String getactid(int position){
        String act="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            act = json.getString(Utils.TAG_LACT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return act;
    }

    private String getdatetgt(int position){
        String date="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            date = json.getString(Utils.TAG_LDATE_TGT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return date;
    }


    private String gettech(int position){
        String tech="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            tech = json.getString(Utils.TAG_LTECH);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return tech;
    }

    private String getmodule(int position){
        String module="";
        try {
            //Getting object of given index
            JSONObject json = resultSite.getJSONObject(position);

            //Fetching name from that object
            module = json.getString(Utils.TAG_LMODULE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return module;
    }



/*
    @Override
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(NewTicket.this);
        ab.setTitle("IXT Application Message");
        ab.setMessage("are you sure to cancel create ticket?");
        ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if you want to finish just current activity
                Intent setIntent = new Intent(NewTicket.this,MainActivity.class);
                startActivity(setIntent);
                NewTicket.this.finish();
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
                                    final String updlink = json.getString(Utils.TAG_UPDLINK);

                                    if(!message.isEmpty()&&!updvers.isEmpty()){
                                        if(!updvers.equalsIgnoreCase(versionCode)){

                                            AlertDialog.Builder ab = new AlertDialog.Builder(NewTicket.this);
                                            ab.setCancelable(false);
                                            ab.setTitle("IXT Application Message");
                                            ab.setMessage(message);
                                            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    goToUrl(updlink);

                                                }
                                            });
                                            ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    ActivityCompat.finishAffinity(NewTicket.this);
                                                    NewTicket.this.finish();
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
    public void onPause() {
        //finish();
        //dialog.dismiss();
        super.onPause();

    }


    /**
     * Below is module for upload image and photos to folder ticket
     *
     */


    private void getFileUri() {
        Random rand = new Random(); int value = rand.nextInt(50);
        image_name = "Image"+Integer.toString(ARuri.size()+1)+".jpg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + image_name
        );

        file_uri = FileProvider.getUriForFile(NewTicket.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            //String path = file_uri.getPath();
            //String idStr = path.substring(path.lastIndexOf('/') + 1);
            String idStr = "Image"+Integer.toString(ARuri.size()+1)+".jpg";
            ARimagename.add(idStr);
            ARuri.add(file_uri);
            filename.setText(ARimagename.toString());

        }

        if (requestCode == 20 && resultCode == RESULT_OK) {

            Uri originalUri = data.getData();

            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

            /* now extract ID from Uri path using getLastPathSegment() and then split with ":"
            then call get Uri to for Internal storage or External storage for media I have used getUri()
            */
/*
            String id = originalUri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA };
            final String imageOrderBy = null;

            Uri uri = getUri();
            String selectedImagePath = "path";

            Cursor imageCursor = managedQuery(uri, imageColumns,
                    MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);

            if (imageCursor.moveToFirst()) {
                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            Log.e("path",selectedImagePath ); // use selectedImagePath

            //Toast.makeText(NewTicket.this,selectedImagePath,Toast.LENGTH_LONG).show();

            Uri myUri = Uri.parse(selectedImagePath);
*/
            //image_name = "image_selected.jpg";
            // String path = myUri.getPath();
            // String idStr = path.substring(path.lastIndexOf('/') + 1);
            String idStr = "Image"+Integer.toString(ARuri.size()+1)+".jpg";
            ARimagename.add(idStr);
            ARuri.add(originalUri);
            filename.setText(ARimagename.toString());
        }
    }


    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(NewTicket.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("are you sure to cancel create ticket?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //if you want to finish just current activity
                    Intent setIntent = new Intent(NewTicket.this,MainActivity.class);
                    startActivity(setIntent);
                    NewTicket.this.finish();
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
            /*
            userSession.setLoggedin(true);
            startActivity(new Intent(NewTicket.this, NewTicket.class));
            finish();
            */
        } else if (id == R.id.nav_drop_in) {
            userSession.setLoggedin(true);
            startActivity(new Intent(NewTicket.this, Inbox.class));
            finish();
        } else if (id == R.id.nav_working_canvas) {
            userSession.setLoggedin(true);
            startActivity(new Intent(NewTicket.this, WcList.class));
            finish();
        }else if (id == R.id.nav_ongoing) {
            userSession.setLoggedin(true);
            startActivity(new Intent(NewTicket.this, SentTicket.class));
            finish();
        } else if (id == R.id.nav_closed) {
            userSession.setLoggedin(true);
            startActivity(new Intent(NewTicket.this, History.class));
            finish();
        } else if (id == R.id.nav_about) {
            userSession.setLoggedin(true);
            startActivity(new Intent(NewTicket.this, About.class));
            finish();
        }else if (id == R.id.nav_logout) {

            AlertDialog.Builder ab = new AlertDialog.Builder(NewTicket.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("Are you sure to Log Out from application?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    sendRegistrationToServer("",email);
                    userSession.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(NewTicket.this, Login.class));
                    ActivityCompat.finishAffinity(NewTicket.this);
                    NewTicket.this.finish();
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

    private class Encode_image extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
            progressDialog= ProgressDialog.show(NewTicket.this, "uploading image","Please wait until finish uploading", true);
            //do initialization of required objects objects here
        };
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                for (int x = 0; x < ARuri.size(); x++) {


                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    ContentResolver cr = getContentResolver();
                    InputStream input = null;
                    InputStream input1 = null;
                    try {
                        input = cr.openInputStream(ARuri.get(x));
                        BitmapFactory.decodeStream(input, null, bmOptions);
                        if (input != null) {
                            input.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;
                    try {
                        input1 = cr.openInputStream(ARuri.get(x));
                        Bitmap takenImage = BitmapFactory.decodeStream(input1);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        takenImage.compress(Bitmap.CompressFormat.JPEG, 40, stream);
                        takenImage.recycle();

                        byte[] array = stream.toByteArray();
                        encoded_string = Base64.encodeToString(array, 0);
                        ARencoded.add(encoded_string);
                        if (input1 != null) {
                            input1.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*
                    bitmap = BitmapFactory.decodeFile(ARuri.get(x).getPath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
                    bitmap.recycle();

                    byte[] array = stream.toByteArray();
                    encoded_string = Base64.encodeToString(array, 0);
                    ARencoded.add(encoded_string);
                    */
                }
            }catch (Exception e){
                Toast.makeText(NewTicket.this, "still uploading on background",Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            for (int x = 0; x < ARencoded.size(); x++){
                makeRequest(ARencoded.get(x),ARimagename.get(x),imagepath);
            }
            ARuri.clear();
            ARimagename.clear();
            ARimage.clear();
            ARencoded.clear();
            filename.setText(ARimagename.toString());
            progressDialog.dismiss();
        }
    }

    private void makeRequest(final String encoded,final String imagename,final String imagepath) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_files/photos/connection.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded);
                map.put("image_name",imagename);
                map.put("image_path",imagepath);

                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    /*end of image module*/




}
