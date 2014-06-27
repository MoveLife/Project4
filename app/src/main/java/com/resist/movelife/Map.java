package com.resist.movelife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


public class Map extends Activity implements LocationListener {
    private Context context = this;
    // Within which the entire activity is enclosed
    private DrawerLayout mDrawerLayout;
    // ListView represents Navigation Drawer
    private ListView mDrawerList;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    private ActionBarDrawerToggle mDrawerToggle;
    // Title of the action bar
    private String mTitle = "";
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
                getActionBar().setTitle(getResources().getString(R.string.map_select_category));
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
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BAKERY), R.drawable.ic_map_bakery, R.drawable.ic_map_bakery_event);
                        break;
                    case 1:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BANK), R.drawable.ic_map_bank, R.drawable.ic_map_bank_event);
                        break;
                    case 2:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BAR), R.drawable.ic_map_bar, R.drawable.ic_map_bar_event);
                        break;
                    case 3:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_BOOKSHOP), R.drawable.ic_map_bookshops, R.drawable.ic_map_bookshops_event);
                        break;
                    case 4:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_CAFE), R.drawable.ic_map_cafe, R.drawable.ic_map_cafe_event);
                        break;
                    case 5:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_CINEMA), R.drawable.ic_map_cinema, R.drawable.ic_map_cinema_event);
                        break;
                    case 6:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_CLUB), R.drawable.ic_map_club,R.drawable.ic_map_club_event);
                        break;
                    case 7:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_LOUNGE), R.drawable.ic_map_lounge, R.drawable.ic_map_lounge_event);
                        break;
                    case 8:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_MUSEUM), R.drawable.ic_map_museum, R.drawable.ic_map_museum_event);
                        break;
                    case 9:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_SUPERMARKET), R.drawable.ic_map_supermarket, R.drawable.ic_map_supermarket_event);
                        break;
                    default:
                        getTypeMarker(Company.getCompaniesOfType(Company.TYPE_RESTAURANT), R.drawable.ic_map_restaurant, R.drawable.ic_map_restaurant_event);
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
            mp.title(getResources().getString(R.string.map_mijnpositie));
            mMap.addMarker(mp);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mapmenu, menu);
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
        mp.title(getResources().getString(R.string.map_mijnpositie));
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

            MarkerOptions marker = new MarkerOptions()
                    .position(l);


            if (store.hasEvent()){
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_event));
            } else {

                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }


            Marker m = mMap.addMarker(marker);
            markerMap.put(m, store);
        }



        final List<User> friends = Friends.getFriends();
        for(User friend : friends) {
            if(friend.getLatitude() == null || friend.getLongitude() == null) {
                continue;
            }
            LatLng l = new LatLng(friend.getLatitude(), friend.getLongitude());
            MarkerOptions marker = new MarkerOptions()
                    .position(l)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_friend))
                    .title((friend.getName() != null ? friend.getName() : friend.getEmail()))
                    .snippet((friend.getLastSeen() != null ? friend.getLastSeen().toString() : ""));

            mMap.addMarker(marker);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {

                Company opslag = markerMap.get(arg0);

                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                if(opslag != null) {
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_infowindow_bedrijfsnaam);
                    RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBarInfoWindow);
                    tvLat.setText(opslag.getName());

                    if (ratingBar != null && opslag.getRating() != null) {
                        ratingBar.setEnabled(false);
                        ratingBar.setMax(5);
                        ratingBar.setStepSize(0.01f);
                        ratingBar.setRating((float) opslag.getRating().doubleValue());
                        ratingBar.invalidate();
                    }
                }
                return v;

            }
        });

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


    private void getTypeMarker(final List<Company> array, int resource, int resourceEvent) {
        mMap.clear();
        final java.util.Map<Marker, Company> markerMap = new HashMap<Marker, Company>();

        for (Company store : array) {
            LatLng l = new LatLng(store.getLatitude(), store.getLongitude());

            MarkerOptions marker = new MarkerOptions()
                    .position(l)
                    .title(store.getName())
                    .snippet("" + store.getRating());
            if (store.hasEvent()){
                marker.icon(BitmapDescriptorFactory.fromResource(resourceEvent));
            } else {

                marker.icon(BitmapDescriptorFactory.fromResource(resource));
            }






            Marker m = mMap.addMarker(marker);
            markerMap.put(m, store);
        }
        onInfoClick(markerMap);
    }
}

