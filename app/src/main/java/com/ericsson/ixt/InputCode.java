package com.ericsson.ixt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import prefs.UserInfo;
import prefs.UserSession;

/**
 * Created by ericsson on 3/7/2018.
 */

public class InputCode extends AppCompatActivity {

    private UserSession session;
    private UserInfo userInfo;
    private TextView tvtimer,tvdateindate, tvdateinmilis;
    private EditText etinputCode;
    private Button resendButton,submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_token);

        userInfo        =  new UserInfo(this);
        final String email = userInfo.getUserid().toString();
        tvtimer = (TextView) findViewById(R.id.tvcountdown);
      //  tvdateindate = (TextView) findViewById(R.id.tvdateindate);
      //  tvdateinmilis=(TextView) findViewById(R.id.tvdateinmilis);
        resendButton = (Button) findViewById(R.id.btresend);
        submitButton = (Button) findViewById(R.id.btsubmit);
        etinputCode = (EditText) findViewById(R.id.etinputCode);

        Intent intent = getIntent();
        final String ticketid = intent.getStringExtra("ticketid");
        final String requestor = intent.getStringExtra("requestor");
        final String siteid = intent.getStringExtra("siteid");



       // tvdateindate.setText(userInfo.getUserid());
       // tvdateinmilis.setText(userInfo.getToken());

         Long milisinfuture =  Long.parseLong(userInfo.getTimetoken().toString());



       final CountDownTimer countdown = new CountDownTimer(milisinfuture, 1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                //tvtimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                tvtimer.setText("Time remaining: " + String.format("%d hour, %d min, %d sec",
                        (millisUntilFinished/1000/60/60) % 24 ,(millisUntilFinished/1000/60)%60,
                        (millisUntilFinished/1000)%60));

                userInfo.setTimetoken(Long.toString(millisUntilFinished));


                /// /here you can have your logic to set text to edittext
                resendButton.setEnabled(false);
                submitButton.setEnabled(true);
            }

            public void onFinish() {
                tvtimer.setText("Waktu Anda Habis, Silahkan resend code!");
                userInfo.setToken("");
                userInfo.setUserid("");
                resendButton.setEnabled(true);
                submitButton.setEnabled(false);
            }

        }.start();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etinputCode.getText().toString().equalsIgnoreCase("")) {
                    if (userInfo.getToken().equalsIgnoreCase(etinputCode.getText().toString())) {
                        startActivity(new Intent(InputCode.this, ResetPassword.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Wrong Token Entered", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Input Token", Toast.LENGTH_LONG).show();
                }
            }
        });


        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }




}
