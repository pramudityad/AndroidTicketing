package com.ericsson.ixt;

import android.widget.CheckBox;

/**
 * Created by ericsson on 11/9/2018.
 */

public class Wc_checkbox {
    private CheckBox cb_canvas;
    private String cb_text;
    private String cb_fieldname;
    private String cb_placeholder;
    private String cb_sequence;
    private String cb_item;

    public Wc_checkbox(CheckBox cb_canvas, String cb_text, String cb_fieldname, String cb_placeholder, String cb_sequence,String cb_item) {
        this.cb_canvas = cb_canvas;
        this.cb_text = cb_text;
        this.cb_fieldname = cb_fieldname;
        this.cb_placeholder = cb_placeholder;
        this.cb_sequence = cb_sequence;
        this.cb_item=cb_item;
    }

    public String getCb_item() {
        return cb_item;
    }

    public void setCb_item(String cb_item) {
        this.cb_item = cb_item;
    }

    public CheckBox getCb_canvas() {
        return cb_canvas;
    }

    public void setCb_canvas(CheckBox cb_canvas) {
        this.cb_canvas = cb_canvas;
    }

    public String getCb_text() {
        return cb_text;
    }

    public void setCb_text(String cb_text) {
        this.cb_text = cb_text;
    }

    public String getCb_fieldname() {
        return cb_fieldname;
    }

    public void setCb_fieldname(String cb_fieldname) {
        this.cb_fieldname = cb_fieldname;
    }

    public String getCb_placeholder() {
        return cb_placeholder;
    }

    public void setCb_placeholder(String cb_placeholder) {
        this.cb_placeholder = cb_placeholder;
    }

    public String getCb_sequence() {
        return cb_sequence;
    }

    public void setCb_sequence(String cb_sequence) {
        this.cb_sequence = cb_sequence;
    }
}
