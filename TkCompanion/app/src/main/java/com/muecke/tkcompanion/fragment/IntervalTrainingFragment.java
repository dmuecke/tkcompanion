package com.muecke.tkcompanion.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.muecke.tkcompanion.R;
import com.muecke.tkcompanion.activity.ResultDetails;
import com.muecke.tkcompanion.adapter.IntervalWatchAdapter;
import com.muecke.tkcompanion.model.Swimmer;
import com.muecke.tkcompanion.model.Team;
import com.muecke.tkcompanion.model.WatchStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntervalTrainingFragment.InteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntervalTrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class IntervalTrainingFragment extends Fragment {


    private InteractionListener listener;
    private IntervalWatchAdapter swimmerAdapter;
    private WatchStatus timerStatus = WatchStatus.FRESH;
    private ListView viewSwimmers;

    private long gapTime = 5;
    private int interval = 40;
    private int lanesPerInterval = 1;



    final List<Swimmer> starters = new ArrayList<Swimmer>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SwimmersFragment.
     */
    public static IntervalTrainingFragment newInstance() {
        IntervalTrainingFragment fragment = new IntervalTrainingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public IntervalTrainingFragment() {
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

        swimmerAdapter = new IntervalWatchAdapter(getActivity(), starters);

        Button addSwimmer = (Button) view.findViewById(R.id.button_add_swimmer);
        addSwimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starters.clear();
                starters.addAll(Team.getTeam());
                final String[] names = new String[starters.size()];
                final boolean[] checkedItems = new boolean[starters.size()];

                for (int i = 0; i < starters.size(); i++) {
                    Swimmer swimmer = starters.get(i);
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
                        for (i = starters.size() - 1; i >= 0; i--) {
                            if (!checkedItems[i]) {
                                starters.remove(i);
                            }
                        }
                        resetSwimmers();
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

        final String[] watchModes = new String[]{"Race","Relay","Staggered"};
        final TextView watchModeView = (TextView) view.findViewById(R.id.watch_mode);
        watchModeView.setText("Mode:" + watchModes[2]);


        final TextView sendOffView = (TextView) view.findViewById(R.id.send_off_time);
        sendOffView.setVisibility(View.VISIBLE);
        sendOffView.setText(String.format("Send-Off: %ds", gapTime));
        sendOffView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStatus.equals(WatchStatus.RUNNING)) {

                    final AlertDialog.Builder sendOffDialog = new AlertDialog.Builder(getActivity());

                    sendOffDialog.setTitle("Send-Off Time");
                    sendOffDialog.setMessage("Define send-off time between swimmers in seconds.");

                    // Set an EditText view to get user input
                    final EditText sendOffInput = new EditText(getActivity());
                    sendOffInput.setText(String.valueOf(gapTime));
                    sendOffDialog.setView(sendOffInput);

                    sendOffDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            gapTime = Integer.parseInt(sendOffInput.getText().toString());
                            sendOffView.setText(String.format("Send-Off: %ds", gapTime));
                        }
                    });

                    sendOffDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    sendOffDialog.show();
                }
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
                        if (position + 1 == starters.size()) {
                            position = 0;
                        } else if (position > 3) {
                            position -= 3;
                        }
                        viewSwimmers.setSelection(position);

                        break;
                    }

                    case FRESH: {
                        if (position < starters.size() - 1) {
                            starters.remove(position);
                            starters.add(swimmer);
                        } else {
                            starters.remove(position);
                            starters.add(0 , swimmer);

                        }
                        swimmerAdapter.notifyDataSetChanged();

                        break;
                    }

                    case STOPPED: {
                        Intent launchactivity= new Intent(getActivity(),ResultDetails.class);
                        launchactivity.putExtra("RESULT", swimmer.getResult(lanesPerInterval));
                        startActivity(launchactivity);

                    }
                }

            }
        });
        viewSwimmers.setAdapter(swimmerAdapter);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Team.stopInterval(starters);
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

    public void ChronometerTick(long base) {
        long realtime = SystemClock.elapsedRealtime();
        final long elapsed = (realtime - base) / 1000;
        int index = (int) (elapsed / gapTime);
        boolean pushOff = (elapsed % gapTime) == 0;
        if (index < starters.size() && pushOff) {
            starters.get(index).pushOff(interval, realtime);
        }

        swimmerAdapter.notifyDataSetChanged();

    }

    private void resetSwimmers() {
        for (Swimmer swimmer : starters) {
            swimmer.reset();
        }
        swimmerAdapter.notifyDataSetChanged();
    }

    public void timerStatus(WatchStatus timerStatus, Context context) {
        this.timerStatus = timerStatus;
        switch (timerStatus) {
            case RUNNING: {
                viewSwimmers.setSelection(0);
                break;
            }
            case STOPPED: {
                Team.stopInterval(starters);
                Team.saveIntervals(context, starters, lanesPerInterval);
                break;
            }
            case FRESH: {
                resetSwimmers();
                break;
            }
        }
    }

    public void setInterval(int interval, int lanesPerInterval) {
        this.interval = interval;
        this.lanesPerInterval = lanesPerInterval;
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
