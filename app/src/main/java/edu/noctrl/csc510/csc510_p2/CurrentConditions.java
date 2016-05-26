package edu.noctrl.csc510.csc510_p2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentConditions.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentConditions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentConditions extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
     private static final String TIME = "time";
     private static final String COND = "cond";
     private static final String TEMP = "temp";
     private static final String HUMID = "humid";
     private static final String DEW = "dew";
     private static final String PRES = "pres";
     private static final String VISI = "visi";
     private static final String WNDSP = "wndsp";
     private static final String GUST = "gust";
     private static final String LOC = "loc";
     private static final String LAT = "lat";
     private static final String LON = "lon";
     private static final String IMG = "img";
     private static final String DIRECTION = "direction";
     String units;
     TextView  currentTime, currentConditions,temperature,relativeHumidity,dewPoint,pressure,
            visibility, windSpeed, gusts, location;
     ImageView image;
     int day;
     private String Time, Cond, Temp, Humid, Dew, Pres, Visi, Wndsp, Wdirect, Gust,Loc, Lat, Lon, imageUrl;
     private OnFragmentInteractionListener mListener;


    public CurrentConditions() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CurrentConditions.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentConditions newInstance(WeatherInfo weatherObject) {
        CurrentConditions fragment = new CurrentConditions();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);*/
        args.putString(TIME, weatherObject.current.timestamp);
        args.putString(TEMP, String.valueOf(weatherObject.current.temperature));
        args.putString(COND, String.valueOf(weatherObject.current.summary));
        args.putString(HUMID, String.valueOf(weatherObject.current.humidity));
        args.putString(VISI, String.valueOf(weatherObject.current.visibility));
        args.putString(DEW, String.valueOf(weatherObject.current.dewPoint));
        args.putString(WNDSP, String.valueOf(weatherObject.current.windSpeed));
        args.putString(GUST, String.valueOf(weatherObject.current.gusts));
        args.putString(PRES, String.valueOf(weatherObject.current.pressure));
        args.putString(LOC, String.valueOf(weatherObject.location.name));
        args.putString(LAT,String.valueOf(weatherObject.location.latitude));
        args.putString(LON, String.valueOf(weatherObject.location.longitude));
        args.putString(IMG, String.valueOf(weatherObject.current.imageUrl));
        args.putString(DIRECTION, weatherObject.current.windDirectionStr());

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        units = sharedPref.getString(getString(R.string.units), "Imperial");

        if (getArguments() != null) {
            Time = getArguments().getString(TIME);
            Temp = getArguments().getString(TEMP);
            Humid = getArguments().getString(HUMID);
            Visi = getArguments().getString(VISI);
            Dew = getArguments().getString(DEW);
            Wndsp = getArguments().getString(WNDSP);
            Gust = getArguments().getString(GUST);
            Pres = getArguments().getString(PRES);
            Cond = getArguments().getString(COND);
            Loc = getArguments().getString(LOC);
            Lat = getArguments().getString(LAT);
            Lon = getArguments().getString(LON);
            imageUrl = getArguments().getString(IMG);
            Wdirect = getArguments().getString(DIRECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_conditions, container, false);

        //zipCode = (EditText)view.findViewById(R.id.dialog_zipcode);
        currentTime = (TextView)view.findViewById(R.id.textDateTime);
        currentConditions = (TextView)view.findViewById(R.id.textView2);
        temperature = (TextView)view.findViewById(R.id.texttemperature);
        relativeHumidity = (TextView)view.findViewById(R.id.textrelative_humidity);
        dewPoint = (TextView)view.findViewById(R.id.textdew_point);
        pressure = (TextView)view.findViewById(R.id.textpressure);
        visibility = (TextView)view.findViewById(R.id.textvisibility);
        windSpeed = (TextView)view.findViewById(R.id.textwind_speed);
        gusts = (TextView)view.findViewById(R.id.textgusts);
        image = (ImageView)view.findViewById(R.id.imageView);
        location = (TextView)view.findViewById(R.id.currentLocation);

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
        location.setOnClickListener(listener);

       //** Getting Cache Directory *//*
        File cDir = getActivity().getCacheDir();
        String fileName = Uri.parse(imageUrl).getLastPathSegment();
        File newImage = new File(cDir,fileName);
        if(newImage.exists())
        {
            Log.i("cache", "It exists");
            Bitmap bmp = BitmapFactory.decodeFile(newImage.getAbsolutePath());
            image.setImageBitmap(bmp);
        }
        else {
            try {
                URL url = new URL(imageUrl);
                new DownloadImage().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        //SET VIEW VALUES
        if (units.equals("Metric")) {
            setMetric();
        } else {
            setImperial();
        }

         return view;
      }

      public void setMetric() {
          DecimalFormat df = new DecimalFormat("#.##");
          DecimalFormat tdf = new DecimalFormat("#");

          //currentTime.setText(obj.getCurrentTime());
          currentTime.setText(Time);

          location.setText(Loc);

          //temperature conversion from F to C
          if (Time.equals("") || Time.toLowerCase().equals("na") || Time == null) {
              temperature.setText("na");
          } else {
              double tempFahrenheit = Double.parseDouble(Temp);
              double tempCelsius = ((tempFahrenheit - 32) * (5.0 / 9));
              String tempCelsiusString = String.valueOf(tdf.format(tempCelsius));
              temperature.setText(tempCelsiusString + (char)176 +  " C");
          }

          //add percent sign to humidity
          if (Humid.equals("") || Humid.toLowerCase().equals("na") || Humid == null) {
              relativeHumidity.setText("na");
          } else {
              relativeHumidity.setText(Humid + "%");
          }
          //convert pressure from IN to mm
          if (Pres.equals("") || Pres.toLowerCase().equals("na") || Pres == null) {
              pressure.setText("na");
          } else {
              double pressureInches = Double.parseDouble(Pres);
              double pressureMM = pressureInches * 25.4;
              String pressureMMString = String.valueOf(df.format(pressureMM));
              pressure.setText(pressureMMString + " mm");
          }

          currentConditions.setText(Cond);

          //convert dew point from F to C
          if (Dew.equals("") || Dew.toLowerCase().equals("na") || Dew == null) {
              dewPoint.setText("na");
          } else {
              double dewPointIM = Double.parseDouble(Dew);
              double dewPointC = ((dewPointIM - 32) * (5.0 / 9));
              String dewPointString = Integer.toString((int) dewPointC);
              dewPoint.setText(dewPointString + (char)176 + " C");
          }

          //convert visibility from miles to kilometers
          if (Visi.equals("") || Visi.toLowerCase().equals("na") || Visi == null) {
              visibility.setText("na");
          } else {
              double visibilityIM = Double.parseDouble(Visi);
              double visibilityKM = visibilityIM * 1.609344;
              String visibilityString = String.valueOf(df.format(visibilityKM));
              visibility.setText(visibilityString + " km");
          }

          //convert wind speed from mph to kph
          if (Wndsp.equals("") || Wndsp.toLowerCase().equals("na") || Wndsp == null) {
              windSpeed.setText("na");
          } else {
              double windSpeedIM = Double.parseDouble(Wndsp);
              double windSpeedKM = windSpeedIM * 1.609344;
              String windSpeedString = Integer.toString((int) windSpeedKM);
              windSpeed.setText(Wdirect + " @ " + windSpeedString + " km/h");
          }

          //convert gusts from MPH to KPH
          if (Gust.equals("") || Gust.toLowerCase().equals("na") || Gust == null) {
              gusts.setText("na");
          } else {
              double gustIM = Double.parseDouble(Gust);
              double gustKM = gustIM * 1.609344;
              String gustString = Integer.toString((int) gustKM);
              gusts.setText(gustString + " km/h");
          }

    }
                //SET VIEW VALUES
    public void setImperial() {
        location.setText(Loc);
        currentTime.setText(Time);
        temperature.setText(Temp + (char) 176 + " F");
        relativeHumidity.setText(Humid + "%");
        visibility.setText(Visi + " mi");
        dewPoint.setText(Dew + (char) 176 + " F");
        windSpeed.setText(Wdirect + " @ " + Wndsp + " mph");
        gusts.setText(Gust + " mph");
        pressure.setText(Pres + " in");
        currentConditions.setText(Cond);

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
        //public void onFragmentInteraction(Uri uri);
        //setCurrent(weatherObject);
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
                image.setImageBitmap(resizedBitmap);
                String fileName = Uri.parse(imageUrl).getLastPathSegment();
                try {
                    bitMaptoFileCache(resizedBitmap,fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                image.setImageResource(R.drawable.default_image);
            }
        }
    };
}
