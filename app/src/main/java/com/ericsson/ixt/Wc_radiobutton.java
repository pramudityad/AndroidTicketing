package com.ericsson.ixt;

/**
 * Created by ericsson on 11/9/2018.
 */

public class Wc_radiobutton {
    private String r_id;
    private String r_name;
    private String r_ev_type;
    private String r_ev_activity;
    private String r_status;
    private String r_desc;


    public Wc_radiobutton(String r_id, String r_name, String r_ev_type, String r_ev_activity, String r_status, String r_desc) {
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_ev_type = r_ev_type;
        this.r_ev_activity = r_ev_activity;
        this.r_status = r_status;
        this.r_desc = r_desc;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getR_ev_type() {
        return r_ev_type;
    }

    public void setR_ev_type(String r_ev_type) {
        this.r_ev_type = r_ev_type;
    }

    public String getR_ev_activity() {
        return r_ev_activity;
    }

    public void setR_ev_activity(String r_ev_activity) {
        this.r_ev_activity = r_ev_activity;
    }

    public String getR_status() {
        return r_status;
    }

    public void setR_status(String r_status) {
        this.r_status = r_status;
    }

    public String getR_desc() {
        return r_desc;
    }

    public void setR_desc(String r_desc) {
        this.r_desc = r_desc;
    }
}
