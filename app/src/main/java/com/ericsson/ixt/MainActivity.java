package com.ericsson.ixt;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.highsoft.highcharts.Common.HIChartsClasses.HIChart;
import com.highsoft.highcharts.Common.HIChartsClasses.HICondition;
import com.highsoft.highcharts.Common.HIChartsClasses.HICredits;
import com.highsoft.highcharts.Common.HIChartsClasses.HIDataLabels;
import com.highsoft.highcharts.Common.HIChartsClasses.HIExporting;
import com.highsoft.highcharts.Common.HIChartsClasses.HILabel;
import com.highsoft.highcharts.Common.HIChartsClasses.HILegend;
import com.highsoft.highcharts.Common.HIChartsClasses.HILine;
import com.highsoft.highcharts.Common.HIChartsClasses.HIOptions;
import com.highsoft.highcharts.Common.HIChartsClasses.HIPie;
import com.highsoft.highcharts.Common.HIChartsClasses.HIPlotOptions;
import com.highsoft.highcharts.Common.HIChartsClasses.HIResponsive;
import com.highsoft.highcharts.Common.HIChartsClasses.HIRules;
import com.highsoft.highcharts.Common.HIChartsClasses.HISeries;
import com.highsoft.highcharts.Common.HIChartsClasses.HIStyle;
import com.highsoft.highcharts.Common.HIChartsClasses.HISubtitle;
import com.highsoft.highcharts.Common.HIChartsClasses.HITitle;
import com.highsoft.highcharts.Common.HIChartsClasses.HITooltip;
import com.highsoft.highcharts.Common.HIChartsClasses.HIXAxis;
import com.highsoft.highcharts.Common.HIChartsClasses.HIYAxis;
import com.highsoft.highcharts.Common.HIColor;
import com.highsoft.highcharts.Core.HIChartView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String requestType;
    private String headerTVLeft= "";
    private String headerTVRight= "";
    private String headerGTLeft= "";
    private String headerGTRight= "";
    private String headerLCWeekly= "";
    private String headerLCMonthly= "";
    private String headerLCYearly= "";

    private int dataTVLeft_1 = 0;
    private int dataTVLeft_2 = 0;

    private int dataTVRight_1 = 0;
    private int dataTVRight_2 = 0;

    private int dataGTLeft_1 = 0;
    private int dataGTLeft_2 = 0;

    private int dataGTRight_1 = 0;
    private int dataGTRight_2 = 0;

    private String[] footerWeekly ={};
    private String[] footerMonthly ={};
    private String[] footerYearly = {};

    private Integer[] dataWeekly ={};
    private Integer[] dataMonthly ={};
    private Integer[] dataYearly ={};

    private ArrayList<String> ARevtype;
    JSONArray resultReqopt;

    String tag = "";
    private UserInfo userInfo;
    private UserSession userSession;
    private JSONArray resultNotif;

    private String itemSelected="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        final ProgressDialog progressDialog1 = ProgressDialog.show(MainActivity.this, "Processing Dashboard", "Please wait a moment", true);

        ARevtype = new ArrayList<String>();
        ARevtype.clear();
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


        final NestedScrollView mainScrollView = (NestedScrollView)findViewById(R.id.groupsScrollView);
        final AppBarLayout appBarLayout = findViewById (R.id.app_bar);
        appBarLayout.setExpanded(true);

        final HIChartView chartView = (HIChartView) findViewById(R.id.hc);
        final HIChartView chartView2 = (HIChartView) findViewById(R.id.hc2);
        final HIChartView chartView3 = (HIChartView) findViewById(R.id.hc3);
        final HIChartView chartView4 = (HIChartView) findViewById(R.id.hc4);
        final HIChartView chartView5 = (HIChartView) findViewById(R.id.hc5);
        final Button btWeekly = (Button) findViewById(R.id.daily);
        final Button btMonthly = (Button) findViewById(R.id.monthly);
        final Button btYearly = (Button) findViewById(R.id.yearly);
        final NDSpinner speventype    = (NDSpinner) findViewById(R.id.speventype);
        final TextView titleDashboard = (TextView) findViewById(R.id.titleDashboard);
        final TextView projectDashboard = (TextView) findViewById(R.id.projectDashboard);
        final ImageButton btswapproject   = (ImageButton) findViewById(R.id.btswapproject);
        final ImageView avatar = (ImageView) findViewById(R.id.avatar);

        final Button rreset = (Button) findViewById(R.id.rreset);
        rreset.setVisibility(Button.GONE);

        //titleDashboard.setVisibility(TextView.GONE);
        //projectDashboard.setVisibility(TextView.GONE);
        //avatar.setVisibility(TextView.GONE);



        userInfo        =  new UserInfo(this);
        userSession     =  new UserSession(this);
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



        if(!userSession.isUserLoggedin()){
            startActivity(new Intent(this, Login.class));
            finish();
        }
