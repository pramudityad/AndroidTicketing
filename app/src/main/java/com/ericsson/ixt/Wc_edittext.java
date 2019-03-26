package com.ericsson.ixt;

import android.widget.EditText;

/**
 * Created by ericsson on 11/9/2018.
 */

public class Wc_edittext {
    private EditText et_canvas;
    private String et_text;
    private String et_fieldname;
    private String et_placeholder;
    private String et_sequence;
    private String et_item;

    public Wc_edittext(EditText et_canvas, String et_text, String et_fieldname, String et_placeholder, String et_sequence, String et_item) {
        this.et_canvas = et_canvas;
        this.et_text = et_text;
        this.et_fieldname = et_fieldname;
        this.et_placeholder = et_placeholder;
        this.et_sequence = et_sequence;
        this.et_item=et_item;
    }

    public String getEt_item() {
        return et_item;
    }

    public void setEt_item(String et_item) {
        this.et_item = et_item;
    }

    public EditText getEt_canvas() {
        return et_canvas;
    }

    public void setEt_canvas(EditText et_canvas) {
        this.et_canvas = et_canvas;
    }

    public String getEt_text() {
        return et_text;
    }

    public void setEt_text(String et_text) {
        this.et_text = et_text;
    }

    public String getEt_fieldname() {
        return et_fieldname;
    }

    public void setEt_fieldname(String et_fieldname) {
        this.et_fieldname = et_fieldname;
    }

    public String getEt_placeholder() {
        return et_placeholder;
    }

    public void setEt_placeholder(String et_placeholder) {
        this.et_placeholder = et_placeholder;
    }

    public String getEt_sequence() {
        return et_sequence;
    }

    public void setEt_sequence(String et_sequence) {
        this.et_sequence = et_sequence;
    }
}
