package com.ericsson.ixt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ericsson on 2/15/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private JSONArray resultNotif;

    public static final String TAG_HTID = "t_id";
    public static final String TAG_HSITEID = "t_site_id";
    public static final String TAG_HSITENAME = "t_site_name";
    public static final String TAG_HEVACTIVITY= "ev_activity";
    public static final String TAG_HINPUTTIME= "t_input_time";
    public static final String TAG_HOPENTIME = "t_open_time";
    public static final String TAG_HCLOSEDTIME = "t_closed_time";
    public static final String TAG_HLEADTIME = "t_lead_time";
    public static final String TAG_HSLAATCLOSED = "t_sla_at_closed";
    public static final String TAG_OTID = "t_id";
    public static final String TAG_OSITEID = "t_site_id";
    public static final String TAG_OSITENAME = "t_site_name";
    public static final String TAG_OEVACTIVITY= "ev_activity";
    public static final String TAG_OINPUTTIME ="t_input_time";
    public static final String TAG_OSERVEDBY = "t_og_served_by";
    public static final String TAG_OTSTATUS = "t_status";
    public static final String JSON_ARRAY = "result";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        String queue_id = "0";
        String job_id = "0";
        String t_id = "0";
        String cu_id = "0";
        String account_id = "0";
        String project_id = "0";
        String asp_id = "0";
        String sent_to = "0";
        String t_status = "0";
        String status = "0";
        String error_info = "0";
        String user_comment = "0";


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            queue_id = remoteMessage.getData().get("queue_id");
            job_id = remoteMessage.getData().get("job_id");
            t_id= remoteMessage.getData().get("t_id");
            cu_id = remoteMessage.getData().get("cu_id");
            account_id = remoteMessage.getData().get("account_id");
            project_id = remoteMessage.getData().get("project_id");
            asp_id = remoteMessage.getData().get("asp_id");
            sent_to = remoteMessage.getData().get("sent_to");
            t_status = remoteMessage.getData().get("t_status");
            status = remoteMessage.getData().get("status");
            error_info = remoteMessage.getData().get("error_info");
            user_comment = remoteMessage.getData().get("user_comment");
        }

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //sendNotification(queue_id,job_id,t_id,cu_id,account_id,project_id,asp_id,sent_to,t_status,status,error_info,user_comment);

        if(job_id.equalsIgnoreCase("INBOX")){
            sendNotifInbox(queue_id,job_id,t_id,cu_id,account_id,project_id,asp_id,sent_to,t_status,status,error_info,user_comment);

        }else if(job_id.equalsIgnoreCase("STATUS")){
            sendNotifStatus(queue_id, job_id, t_id, cu_id, account_id, project_id, asp_id, sent_to, t_status, status, error_info, user_comment);
        }





    }

    private void sendNotifInbox(final String queue_id, final String job_id, final String t_id, final String cu_id, final String account_id, final String project_id, final String asp_id, final String sent_to, final String t_status, final String status, final String error_info, final String user_comment) {
        Intent intent = new Intent(this, Inbox.class);
        intent.putExtra("account_id", account_id);
        intent.putExtra("project_id", project_id);
        intent.putExtra("t_status", t_status);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xff);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String CHANNEL_ID = "my_channel_02";// The id of the channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notif2";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.eid)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.eid)
                .setContentTitle("You got New Ticket on your Inbox")
                .setContentText("Please open IXT application to accept the ticket")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setDeleteIntent(pendingIntent)
                .setOngoing(true)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true);
        Random rand = new Random();

        int  n = rand.nextInt(50) + 1;


        notificationManager.notify(n, notificationBuilder.build());


    }

    private void sendNotifStatus(final String queue_id, final String job_id, final String t_id, final String cu_id, final String account_id, final String project_id, final String asp_id, final String sent_to, final String t_status, final String status, final String error_info, final String user_comment) {
        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_20_UAT/notifstatus.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            //Toast.makeText(getApplicationContext(), "MAsuk status notif", Toast.LENGTH_LONG).show();

                            //Storing the Array of JSON String to our JSON Array
                            resultNotif = j.getJSONArray(JSON_ARRAY);


                            for (int i = 0; i < resultNotif.length(); i++) {
                                try {
                                    //Getting json object
                                    JSONObject json = resultNotif.getJSONObject(i);

                                    String ticketid = json.getString(TAG_HTID);
                                    String siteid = json.getString(TAG_HSITEID);
                                    String sitename= json.getString(TAG_HSITENAME);
                                    String evactivity = json.getString(TAG_HEVACTIVITY);
                                    String submittime = json.getString(TAG_HINPUTTIME);
                                    String opentime = json.getString(TAG_HOPENTIME);
                                    String closedtime = json.getString(TAG_HCLOSEDTIME);
                                    String leadtimetoclose = json.getString(TAG_HLEADTIME);
                                    String slacompliment = json.getString(TAG_HSLAATCLOSED);
                                    String inputtime = json.getString(TAG_OINPUTTIME);
                                    String servedby = json.getString(TAG_OSERVEDBY);
                                    String tstatus = json.getString(TAG_OTSTATUS);

                                    String Tstatusn = "";
                                    if (t_status.equalsIgnoreCase("1")) {
                                        Tstatusn = "Handover";
                                    } else if (t_status.equalsIgnoreCase("2")) {
                                        Tstatusn = "SUBMITTED";
                                    }else if (t_status.equalsIgnoreCase("3")) {
                                        Tstatusn = "ROUTED";
                                    } else if (t_status.equalsIgnoreCase("4")) {
                                        Tstatusn = "OPEN";
                                    } else if (t_status.equalsIgnoreCase("5")) {
                                        Tstatusn = "On P2 State";
                                    } else if (t_status.equalsIgnoreCase("6")) {
                                        Tstatusn = "On P1 State";
                                    } else if (t_status.equalsIgnoreCase("7")) {
                                        Tstatusn = "Integration Finish";
                                    } else if (t_status.equalsIgnoreCase("8")) {
                                        Tstatusn = "Waiting";
                                    } else if (t_status.equalsIgnoreCase("9")) {
                                        Tstatusn = "CLOSED";
                                    } else if (t_status.equalsIgnoreCase("10")) {
                                        Tstatusn = "SENTBACK";
                                    } else if (t_status.equalsIgnoreCase("11")) {
                                        Tstatusn = "INCOMPLETE";
                                    } else if (t_status.equalsIgnoreCase("12")) {
                                        Tstatusn = "RECALL";
                                    } else if (t_status.equalsIgnoreCase("14")) {
                                        Tstatusn = "Expired";
                                    }

                                    if(Tstatusn.equalsIgnoreCase("Handover")||
                                            Tstatusn.equalsIgnoreCase("SUBMITTED")||
                                            Tstatusn.equalsIgnoreCase("OPEN")||
                                            Tstatusn.equalsIgnoreCase("ROUTED")||
                                            Tstatusn.equalsIgnoreCase("On P2 State")||
                                            Tstatusn.equalsIgnoreCase("On P1 State")||
                                            Tstatusn.equalsIgnoreCase("Integration Finish")||
                                            Tstatusn.equalsIgnoreCase("Waiting")){

                                        final Intent repeating_intent = new Intent(getApplicationContext(), SentTicketDetail.class);
                                        repeating_intent.putExtra("ticketid", ticketid);
                                        repeating_intent.putExtra("siteid", siteid);
                                        repeating_intent.putExtra("sitename", sitename);
                                        repeating_intent.putExtra("picname", servedby);
                                        repeating_intent.putExtra("requestoption", evactivity);
                                        repeating_intent.putExtra("submittime", inputtime);
                                        repeating_intent.putExtra("account_id", account_id);
                                        repeating_intent.putExtra("project_id", project_id);
                                        repeating_intent.putExtra("t_status", t_status);
                                        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 101, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        String CHANNEL_ID = "my_channel_01";// The id of the channel.
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = "notif1";// The user-visible name of the channel.
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                            notificationManager.createNotificationChannel(mChannel);
                                        }
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                                .setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.eid)
                                                .setContentTitle("Ticket ID :" + ticketid + " Status " + Tstatusn)
                                                .setContentText("Please open IXT application to check the ticket")
                                                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setDeleteIntent(pendingIntent)
                                                .setChannelId(CHANNEL_ID)
                                                .setOngoing(true)
                                                .setAutoCancel(true);

                                        Random rand = new Random();

                                        int n = rand.nextInt(50) + 1;
                                        notificationManager.notify(n, builder.build());

                                    }else if(Tstatusn.equalsIgnoreCase("CLOSED")||
                                            Tstatusn.equalsIgnoreCase("SENTBACK")||
                                            Tstatusn.equalsIgnoreCase("INCOMPLETE")||
                                            Tstatusn.equalsIgnoreCase("Expired")||
                                            Tstatusn.equalsIgnoreCase("RECALL")){

                                        final Intent repeating_intent = new Intent(getApplicationContext(), HistoryTicketDetail.class);
                                        repeating_intent.putExtra("ticketid", ticketid);
                                        repeating_intent.putExtra("siteid", siteid);
                                        repeating_intent.putExtra("sitename", sitename);
                                        repeating_intent.putExtra("evactivity", evactivity);
                                        repeating_intent.putExtra("submittime", submittime);
                                        repeating_intent.putExtra("opentime", opentime);
                                        repeating_intent.putExtra("closedtime", closedtime);
                                        repeating_intent.putExtra("leadtimetoclose", leadtimetoclose);
                                        repeating_intent.putExtra("slacompliment",slacompliment);
                                        repeating_intent.putExtra("account_id", account_id);
                                        repeating_intent.putExtra("project_id", project_id);
                                        repeating_intent.putExtra("t_status", t_status);
                                        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 101, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        String CHANNEL_ID = "my_channel_01";// The id of the channel.
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = "notif1";// The user-visible name of the channel.
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                            notificationManager.createNotificationChannel(mChannel);
                                        }
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                                                .setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.eid)
                                                .setContentTitle("Ticket ID :" + ticketid + " Status " + Tstatusn)
                                                .setContentText("Please open IXT application to check the ticket")
                                                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setDeleteIntent(pendingIntent)
                                                .setChannelId(CHANNEL_ID)
                                                .setOngoing(true)
                                                .setAutoCancel(true);

                                        Random rand = new Random();

                                        int n = rand.nextInt(500) + 1;
                                        notificationManager.notify(n, builder.build());

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
                params.put("t_id", t_id);
                params.put("projid", project_id);
                params.put("custid", account_id);
                params.put("email", sent_to);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }





    private void sendNotification(String queue_id, String job_id, String t_id, String cu_id, String account_id, String project_id, String asp_id, String sent_to, String t_status, String status, String error_info, String user_comment) {

        Intent intent = new Intent(this, InboxTicketDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xff);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.eid)
                .setContentText(queue_id+" "+job_id+" "+t_id+" "+cu_id)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Random rand = new Random();

        int  n = rand.nextInt(50) + 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(n, notificationBuilder.build());

    }

}