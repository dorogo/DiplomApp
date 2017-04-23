package com.diplom.ilya.diplom.CustomAdapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.diplom.ilya.diplom.R;

import java.util.List;

/**
 * Created by user on 13.04.17.
 */
public class CustomAdapter extends ArrayAdapter<Items> {
    private int resource;
    private LayoutInflater mLayoutInflater;

    public CustomAdapter(Context context, @LayoutRes int resourceId, @NonNull List objects) {
        super(context, resourceId, objects);

        resource = resourceId;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = (RelativeLayout) mLayoutInflater.inflate( resource, null );

        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(resource, parent, false);
        }

        Items item = (Items) getItem( position );

        TextView txtName = (TextView) view.findViewById(R.id.label);
        txtName.setText(item.getName());

        TextView txtDesc = (TextView) view.findViewById(R.id.secondLabel);
        txtDesc.setText(item.getResult());



        return view;
    }
}
