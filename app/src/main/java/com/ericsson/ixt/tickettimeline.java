package com.ericsson.ixt;

/**
 * Created by ericsson on 1/6/2018.
 */

public class tickettimeline {


    final String timestamp;
    final String tflow;
    final String tid;
    final String userid;
    final String evactivity;
    final String usercomment;

    public tickettimeline(String timestamp, String tflow, String userid, String evactivity, String usercomment,String tid) {
        this.timestamp = timestamp;
        this.tflow = tflow;
        this.tid = tid;
        this.userid = userid;
        this.evactivity = evactivity;
        this.usercomment = usercomment;
    }

    public String getTid() {
        return tid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTflow() {
        return tflow;
    }

    public String getUserid() {
        return userid;
    }

    public String getEvactivity() {
        return evactivity;
    }

    public String getUsercomment() {
        return usercomment;
    }
}
