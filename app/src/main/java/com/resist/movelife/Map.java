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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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
    Context context = this;
    // Within which the entire activity is enclosed
    DrawerLayout mDrawerLayout;
    // ListView represents Navigation Drawer
    ListView mDrawerList;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    ActionBarDrawerToggle mDrawerToggle;
    // Title of the action bar
    String mTitle = "";
    private GoogleMap mMap;
    private boolean change = true;
    private boolean movedCamera = false;
    private Marker myPos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.map);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        setUpMapIfNeeded();
        getMarkers();
        setGoToLocation();
        mTitle = (String) getTitle();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            public void onMapLongClick(final LatLng latlng) {
                LayoutInflater li = LayoutInflater.from(context);
                final View v = li.inflate(R.layout.event_alert, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(v);
                builder.setCancelable(false);

                builder.setPositiveButton(getString(R.string.map_maakevenement), new DialogInterface.OnClickListener() {

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

                builder.setNegativeButton(getString(R.string.map_cancelevenement), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Getting reference to the DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        // Getting reference to the ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Selecteer categorie");
                invalidateOptionsMenu();
            }
        };

        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(),
                R.layout.drawer_list_item,
                getResources().getStringArray(R.array.categorieën)
        );

        // Enabling Home button
        getActionBar().setHomeButtonEnabled(true);

        // Enabling Up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);


        // Setting item click listener for the listview mDrawerList
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                // Getting an array of categorieën
                String[] categorieën = getResources().getStringArray(R.array.categorieën);
                //Currently selected categorie
                mTitle = categorieën[position];



                switch(position) {
                    case 0:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BAKERY), R.drawable.ic_map_bakery);
                        break;
                    case 1:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BANK), R.drawable.ic_bank);
                        break;
                    case 2:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BAR), R.drawable.ic_map_bakery);
                        break;
                    case 3:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BOOKSHOP), R.drawable.ic_bank);
                        break;
                    case 4:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_CAFE), R.drawable.ic_map_bakery);
                        break;
                    case 5:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_CINEMA), R.drawable.ic_bank);
                        break;
                    case 6:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_CLUB), R.drawable.ic_map_bakery);
                        break;
                    case 7:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_LOUNGE), R.drawable.ic_bank);
                        break;
                    case 8:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_MUSEUM), R.drawable.ic_map_bakery);
                        break;
                    case 9:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_SUPERMARKET), R.drawable.ic_map_bakery);
                        break;
                    default:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_RESTAURANT), R.drawable.ic_bank);
                        break;
                }
                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
            //mp.title("my position");
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
        mMap.setMyLocationEnabled(true);

        return true;
    }


    @Override
    public void onLocationChanged(Location location) {

        if (!change) {
            return;
        }
        if (myPos != null) {
            myPos.remove();
        }
        MarkerOptions mp = new MarkerOptions();
        mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
        mp.title(getString(R.string.map_mijnpositie));
        myPos = mMap.addMarker(mp);
        if (!movedCamera) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));
            movedCamera = true;
        }

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

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }


                @Override
                public View getInfoContents(Marker arg0) {

                    Company opslag = markerMap.get(arg0);

                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_infowindow_bedrijfsnaam);
                    RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBarInfoWindow);
                    tvLat.setText(opslag.getName());
                    double rating = opslag.getRating();
                    float frating = (float) rating;

                    if (ratingBar != null) {
                        ratingBar.setEnabled(false);
                        ratingBar.setMax(5);
                        ratingBar.setStepSize(0.01f);
                        ratingBar.setRating(frating);
                        ratingBar.invalidate();
                    }

                    return v;

                }
            });


            MarkerOptions marker = new MarkerOptions()
                    .position(l)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    Marker m = mMap.addMarker(marker);
            markerMap.put(m, store);
        }

        onInfoClick(markerMap);

    }

    private void onInfoClick(final java.util.Map<Marker, Company> markerMap) {

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Company store = markerMap.get(marker);
                if (store != null) {
                    ResultsInfoBedrijven.filteredCompany = store;
                    Intent i = new Intent(Map.this, ResultsInfoBedrijven.class);
                    startActivity(i);
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            Intent intent = new Intent(this, ZoekBedrijven.class);
            startActivity(intent);
        }
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    private void getTypeMarker(final List<Company> array, int resource) {
        mMap.clear();
        final java.util.Map<Marker, Company> markerMap = new HashMap<Marker, Company>();

        for (Company store : array) {
            LatLng l = new LatLng(store.getLatitude(), store.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(l)
                    .title(store.getName())
                    .snippet("" + store.getRating())
                    .icon(BitmapDescriptorFactory.fromResource(resource));
            Marker m = mMap.addMarker(marker);
            markerMap.put(m, store);
        }
        onInfoClick(markerMap);
    }


}

