package com.resist.movelife;

import android.app.Activity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi")
public class Map extends Activity implements LocationListener {
    GoogleMap map;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map);
            overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }

        @Override
        public void onLocationChanged(Location location) {
            map.clear();
            MarkerOptions mp = new MarkerOptions();
            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mp.title("My position");
            map.addMarker(mp);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }




























