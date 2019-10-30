package com.example.a7th_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    private List<CustomListItem> rowItems;

    public CustomAdapter(@NonNull Context context, ArrayList<CustomListItem> customListItems) {
        super(context, 0, customListItems);
        this.rowItems = customListItems;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Get the item to be displayed
        final CustomListItem rowItem = rowItems.get(position);

        // IF null then inflate the layout. Otherwise just update layout contents.
        if (convertView == null) {
            // Get the custom layout and inflate it
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the customLayout components
        TextView textOwner = convertView.findViewById(R.id.textViewOwner);
        TextView textLicense = convertView.findViewById(R.id.textViewLicense);
        ImageView mainImage = convertView.findViewById(R.id.imageViewItemImage);

        // Set content
        textOwner.setText(rowItem.owner);
        textLicense.setText(rowItem.license);
        mainImage.setImageBitmap(rowItem.image);

        return convertView;
    }
}
