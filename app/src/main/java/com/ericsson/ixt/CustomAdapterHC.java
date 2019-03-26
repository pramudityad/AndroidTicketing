package com.ericsson.ixt;

/**
 * Created by ericsson on 11/15/2017.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class CustomAdapterHC extends ArrayAdapter<String>{

    //Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/ericssoncapitaltt-webfont.ttf");
    public CustomAdapterHC(Context context, String[] subject) {
        super(context, R.layout.list_item,subject);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // add the layout
        LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
        View customView = myCustomInflater.inflate(R.layout.list_item, parent, false);
        // get references.
        String singleItem = getItem(position);
        TextView itemText = (TextView) customView.findViewById(R.id.item_text);
        ImageView buckysImage = (ImageView) customView.findViewById(R.id.my_profile_image);

        //itemText.setTypeface(face);


        // dynamically update the text from the array
        itemText.setText(singleItem);
        // using the same image every time
        buckysImage.setImageResource(R.drawable.inbox_flat);
        // Now we can finally return our custom View or custom item
        return customView;
    }
}