/*
        if (isInternetAvailable("https://ixt.eid-tools.com", 53, 1000)) {
            // Internet available, do something
            progressDialog1.dismiss();
        } else {
            // Internet not available

            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("Your Connection is unstable, Do You Want to reload IXT-M");
            ab.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog1.dismiss();
                    titleDashboard.setVisibility(TextView.GONE);
                    projectDashboard.setVisibility(TextView.GONE);
                    avatar.setVisibility(TextView.GONE);
                    userSession.setLoggedin(true);
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();

                }
            });
            ab.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progressDialog1.dismiss();
                    ActivityCompat.finishAffinity(MainActivity.this);
                    MainActivity.this.finish();
                }
            });

            ab.show();
        }
*/


        checkUpdate(email,progressDialog1);

        String token="";
        try

        {
            token = FirebaseInstanceId.getInstance().getToken();
            Log.e("path",token ); // use selectedImagePath
            sendRegistrationToServer(token,email);
        }
        catch (Exception e) {
            //error handling code
            //Toast.makeText(getApplicationContext(), "try to find notification id", Toast.LENGTH_LONG).show();
        }





        titleDashboard.setText(cuname);
        projectDashboard.setText(custid.toUpperCase()+" "+projid.toUpperCase());
        getMyEventType( email,custid ,projid,usertype,speventype);

        ArrayList<String> filterby = new ArrayList<String>(Arrays.asList(footerWeekly));
        ArrayList<Integer> arDataWeekly = new ArrayList<Integer>(Arrays.asList(dataWeekly));


        createPieChart(chartView2,headerTVLeft,dataTVLeft_1,dataTVLeft_2,compname,"Other");
        createPieChart(chartView3,headerTVRight,dataTVRight_1,dataTVRight_2,email,compname);
        createPieChartInside(chartView4,headerGTLeft,dataGTLeft_1,dataGTLeft_2);
        createPieChartInside(chartView5,headerGTRight,dataGTRight_1,dataGTRight_2 );
        createGraphLineDynamic(chartView,headerLCWeekly,filterby,arDataWeekly,"Select Request Type");

        chartView.reload();
        chartView2.reload();
        chartView3.reload();
        chartView4.reload();
        chartView5.reload();



        rreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(true);
            }
        });


        speventype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                //cleardata();
                //mainScrollView.fullScroll(NestedScrollView.FOCUS_UP);
                //ViewGroup vg = findViewById (R.id.drawer_layout);
                //vg.scrollTo(0,0);
                String eventType= speventype.getItemAtPosition(position).toString();
                requestType=eventType;
                itemSelected=eventType;
                //titleDashboard.setVisibility(TextView.GONE);
                //projectDashboard.setVisibility(TextView.GONE);
                //avatar.setVisibility(TextView.GONE);

                if(eventType.equalsIgnoreCase("Select Request Type")){
                    // Toast.makeText(MainActivity.this, eventType, Toast.LENGTH_LONG).show();
                    final ProgressDialog progressDialog3 = ProgressDialog.show(MainActivity.this, "Processing Dashboard", "Please wait a moment", true);


                    btWeekly.setVisibility(Button.GONE);
                    btMonthly.setVisibility(Button.GONE);
                    btYearly.setVisibility(Button.GONE);

                    headerTVLeft= "";
                    headerTVRight= "";
                    headerGTLeft= "";
                    headerGTRight= "";
                    headerLCWeekly= "";
                    headerLCMonthly= "";
                    headerLCYearly= "";

                    dataTVLeft_1 = 0;
                    dataTVLeft_2 = 0;

                    dataTVRight_1 = 0;
                    dataTVRight_2 = 0;

                    dataGTLeft_1 = 0;
                    dataGTLeft_2 = 0;

                    dataGTRight_1 = 0;
                    dataGTRight_2 = 0;
                    createPieChart(chartView2,headerTVLeft,dataTVLeft_1,dataTVLeft_2,compname,"Other");
                    createPieChart(chartView3,headerTVRight,dataTVRight_1,dataTVRight_2,email,compname);
                    createPieChartInside(chartView4, headerGTLeft, dataGTLeft_1, dataGTLeft_2);
                    createPieChartInside(chartView5, headerGTRight, dataGTRight_1, dataGTRight_2);
                    chartView2.reload();
                    chartView3.reload();
                    chartView4.reload();
                    chartView5.reload();

                    footerWeekly =new String[]{};
                    footerMonthly =new String[]{};
                    footerYearly = new String[]{};

                    dataWeekly =new Integer[]{};
                    dataMonthly =new Integer[]{};
                    dataYearly =new Integer[]{};
                    headerLCWeekly="";
                    ArrayList<String> filterby = new ArrayList<String>(Arrays.asList(footerWeekly));

                    ArrayList<Integer> arDataWeekly = new ArrayList<Integer>(Arrays.asList(dataWeekly));

                    createGraphLineDynamic(chartView, headerLCWeekly, filterby, arDataWeekly,requestType);
                    chartView.reload();
                    appBarLayout.setExpanded(true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rreset.performClick();
                            progressDialog1.dismiss();
                            progressDialog3.dismiss();
                            titleDashboard.setVisibility(TextView.VISIBLE);
                            projectDashboard.setVisibility(TextView.VISIBLE);
                            avatar.setVisibility(TextView.VISIBLE);

                        }
                    },2000);


                }else {
                    // Toast.makeText(MainActivity.this, eventType, Toast.LENGTH_LONG).show();
                    titleDashboard.setVisibility(TextView.GONE);
                    projectDashboard.setVisibility(TextView.GONE);
                    avatar.setVisibility(TextView.GONE);
                    final ProgressDialog progressDialog2 = ProgressDialog.show(MainActivity.this, "Processing Dashboard", "Please wait a moment", true);

                    requestType=eventType;

                    btWeekly.setVisibility(Button.VISIBLE);
                    btMonthly.setVisibility(Button.VISIBLE);
                    btYearly.setVisibility(Button.VISIBLE);
                    getDataPieCart(chartView2, chartView3, chartView4,chartView5,email, eventType,userprev,usertype,custid,projid,compid,compname);
                    getDataLineCart(chartView,email, eventType, userprev,custid,projid,compid);
                    appBarLayout.setExpanded(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rreset.performClick();
                        }
                    },1000);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appBarLayout.setExpanded(false);
                            progressDialog2.dismiss();
                            titleDashboard.setVisibility(TextView.VISIBLE);
                            projectDashboard.setVisibility(TextView.VISIBLE);
                            avatar.setVisibility(TextView.VISIBLE);

                        }
                    },1000);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                appBarLayout.setExpanded(false);
                progressDialog1.dismiss();

            }

        });








        btWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> filterby = new ArrayList<String>();
                filterby=new ArrayList<String>(Arrays.asList(footerWeekly));

                ArrayList<Integer> arDataWeekly = new ArrayList<Integer>();
                arDataWeekly=new ArrayList<Integer>(Arrays.asList(dataWeekly));

                createGraphLineDynamic(chartView,headerLCWeekly,filterby,arDataWeekly,requestType);
                chartView.reload();

            }
        });

        btMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> filterby = new ArrayList<String>();
                filterby=new ArrayList<String>(Arrays.asList(footerMonthly));

                ArrayList<Integer> arDataMonthly = new ArrayList<Integer>();
                arDataMonthly=new ArrayList<Integer>(Arrays.asList(dataMonthly));

                createGraphLineDynamic(chartView,headerLCMonthly,filterby,arDataMonthly,requestType);
                chartView.reload();
            }
        });

        btYearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> filterby = new ArrayList<String>();
                filterby=new ArrayList<String>(Arrays.asList(footerYearly));

                ArrayList<Integer> arDataYearly = new ArrayList<Integer>();
                arDataYearly=new ArrayList<Integer>(Arrays.asList(dataYearly));

                createGraphLineDynamic(chartView,headerLCYearly,filterby,arDataYearly,requestType);
                chartView.reload();
            }
        });

        btswapproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  userInfo.setEmail(email);
                //  userInfo.setUsername(username);
                userSession.setLoggedin(true);
                startActivity(new Intent(MainActivity.this, ProjectSelector.class));
                finish();
            }
        });





    }

    public boolean isInternetAvailable(String address, int port, int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(address, port);

            sock.connect(sockaddr, timeoutMs); // This will block no more than timeoutMs
            sock.close();

            return true;

        } catch (IOException e) { return false; }
    }




    private void getMyEventType(final String email, final String custid , final String projid, final String usertype,final Spinner speventype){

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
                            fillreqopt(resultReqopt,speventype);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // restartConnection();
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("usertype", usertype);
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

    private void fillreqopt(JSONArray j,final Spinner speventype) {
        //Traversing through all the items in the json array
        for(int i=-1;i<j.length();i++){
            try {
                if(i==-1){
                    //Adding the name of the student to array list
                    String reqopt="Select Request Type";
                    ARevtype.add(reqopt);

                }else {
                    //Getting json object
                    JSONObject json = j.getJSONObject(i);

                    //Adding the name of the student to array list
                    String reqopt=json.getString(Utils.TAG_REQ_OPT);
                    if(reqopt.equalsIgnoreCase("null")){

                    }else {
                        ARevtype.add(reqopt);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Setting adapter to show the items in the spinner
        speventype.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, ARevtype));

    }

    private void getDataLineCart(HIChartView chartView,String email, String eventType,String userprev,String custid,String projid,String compid){
        getArray(chartView,email,custid,projid,userprev,eventType,compid);
    }

    private void getDataPieCart(final HIChartView chartView2,final HIChartView chartView3,final HIChartView chartView4,final HIChartView chartView5,String email, String eventType,String userprev,String usertype,String custid,String projid,String compid,String compname){
        getPieTv( chartView2, chartView3, chartView4,chartView5,email,eventType,usertype,custid,projid,compid,compname);
        getPieGT( chartView2, chartView3, chartView4,chartView5,email,eventType,userprev,custid,projid,compid,compname);
    }

    private void createPieChart(HIChartView chartView,String titleData,Integer dataUser,Integer dataAll,String leftTooltip,String rightTooltip){

        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("pie");
        chart.setPlotBackgroundColor(null);
        chart.setPlotBorderWidth(null);
        chart.setPlotShadow(false);
        options.setChart(chart);

        HICredits credit =new HICredits();
        credit.setEnabled(false);
        options.setCredits(credit);

        HIExporting export =new HIExporting();
        export.setEnabled(false);
        options.setExporting(export);

        HITitle title = new HITitle();
        title.setText(titleData);
        options.setTitle(title);

        HITooltip tooltip = new HITooltip();
        tooltip.setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");
        options.setTooltip(tooltip);


        HIPlotOptions plotOptions = new HIPlotOptions();
        HIPie pie=new HIPie();
        HIDataLabels dataLabels = new HIDataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");
        dataLabels.setDistance(-40);
        HIStyle histyle =new HIStyle();
        histyle.setColor("White");
        dataLabels.setStyle(histyle);
        pie.setDataLabels(dataLabels);
        pie.setSize("120%");
        pie.setAllowPointSelect(true);
        pie.setCursor("pointer");
        plotOptions.setPie(pie);
        //pie.setShowInLegend(true);
        options.setPlotOptions(plotOptions);

        HIPie series = new HIPie();
        series.setName("Status");

        plotOptions.setSeries(series);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", leftTooltip);
        map1.put("y", dataUser);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", rightTooltip);
        map2.put("y", dataAll);
        // map2.put("sliced", true);
        // map2.put("selected", true);


        ArrayList data=new ArrayList<>(Arrays.asList(map1, map2));
        series.setData(data);

        ArrayList seriesAr = new ArrayList<>(Arrays.asList(series));
        options.setSeries(seriesAr);

        chartView.setOptions(options);
    }

    private void createGraphLineDynamic(HIChartView chartView,String titleData,ArrayList<String> arx,ArrayList<Integer> data,final String evtype){
        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("line");
        options.setChart(chart);

        HICredits credit =new HICredits();
        credit.setEnabled(false);
        options.setCredits(credit);

        HIExporting export =new HIExporting();
        export.setEnabled(false);
        options.setExporting(export);

        HITitle title = new HITitle();
        title.setText(titleData);
        options.setTitle(title);

        HISubtitle subtitle = new HISubtitle();
        subtitle.setText("Source: ixt.eid-tools.com");
        options.setSubtitle(subtitle);

        HIYAxis yaxis = new HIYAxis();
        HITitle yaxisTitle = new HITitle();
        yaxisTitle.setText("Number of ticket");
        yaxis.setTitle(yaxisTitle);
        options.setYAxis(new ArrayList<>(Collections.singletonList(yaxis)));


        final HIXAxis xAxis = new HIXAxis();
        xAxis.setCategories(new ArrayList<>(arx));
        options.setXAxis(new ArrayList<HIXAxis>(){{add(xAxis);}});

        HILegend legend = new HILegend();
        legend.setLayout("vertical");
        legend.setAlign("right");
        legend.setVerticalAlign("middle");
        options.setLegend(legend);

        HIPlotOptions plotoptions = new HIPlotOptions();
        HISeries series = new HISeries();
        HILabel label = new HILabel();
        series.setLabel(label);
        series.setPointStart(2010);
        label.setConnectorAllowed(false);;
        plotoptions.setSeries(series);


        HILine line1 = new HILine();
        line1.setName(evtype);

        line1.setData(new ArrayList<>(data));

        HIResponsive responsive = new HIResponsive();

        HIRules rules1 = new HIRules();
        HICondition condition= new HICondition();
        condition.setMaxWidth(500);
        rules1.setCondition(condition);
        HashMap<String, HashMap> chartLegend = new HashMap<>();
        HashMap<String, String> legendOptions = new HashMap<>();
        legendOptions.put("layout", "horizontal");
        legendOptions.put("align", "center");
        legendOptions.put("verticalAlign", "bottom");
        chartLegend.put("legend", legendOptions);
        rules1.setChartOptions(chartLegend);
        responsive.setRules(new ArrayList<>(Collections.singletonList(rules1)));
        options.setResponsive(responsive);
        //ArrayList seriesAr =new ArrayList<>(Arrays.asList(line1, line2, line3, line4, line5));
        ArrayList seriesAr =new ArrayList<>(Arrays.asList(line1));
        options.setSeries(seriesAr);

        chartView.setOptions(options);

    }

    private void createPieChartInside(HIChartView chartView ,String datatTitle,Integer datayes,Integer datano){
        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("pie");
        chart.setPlotBackgroundColor(null);
        chart.setPlotBorderWidth(null);
        chart.setPlotShadow(false);
        options.setChart(chart);

        HICredits credit =new HICredits();
        credit.setEnabled(false);
        options.setCredits(credit);

        HIColor color1 = HIColor.initWithRGB(15, 72, 127);
        HIColor color2 = HIColor.initWithRGB(52, 109, 164);

        ArrayList<HIColor> colors = new ArrayList<>(Arrays.asList(color1, color2));
        options.setColors(colors);

        HITitle title = new HITitle();
        title.setText(datatTitle);
        options.setTitle(title);

        HITooltip tooltip = new HITooltip();
        tooltip.setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");
        options.setTooltip(tooltip);

        HIExporting export =new HIExporting();
        export.setEnabled(false);
        options.setExporting(export);


        HIPlotOptions plotOptions = new HIPlotOptions();
        HIPie pie=new HIPie();
        HIDataLabels dataLabels = new HIDataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");
        dataLabels.setDistance(-50);
        HIStyle histyle =new HIStyle();
        histyle.setColor("White");
        dataLabels.setStyle(histyle);
        pie.setSize("120%");
        pie.setDataLabels(dataLabels);
        pie.setAllowPointSelect(true);
        pie.setCursor("pointer");
        plotOptions.setPie(pie);
        //pie.setShowInLegend(true);
        options.setPlotOptions(plotOptions);

        HIPie series1 = new HIPie();
        series1.setName("Status");

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", "Using");
        map1.put("y", datayes);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Not Using");
        map2.put("y", datano);

        ArrayList data = new ArrayList<>(Arrays.asList(map1, map2));

        series1.setData(data);

        ArrayList series = new ArrayList<>(Arrays.asList(series1));

        options.setSeries(series);

        chartView.setOptions(options);
    }

    private void createPieChartBreakdown(HIChartView chartView ,String datatTitle,Integer datayes,Integer datano){

    }

    private void getArray(final HIChartView chartView,final String email, final String custid, final String projid, final String userprev,final String evtype,final String compid){
        //Creating a string request
        // Toast.makeText(MainActivity.this, email+  custid+  projid+  usertype+evtype+compid, Toast.LENGTH_LONG).show();
        final ProgressDialog progressLine = ProgressDialog.show(MainActivity.this, "Processing Dashboard", "Please wait a moment", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://android.eid-tools.com/other/responsearray/arrayget.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultReqopt = j.getJSONArray("result");


                            for (int i = 0; i < resultReqopt.length(); i++) {
                                try {

                                    JSONObject json = resultReqopt.getJSONObject(i);

                                    String[] footerWeeklyn = new String[]{};
                                    String[] footerMonthlyn = new String[]{};
                                    String[] footerYearlyn = new String[]{};

                                    headerLCWeekly= "Weekly";
                                    headerLCMonthly= "Monthly";
                                    headerLCYearly= "Yearly";

                                    String dtwk = json.getString("dataWeekly");
                                    String dtmt = json.getString("dataMonthly");
                                    String dtyr = json.getString("dataYearly");
                                    String [] weekly=dtwk.replace("[", "").replace("]", "").replace("\"", "").split(",") ;
                                    String [] monthly=dtmt.replace("[", "").replace("]", "").replace("\"", "").split(",") ;
                                    String [] yearly=dtyr.replace("[", "").replace("]", "").replace("\"", "").split(",") ;

                                    Integer[] dataWeeklyn = new Integer[weekly.length];
                                    Integer[] dataMonthlyn = new Integer[monthly.length];
                                    Integer[] dataYearlyn = new Integer[yearly.length];

                                    for (int o=0;o<weekly.length;o++){
                                        if(weekly[o].equalsIgnoreCase("")){
                                            dataWeeklyn[o] = 0;
                                        }else{
                                            dataWeeklyn[o] = Integer.parseInt(weekly[o]);
                                        }

                                    }
                                    for (int p=0;p<monthly.length;p++){
                                        if(weekly[p].equalsIgnoreCase("")){
                                            dataMonthlyn [p] = 0;
                                        }else{
                                            dataMonthlyn [p] = Integer.parseInt(monthly[p]);
                                        }

                                    }
                                    for (int q=0;q<yearly.length;q++){
                                        if(weekly[q].equalsIgnoreCase("")){
                                            dataYearlyn[q] = 0;
                                        }else{
                                            dataYearlyn[q] = Integer.parseInt(yearly[q]);
                                        }
                                        
                                    }



                                    dataWeekly=dataWeeklyn ;
                                    dataMonthly=dataMonthlyn;
                                    dataYearly=dataYearlyn ;


                                    String ftwk = json.getString("footerWeekly");
                                    String ftmt = json.getString("footerMonthly");
                                    String ftyr = json.getString("footerYearly");
                                    //Toast.makeText(MainActivity.this, ftwk, Toast.LENGTH_LONG).show();

                                    footerWeeklyn = ftwk.replace("[", "").replace("]", "").replace("\"", "").split(",") ;
                                    footerMonthlyn = ftmt.replace("[", "").replace("]", "").replace("\"", "").split(",") ;
                                    footerYearlyn = ftyr.replace("[", "").replace("]", "").replace("\"", "").split(",");

                                    footerWeekly=footerWeeklyn ;
                                    footerMonthly=footerMonthlyn;
                                    footerYearly=footerYearlyn ;

                                    ArrayList<String> filterby = new ArrayList<String>(Arrays.asList(footerWeekly));

                                    ArrayList<Integer> arDataWeekly = new ArrayList<Integer>(Arrays.asList(dataWeekly));

                                    createGraphLineDynamic(chartView, headerLCWeekly, filterby, arDataWeekly,evtype);
                                    chartView.reload();

                                    progressLine.dismiss();


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
                        restartConnection();
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("custid", custid);
                params.put("projid", projid);
                params.put("userprev", userprev);
                params.put("evtype", evtype);
                params.put("compid", compid);

                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void getPieTv(final HIChartView chartView2,final HIChartView chartView3,final HIChartView chartView4,final HIChartView chartView5, final String email,final String evtype,final String usertype, final String custid, final String projid ,final String compid,final String compname) {
        //Creating a string request

        //Toast.makeText(MainActivity.this, email+  custid+  projid+  userprev+evtype+compid, Toast.LENGTH_LONG).show();
        final ProgressDialog progressTV = ProgressDialog.show(MainActivity.this, "Processing Dashboard", "Please wait a moment", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/other/responsearray/piecharttv.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultReqopt = j.getJSONArray("result");


                            for (int i = 0; i < resultReqopt.length(); i++) {
                                try {
                                    //  Toast.makeText(MainActivity.this, "masuk request", Toast.LENGTH_LONG).show();
                                    JSONObject json = resultReqopt.getJSONObject(i);


                                    String SRheaderTVLeft = json.getString("headerTVLeft");
                                    String SRheaderTVRight = json.getString("headerTVRight");
                                    String SRdataTVLeft_1 = json.getString("dataTVLeft_1");
                                    String SRdataTVLeft_2 = json.getString("dataTVLeft_2");
                                    String SRdataTVRight_1 = json.getString("dataTVRight_1");
                                    String SRdataTVRight_2 = json.getString("dataTVRight_2");

                                    // Toast.makeText(MainActivity.this, SRheaderTVLeft+  SRheaderTVRight+  SRheaderGTLeft+  SRheaderGTRight, Toast.LENGTH_LONG).show();

                                    headerTVLeft = SRheaderTVLeft;
                                    headerTVRight = SRheaderTVRight;

                                    dataTVLeft_1 = Integer.parseInt(SRdataTVLeft_1);
                                    dataTVLeft_2 = Integer.parseInt(SRdataTVLeft_2);

                                    dataTVRight_1 = Integer.parseInt(SRdataTVRight_1);
                                    dataTVRight_2 = Integer.parseInt(SRdataTVRight_2);

                                    createPieChart(chartView2,headerTVLeft,dataTVLeft_1,dataTVLeft_2,compname,"Other");
                                    createPieChart(chartView3,headerTVRight,dataTVRight_1,dataTVRight_2,email,compname);
                                    chartView2.reload();
                                    chartView3.reload();

                                    progressTV.dismiss();


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
                        restartConnection();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("custid", custid);
                params.put("projid", projid);
                params.put("usertype", usertype);
                params.put("evtype", evtype);
                params.put("compid", compid);

                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void getPieGT(final HIChartView chartView2,final HIChartView chartView3,final HIChartView chartView4,final HIChartView chartView5, final String email,final String evtype,final String userprev, final String custid, final String projid ,final String compid,final String compname) {
        //Creating a string request

       // Toast.makeText(MainActivity.this, email+  custid+  projid+  userprev+evtype+compid, Toast.LENGTH_LONG).show();
        final ProgressDialog progressGT = ProgressDialog.show(MainActivity.this, "Processing Dashboard", "Please wait a moment", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://android.eid-tools.com/other/responsearray/piechartgt.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultReqopt = j.getJSONArray("result");


                            for (int i = 0; i < resultReqopt.length(); i++) {
                                try {
                                    //  Toast.makeText(MainActivity.this, "masuk request", Toast.LENGTH_LONG).show();
                                    JSONObject json = resultReqopt.getJSONObject(i);


                                    String SRheaderGTLeft = json.getString("headerGTLeft");
                                    String SRheaderGTRight = json.getString("headerGTRight");
                                    String SRdataGTLeft_1 = json.getString("dataGTLeft_1");
                                    String SRdataGTLeft_2 = json.getString("dataGTLeft_2");
                                    String SRdataGTRight_1 = json.getString("dataGTRight_1");
                                    String SRdataGTRight_2 = json.getString("dataGTRight_2");

                                    // Toast.makeText(MainActivity.this, SRheaderGTLeft+  SRheaderGTRight, Toast.LENGTH_LONG).show();

                                    headerGTLeft = SRheaderGTLeft;
                                    headerGTRight = SRheaderGTRight;

                                    dataGTLeft_1 = Integer.parseInt(SRdataGTLeft_1);
                                    dataGTLeft_2 = Integer.parseInt(SRdataGTLeft_2);

                                    dataGTRight_1 = Integer.parseInt(SRdataGTRight_1);
                                    dataGTRight_2 = Integer.parseInt(SRdataGTRight_2);

                                    createPieChartInside(chartView4, headerGTLeft, dataGTLeft_1, dataGTLeft_2);
                                    createPieChartInside(chartView5, headerGTRight, dataGTRight_1, dataGTRight_2);

                                    chartView4.reload();
                                    chartView5.reload();

                                    progressGT.dismiss();


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
                        restartConnection();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("custid", custid);
                params.put("projid", projid);
                params.put("userprev", userprev);
                params.put("evtype", evtype);
                params.put("compid", compid);

                return params;
            }

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("are you sure to exit?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.finishAffinity(MainActivity.this);
                    MainActivity.this.finish();
                }
            });
            ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            ab.show();
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("are you sure to exit?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.finishAffinity(MainActivity.this);
                    MainActivity.this.finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            userSession.setLoggedin(true);

            final String keyemail = userInfo.getUserid();


            // Response received from the server
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            String email = jsonResponse.getString("email");
                            String Compid= jsonResponse.getString("Compid");
                            String ValidUnt= jsonResponse.getString("ValidUnt");
                            String Registerdate= jsonResponse.getString("Registerdate");
                            String Phonenum= jsonResponse.getString("Phonenum");
                            String Wanum= jsonResponse.getString("Wanum");
                            // String Ticketcreated= Integer.toString(jsonResponse.getInt("Ticketcreated"));
                            // String Ticketclosed= Integer.toString(jsonResponse.getInt("Ticketclosed"));


                            Intent intent = new Intent(MainActivity.this, Profile.class);
                            intent.putExtra("email", email);
                            intent.putExtra("Compid", Compid);
                            intent.putExtra("ValidUnt", ValidUnt);
                            intent.putExtra("Registerdate", Registerdate);
                            intent.putExtra("Phonenum", Phonenum);
                            intent.putExtra("Wanum", Wanum);
                            //intent.putExtra("Ticketcreated", Ticketcreated);
                            //intent.putExtra("Ticketclosed", Ticketclosed);


                            MainActivity.this.startActivity(intent);
                            finish();
                        } else {


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Profile Not Found")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        restartConnection();
                    }
                }
            };

            ProfileRequest profilerequest = new ProfileRequest(keyemail, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(profilerequest);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        userInfo              =  new UserInfo(this);
        final String email    = userInfo.getUserid();

        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            final TextView titleDashboard = (TextView) findViewById(R.id.titleDashboard);
            final TextView projectDashboard = (TextView) findViewById(R.id.projectDashboard);
            final ImageView avatar = (ImageView) findViewById(R.id.avatar);
            titleDashboard.setVisibility(TextView.GONE);
            projectDashboard.setVisibility(TextView.GONE);
            avatar.setVisibility(TextView.GONE);
            userSession.setLoggedin(true);
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        } else if (id == R.id.nav_new_request) {
            userSession.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, NewTicket.class));
            finish();
        } else if (id == R.id.nav_drop_in) {
            userSession.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, Inbox.class));
            finish();
        } else if (id == R.id.nav_working_canvas) {
            userSession.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, WcList.class));
            finish();
        } else if (id == R.id.nav_ongoing) {
            userSession.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, SentTicket.class));
            finish();
        } else if (id == R.id.nav_closed) {
            userSession.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, History.class));
            finish();
        } else if (id == R.id.nav_about) {
            userSession.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, About.class));
            finish();
        }else if (id == R.id.nav_logout) {

            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("IXT Application Message");
            ab.setMessage("Are you sure to Log Out from application?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    sendRegistrationToServer("",email);
                    userSession.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    ActivityCompat.finishAffinity(MainActivity.this);
                    MainActivity.this.finish();
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
                        restartConnection();
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



    public void checkUpdate(final String email, final ProgressDialog progressDialog1){
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
                                    //  Toast.makeText(getApplicationContext(), updlink, Toast.LENGTH_LONG).show();

                                    if(!message.isEmpty()&&!updvers.isEmpty()){
                                        if(!updvers.equalsIgnoreCase(versionCode)){

                                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
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
                                                    ActivityCompat.finishAffinity(MainActivity.this);
                                                    MainActivity.this.finish();
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
                       // progressDialog1.dismiss();
                        restartConnection();


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

    private void restartConnection(){

        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        ab.setTitle("IXT Application Message");
        ab.setMessage("Your Connection is unstable, Do You Want to reload IXT-M");
        ab.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                userSession.setLoggedin(true);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();

            }
        });
        ab.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ActivityCompat.finishAffinity(MainActivity.this);
                MainActivity.this.finish();
            }
        });

        ab.show();
    }


}
