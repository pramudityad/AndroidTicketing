package com.ericsson.ixt;

/**
 * Created by ericsson on 11/15/2017.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapterHistory extends BaseAdapter {
    private Context mContext;
    private List<tickethist> mHistoryList;
    private ArrayList<tickethist> arraylist;


    //Constructor

    public CustomAdapterHistory(Context mContext, List<tickethist> mHistoryList) {
        this.mContext = mContext;
        this.mHistoryList = mHistoryList;
        this.arraylist = new ArrayList<tickethist>();
        this.arraylist.addAll(mHistoryList);
    }

    @Override
    public int getCount() {
        return mHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       // Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");


        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView tvitemtext = (TextView)v.findViewById(R.id.item_text);
        TextView tvpreview = (TextView)v.findViewById(R.id.tvpreview);
        ImageView buckysImage = (ImageView) v.findViewById(R.id.my_profile_image);
        //Set text for TextView
        tvitemtext.setText(mHistoryList.get(position).getTicketid());
        tvpreview.setText(String.valueOf(mHistoryList.get(position).getEvactivity()));
        buckysImage.setImageResource(R.drawable.history_flat);
        //buckysImage.setImageResource(R.drawable.ic_profile);
        //tvitemtext.setTypeface(face);
        //tvpreview.setTypeface(face);

        //Save product id to tag
        return v;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
            mHistoryList.clear();

        if (charText.length() == 0 ||charText.equalsIgnoreCase("All") ) {
            mHistoryList.addAll(arraylist);
        }
        else
        {
            for (tickethist wp : arraylist)
            {


                if (wp.getEvactivity().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mHistoryList.add(wp);
                }



            }
        }
        notifyDataSetChanged();
    }


}
