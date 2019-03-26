package com.ericsson.ixt;

import android.widget.EditText;

/**
 * Created by ericsson on 11/9/2018.
 */

public class Wc_longtext {
    private EditText lt_canvas;
    private String lt_text;
    private String lt_fieldname;
    private String lt_placeholder;
    private String lt_sequence;
    private String lt_item;

    public Wc_longtext(EditText lt_canvas, String lt_text, String lt_fieldname, String lt_placeholder, String lt_sequence, String lt_item) {
        this.lt_canvas = lt_canvas;
        this.lt_text = lt_text;
        this.lt_fieldname = lt_fieldname;
        this.lt_placeholder = lt_placeholder;
        this.lt_sequence = lt_sequence;
        this.lt_item=lt_item;
    }

    public String getLt_item() {
        return lt_item;
    }

    public void setLt_item(String lt_item) {
        this.lt_item = lt_item;
    }

    public EditText getLt_canvas() {
        return lt_canvas;
    }

    public void setLt_canvas(EditText lt_canvas) {
        this.lt_canvas = lt_canvas;
    }

    public String getLt_text() {
        return lt_text;
    }

    public void setLt_text(String lt_text) {
        this.lt_text = lt_text;
    }

    public String getLt_fieldname() {
        return lt_fieldname;
    }

    public void setLt_fieldname(String lt_fieldname) {
        this.lt_fieldname = lt_fieldname;
    }

    public String getLt_placeholder() {
        return lt_placeholder;
    }

    public void setLt_placeholder(String lt_placeholder) {
        this.lt_placeholder = lt_placeholder;
    }

    public String getLt_sequence() {
        return lt_sequence;
    }

    public void setLt_sequence(String lt_sequence) {
        this.lt_sequence = lt_sequence;
    }
}
