package com.muecke.tkcompanion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.model.Person;

import java.util.List;

public class PersonsAdapter extends ArrayAdapter<Person> {
    public PersonsAdapter(Context context, List<Person> list) {
        super(context, R.layout.list_row_presence,list);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view == null ? inflater.inflate(R.layout.list_row_presence, parent,false) : view;

        Person swimmer = getItem(position);

        final CheckedTextView checkedViewSwimmer = (CheckedTextView) rowView.findViewById(R.id.swim_name);
        checkedViewSwimmer.setChecked(swimmer.isPresent());

        checkedViewSwimmer.setText(swimmer.getName());
        return rowView;
    }
}
