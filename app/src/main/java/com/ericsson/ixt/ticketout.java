package com.ericsson.ixt;

/**
 * Created by ericsson on 12/5/2017.
 */

public class ticketout {



    private String ticketid;
    private String siteid;
    private String sitename;
    private String evactivity;
    private String inputtime;
    private String servedby;
    private String tstatus;



    //Constructor


    public ticketout(String ticketid, String siteid, String sitename, String evactivity, String inputtime, String servedby, String tstatus) {
        this.ticketid = ticketid;
        this.siteid = siteid;
        this.sitename = sitename;
        this.evactivity = evactivity;
        this.inputtime = inputtime;
        this.servedby = servedby;
        this.tstatus = tstatus;
    }


    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
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

    public String getEvactivity() {
        return evactivity;
    }

    public void setEvactivity(String evactivity) {
        this.evactivity = evactivity;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getServedby() {
        return servedby;
    }

    public void setServedby(String servedby) {
        this.servedby = servedby;
    }

    public String getTstatus() {
        return tstatus;
    }

    public void setTstatus(String tstatus) {
        this.tstatus = tstatus;
    }
}
