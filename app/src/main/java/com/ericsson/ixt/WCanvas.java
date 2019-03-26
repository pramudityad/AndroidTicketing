package com.ericsson.ixt;

import android.annotation.TargetApi;
import android.content.ContentResolver;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by ericsson on 11/15/2017.
 */

public class WCanvas extends AppCompatActivity  {
    private TextView tvreqid,
            tvpicname, tvsiteid,tvsitename,etmsgpbs,
            tvsubmittime;
    private Button btsubmit;
    private UserInfo userInfo;
    private UserSession userSession;
    //JSON Array
    private JSONArray resultArray;
    private JSONArray resultArray_rp;
    //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
    private JSONArray resultNotif;

    private EditText etmsgnew;


    private List<tickettimeline> mTimelineList;

    private TextView tvtflowuserid,tvtimestamp,tveventcomment;

    private JSONArray resultArraytl;

    private LinearLayout parentLinearLayout_cb;
    private LinearLayout parentLinearLayout_et;
    private LinearLayout parentLinearLayout_lt;
    private LinearLayout parentLinearLayout_fc;

    private List<Wc_checkbox> mWccheckbox;
    private CheckBox checkbox_item;
    private TextView tv_cb_item;

    private List<Wc_edittext> mWcedittext;

    private List<Wc_longtext> mWclongtext;

   // private List<Wc_filecontainer> mWcfilecontainer;

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
    private String project_id=null;
    private String customer_id=null;
    private ImageView img;
    private LinearLayout parentLinearLayout;
    /*image uploader variable*/

    private ArrayList<String> ARimagetext=new ArrayList<String>();





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_canvas);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/ericssoncapitaltt-webfont.ttf");

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

        project_id=projid;
        customer_id=custid;

        checkUpdate(email);
        Intent intent = getIntent();
        final String ticketid = intent.getStringExtra("ticketid");
        final String requestor = intent.getStringExtra("requestor");
        final String siteid = intent.getStringExtra("siteid");
        final String sitename = intent.getStringExtra("sitename");
        final String submittime = intent.getStringExtra("submittime");
        final String evactivity = intent.getStringExtra("evactivity");
        final String mid = intent.getStringExtra("mid");
        final String r_id =intent.getStringExtra("r_id");
        //Toast.makeText(WCanvas.this, r_id, Toast.LENGTH_LONG).show();




        /**
         * image uploader initiate*/
        ARimage = new ArrayList<String>();
        ARimagename = new ArrayList<String>();
        ARencoded = new ArrayList<String>();
        ARuri = new ArrayList<Uri>();

        final ArrayList<Wc_checkbox> mWccheckbox= new ArrayList<>();
        final ArrayList<Wc_edittext> mWcedittext= new ArrayList<>();
        final ArrayList<Wc_longtext> mWclongtext= new ArrayList<>();
        final ArrayList<Wc_filecontainer> mWcFilecontainer= new ArrayList<>();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }



        tvreqid=(TextView) findViewById(R.id.tvreqid);
        tvpicname=(TextView) findViewById(R.id.tvpicname);
        tvsiteid=(TextView) findViewById(R.id.tvsiteid);
        tvsitename=(TextView) findViewById(R.id.tvsitename);
        tvsubmittime=(TextView) findViewById(R.id.tvsubmittime);
        etmsgpbs=(TextView) findViewById(R.id.etmsgpbs);
        btsubmit=(Button) findViewById(R.id.btsubmit);
        etmsgnew=(EditText)findViewById(R.id.etmsgnew);

