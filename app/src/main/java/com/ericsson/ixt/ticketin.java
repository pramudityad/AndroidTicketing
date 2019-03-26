package com.ericsson.ixt;

/**
 * Created by ericsson on 12/5/2017.
 */

public class ticketin {


    private String ticketid;
    private String requestor;
    private String siteid;
    private String sitename;
    private String mid;
    private String submittime;
    private String activity;




    //Constructor



    public ticketin( String ticketid, String requestor, String siteid, String sitename, String submittime,String activity,String mid) {

        this.ticketid = ticketid;
        this.requestor = requestor;
        this.siteid = siteid;
        this.sitename = sitename;
        this.submittime = submittime;
        this.activity = activity;
        this.mid = mid;


    }

    //Setter, getter


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }


}
