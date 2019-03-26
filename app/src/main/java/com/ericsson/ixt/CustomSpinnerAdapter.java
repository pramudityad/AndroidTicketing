package com.ericsson.ixt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HPEAdmin on 1/4/2018.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<SpinnerDataHistory> {
    private Context context;
    private List<SpinnerDataHistory> spinnerDataHistories;


    public CustomSpinnerAdapter(@NonNull Context context, int resource, List<SpinnerDataHistory>spinnerDataHistories) {
        super(context, resource, spinnerDataHistories);
        this.context = context;
        this.spinnerDataHistories = spinnerDataHistories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return CustomSpinnerHistory(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return CustomSpinnerHistory(position, convertView, parent);
    }

    private View CustomSpinnerHistory(int position, @Nullable View myView, @Nullable ViewGroup parent) {
        //Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.spinner_layout,parent,false);
        ImageView image1 = (ImageView)customView.findViewById(R.id.image1);
        TextView textView1 = (TextView)customView.findViewById(R.id.textView1);
        textView1.setText(spinnerDataHistories.get(position).getIconName());
        image1.setImageResource(spinnerDataHistories.get(position).getIcon());

        //textView1.setTypeface(face);
        return customView;
    }
}