/*
        tvreqid.setTypeface(font);
        tvpicname.setTypeface(font);
        tvsiteid.setTypeface(font);
        tvsitename.setTypeface(font);
        tvsubmittime.setTypeface(font);
        etmsgpbs.setTypeface(font);
        btsubmit.setTypeface(font);
        */







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


        tvreqid.setText(ticketid);
        tvpicname.setText(requestor);
        tvsiteid.setText(siteid);
        tvsitename.setText(sitename);
        tvsubmittime.setText(submittime);

        StringRequest stringRequest_rp = new StringRequest(Request.Method.POST,"https://android.eid-tools.com/other/postjson/postjson.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(InboxTicketDetail.this, "masuk response", Toast.LENGTH_LONG).show();
                        Log.e("path","masuk onrespond" ); // use selectedImagePath
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            resultArray_rp = j.getJSONArray("result_rp");
                            //String pbs="problem";


                            for(int i=0;i<resultArray_rp.length();i++){
                                try {
                                    //Getting json object


                                    JSONObject json =resultArray_rp.getJSONObject(i);
                                    //Log.e("respond",Integer.toString(json.names().length()));
                                    //Log.e("respondfill",json.getString(json.names().toString()) );

                                    String dt_type=json.getString("r_data_type");
                                    String sequence=json.getString("r_sequence");
                                    String field_name=json.getString("r_field_name");
                                    String placeholder=json.getString("r_placeholder");
                                    Log.e("respondfill",dt_type+" "+sequence+" "+field_name+" "+placeholder );
                                    if(dt_type.equalsIgnoreCase("CB")){
                                        CheckBox cb_canvas;
                                        cb_canvas= new CheckBox(WCanvas.this);
                                        mWccheckbox.add(new Wc_checkbox(cb_canvas,dt_type,field_name,placeholder,sequence,""));
                                    }else if(dt_type.equalsIgnoreCase("ET")){
                                        EditText et_canvas;
                                        et_canvas= new EditText(WCanvas.this);
                                        mWcedittext.add(new Wc_edittext(et_canvas,dt_type,field_name,placeholder,sequence,""));
                                    }else if(dt_type.equalsIgnoreCase("LC")){
                                        EditText lt_canvas;
                                        lt_canvas= new EditText(WCanvas.this);
                                        mWclongtext.add(new Wc_longtext(lt_canvas,dt_type,field_name,placeholder,sequence,""));
                                    }else if(dt_type.equalsIgnoreCase("FC")){
                                        Button uriadd,imageselect,clearpic;
                                        TextView filename;
                                            uriadd= new Button(WCanvas.this);
                                            imageselect= new Button(WCanvas.this);
                                            clearpic= new Button(WCanvas.this);
                                            filename= new TextView(WCanvas.this);
                                        mWcFilecontainer.add(new Wc_filecontainer(filename, uriadd,imageselect,clearpic,dt_type,field_name,placeholder,sequence,""));
                                    }

                                    //Adding the name of the student to array list
                                    //pbs=json.getString(Utils.TAG_PBS);
                                    //action=json.getString(Utils.TAG_ACTION);
                                    //  Toast.makeText(InboxTicketDetail.this, pbs, Toast.LENGTH_LONG).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            Log.e("respondfill",Integer.toString(mWccheckbox.size()) );

                            parentLinearLayout_cb = (LinearLayout) findViewById(R.id.layout_checkbox);
                            Log.e("layout",Integer.toString(mWccheckbox.size()) );
                            //Toast.makeText(WCanvas.this, Integer.toString(mWccheckbox.size()), Toast.LENGTH_LONG).show();
                            for(int k =0;k<mWccheckbox.size();k++){
                                mWccheckbox.get(k).getCb_canvas();
                                mWccheckbox.get(k).setCb_item("0");
                                mWccheckbox.get(k).getCb_canvas().setText(mWccheckbox.get(k).getCb_placeholder());
                                parentLinearLayout_cb.addView( mWccheckbox.get(k).getCb_canvas());
                            }

                            TextView tv_et_list;
                            parentLinearLayout_et = (LinearLayout) findViewById(R.id.layout_edit_text);
                            //Toast.makeText(WCanvas.this, Integer.toString(mWcedittext.size()), Toast.LENGTH_LONG).show();
                            for(int m =0;m<mWcedittext.size();m++){
                                tv_et_list= new TextView(WCanvas.this);
                                tv_et_list.setText(mWcedittext.get(m).getEt_placeholder());
                                mWcedittext.get(m).getEt_canvas();
                                mWcedittext.get(m).setEt_item("");
                                parentLinearLayout_et.addView( tv_et_list);
                                parentLinearLayout_et.addView( mWcedittext.get(m).getEt_canvas());

                            }

                            TextView tv_lt_list;
                            parentLinearLayout_lt = (LinearLayout) findViewById(R.id.layout_log_container);
                            //Toast.makeText(WCanvas.this, Integer.toString(mWclongtext.size()), Toast.LENGTH_LONG).show();
                            for(int w =0;w<mWclongtext.size();w++){
                                tv_lt_list= new TextView(WCanvas.this);
                                tv_lt_list.setText(mWclongtext.get(w).getLt_placeholder());

                                mWclongtext.get(w).getLt_canvas();
                                mWclongtext.get(w).getLt_canvas().setSingleLine(false);
                                mWclongtext.get(w).getLt_canvas().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                                mWclongtext.get(w).getLt_canvas().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                mWclongtext.get(w).getLt_canvas().setLines(5);
                                mWclongtext.get(w).getLt_canvas().setMaxLines(Integer.MAX_VALUE);
                                mWclongtext.get(w).getLt_canvas().setVerticalScrollBarEnabled(true);
                                mWclongtext.get(w).getLt_canvas().setMovementMethod(ScrollingMovementMethod.getInstance());
                                mWclongtext.get(w).getLt_canvas().setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                                parentLinearLayout_lt.addView( tv_lt_list);
                                parentLinearLayout_lt.addView( mWclongtext.get(w).getLt_canvas());
                            }

                            TextView tv_fc_list;
                            parentLinearLayout_fc = (LinearLayout) findViewById(R.id.layout_file_container);
                            //Toast.makeText(WCanvas.this, Integer.toString(mWcFilecontainer.size()), Toast.LENGTH_LONG).show();
                            for(int t =0;t<mWcFilecontainer.size();t++){
                                tv_fc_list= new TextView(WCanvas.this);
                                tv_fc_list.setText(mWcFilecontainer.get(t).getFc_placeholder());
                                mWcFilecontainer.get(t).getFilename();
                                mWcFilecontainer.get(t).getImageselect().setText("Select Image");
                                mWcFilecontainer.get(t).getUriadd().setText("take photo");
                                mWcFilecontainer.get(t).getClearpic().setText("CLear");
                                parentLinearLayout_fc.addView(tv_fc_list);
                                parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getFilename());
                                parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getImageselect());
                                parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getUriadd());
                                parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getClearpic());
                            }


                            //Calling method getStudents to get the students from the JSON Array
                            //getTimeline(resultArray);

                            for(int a=0;a<mWcFilecontainer.size();a++){
                                /**
                                 *start button for image uploader*/

                                final int finalA = a;
                                mWcFilecontainer.get(a).getClearpic().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ARuri.clear();
                                        ARimagename.clear();
                                        ARimage.clear();
                                        ARencoded.clear();
                                        mWcFilecontainer.get(finalA).getFilename().setText(ARimagename.toString());
                                    }
                                });
                                mWcFilecontainer.get(a).getUriadd().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        getFileUri();
                                        i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                                        startActivityForResult(i, 10);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mWcFilecontainer.get(finalA).getFilename().setText(ARimagename.toString());

                                            }
                                        },6000);

                                    }
                                });
                                mWcFilecontainer.get(a).getImageselect().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        i.setType("image/*");
                                        i.setAction(Intent.ACTION_OPEN_DOCUMENT);//
                                        startActivityForResult(Intent.createChooser(i, "Select Image"),20);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mWcFilecontainer.get(finalA).getFilename().setText(ARimagename.toString());

                                            }
                                        },3000);
                                    }
                                });


                                /**
                                 * end of button image uploader*/

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.e("path","gagal masuk onrespond" ); // use selectedImagePath
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
                params.put("r_id", r_id);
                params.put("evactivity", evactivity);
                params.put("projid", projid);
                params.put("custid", custid);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue_rp = Volley.newRequestQueue(this);

        requestQueue_rp.add(stringRequest_rp);


