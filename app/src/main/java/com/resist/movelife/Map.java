package com.resist.movelife;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;


@SuppressLint("NewApi")
public class Map extends Activity implements LocationListener {
    private GoogleMap mMap;
    private boolean change = true;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.map);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setUpMapIfNeeded();
        getMarkers();
        setGoToLocation();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            public void onMapLongClick(final LatLng latlng) {
                LayoutInflater li = LayoutInflater.from(context);
                final View v = li.inflate(R.layout.event_alert, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(v);
                builder.setCancelable(false);

                builder.setPositiveButton("Maak evenement", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        EditText title = (EditText) v.findViewById(R.id.ettitle);
                        EditText snippet = (EditText) v.findViewById(R.id.etsnippet);
                        mMap.addMarker(new MarkerOptions()
                                        .title(title.getText().toString())
                                        .snippet(snippet.getText().toString())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .position(latlng)
                        );
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        // List<Company> lijst = new ArrayList<Company>();
        if (!change) {
            return;
        }
        mMap.clear();
        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
        mp.title("Mijn positie");
        mMap.addMarker(mp);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 16));
        getMarkers();
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
        if (!change) {
            return;
        }

        final List<Company> array = Company.getCompanies();
        final java.util.Map<Marker, Company> markerMap = new HashMap<Marker, Company>();
        for (Company store : array) {
            LatLng l = new LatLng(store.getLatitude(), store.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(l)
                    .title(store.getName())
                    .snippet("" + store.getRating())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            Marker m = mMap.addMarker(marker);
            markerMap.put(m, store);

            Log.d("markerlog", "" + array);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Company store = markerMap.get(marker);
                ResultsInfoBedrijven.filteredCompany = store;
                Intent i = new Intent(Map.this, ResultsInfoBedrijven.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {
            getBakeryMarkers();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getBakeryMarkers(){
        mMap.clear();
        final List<Company> array = Company.getCompaniesOfType(1);
        final java.util.Map<Marker, Company> markerMap = new HashMap<Marker, Company>();

        for (Company store : array) {
            LatLng l = new LatLng(store.getLatitude(), store.getLongitude());

            MarkerOptions bakeryMarker = new MarkerOptions()
                    .position(l)
                    .title(store.getName())
                    .snippet("" + store.getRating())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_bakery));
            Marker m = mMap.addMarker(bakeryMarker);
            markerMap.put(m, store);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Company store = markerMap.get(marker);
                ResultsInfoBedrijven.filteredCompany = store;
                Intent i = new Intent(Map.this, ResultsInfoBedrijven.class);
                startActivity(i);
            }
        });

    }

    public void getBankMarkers(){

    }
    public void getBarMarkers(){
    }
    public void getBookshopMarkers(){

    }
    public void getCafeMarkers(){

    }
    public void getCinemaMarkers(){

    }
    public void getClubMarkers(){

    }
    public void getLoungeMarkers(){

    }
    public void getMuseumMarkers(){

    }
    public void getRestaurantMarkers(){

    }
    public void getSupermarketMarkers(){

    }

}

























