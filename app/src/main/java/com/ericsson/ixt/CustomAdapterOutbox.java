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

public class CustomAdapterOutbox extends BaseAdapter {
    private Context mContext;
    private List<ticketout> mOutboxList;
    private ArrayList<ticketout> arraylist;


    //Constructor

    public CustomAdapterOutbox(Context mContext, List<ticketout> mOutboxList) {
        this.mContext = mContext;
        this.mOutboxList = mOutboxList;
        this.arraylist = new ArrayList<ticketout>();
        this.arraylist.addAll(mOutboxList);
    }

    @Override
    public int getCount() {
        return mOutboxList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOutboxList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");

        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView tvitemtext = (TextView)v.findViewById(R.id.item_text);
        TextView tvpreview = (TextView)v.findViewById(R.id.tvpreview);
        ImageView buckysImage = (ImageView) v.findViewById(R.id.my_profile_image);
        //Set text for TextView
        tvitemtext.setText(mOutboxList.get(position).getTicketid());
        tvpreview.setText(String.valueOf(mOutboxList.get(position).getEvactivity()));
        buckysImage.setImageResource(R.drawable.mailbox_icon);
        //buckysImage.setImageResource(R.drawable.ic_profile);

       // tvitemtext.setTypeface(face);
       // tvpreview.setTypeface(face);

        //Save product id to tag
        return v;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
            mOutboxList.clear();

        if (charText.length() == 0 ||charText.equalsIgnoreCase("All") ) {
            mOutboxList.addAll(arraylist);
        }
        else
        {
            for (ticketout wp : arraylist)
            {


                if (wp.getEvactivity().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mOutboxList.add(wp);
                }



            }
        }
        notifyDataSetChanged();
    }


}