/*
        final ArrayList<Wc_checkbox> mWccheckbox= new ArrayList<>();
        CheckBox cb_canvas;
        for(int i = 0; i < 4; i++) {
            cb_canvas= new CheckBox(this);
            mWccheckbox.add(new Wc_checkbox(cb_canvas,"checkbox " + Integer.toString(i),"cb" + Integer.toString(i),"Jelaskan situasi di site",Integer.toString(i),""));
        }

        parentLinearLayout_cb = (LinearLayout) findViewById(R.id.layout_checkbox);
        Log.e("layout",Integer.toString(mWccheckbox.size()) );
        Toast.makeText(WCanvas.this, Integer.toString(mWccheckbox.size()), Toast.LENGTH_LONG).show();
        for(int j =0;j<mWccheckbox.size();j++){
            mWccheckbox.get(j).getCb_canvas();
            mWccheckbox.get(j).setCb_item("0");
            mWccheckbox.get(j).getCb_canvas().setText(mWccheckbox.get(j).getCb_text()+" "+mWccheckbox.get(j).getCb_placeholder());
            parentLinearLayout_cb.addView( mWccheckbox.get(j).getCb_canvas());
        }
         */


/*
        final ArrayList<Wc_edittext> mWcedittext= new ArrayList<>();
        EditText et_canvas;
        for(int i = 0; i < 4; i++) {
            et_canvas= new EditText(this);
            mWcedittext.add(new Wc_edittext(et_canvas,"checkbox " + Integer.toString(i),"et"+ Integer.toString(i),"Jelaskan situasi di site",Integer.toString(i),""));
        }

        TextView tv_et_list;
        parentLinearLayout_et = (LinearLayout) findViewById(R.id.layout_edit_text);
        //Toast.makeText(WCanvas.this, Integer.toString(mWcedittext.size()), Toast.LENGTH_LONG).show();
        for(int j =0;j<mWcedittext.size();j++){
            tv_et_list= new TextView(this);
            tv_et_list.setText(mWcedittext.get(j).getEt_placeholder());
            mWcedittext.get(j).getEt_canvas();
            mWcedittext.get(j).setEt_item("");
            parentLinearLayout_et.addView( tv_et_list);
            parentLinearLayout_et.addView( mWcedittext.get(j).getEt_canvas());

        }
  */

