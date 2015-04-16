package uk.ac.ncl.csc2022.t14.bankingapp.tabs.ATMFinder;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.ATM;
import uk.ac.ncl.csc2022.t14.bankingapp.models.HeatPoint;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ATMDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.HeatMapDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ATMFinderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ATMFinderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//This is going to have to pull double duty as the Heatmap as well, otherwise it will just take ages to load a new map each time
public class ATMFinderFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ATMFinderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ATMFinderFragment newInstance() {
        ATMFinderFragment fragment = new ATMFinderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ATMFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    public static View view;


    GoogleMap map;
    MapView mapView;
    SupportMapFragment mapFragment;
    ServerInterface dSC = DataStore.sharedInstance().getConnector();
    List<ATM> atmlist = new ArrayList<ATM>();
    List<Marker> atmMarkers = new ArrayList<Marker>();
    Location mlocation;
    TileOverlay mOverlay;
    Boolean ATMorHEATMAP; //true for ATM, false for Heatmap

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_awards, container, false);
        if(view!=null)
        {
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent!=null)
            {
                parent.removeView(view);
            }
        }
        try
        {
            view = inflater.inflate(R.layout.fragment_atmfinder, container, false);
        }
        catch(InflateException e)
        {

        }
        mapFragment = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap();
        focusMapOnUser();
        getATMs();
        //Displays ATMs as default
        ATMorHEATMAP=true;




        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

    }
    public void focusMapOnUser()
    {

        //Zooming camera to user-position
        if(mapFragment==null)
        {
            mapFragment = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.map));
        }
        map = mapFragment.getMap();


        MapsInitializer.initialize(getActivity());


        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mlocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));



        //set the location of the user

        if(mlocation != null)
        {
            //move the camera to the users location with a suitable zoom level
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlocation.getLatitude(), mlocation.getLongitude()),13));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mlocation.getLatitude(), mlocation.getLongitude())).zoom(15).bearing(0).tilt(0).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    public void getATMs()
    {
        if(mOverlay!=null)
        {
            //if there are any heatspots, remove them
            mOverlay.remove();
        }
        ATMDelegate aD = new ATMDelegate() {
            @Override
            public void loadATMsPassed(List<ATM> allATMs) {
                atmlist = allATMs;
                for(int i=0;i<atmlist.size();i++)
                {
                    //add all the markers given by the server
                    MarkerOptions mO = new MarkerOptions().position(new LatLng(atmlist.get(i).getLatitude(), atmlist.get(i).getLongitude())).title(atmlist.get(i).getName()+ " " + Double.toString(atmlist.get(i).getCost()));
                    map.addMarker(mO);
                }
            }

            @Override
            public void loadATMsFailed(String errMessage)
            {


            }
        };
        dSC.loadATMS(aD);
    }

    public void getHeatmap()
    {
        //clear all atms
        map.clear();
        List<HeatPoint> transactionLocations;

        int[] accounts = new int[]{};
        //show all transactions from the past month
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date monthAgo = cal.getTime();

        HeatMapDelegate hMD = new HeatMapDelegate() {
            @Override
            public void loadHeatMapPassed(List<HeatPoint> allHeatPoints) {

                List<WeightedLatLng> wLatLngs = new ArrayList<WeightedLatLng>();
                for(int i = 0; i<allHeatPoints.size();i++)
                {
                    //converting heatpoints into weighted latlngs
                    WeightedLatLng w = new WeightedLatLng(new LatLng(allHeatPoints.get(i).getLatitude(), allHeatPoints.get(i).getLongitude()), allHeatPoints.get(i).getRadius());
                    wLatLngs.add(w);

                }
                //add the heatspots
                HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder().weightedData(wLatLngs).build();
                mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            }

            @Override
            public void loadHeatMapFailed(String errMessage) {


            }
        };
        dSC.loadHeatMap(accounts, today, monthAgo, hMD);

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

    @Override
    public void onResume()
    {
        super.onResume();
        mapFragment = (SupportMapFragment)(getChildFragmentManager().findFragmentById(R.id.map));
        focusMapOnUser();
        final Button switchView = (Button)getActivity().findViewById(R.id.change_atm_heatmap_button);
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(ATMorHEATMAP)
                {
                    getHeatmap();
                    switchView.setText("Display nearby ATMs");
                    ATMorHEATMAP = false;
                }
                else
                {
                    getATMs();
                    switchView.setText("Display Transaction Heat-Map");
                    ATMorHEATMAP=true;
                }


            }
        });



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
