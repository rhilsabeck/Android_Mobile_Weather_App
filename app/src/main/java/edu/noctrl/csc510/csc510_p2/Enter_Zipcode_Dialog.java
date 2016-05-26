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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Enter_Zipcode_Dialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Enter_Zipcode_Dialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Enter_Zipcode_Dialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PopupMenu popupMenu;
    EditText editZipCode;
    TextView recentZips;
    private final static int ZERO = 0;
    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int THREE = 3;
    private final static int FOUR = 4;
    private final static int FIVE = 5;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Enter_Zipcode_Dialog() {
        // Required empty public constructor

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Enter_Zipcode_Dialog.
     */
    // TODO: Rename and change types and number of parameters
    public Enter_Zipcode_Dialog newInstance(String param1, String param2) {
        Enter_Zipcode_Dialog fragment = new Enter_Zipcode_Dialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("dialog", "Start");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        Log.i("dialog", "inflater created");
        final View view = inflater.inflate(R.layout.fragment_enter__zipcode__dialog, null);
        recentZips = (TextView)view.findViewById(R.id.get_recent_zips);
        View.OnClickListener listener = new View.OnClickListener()
        {
            public void onClick(View v){
                showRecentZipCodePopUp(view);
            }

        };
        recentZips.setOnClickListener(listener);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        builder.setTitle(R.string.enter_ZipCode);
        builder.setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Dialog f = (Dialog) dialog;
                EditText zipCodeText = (EditText) f.findViewById(R.id.dialog_zipcode);
                String value = zipCodeText.getText().toString();
                MainActivity callingActivity = (MainActivity) getActivity();
                if(callingActivity.zipValidation(value)) {
                    try {
                        zipList(value);
                        callingActivity.onUserSelectValue(value);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                else
                    callingActivity.showToast("Please enter in a valid 5 digit zip");
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Enter_Zipcode_Dialog.this.getDialog().cancel();
            }

        });

        return builder.create();
    }

    public void zipList(String zip)
    {
        String str = "";
        List <String> arrayList;
        SharedPreferences sPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = sPrefs.edit();
        String recentZipList = sPrefs.getString("ZipList", null);
        if(recentZipList == null)
        {
            arrayList = new ArrayList();
            stringIntoPreferences(sEdit,str,zip,arrayList);
        }
        else
        {
            arrayList = new ArrayList<String>(Arrays.asList(recentZipList.split(",")));
            if(arrayList.contains(zip))
            {
                return;
            }
            else
            {
                if(arrayList.size() == 5)
                {
                    arrayList.remove(0);
                }
                stringIntoPreferences(sEdit,str,zip,arrayList);
            }
        }
    }

    public void stringIntoPreferences(SharedPreferences.Editor ed, String str, String zip, List<String> list)
    {
        list.add(zip);
        for (String s : list)
        {
            str += s + ",";
        }
        ed.putString("ZipList", str);
        ed.commit();
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    public void showRecentZipCodePopUp(final View v)
    {
        popupMenu = new PopupMenu(getActivity(),v.findViewById(R.id.get_recent_zips));
        SharedPreferences sPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor sEdit = sPrefs.edit();
        String recentZipList = sPrefs.getString("ZipList", null);
        if(recentZipList == null)
        {
            popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "");
        }
        else
        {
            int i = 1;
            List<String> arrayList = new ArrayList<String>(Arrays.asList(recentZipList.split(",")));
            for (String temp : arrayList) {
                popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, temp);
                 i++;
            }
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item) {

                editZipCode = (EditText)v.findViewById(R.id.dialog_zipcode);
                editZipCode.setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }





    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter__zipcode__dialog, container, false);
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
