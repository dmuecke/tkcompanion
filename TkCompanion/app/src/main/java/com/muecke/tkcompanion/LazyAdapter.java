package com.muecke.tkcompanion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muecke.tkcompanion.model.Swimmer;

import java.util.List;

public class LazyAdapter extends ArrayAdapter<Swimmer> {

    public LazyAdapter(Context context,  List<Swimmer> objects) {
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

        if (swimmer.getRound() > 1) {
            int total = swimmer.getTotal();
            int totmin = total / 600;
            int totsec = total % 600;
            split_time.setText(String.format("%02d:%02d", totmin, totsec/10) + "." + totsec%10);

        } else {
            split_time.setText("00:00.0");
        }
        return rowView;
    }
}