/*
        final ArrayList<Wc_longtext> mWclongtext= new ArrayList<>();
        EditText lt_canvas;
        for(int q = 0; q < 4; q++) {
            lt_canvas= new EditText(this);
            mWclongtext.add(new Wc_longtext(lt_canvas,"checkbox " + Integer.toString(q),"lt"+ Integer.toString(q),"Jelaskan situasi di site",Integer.toString(q),""));
        }

        TextView tv_lt_list;
        parentLinearLayout_lt = (LinearLayout) findViewById(R.id.layout_log_container);
        //Toast.makeText(WCanvas.this, Integer.toString(mWclongtext.size()), Toast.LENGTH_LONG).show();
        for(int w =0;w<mWclongtext.size();w++){
            tv_lt_list= new TextView(this);
            tv_lt_list.setText(mWclongtext.get(w).getLt_placeholder());

            mWclongtext.get(w).getLt_canvas();
            mWclongtext.get(w).getLt_canvas().setSingleLine(false);
            mWclongtext.get(w).getLt_canvas().setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            mWclongtext.get(w).getLt_canvas().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            mWclongtext.get(w).getLt_canvas().setLines(5);
            mWclongtext.get(w).getLt_canvas().setMaxLines(Integer.MAX_VALUE);
            mWclongtext.get(w).getLt_canvas().setVerticalScrollBarEnabled(true);
            mWclongtext.get(w).getLt_canvas().setMovementMethod(ScrollingMovementMethod.getInstance());
            mWclongtext.get(w).getLt_canvas().setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            parentLinearLayout_lt.addView( tv_lt_list);
            parentLinearLayout_lt.addView( mWclongtext.get(w).getLt_canvas());
        }
           */

