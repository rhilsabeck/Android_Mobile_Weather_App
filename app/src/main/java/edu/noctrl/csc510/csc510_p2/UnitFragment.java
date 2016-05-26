package edu.noctrl.csc510.csc510_p2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnitFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnitFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public interface OnDialogSelectorListener {
        public void onSelectedOption(int dialogId);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnitFragment newInstance(String param1, String param2) {
        UnitFragment fragment = new UnitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UnitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    int result = 0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final CharSequence[] items = {" Imperial "," Metric "};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.units);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        result = 1;
                        Log.i("Unit", "Imperial");
                        break;
                    case 1:
                        result = 2;
                        Log.i("Unit", "Metric");
                        break;
                }
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int item) {
                if (result == 0) {
                    Toast.makeText(getActivity(), "Select One Choice",
                            Toast.LENGTH_SHORT).show();
                } else if (result == 1) {
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE) ;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.units), "Imperial");
                    editor.commit();
                    MainActivity callingActivity = (MainActivity) getActivity();
                    callingActivity.convertCurrentPage();
                    Log.i("unit preference", "Imperial Selected");
                } else if (result == 2) {
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE) ;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.units), "Metric");
                    editor.commit();
                    MainActivity callingActivity = (MainActivity) getActivity();
                    callingActivity.convertCurrentPage();
                    Log.i("unit preference", "Metric Selected");
                }
            }
        });
        return builder.create();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unit, container, false);
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
