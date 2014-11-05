package com.muecke.tkcompanion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.model.Competition;
import com.muecke.tkcompanion.model.Result;
import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.Swimming;

import java.util.List;

public class ResultsAdapter extends ArrayAdapter<Result> {

    public ResultsAdapter(Context context, List<Result> objects) {
        super(context, R.layout.result_row, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view == null ? inflater.inflate(R.layout.result_row, parent,false) : view;

        TextView session = (TextView) rowView.findViewById(R.id.session);
        TextView comp_view = (TextView) rowView.findViewById(R.id.competition);

        TextView name = (TextView) rowView.findViewById(R.id.swim_name);
        TextView total_time = (TextView) rowView.findViewById(R.id.total_time);
        TextView avg_time = (TextView)rowView.findViewById(R.id.avg_data);

        Result result = getItem(position);
        session.setText(result.getSession());
        comp_view.setText(result.getCompetition());

        name.setText(result.getName());
        avg_time.setText(Swimmer.formatTime(result.getAvg()));

        total_time.setText(Swimmer.formatTime(result.getTotal()));
        return rowView;
    }
}
