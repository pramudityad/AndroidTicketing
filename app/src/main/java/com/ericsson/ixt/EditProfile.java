package com.ericsson.ixt;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import prefs.UserInfo;
import prefs.UserSession;

import static android.content.Intent.ACTION_PICK;

/**
 * Created by ericsson on 11/17/2017.
 */

public class EditProfile extends AppCompatActivity {

    private UserInfo userInfo;
    private UserSession userSession;
    private TextView tvCompid,tvValidUnt,
            tvRegisterdate,tvprofilename,tvprofilecompany,tvprofilerole;
    private EditText etnumber,etfname,etlname;
    private ImageButton btsave;
    private ImageView ivpicture;

    private Button takePictureButton;
    private CircleImageView imageView;
    private Uri file;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TextView txtname,txtalamat,txtkantor,textView4,
            textView,textView5,textView7,textView6,
            textView3,textView31,textBrand,textView36;
    private EditText editText6,editText7,editText8,editText9;

    byte[] byteArray;

    String ConvertImage;

    ByteArrayOutputStream byteArrayOutputStream;

    ProgressDialog progressDialog;

    String ServerUploadPath = "https://android.eid-tools.com/ixt_20_UAT/upload-image-server.php";

    HttpURLConnection httpURLConnection;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter;

    int RC;

    BufferedReader bufferedReader;

    StringBuilder stringBuilder;

    boolean check = true;

    String ImageTag = "image_tag";

    String ImageName = "image_data";

    String GetImageNameFromEditText="ABC";




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_n);

        tvCompid=(TextView) findViewById(R.id.tvcompid);
        tvValidUnt=(TextView) findViewById(R.id.tvvalidunt);
        tvRegisterdate=(TextView) findViewById(R.id.tvregistdate);
        btsave=(ImageButton) findViewById(R.id.btsave);



        // txtname = (TextView)findViewById(R.id.txtname);
        // txtalamat = (TextView) findViewById(R.id.txtalamat);
        // txtkantor = (TextView) findViewById(R.id.txtkantor);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView = (TextView) findViewById(R.id.textView);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView31 = (TextView) findViewById(R.id.textView31);
        textBrand = (TextView) findViewById(R.id.textBrand);
        //textView36 = (TextView) findViewById(R.id.textView36);

        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText8 = (EditText) findViewById(R.id.editText8);
        //editText9 = (EditText) findViewById(R.id.editText9);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");


        etnumber =(EditText) findViewById(R.id.editText6);
        etfname =(EditText) findViewById(R.id.editText7);
        etlname =(EditText) findViewById(R.id.editText8);
        tvprofilename=(TextView) findViewById(R.id.tvprofilename);
        tvprofilecompany=(TextView) findViewById(R.id.tvprofilecompany);
        tvprofilerole=(TextView) findViewById(R.id.tvprofilerole);

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
        final String userimg   = userInfo.getImg();

        GetImageNameFromEditText=email;


        tvCompid.setTypeface(face);
        tvRegisterdate.setTypeface(face);
        tvValidUnt.setTypeface(face);
        tvprofilename.setTypeface(face);
        tvprofilerole.setTypeface(face);
        tvprofilecompany.setTypeface(face);
        textView4.setTypeface(face);
        textView.setTypeface(face);
        textView5.setTypeface(face);
        textView6.setTypeface(face);
        textView7.setTypeface(face);
        textView3.setTypeface(face);
        textView31.setTypeface(face);
        textBrand.setTypeface(face);
        //textView36.setTypeface(face);

        editText6.setTypeface(face);
        editText7.setTypeface(face);
        editText8.setTypeface(face);
        //editText9.setTypeface(face);

        String imageUri = "https://android.eid-tools.com/ixt_20_UAT/images/"+email+".jpg";
        imageView = (CircleImageView) findViewById(R.id.picture);
        Picasso.with(getApplicationContext()).load(imageUri).into(imageView );
        Picasso.with(getApplicationContext()).invalidate(imageUri);
        Picasso.with(getApplicationContext()).load(imageUri).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
/*
        File imgFile = new  File(userimg);

        if(imgFile.exists()){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;

            Bitmap myBitmap = BitmapFactory.decodeFile(userimg,options);


            ImageView myImage = (ImageView) findViewById(R.id.picture);


            myImage.setImageBitmap(myBitmap);


        }
*/

        takePictureButton = (Button) findViewById(R.id.btnImage);
        //imageView = (CircleImageView) findViewById(R.id.picture);

        //initiate permission camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });







        Intent intent = getIntent();
        //final String email = intent.getStringExtra("email");
        final String Compid = intent.getStringExtra("Compid");
        final String ValidUnt = intent.getStringExtra("ValidUnt");
        final String Registerdate = intent.getStringExtra("Registerdate");

        tvprofilename.setText(username+" "+lname);
        tvprofilecompany.setText(compid +" - "+compname);
        tvprofilerole.setText(userprev);
        tvCompid.setText(Compid);
        tvValidUnt.setText(ValidUnt);
        tvRegisterdate.setText(Registerdate);
        etlname.setText(lname);
        etfname.setText(username);
        etnumber.setText(contact);






        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String newnumber=etnumber.getText().toString();
                final String newfname=etfname.getText().toString();
                final String newlname=etlname.getText().toString();
                userInfo.setLname(newlname);
                userInfo.setFname(newfname);
                userInfo.setContact(newnumber);
                // onBackPressed();
                if(!newnumber.equalsIgnoreCase("")&&!newfname.equalsIgnoreCase("")&&!newlname.equalsIgnoreCase("")) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.EDIT_PROFILE_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject j = null;
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            Intent intent = new Intent(EditProfile.this, Profile.class);
                                            EditProfile.this.startActivity(intent);
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                                            builder.setMessage("UPDATE Failed")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
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
                            params.put("user_fname", newfname);
                            params.put("user_lname", newlname);
                            params.put("user_contact", newnumber);
                            params.put("email", email);
                            return params;
                        }

                    };

                    //Creating a request queue
                    RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);

                    //Adding request to the queue
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(EditProfile.this, "Fill the required field",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    //else if (userChoosenTask.equals("Choose from Library"))
                    //  galleryIntent();
                } else {
                    //code for deny

                }
                break;
        }
    }

    private void selectImage() {
        //final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        final CharSequence[] items = { "Take Photo", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(EditProfile.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } /*else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } */else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent(ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        //Toast.makeText(this,destination.toString(), Toast.LENGTH_SHORT).show();
        userInfo.setImg(destination.toString());
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);


        byteArray = bytes.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(EditProfile.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                Toast.makeText(EditProfile.this,string1,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                EditProfile.ImageProcessClass imageProcessClass = new EditProfile.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                 HashMapParams.put(ImageTag, GetImageNameFromEditText);

                 HashMapParams.put(ImageName, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();



    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        String cek="";
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                CursorLoader loader = new CursorLoader(getApplicationContext(), uri, proj, null, null, null);
                Cursor cursor = getContentResolver().query(
                        uri, proj, null, null, null);

                //Cursor cursor = loader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
                cursor.moveToFirst();
                cek = cursor.getString(column_index);
                userInfo.setImg(cek);
                //Toast.makeText(this,cek, Toast.LENGTH_SHORT).show();
                cursor.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        imageView.setImageBitmap(bm);
    }


    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(100000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }



}
