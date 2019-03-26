package com.ericsson.ixt;

/**
 * Created by ericsson on 12/5/2017.
 */

public class tickethist {



    private String ticketid;
    private String siteid;
    private String sitename;
    private String evactivity;
    private String submittime;
    private String opentime;
    private String closedtime;
    private String leadtimetoclose;
    private String slacompliment;

    public tickethist(String ticketid, String siteid, String sitename, String evactivity, String submittime, String opentime, String closedtime, String leadtimetoclose, String slacompliment) {
        this.ticketid = ticketid;
        this.siteid = siteid;
        this.sitename = sitename;
        this.evactivity = evactivity;
        this.submittime = submittime;
        this.opentime = opentime;
        this.closedtime = closedtime;
        this.leadtimetoclose = leadtimetoclose;
        this.slacompliment = slacompliment;
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

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosedtime() {
        return closedtime;
    }

    public void setClosedtime(String closedtime) {
        this.closedtime = closedtime;
    }

    public String getLeadtimetoclose() {
        return leadtimetoclose;
    }

    public void setLeadtimetoclose(String leadtimetoclose) {
        this.leadtimetoclose = leadtimetoclose;
    }

    public String getSlacompliment() {
        return slacompliment;
    }

    public void setSlacompliment(String slacompliment) {
        this.slacompliment = slacompliment;
    }
}
