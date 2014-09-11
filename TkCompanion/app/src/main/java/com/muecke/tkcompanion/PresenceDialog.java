package com.muecke.tkcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link com.muecke.tkcompanion.PresenceDialog.InteractionListener} interface
 * to handle interaction events.
 * Use the {@link PresenceDialog#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PresenceDialog extends DialogFragment  {
    private static final String PERSONS = "names";


    private InteractionListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PresenceDialog.
     */
    public static PresenceDialog newInstance(String[] names) {
        PresenceDialog fragment = new PresenceDialog();
        Bundle args = new Bundle();
        args.putStringArray(PERSONS, names);
        fragment.setArguments(args);
        return fragment;
    }

    public PresenceDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] persons = getArguments().getStringArray(PERSONS);
        final boolean[] checkedItems = new boolean[persons.length];

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
                if (listener != null) {
                    listener.onPresenceInteraction(checkedItems);
                }
            }
        });

        builder.setMultiChoiceItems(persons,checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item, boolean checked) {
                checkedItems[item] = checked;
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (InteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        public void onPresenceInteraction(boolean[] checkedSwimmers);
    }

}
