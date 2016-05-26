//Ryan Hilsabeck and Tyler Koch Project 2

package edu.noctrl.csc510.csc510_p2;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements CurrentConditions.OnFragmentInteractionListener,
            ForecastFragment.OnFragmentInteractionListener, Enter_Zipcode_Dialog.OnFragmentInteractionListener,
            AboutFragment.OnFragmentInteractionListener, UnitFragment.OnFragmentInteractionListener{
    ImageView image;
    String JSONurl, xmlURL, latitude, longitude, currentZip;
    WeatherInfo weatherObject;
    String json = "";
    Activity activity;

        /**
         * The number of pages (wizard steps) to show in this demo.
         */
        private static final int NUM_PAGES = 8;

        /**
         * The pager widget, which handles animation and allows swiping horizontally to access previous
         * and next wizard steps.
         */
        private ViewPager mPager;
        /**
         * The pager adapter, which provides the pages to the view pager widget.
         */
        private PagerAdapter mPagerAdapter;

        GestureDetector.OnGestureListener glistener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //swapFragments();
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        protected GestureDetectorCompat mDetector;

/*        protected void swapFragments(){
            android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container,ForecastFragment.newInstance("",""));
            trans.addToBackStack(null);
            trans.commit();
        }*/

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

            //remove preferences at the start of app for testing purposes
            //SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
            //preferences.edit().remove("ZipList").commit();
            //preferences.edit().remove(getString(R.string.units)).commit();


            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            //Get preferred units and if not there yet, then set as imperial
            String units = sharedPref.getString(getString(R.string.units),"Imperial");
            Log.i("unit preference", units);
            //get zip list and
            String zips = sharedPref.getString("ZipList", null);
            if(zips == null)
            {
                try {
                    onUserSelectValue("10002");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else {
                List arrayList = new ArrayList<String>(Arrays.asList(zips.split(",")));
                String last = arrayList.get(arrayList.size()-1).toString();
                Log.i("ryanDebug", zips);
                try {
                    onUserSelectValue(last);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.i("zips", zips);
            }
    }

        @Override
        public void onBackPressed() {
            if (mPager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            } else {
                // Otherwise, select the previous step.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //this.mDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        }
        /**
         * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
         * sequence.
         */
        private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
            public ScreenSlidePagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public android.support.v4.app.Fragment getItem(int position) {

                switch(position) {
                    case 0:
                        return CurrentConditions.newInstance(weatherObject);
                    case 1:
                        return ForecastFragment.newInstance(weatherObject, 0); //today
                    case 2:
                        return ForecastFragment.newInstance(weatherObject, 1);
                    case 3:
                        return ForecastFragment.newInstance(weatherObject, 2);
                    case 4:
                        return ForecastFragment.newInstance(weatherObject, 3);
                    case 5:
                        return ForecastFragment.newInstance(weatherObject, 4);
                    case 6:
                        return ForecastFragment.newInstance(weatherObject, 5);
                    case 7:
                        return ForecastFragment.newInstance(weatherObject, 6);
                    case 8:
                        return ForecastFragment.newInstance(weatherObject, 7); //a wk from today
                    default:
                        return ForecastFragment.newInstance(weatherObject, 0);
                }
            }

            @Override
            public int getCount() {
                return NUM_PAGES;
            }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId())
        {
            case R.id.enter_zipCode:
                Enter_Zipcode_Dialog fragment = new Enter_Zipcode_Dialog();
                fragment.show(getSupportFragmentManager(),"frag");
                return true;
            case R.id.current_weather:
                mPager.setCurrentItem(0);
                return true;
            case R.id.sevenDay_Forecast:
                mPager.setCurrentItem(1);
                return true;
            case R.id.units:
                UnitFragment unitFrag = new UnitFragment();
                unitFrag.show(getSupportFragmentManager(),"unitFrag");
                return true;
            case R.id.about:
                AboutFragment aboutFrag = new AboutFragment();
                aboutFrag.show(getSupportFragmentManager(),"aboutFrag");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


       //this is the callback method to get the zip code entered by user in the enter
       //zip code dialog. The zip code will be added to the url and then send it to
       //the getJSON async task to get lat/long for the zip code.
       public void onUserSelectValue(String selectedValue) throws MalformedURLException{
           currentZip = selectedValue;
           JSONurl = "http://craiginsdev.com/zipcodes/findzip.php?zip=" + selectedValue;
            WeatherInfoIO.loadFromUrl(JSONurl,listen);
       }

    //This listener will be used to parse the JSON response and then handle the result to send to
    //get the xml file
    Downloader.DownloadListener<JSONObject> listen = new Downloader.DownloadListener<JSONObject>() {
        @Override
        public JSONObject parseResponse(InputStream in) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            // try parse the string to a JSON object
            try {
                JSONObject jsonObj = new JSONObject(json);
                return jsonObj;

            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                return null;
            }
        }
        @Override
        public void handleResult(JSONObject result) {
            try {
                if(result == null)
                    showToast("This zip code does not exist or no internet connect. Try again.");
                else {
                    latitude = result.getString("latitude");
                    longitude = result.getString("longitude");
                    xmlURL = "http://forecast.weather.gov/MapClick.php?lat=" + latitude +
                            "&lon=" + longitude + "&unit=0&lg=english&FcstType=dwml";
                    Log.i("ryanDebug", xmlURL);
                    handleXMLParse(xmlURL);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //this listener will handle xml parsed weather object and create view pager
    WeatherInfoIO.WeatherListener wListner = new WeatherInfoIO.WeatherListener() {
        @Override
        public void handleResult(WeatherInfo result){
            weatherObject = result;
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);

            if(weatherObject.alerts.size() != 0)
            {
                alertsNotifications(weatherObject);
            }


        }

    };

        //this method will take the xml url needed and send it to another async task
        //to go out and download the xml file and get the data we need to populate
        //the fragments
        public void handleXMLParse(String url)
        {
            WeatherInfoIO.loadFromUrl(url, wListner);

            Log.i("ryanDebug", url);
        }

    //Use this to show Toast error messages
    public void showToast(String message)
    {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    //This will validate if the data entered by user is no more or no less then 5 characters and
    //to make sure all the characters that were entered were numbers or else an error will show
    public boolean zipValidation(String zip)
    {
        boolean zipValid = true;

        if(zip.length() != 5)
        {
            zipValid = false;
        }
        else
        {
            try
            {
                double d = Double.parseDouble(zip);
            }
            catch(NumberFormatException nfe)
            {
                zipValid = false;
            }
        }
        return zipValid;
    }

    //go through array of alerts and create a new notification for each alert
    public void alertsNotifications(WeatherInfo weatherObject)
    {
        for(int i = 0; i < weatherObject.alerts.size(); i++)
        {
            String alertURL = weatherObject.alerts.get(i);
            buildNotification(alertURL);
        }

    }

    //This actually builds each unique alert notification to show
    public void buildNotification(String url)
    {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
        b.setSmallIcon(R.drawable.alert)
        .setContentTitle(getString(R.string.weatherAlert))
        .setContentText(url);

        Uri uri = Uri.parse(url);
        Intent urlClick = new Intent(Intent.ACTION_VIEW, uri);
        int uniqueInt = new Random().nextInt(543254);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, urlClick, PendingIntent.FLAG_UPDATE_CURRENT);
        b.setContentIntent(pendingIntent);
        NotificationManager nm =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueInt,b.build());
    }

    //When user change units after they have already entered a zip and have info displaying, this
    //will check to see what page the user is currently on and refresh the data with update to
    //units
    public void convertCurrentPage()
    {
        int currentPage = mPager.getCurrentItem();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(currentPage == 0) {

            CurrentConditions cFrag = CurrentConditions.newInstance(weatherObject);
            ft.replace(R.id.container, cFrag);
            ft.commit();
        }
        else
        {
            ForecastFragment fFrag = ForecastFragment.newInstance(weatherObject,currentPage);
            ft.replace(R.id.container, fFrag);
            ft.commit();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_current_conditions, container, false);
            return rootView;
        }
    }
}


