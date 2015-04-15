package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ATMDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class AddTransactionLocationActivity extends LloydsActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction_location);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction_location, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {
        GoogleMap map;
        MapView mapView;
        SupportMapFragment mapFragment;
        MarkerOptions mO = new MarkerOptions();
        ServerInterface dSC = DataStore.sharedInstance().getConnector();


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_transaction_location, container, false);
            focusMapOnUser();

            return rootView;
        }
        public void focusMapOnUser()
        {

            //Zooming camera to user-position
            if(mapFragment==null)
            {
                Log.d("hello", "null");
                mapFragment = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.category_map));
            }

            map = mapFragment.getMap();


            MapsInitializer.initialize(getActivity());

            map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            //set the location of the user
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if(location != null)
            {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).bearing(0).tilt(0).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {
                    //Clear any other markers
                    map.clear();
                    //add a marker to where the user clicked
                    mO.position(latLng);
                    map.addMarker(mO);
                    //show the button
                    Button confirm = (Button)getActivity().findViewById(R.id.confirm_location_button);
                    confirm.setVisibility(View.VISIBLE);
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            //add the latlng the user selected
                            i.putExtra("Lng", latLng.longitude);
                            i.putExtra("Lat", latLng.latitude);
                            getActivity().setResult(Activity.RESULT_OK, i);
                            getActivity().finish();

                        }
                    });

                }
            });

        }

    }

}
