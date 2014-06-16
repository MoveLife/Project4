package com.resist.movelife;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("NewApi")
public class Map extends Activity implements LocationListener {
    private GoogleMap mMap;
    private boolean change = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Onderstaande regel uitzetten voor testen in emulator.
        //  lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        setUpMapIfNeeded();
        getMarkers();
        setGoToLocation();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }

    public void setGoToLocation() {

        Bundle b = getIntent().getExtras();


        if (b != null && b.containsKey("latitude") && b.containsKey("longitude")) {
            double latitude = b.getDouble("latitude");
            double longitude = b.getDouble("longitude");
            change = false;
            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(latitude, longitude));
            mp.title("my position");
            mMap.addMarker(mp);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitude, longitude), 16));

            Log.d("Checkingla", "" + latitude);
            Log.d("Checkinglo", "" + longitude);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        List<Company> lijst = new ArrayList<Company>();

        if (!change) {
            return;
        }
        mMap.clear();
        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
        mp.title(lijst.get(0).getName());
        mMap.addMarker(mp);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    public void getMarkers() {


        //Cursor cursor = LocalDatabaseConnector.get("companies", "latitude");


        final List<Company> array = Company.getCompanies();
        int i = 0;
        for (Company store : array) {
            LatLng l = new LatLng(store.getLatitude(), store.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(l)
                    .title(store.getName())
                    .snippet("" + store.getType())
                    .snippet("" + store.getRating())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(marker);
            ++i;
            Log.d("markerlog", "" + array);

        }

       mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

           @Override
           public void onInfoWindowClick(Marker marker) {

               try {
                   Company store = array
                           .get(Integer.parseInt(marker
                                   .getSnippet()));

                 // set details




                   double storeLat = store.getLatitude();
                   double storelng = store.getLongitude();

               } catch (ArrayIndexOutOfBoundsException e) {
                   Log.e("ArrayIndexOutOfBoundsException", " Occured");
               }

           }
       });


    }

}


