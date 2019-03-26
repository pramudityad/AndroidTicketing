package prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 10/1/2016.
 */

public class UserInfo {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME = "userinfo";
    //private static final String KEY_USERNAME = "username";
    //private static final String KEY_EMAIL = "email";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_FNAME  = "user_fname";
    private static final String KEY_LNAME = "user_lname";
    private static final String KEY_CONTACT = "user_contact";
    private static final String KEY_CUID = "user_cu_id";
    private static final String KEY_CUNAME = "user_cu_name";
    private static final String KEY_COMPID = "user_asp_id";
    private static final String KEY_COMPNAME = "user_asp_name";
    private static final String KEY_CUSTID = "user_cust_id";
    private static final String KEY_PROJID = "user_project_id";
    private static final String KEY_JOINDATE = "user_join_date";
    private static final String KEY_LASTACT = "user_last_activity";
    private static final String KEY_STATUS = "user_status";
    private static final String KEY_TYPE = "user_type";
    private static final String KEY_PREV = "user_prev";
    private static final String KEY_IMG = "user_img";
    private static final String KEY_TOKEN = "user_token";
    private static final String KEY_TIMETOKEN = "time_token";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserInfo(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /*
    public void setUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }
    */

    public void setUserid(String userid){
        editor.putString(KEY_USERID, userid);
        editor.apply();
    }

    public void setFname(String fname){
        editor.putString(KEY_FNAME, fname);
        editor.apply();
    }

    public void setLname(String lname){
        editor.putString(KEY_LNAME, lname);
        editor.apply();
    }

    public void setContact(String contact){
        editor.putString(KEY_CONTACT, contact);
        editor.apply();
    }

    public void setCuid(String cuid){
        editor.putString(KEY_CUID, cuid);
        editor.apply();
    }

    public void setCuname(String cuname){
        editor.putString(KEY_CUNAME, cuname);
        editor.apply();
    }

    public void setCompid(String compid){
        editor.putString(KEY_COMPID, compid);
        editor.apply();
    }

    public void setCompname(String compname){
        editor.putString(KEY_COMPNAME, compname);
        editor.apply();
    }

    public void setCustid(String custid){
        editor.putString(KEY_CUSTID, custid);
        editor.apply();
    }

    public void setProjid(String projid){
        editor.putString(KEY_PROJID, projid);
        editor.apply();
    }

    public void setJoindate(String joindate){
        editor.putString(KEY_JOINDATE, joindate);
        editor.apply();
    }

    public void setLastact(String lastact){
        editor.putString(KEY_LASTACT, lastact);
        editor.apply();
    }

    public void setStatus(String status){
        editor.putString(KEY_STATUS, status);
        editor.apply();
    }

    public void setType(String type){
        editor.putString(KEY_TYPE, type);
        editor.apply();
    }

    public void setPrev(String prev){
        editor.putString(KEY_PREV, prev);
        editor.apply();
    }

    public void setImg(String img){
        editor.putString(KEY_IMG, img);
        editor.apply();
    }

    public void setToken(String token){
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public void setTimetoken(String time){
        editor.putString(KEY_TIMETOKEN, time);
        editor.apply();
    }
    public void clearUserInfo(){
        editor.clear();
        editor.commit();
    }



    // public String getKeyUsername(){return prefs.getString(KEY_USERNAME, "");}

    // public String getKeyEmail(){return prefs.getString(KEY_EMAIL, "");}

    public String getUserid(){return prefs.getString(KEY_USERID, "");}

    public String getFname(){return prefs.getString(KEY_FNAME, "");}

    public String getLname(){return prefs.getString(KEY_LNAME, "");}

    public String getContact(){return prefs.getString(KEY_CONTACT, "");}

    public String getCuid(){return prefs.getString(KEY_CUID, "");}

    public String getCuname(){return prefs.getString(KEY_CUNAME, "");}

    public String getCompid(){return prefs.getString(KEY_COMPID, "");}

    public String getCompname(){return prefs.getString(KEY_COMPNAME, "");}

    public String getCustid(){return prefs.getString(KEY_CUSTID, "");}

    public String getProjid(){return prefs.getString(KEY_PROJID, "");}

    public String getJoindate(){return prefs.getString(KEY_JOINDATE, "");}

    public String getLastact(){return prefs.getString(KEY_LASTACT, "");}

    public String getStatus(){return prefs.getString(KEY_STATUS, "");}

    public String getType(){return prefs.getString(KEY_TYPE, "");}

    public String getPrev(){return prefs.getString(KEY_PREV, "");}

    public String getImg(){return prefs.getString(KEY_IMG, "");}

    public String getToken(){return prefs.getString(KEY_TOKEN, "");}

    public String getTimetoken(){return prefs.getString(KEY_TIMETOKEN, "0");}


}
