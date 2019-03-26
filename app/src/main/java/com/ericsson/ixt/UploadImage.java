package com.ericsson.ixt;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import prefs.UserInfo;
import prefs.UserSession;

import static android.content.Intent.ACTION_PICK;

/**
 *
 * Created by ericsson on 11/13/2017.
 */

public class UploadImage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private UserInfo userInfo;
    private UserSession userSession;
    private Button btsubmit;
    private ImageButton btswapproject;

    private LinearLayout attachmentbox,attachmentlabel;

    private JSONArray resultNotif;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_new_ticket);


        btsubmit    = (Button) findViewById(R.id.btsubmit);
        btswapproject = (ImageButton) findViewById(R.id.btswapproject);

        imgSwitch=(Switch) findViewById(R.id.imgSwitch);

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

        checkUpdate(email);



        imgSwitch=(Switch) findViewById(R.id.imgSwitch);
        attachmentlabel=(LinearLayout) findViewById(R.id.attachmentlabel);

        attachmentbox=(LinearLayout) findViewById(R.id.attachmentbox);
        //attachmentbox.setVisibility(LinearLayout.GONE);
        //attachmentlabel.setVisibility(LinearLayout.GONE);




        /**
         * image uploader initiate*/
        ARimage = new ArrayList<String>();
        ARimagename = new ArrayList<String>();
        ARencoded = new ArrayList<String>();
        ARuri = new ArrayList<Uri>();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        filename=(TextView) findViewById(R.id.filename);
        uriadd =(Button) findViewById(R.id.uriadd);
        imageselect=(Button) findViewById(R.id.imageselect);
        clearpic =(Button) findViewById(R.id.clearpic);
        /**
         * end of image uploader initiate*/


        btswapproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.setLoggedin(true);
                startActivity(new Intent(UploadImage.this, ProjectSelector.class));
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
            @Override
            public void onClick(View view) {
                                        String urisize=Integer.toString(ARuri.size());
                                        //String path_ticket_id = user.getString("ticket_id");
                                        imagepath = "TS1245776666";
                                        new Encode_image().execute();

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
                                    final String updlink = json.getString(Utils.TAG_UPDLINK);

                                    if(!message.isEmpty()&&!updvers.isEmpty()){
                                        if(!updvers.equalsIgnoreCase(versionCode)){

                                            AlertDialog.Builder ab = new AlertDialog.Builder(UploadImage.this);
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
                                                    ActivityCompat.finishAffinity(UploadImage.this);
                                                    UploadImage.this.finish();
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

        file_uri = FileProvider.getUriForFile(UploadImage.this,
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
            AlertDialog.Builder ab = new AlertDialog.Builder(UploadImage.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("are you sure to cancel create ticket?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //if you want to finish just current activity
                    Intent setIntent = new Intent(UploadImage.this,MainActivity.class);
                    startActivity(setIntent);
                    UploadImage.this.finish();
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
            startActivity(new Intent(UploadImage.this, UploadImage.class));
            finish();
            */
        } else if (id == R.id.nav_drop_in) {
            userSession.setLoggedin(true);
            startActivity(new Intent(UploadImage.this, Inbox.class));
            finish();
        } else if (id == R.id.nav_ongoing) {
            userSession.setLoggedin(true);
            startActivity(new Intent(UploadImage.this, SentTicket.class));
            finish();
        } else if (id == R.id.nav_closed) {
            userSession.setLoggedin(true);
            startActivity(new Intent(UploadImage.this, History.class));
            finish();
        } else if (id == R.id.nav_about) {
            userSession.setLoggedin(true);
            startActivity(new Intent(UploadImage.this, About.class));
            finish();
        }else if (id == R.id.nav_logout) {

            AlertDialog.Builder ab = new AlertDialog.Builder(UploadImage.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("Are you sure to Log Out from application?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    sendRegistrationToServer("",email);
                    userSession.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(UploadImage.this, Login.class));
                    ActivityCompat.finishAffinity(UploadImage.this);
                    UploadImage.this.finish();
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
            progressDialog= ProgressDialog.show(UploadImage.this, "uploading image","Please wait until finish uploading", true);
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

                }
            }catch (Exception e){
                Toast.makeText(UploadImage.this, "still uploading on background",Toast.LENGTH_LONG).show();
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