/*
        final ArrayList<Wc_filecontainer> mWcFilecontainer= new ArrayList<>();
        Button uriadd,imageselect,clearpic;
        TextView filename;
        for(int r = 0; r < 4; r++) {
            uriadd= new Button(this);
            imageselect= new Button(this);
            clearpic= new Button(this);
            filename= new TextView(this);
            mWcFilecontainer.add(new Wc_filecontainer(filename, uriadd,imageselect,clearpic,"checkbox " + Integer.toString(r),"fc"+ Integer.toString(r),"Jelaskan situasi di site",Integer.toString(r),""));
        }

        parentLinearLayout_fc = (LinearLayout) findViewById(R.id.layout_file_container);
        //Toast.makeText(WCanvas.this, Integer.toString(mWcFilecontainer.size()), Toast.LENGTH_LONG).show();
        for(int t =0;t<mWcFilecontainer.size();t++){
            mWcFilecontainer.get(t).getFilename();
            mWcFilecontainer.get(t).getImageselect().setText("Select Image");
            mWcFilecontainer.get(t).getUriadd().setText("take photo");
            mWcFilecontainer.get(t).getClearpic().setText("CLear");
            parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getFilename());
            parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getImageselect());
            parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getUriadd());
            parentLinearLayout_fc.addView(mWcFilecontainer.get(t).getClearpic());
        }
          */

        /*
        HashMap<String, ArrayList> prodHashMap = new HashMap<String, ArrayList>();
        prodHashMap.put("t_target_asp_id", t_target_asp_id);
*/

        /*
        for(int a=0;a<mWcFilecontainer.size();a++){


            final int finalA = a;
            mWcFilecontainer.get(a).getClearpic().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ARuri.clear();
                    ARimagename.clear();
                    ARimage.clear();
                    ARencoded.clear();
                    mWcFilecontainer.get(finalA).getFilename().setText(ARimagename.toString());
                }
            });
            mWcFilecontainer.get(a).getUriadd().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    getFileUri();
                    i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                    startActivityForResult(i, 10);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mWcFilecontainer.get(finalA).getFilename().setText(ARimagename.toString());

                        }
                    },2000);

                }
            });
            mWcFilecontainer.get(a).getImageselect().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ACTION_PICK,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_OPEN_DOCUMENT);//
                    startActivityForResult(Intent.createChooser(i, "Select Image"),20);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mWcFilecontainer.get(finalA).getFilename().setText(ARimagename.toString());

                        }
                    },2000);
                }
            });




        }
        */






        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(WCanvas.this,ARencoded.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(WCanvas.this,ARuri.toString(),Toast.LENGTH_LONG).show();
                imagepath = ticketid;
                new Encode_image_canvas().execute();

                HashMap<String, String> prodHashMap = new HashMap<String, String>();



                for(int x=0;x<mWccheckbox.size();x++){
                    if(mWccheckbox.get(x).getCb_canvas().isChecked()){
                        mWccheckbox.get(x).setCb_item("1");
                        prodHashMap.put(mWccheckbox.get(x).getCb_fieldname(),mWccheckbox.get(x).getCb_item());
                       // Toast.makeText(WCanvas.this,mWccheckbox.get(x).getCb_text(), Toast.LENGTH_LONG).show();
                    }else {
                        prodHashMap.put(mWccheckbox.get(x).getCb_fieldname(),mWccheckbox.get(x).getCb_item());
                    }
                }

                for(int t=0;t<mWcedittext.size();t++){
                    if(!mWcedittext.get(t).getEt_canvas().getText().toString().equalsIgnoreCase("")){
                        mWcedittext.get(t).setEt_item(mWcedittext.get(t).getEt_canvas().getText().toString());
                        prodHashMap.put(mWcedittext.get(t).getEt_fieldname(),mWcedittext.get(t).getEt_item());
                        //  Toast.makeText(WCanvas.this,mWcedittext.get(t).getEt_canvas().getText(), Toast.LENGTH_LONG).show();
                    }else{
                        prodHashMap.put(mWcedittext.get(t).getEt_fieldname(),mWcedittext.get(t).getEt_item());
                    }
                }

                for(int y=0;y<mWclongtext.size();y++){
                    if(!mWclongtext.get(y).getLt_canvas().getText().toString().equalsIgnoreCase("")){
                        mWclongtext.get(y).setLt_item(mWclongtext.get(y).getLt_canvas().getText().toString());
                        prodHashMap.put(mWclongtext.get(y).getLt_fieldname(),mWclongtext.get(y).getLt_item());
                        //  Toast.makeText(WCanvas.this,mWcedittext.get(t).getEt_canvas().getText(), Toast.LENGTH_LONG).show();
                    }else{
                        prodHashMap.put(mWclongtext.get(y).getLt_fieldname(),mWclongtext.get(y).getLt_item());
                    }
                }

                for(int u=0;u<mWcFilecontainer.size();u++){
                    if(!mWcFilecontainer.get(u).getFilename().getText().toString().equalsIgnoreCase("")){
                        mWcFilecontainer.get(u).setFc_item(mWcFilecontainer.get(u).getFilename().getText().toString());
                        prodHashMap.put(mWcFilecontainer.get(u).getFc_fieldname(),"assets/report/"+custid+"_"+projid+"/"+ticketid+"/");
                        //  Toast.makeText(WCanvas.this,mWcedittext.get(t).getEt_canvas().getText(), Toast.LENGTH_LONG).show();
                    }else{
                        prodHashMap.put(mWcFilecontainer.get(u).getFc_fieldname(),"");
                    }
                }

                //prodHashMap.put("comments",etmsgnew.getText().toString());
                String usercomment=etmsgnew.getText().toString();

                Gson gson=new Gson();
                final String ar_report_string=gson.toJson(prodHashMap);
                //Toast.makeText(WCanvas.this,ar_report_string, Toast.LENGTH_LONG).show();
                Log.e("path",ar_report_string ); // use selectedImagePath
                Log.e("path",mid ); // use selectedImagePath
                Log.e("path",usercomment); // use selectedImagePath
                Log.e("path",compid); // use selectedImagePath



                    Response.Listener<String> responseListener_close = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Log.e("path","response sukses" ); // use selectedImagePath
                                    JSONObject user = jsonResponse.getJSONObject("user");
                                    String path_ticket_id = user.getString("close_result");

                                    Intent intent = new Intent(WCanvas.this, MainActivity.class);
                                    Toast.makeText(WCanvas.this,"Ticket Closed",Toast.LENGTH_LONG).show();
                                    WCanvas.this.startActivity(intent);
                                    finish();

                                } else {
                                    //JSONObject user = jsonResponse.getJSONObject("user");
                                    //String message = user.getString("message");

                                }
                            } catch (JSONException e) {
                                Log.e("path","response gagal" ); // use selectedImagePath
                                Toast.makeText(WCanvas.this,"Fail to Closing Ticket",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };


                WCanvasTicketRequest WCanvasTicket= new WCanvasTicketRequest(email,requestor,ticketid,mid,evactivity,projid,custid,compid,r_id,cuid,usercomment,ar_report_string,responseListener_close);
                RequestQueue queue_wc = Volley.newRequestQueue(WCanvas.this);
                queue_wc.add(WCanvasTicket);





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

                                            AlertDialog.Builder ab = new AlertDialog.Builder(WCanvas.this);
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
                                                    ActivityCompat.finishAffinity(WCanvas.this);
                                                    WCanvas.this.finish();
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

        file_uri = FileProvider.getUriForFile(WCanvas.this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            //String path = file_uri.getPath();
            //String idStr = path.substring(path.lastIndexOf('/') + 1);
            String idStr = "Photo"+Integer.toString(ARuri.size()+1)+".jpg";
            ARimagename.add(idStr);
            ARuri.add(file_uri);
            //filename.setText(ARimagename.toString());

        }

        if (requestCode == 20 && resultCode == RESULT_OK) {

            Uri originalUri = data.getData();

            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // Check for the freshest data.
            getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
            String idStr = "Image"+Integer.toString(ARuri.size()+1)+".jpg";
            ARimagename.add(idStr);
            ARuri.add(originalUri);
           // filename.setText(ARimagename.toString());
        }
    }


    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    private class Encode_image_canvas extends AsyncTask<Void, Void, Void> {
        //ProgressDialog progressDialog;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
            //progressDialog= ProgressDialog.show(WCanvas.this, "uploading image","Please wait until finish uploading", true);
            //do initialization of required objects objects here
            //Toast.makeText(WCanvas.this,"masuk do in pre exe",Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //Toast.makeText(WCanvas.this,"masuk do in background",Toast.LENGTH_LONG).show();
            try {
                for (int x = 0; x < ARuri.size(); x++) {
                    //Toast.makeText(WCanvas.this,"masuk do in background",Toast.LENGTH_LONG).show();
                    Log.e("path","masuk do in background" ); // use selectedImagePath

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
                        Log.e("path",encoded_string.toString() );
                        //Toast.makeText(WCanvas.this, encoded_string.toString(),Toast.LENGTH_LONG).show();
                        ARencoded.add(encoded_string);
                        if (input1 != null) {
                            input1.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }catch (Exception e){
                Toast.makeText(WCanvas.this, "still uploading on background",Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("path",Integer.toString(ARencoded.size()) );
            for (int x = 0; x < ARencoded.size(); x++){
                Log.e("path","masuk post execute" );
                makeRequest(ARencoded.get(x),ARimagename.get(x),imagepath,customer_id,project_id);
            }

            ARuri.clear();
            ARimagename.clear();
            ARimage.clear();
            ARencoded.clear();
            //filename.setText(ARimagename.toString());
           // progressDialog.dismiss();
        }
    }

    private void makeRequest(final String encoded,final String imagename,final String imagepath,final String customer_id,final String project_id) {
        Log.e("path","cek" );
        Log.e("path",encoded.toString() );
        Log.e("upload",imagename +" "+imagepath+" "+customer_id+" "+project_id);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https:/uat.eid-tools.com/assets/report/imageReport.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("path","berhasil" );
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("path","fail to upload" );
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded);
                map.put("image_name",imagename);
                map.put("image_path",imagepath);
                map.put("custid",customer_id);
                map.put("projid",project_id);

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
