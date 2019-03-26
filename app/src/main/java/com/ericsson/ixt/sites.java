package com.ericsson.ixt;

/**
 * Created by ericsson on 12/22/2017.
 */

public class sites {

    private String siteid,sitename,sow;

    public sites(String siteid, String sitename, String sow) {
        this.siteid = siteid;
        this.sitename = sitename;
        this.sow = sow;
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

    public String getSow() {
        return sow;
    }

    public void setSow(String sow) {
        this.sow = sow;
    }

    public String toString() {
        return siteid;
    }
}
