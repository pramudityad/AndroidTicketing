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

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<ticketin> mInboxList;



    //Constructor

    public CustomAdapter(Context mContext, List<ticketin> mProductList) {
        this.mContext = mContext;
        this.mInboxList = mProductList;
    }

    @Override
    public int getCount() {
        return mInboxList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInboxList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        //LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View v = myCustomInflater.inflate(R.layout.list_item, null);

        String singleItem = getItem(position);

        TextView tvitemtext = (TextView)v.findViewById(R.id.item_text);
        TextView tvpreview = (TextView)v.findViewById(R.id.tvpreview);
        ImageView buckysImage = (ImageView) v.findViewById(R.id.my_profile_image);
        //Set text for TextView
        tvitemtext.setText(mInboxList.get(position).getItemtext());
        tvpreview.setText(String.valueOf(mInboxList.get(position).getPreview()));
        // using the same image every time
        buckysImage.setImageResource(R.drawable.ic_profile);

        return v; */
        //Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");

        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView tvitemtext = (TextView)v.findViewById(R.id.item_text);
        TextView tvpreview = (TextView)v.findViewById(R.id.tvpreview);
        ImageView buckysImage = (ImageView) v.findViewById(R.id.my_profile_image);
    //Set text for TextView
        tvitemtext.setText(mInboxList.get(position).getTicketid());
        tvpreview.setText(String.valueOf(mInboxList.get(position).getRequestor()));

        //tvitemtext.setTypeface(face);
        //tvpreview.setTypeface(face);



    //Save product id to tag
        v.setTag(mInboxList.get(position).getTicketid());


        return v;


    }

}
