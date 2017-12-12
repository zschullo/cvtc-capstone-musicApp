package edu.cvtc.android.capstonemusic;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView playTextField;
    private LatLng latLng;

    private Toast toast = null;

    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Location currentLocation;
    private Location lastLocation;
    LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        playTextField = (TextView) findViewById(R.id.play);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lastLocation = currentLocation;
                displayToast("Location Changed");
                displayToast("Last location = " + location.getLatitude() + " and Longitude = " + location.getLongitude());
                currentLocation = location;
//                float distanceTraveled = lastLocation.distanceTo(location);
//                displayToast("Distance = " + distanceTraveled);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                playTextField.setText("Latitude = " + location.getLatitude() + " and Longitude = " + location.getLongitude());

                updateMap();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,1,locationListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void updateMap() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 20));
    }

    private void displayToast(String message) {

        // Will prevent toast from showing if tapping the button a lot.
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
