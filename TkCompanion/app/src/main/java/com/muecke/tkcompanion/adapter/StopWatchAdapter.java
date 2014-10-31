package com.muecke.tkcompanion.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.model.Swimmer;

import java.util.List;

public class StopWatchAdapter extends ArrayAdapter<Swimmer> {

    public StopWatchAdapter(Context context, List<Swimmer> objects) {
        super(context, R.layout.list_row, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view == null ? inflater.inflate(R.layout.list_row, parent,false) : view;

        TextView name = (TextView) rowView.findViewById(R.id.swim_name);
        TextView split_time = (TextView) rowView.findViewById(R.id.split_time);
        TextView round = (TextView)rowView.findViewById(R.id.text_round);

        Swimmer swimmer = getItem(position);
        name.setText(swimmer.getName());
        round.setText("" + swimmer.getRound());

        split_time.setText(Swimmer.formatTime(swimmer.getTotal()));
        return rowView;
    }
}
