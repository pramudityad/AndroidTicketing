package com.ericsson.ixt;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ericsson on 11/9/2018.
 */

public class Wc_filecontainer {
    private TextView filename;
    private Button uriadd,imageselect,clearpic;
    private String fc_text;
    private String fc_fieldname;
    private String fc_placeholder;
    private String fc_sequence;
    private String fc_item;

    public Wc_filecontainer(TextView filename, Button uriadd, Button imageselect, Button clearpic, String fc_text, String fc_fieldname, String fc_placeholder, String fc_sequence, String fc_item) {
        this.filename = filename;
        this.uriadd = uriadd;
        this.imageselect = imageselect;
        this.clearpic = clearpic;
        this.fc_text = fc_text;
        this.fc_fieldname = fc_fieldname;
        this.fc_placeholder = fc_placeholder;
        this.fc_sequence = fc_sequence;
        this.fc_item = fc_item;
    }

    public TextView getFilename() {
        return filename;
    }

    public void setFilename(TextView filename) {
        this.filename = filename;
    }

    public Button getUriadd() {
        return uriadd;
    }

    public void setUriadd(Button uriadd) {
        this.uriadd = uriadd;
    }

    public Button getImageselect() {
        return imageselect;
    }

    public void setImageselect(Button imageselect) {
        this.imageselect = imageselect;
    }

    public Button getClearpic() {
        return clearpic;
    }

    public void setClearpic(Button clearpic) {
        this.clearpic = clearpic;
    }

    public String getFc_text() {
        return fc_text;
    }

    public void setFc_text(String fc_text) {
        this.fc_text = fc_text;
    }

    public String getFc_fieldname() {
        return fc_fieldname;
    }

    public void setFc_fieldname(String fc_fieldname) {
        this.fc_fieldname = fc_fieldname;
    }

    public String getFc_placeholder() {
        return fc_placeholder;
    }

    public void setFc_placeholder(String fc_placeholder) {
        this.fc_placeholder = fc_placeholder;
    }

    public String getFc_sequence() {
        return fc_sequence;
    }

    public void setFc_sequence(String fc_sequence) {
        this.fc_sequence = fc_sequence;
    }

    public String getFc_item() {
        return fc_item;
    }

    public void setFc_item(String fc_item) {
        this.fc_item = fc_item;
    }
}
