package com.muecke.tkcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.Team;
import com.muecke.tkcompanion.model.WatchStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.muecke.tkcompanion.SwimmersFragment.InteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwimmersFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SwimmersFragment extends Fragment {


    private InteractionListener listener;
    private LazyAdapter swimmerAdapter;
    private WatchStatus timerStatus = WatchStatus.FRESH;
    private ListView viewSwimmers;

    private long gapTime = 5;


    final List<Swimmer> team = new ArrayList<Swimmer>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SwimmersFragment.
     */
    public static SwimmersFragment newInstance() {
        SwimmersFragment fragment = new SwimmersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public SwimmersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swimmers, container, false);

        swimmerAdapter = new LazyAdapter(getActivity(), team);

        Button addSwimmer = (Button) view.findViewById(R.id.button_add_swimmer);
        addSwimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                team.clear();
                team.addAll(Team.getTeam());
                final String[] names = new String[team.size()];
                final boolean[] checkedItems = new boolean[team.size()];

                for (int i = 0; i < team.size(); i++) {
                    Swimmer swimmer = team.get(i);
                    names[i] = swimmer.getName();
                    checkedItems[i]=true;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Swimmers");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (i = team.size() - 1; i >= 0; i--) {
                            if (!checkedItems[i]) {
                                team.remove(i);
                            }
                        }
                        SwimmerReset();
                    }
                });

                builder.setMultiChoiceItems(names,checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item, boolean checked) {
                        checkedItems[item] = checked;
                    }
                });

                builder.show();
            }
        });



        viewSwimmers = (ListView) view.findViewById(R.id.list_swimmer);
        viewSwimmers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Swimmer swimmer = (Swimmer) parent.getItemAtPosition(position);
                switch (timerStatus) {
                    case RUNNING: {
                        swimmer.setLapTime(SystemClock.elapsedRealtime());
                        swimmerAdapter.notifyDataSetChanged();
                        if (position + 1 == team.size()) {
                            position = 0;
                        } else if (position > 3) {
                            position -= 3;
                        }
                        Log.d("selection position", "" + position);
                        viewSwimmers.setSelection(position);

                        break;
                    }

                    case FRESH: {
                        if (position < team.size() - 1) {
                            team.remove(position);
                            team.add(swimmer);
                        } else {
                            team.remove(position);
                            team.add(0 , swimmer);

                        }
                        swimmerAdapter.notifyDataSetChanged();

                        break;
                    }

                    case STOPPED: {
                        Intent launchactivity= new Intent(getActivity(),ListResultsActivity.class);
                        launchactivity.putExtra("SWIMMER", swimmer);
                        startActivity(launchactivity);

                    }
                }

            }
        });
        viewSwimmers.setAdapter(swimmerAdapter);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (InteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void ChronometerTick(int stopWatchMode, long base) {
        long realtime = SystemClock.elapsedRealtime();
        final long elapsed = (realtime - base) / 1000;

        switch (stopWatchMode) {
            case 1:
            case 2: {
                if (elapsed == 0) {
                    for (Swimmer swimmer : team) {
                        swimmer.pushOff(realtime);
                    }
                }
                break;
            }
            case 3: {
                int index = (int) (elapsed / gapTime);
                boolean pushOff = (elapsed % gapTime) == 0;
                if (index < team.size() && pushOff) {
                    team.get(index).pushOff(realtime);

                }
            }
        }
        swimmerAdapter.notifyDataSetChanged();

    }

    public void SwimmerReset() {
        for (Swimmer swimmer : team) {
            swimmer.reset();
        }
        swimmerAdapter.notifyDataSetChanged();
    }

    public void timerStatus(WatchStatus timerStatus) {
        this.timerStatus = timerStatus;
        if (timerStatus == WatchStatus.RUNNING) {
            viewSwimmers.setSelection(0);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface InteractionListener {
        public void onInteraction(Swimmer swimmer);
    }

}
