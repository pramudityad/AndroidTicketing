package com.ericsson.ixt;

/**
 * Created by Administrator on 10/1/2016.
 */

public class Utils {
    //public static final String BASE_IP          = "http://10.155.205.135/";
    //public static final String BASE_IP          = "http://150.236.178.76/";
    public static final String BASE_IP          = "https://android.eid-tools.com/";
    public static final String LOGIN_URL        = BASE_IP + "ixt_20_UAT/login.php";
    public static final String PROFILE_URL      = BASE_IP + "ixt_20_UAT/profil.php";
    public static final String EDIT_PROFILE_URL = BASE_IP + "ixt_20_UAT/editprofil.php";
    public static final String REGISTER_URL     = BASE_IP + "androidloginwithmysql/register.php";
    public static final String SITEID_URL       = BASE_IP + "ixt_20_UAT/json.php";

    public static final String SITE_URL         = BASE_IP + "ixt_20_UAT/site.php";
    public static final String SITE_STAT_URL    = BASE_IP + "ixt_20_UAT/trafficlight.php";
    public static final String REQ_OPT_URL      = BASE_IP + "ixt_20_UAT/reqopt.php";

    public static final String TICKETIN_URL     = BASE_IP + "ixt_20_UAT/inticket.php";
    public static final String INOTIFY_URL      = BASE_IP + "ixt_20_UAT/inotify.php";
    public static final String TICKETOUT_URL    = BASE_IP + "ixt_20_UAT/outbox.php";
    public static final String TICKETHIST_URL   = BASE_IP + "ixt_20_UAT/History.php";
    public static final String TICKETNOTIFHIST_URL   = BASE_IP + "ixt_20_UAT/notifhistory.php";
    public static final String TICKETACC_URL    = BASE_IP + "ixt_20_UAT/Accept.php";
    public static final String PBSACT_URL       = BASE_IP + "ixt_20_UAT/pbsaction.php";
    public static final String GETTIMELINE_URL  = BASE_IP + "ixt_20_UAT/gettimeline.php";
    public static final String GETNUMBER_URL    = BASE_IP + "ixt_20_UAT/getcontact.php";
    public static final String GETTICKETCOUNTER_URL  = BASE_IP + "ixt_20_UAT/getticketcounter.php";
    public static final String TICKETNOTIFUPD_URL   = BASE_IP + "ixt_20_UAT/notifupdate.php";
    public static final String GETPROJLIST_URL = BASE_IP + "ixt_20_UAT/getprojectlist.php";

    //Tags used in the JSON String
    public static final String TAG_ROLE = "role";
    public static final String TAG_SITE_ID = "m_site_id";
    public static final String TAG_SITE_NAME = "m_site_name";
    public static final String TAG_SITE_ADDRESS = "siteaddress";
    public static final String TAG_SOW= "m_data_sow";
    public static final String TAG_STATUS = "status";

    public static final String TAG_MILESTONE ="m_sow_cat";
    public static final String TAG_PROG_ID ="m_program_id";
    public static final String TAG_DF ="m_design_ack";
    public static final String TAG_DFEXP ="df_exp_stat";
    public static final String TAG_CF ="m_conf_files_ack";
    public static final String TAG_RSA ="m_rsa_ack";
    public static final String TAG_INT ="m_int_finish_ack";
    public static final String TAG_OV ="m_opt_verified_ack";
    public static final String TAG_M_ID= "m_id";

    public static final String TAG_REQ_OPT ="reqtype";
    public static final String TAG_EV_TYPE ="ev_type";
    public static final String TAG_TGT_GRP ="ev_user_target";


    //TAG used for Inbox
    public static final String TAG_TICKETID = "t_id";
    public static final String TAG_REQUESTOR = "t_requestor_id";
    public static final String TAG_SITEID = "t_site_id";
    public static final String TAG_SITENAME = "t_site_name";
    public static final String TAG_SUBMITTIME = "t_open_time";
    public static final String TAG_EVACTIVITY= "ev_activity";
    public static final String TAG_MID= "t_m_id";


    public static final String TAG_ITEMTEXT = "itemtext";
    public static final String TAG_PREVIEW = "preview";
    public static final String TAG_PBS = "pr_name";
    public static final String TAG_ACTION = "action";
    public static final String TAG_CATEGORY = "category";


    //TAG used for Outbox
    public static final String TAG_OTID = "t_id";
    public static final String TAG_OSITEID = "t_site_id";
    public static final String TAG_OSITENAME = "t_site_name";
    public static final String TAG_OEVACTIVITY= "ev_activity";
    public static final String TAG_OINPUTTIME ="t_input_time";
    public static final String TAG_OSERVEDBY = "t_og_served_by";
    public static final String TAG_OTSTATUS = "t_status";


    public static final String TAG_ONEWMSG = "newmessage";

    //TAG used for History
    public static final String TAG_HTID = "t_id";
    public static final String TAG_HSITEID = "t_site_id";
    public static final String TAG_HSITENAME = "t_site_name";
    public static final String TAG_HEVACTIVITY= "ev_activity";
    public static final String TAG_HINPUTTIME= "t_input_time";
    public static final String TAG_HOPENTIME = "t_open_time";
    public static final String TAG_HCLOSEDTIME = "t_closed_time";
    public static final String TAG_HLEADTIME = "t_lead_time";
    public static final String TAG_HSLAATCLOSED = "t_sla_at_closed";


    //TAG used for Timeline
    public static final String TAG_TLTIMESTAMP = "timestamp";
    public static final String TAG_TLTFLOW= "t_flow";
    public static final String TAG_TLTID= "t_id";
    public static final String TAG_TLUSERID = "user_id";
    public static final String TAG_TLUSERACT = "ev_activity";
    public static final String TAG_TLUSERCOM = "user_comment";

    //TAG used for contact
    public static final String TAG_CONTACT = "user_contact";

    //TAG used for conunter
    public static final String TAG_TOTAL = "total";
    public static final String TAG_CLOSED = "closed";

    //TAG used for site
    public static final String TAG_LSITEID = "m_site_id";
    public static final String TAG_LSITENAME = "m_site_name";
    public static final String TAG_LSITESOW = "m_data_sow";
    public static final String TAG_LTECH = "sow_rat";
    public static final String TAG_LMODULE = "sow_dutype";

    public static final String TAG_LACT_ID = "m_activity_id";
    public static final String TAG_LDATE_TGT = "m_date_target";
    //JSON array name
    public static final String JSON_ARRAY = "result";

    public static final String SITE_STAT = "site_stat";

    public static final String TAG_UPDMSG="upd_msg";
    public static final String TAG_UPDVERS="upd_vers";
    public static final String TAG_UPDLINK="upd_link";

    public static final String TAG_USRCUST="user_cust_id";
    public static final String TAG_USRPROJ="user_project_id";


}
