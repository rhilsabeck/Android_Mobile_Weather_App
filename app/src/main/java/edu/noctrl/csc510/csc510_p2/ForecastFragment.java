package edu.noctrl.csc510.csc510_p2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;


public class ForecastFragment extends android.support.v4.app.Fragment {


    private static final String AREA = "area";
    private static final String DAY = "day";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String AMTITLE = "amtitle";
    private static final String PMTITLE = "pmtitle";
    private static final String FOREAM = "foream";
    private static final String FOREPM = "forepm";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String IMG = "img";
    private static final String PRECIP = "precipitation";
    TextView area_description, day_of_week, forecast_high, forecast_low, forecast_am_title, forecast_pm_title, forecast, precipitation;
    ImageView forcast_image;
    LinearLayout am_container, pm_container;
    DecimalFormat df = new DecimalFormat("#.##");
    DecimalFormat tdf = new DecimalFormat("#");
    private String Area, Day, Amtitle, Pmtitle, Foream, Forepm,Lat, Lon, Precipitation, imageUrl;
    private Double High, Low;
    private String units;
    private OnFragmentInteractionListener mListener;
    private View.OnClickListener am_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            forecast.setText(Foream);
            am_container.setBackgroundColor(Color.rgb(189, 215, 238));
            pm_container.setBackgroundColor(Color.rgb(217, 217, 217));
        }
    };
    private View.OnClickListener pm_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            forecast.setText(Forepm);
            pm_container.setBackgroundColor(Color.rgb(189, 215, 238));
            am_container.setBackgroundColor(Color.rgb(217, 217, 217));
        }
    };


    public ForecastFragment() {
        // Required empty public constructor
    }


    public static ForecastFragment newInstance(WeatherInfo info, int day) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();


        args.putString(AREA, info.location.name);
        if (day == 0) {
            args.putString(DAY, "TODAY");
        } else {
            args.putString(DAY, info.forecast.get(day).day.toString());
        }
        //DETERMINE HIGH AND LOW
        Log.d("tylerDebug","Determine High and Low");
        //try {
        String amdesc = "";
        String amdet = "";
        String pmdesc = "";
        String pmdet = "";
        Double amt = 0.00;
        Double pmt = 0.00;
        if(info.forecast.get(day).amForecast != null){
            amt = info.forecast.get(day).amForecast.temperature;
            amdesc = info.forecast.get(day).amForecast.description;
            amdet = info.forecast.get(day).amForecast.details;
        }
        if(info.forecast.get(day).pmForecast != null){
            pmt = info.forecast.get(day).pmForecast.temperature;
            pmdesc = info.forecast.get(day).pmForecast.description;
            pmdet = info.forecast.get(day).pmForecast.details;
        }

        if (amt > pmt) {
            args.putDouble(HIGH, amt);
            args.putDouble(LOW, pmt);
        } else {
            args.putDouble(HIGH, pmt);
            args.putDouble(LOW, amt);
        }


        args.putString(AMTITLE, amdesc);
        args.putString(PMTITLE, pmdesc);


        args.putString(FOREAM, amdet);
        args.putString(FOREPM, pmdet);


        args.putString(LAT,String.valueOf(info.location.latitude));
        args.putString(LON, String.valueOf(info.location.longitude));
        args.putString(PRECIP, String.valueOf(info.forecast.get(day).precipitation));
        args.putString(IMG, String.valueOf(info.forecast.get(day).icon));

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        units = sharedPref.getString(getString(R.string.units), "Imperial");


        if (getArguments() != null) {
            Area = getArguments().getString(AREA);
            Day = getArguments().getString(DAY);
            High = getArguments().getDouble(HIGH);
            Low = getArguments().getDouble(LOW);
            Amtitle = getArguments().getString(AMTITLE);
            Pmtitle = getArguments().getString(PMTITLE);
            Foream = getArguments().getString(FOREAM);
            Forepm = getArguments().getString(FOREPM);
            Lon = getArguments().getString(LON);
            Lat = getArguments().getString(LAT);
            Precipitation = getArguments().getString(PRECIP);
            imageUrl = getArguments().getString(IMG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);


        //get fragment view items
        area_description = (TextView) view.findViewById(R.id.area_description);
        day_of_week = (TextView) view.findViewById(R.id.day_of_week);
        forecast_high = (TextView) view.findViewById(R.id.forecast_high);
        forecast_low = (TextView) view.findViewById(R.id.forecast_low);
        forecast_am_title = (TextView) view.findViewById(R.id.forecast_am_title);
        forecast_pm_title = (TextView) view.findViewById(R.id.forecast_pm_title);
        am_container = (LinearLayout) view.findViewById(R.id.am_container);
        pm_container = (LinearLayout) view.findViewById(R.id.pm_container);
        forecast = (TextView) view.findViewById(R.id.forecast);
        precipitation = (TextView) view.findViewById(R.id.precipitation);
        forcast_image = (ImageView) view.findViewById(R.id.forcast_image);

        //Geo intent to click on current location and get google maps(when target is Google API
        //in emulator)
        View.OnClickListener listener = new View.OnClickListener()
        {
            public void onClick(View v){
                String geo = "geo:" + Lat + "," + Lon;
                Uri uri = Uri.parse(geo);
                Intent urlClick = new Intent(Intent.ACTION_VIEW, uri);
                getActivity().startActivity(urlClick);
            }
        };
        area_description.setOnClickListener(listener);

       //** Getting Cache Directory *//**//*
        File cDir = getActivity().getCacheDir();
        String fileName = Uri.parse(imageUrl).getLastPathSegment();
        File newImage = new File(cDir,fileName);
        if(newImage.exists())
        {
            Log.i("cache", "It exists");
            Bitmap bmp = BitmapFactory.decodeFile(newImage.getAbsolutePath());
            forcast_image.setImageBitmap(bmp);
        }
        else {
            try {
                URL url = new URL(imageUrl);
                new DownloadImage().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        }


        //set fragment data
        area_description.setText(Area);
        day_of_week.setText(Day);
        if (units.equals("Imperial")) {
            forecast_high.setText(String.valueOf(High) + (char) 176 + " F");
            forecast_low.setText(String.valueOf(Low) + (char) 176 + " F");
        } else {
            String h = String.valueOf(tdf.format(convertTempToCelcius(High)));
            String l = String.valueOf(tdf.format(convertTempToCelcius(Low)));
            forecast_high.setText(h + (char) 176 + " C");
            forecast_low.setText(l + (char) 176 + " C");
        }
        forecast_am_title.setText(Amtitle);
        forecast_pm_title.setText(Pmtitle);
        //default view details to am
        forecast.setText(Foream);
        //depending on which container is clicked show details
        am_container.setOnClickListener(am_Listener);
        pm_container.setOnClickListener(pm_Listener);
        precipitation.setText(Precipitation +"%");

        return view;
    }


    public Double convertTempToCelcius(Double temperature) {
        //temperature conversion from F to C
        return ((temperature - 32) * (5.0 / 9));
    }


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
        void onFragmentInteraction(Uri uri);
    }

    public void bitMaptoFileCache(Bitmap bmap,String filename) throws IOException {
        //create a file to write bitmap data
        File f = new File(getActivity().getCacheDir(), filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap = bmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
    }

    //async task class to download weather image if not cached
    private class DownloadImage extends AsyncTask<URL, Void, Boolean> {
        public Bitmap mIcon_val;
        public IOException error;
        public Bitmap resizedBitmap;

        @Override
        protected Boolean doInBackground(URL... params) {
            try {
                mIcon_val = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
                resizedBitmap = Bitmap.createScaledBitmap(mIcon_val,125 ,175, false);

            } catch (IOException e) {
                this.error = e;
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                forcast_image.setImageBitmap(resizedBitmap);
                String fileName = Uri.parse(imageUrl).getLastPathSegment();
                try {
                    bitMaptoFileCache(resizedBitmap,fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                forcast_image.setImageResource(R.drawable.default_image);
            }
        }
    };

}
