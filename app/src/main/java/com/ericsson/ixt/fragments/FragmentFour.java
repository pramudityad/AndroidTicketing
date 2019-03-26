package com.ericsson.ixt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ericsson.ixt.CustomAdapter;
import com.ericsson.ixt.InboxTicketDetail;
import com.ericsson.ixt.R;
import com.ericsson.ixt.Utils;
import com.ericsson.ixt.ticketin;

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
 * Created by Anu on 22/04/17.
 */



public class FragmentFour extends Fragment {
    private ListView lvInbox;
    private CustomAdapter adapter;
    private List<ticketin> mInboxList;
    private UserInfo userInfo;
    private UserSession userSession;
    private JSONArray resultNotif;
    private TextView tvprojectname,testtv;
    private ImageButton btswapproject;

    //JSON Array
    private JSONArray resultInbox;

    public FragmentFour() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_one, container, false);

        userInfo        =  new UserInfo(getActivity().getApplicationContext());
        userSession     =  new UserSession(getActivity().getApplicationContext());

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


        lvInbox = (ListView)view.findViewById(R.id.listviewticketinbox);

        mInboxList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/ixt_19_11_UAT/inticket_personal.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultInbox = j.getJSONArray(Utils.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getInboxBody(resultInbox);
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
                params.put("projid", projid);
                params.put("custid", custid);
                params.put("compid", compid);
                params.put("usertype", usertype);
                params.put("email", email);
                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);

        lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Do something
                //Ex: display msg with product id get from view.getTag


                String ticketid = mInboxList.get(position).getTicketid();
                String requestor = mInboxList.get(position).getRequestor();
                String siteid = mInboxList.get(position).getSiteid();
                String sitename = mInboxList.get(position).getSitename();
                String submittime = mInboxList.get(position).getSubmittime();
                String evactivity = mInboxList.get(position).getActivity();
                String mid = mInboxList.get(position).getMid();

                //Toast.makeText(Inbox.this, ticketid, Toast.LENGTH_LONG).show();


                Intent intent = new Intent((getActivity().getApplicationContext()), InboxTicketDetail.class);
                intent.putExtra("ticketid", ticketid);
                intent.putExtra("requestor", requestor);
                intent.putExtra("siteid", siteid);
                intent.putExtra("sitename", sitename);
                intent.putExtra("submittime", submittime);
                intent.putExtra("evactivity", evactivity);
                intent.putExtra("mid", mid);
                intent.putExtra("fromfragment", "Four");


                (getActivity().getApplicationContext()).startActivity(intent);
                // startActivity(new Intent(Inbox.this, InboxTicketDetail.class));
            }
        });

        return view;
    }

    private void getInboxBody(JSONArray j){
        //Traversing through all the items in the json array
        /*
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
        mInboxList.add(new ticketin("1111","1111","1111","1111","1111","1111","1111"));
*/
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                String ticketid=json.getString(Utils.TAG_TICKETID);
                String requestor=json.getString(Utils.TAG_REQUESTOR);
                String siteid=json.getString(Utils.TAG_SITEID);
                String sitename=json.getString(Utils.TAG_SITENAME);
                String submittime=json.getString(Utils.TAG_SUBMITTIME);
                String evactivity=json.getString(Utils.TAG_EVACTIVITY);
                String mid=json.getString(Utils.TAG_MID);

                mInboxList.add(new ticketin(ticketid,requestor,siteid,sitename,submittime,evactivity,mid));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        // spsiteid.setAdapter(new ArrayAdapter<String>(NewTicket.this, android.R.layout.simple_spinner_dropdown_item, ARsiteid));
        //Init adapter
        adapter = new CustomAdapter((getActivity().getApplicationContext()), mInboxList);
        lvInbox.setAdapter(adapter);
    }

}