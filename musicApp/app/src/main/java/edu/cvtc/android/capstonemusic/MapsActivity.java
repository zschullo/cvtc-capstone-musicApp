package edu.cvtc.android.capstonemusic;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private LatLng latLng;
    private ProgressBar progressBar;
    private Button resetButton;
    private TextView distanceTextView;
    LocationListener locationListener;

    private Toast toast = null;

    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Location currentLocation;
    private Location lastLocation;
    LocationManager locationManager;
    private float distanceTraveled = 0;
    private int progress = 0;
    private int totalProgress = 0;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resetButton = (Button) findViewById(R.id.resetButton);
        distanceTextView = (TextView) findViewById(R.id.distanceText);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            progress = extras.getInt("progress");
        }

        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lastLocation = currentLocation;
                currentLocation = location;

                if (lastLocation != null && currentLocation != null) {
                    distanceTraveled = lastLocation.distanceTo(location);
                }

                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //if (location.getSpeed() > 10) {
                    updateProgressBar();
                //}

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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,1,locationListener);

    }



    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,1,locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void updateMap() {
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 20));
    }

    public void updateProgressBar() {
        if (progress >= 1610) {
            progress = progress - 1610;
            progressBar.setProgress(progress);
            displayToast("A new song was unlocked!");
        } else {
            int distanceRounded = Math.round(distanceTraveled);
            progress += distanceRounded;
            totalProgress += distanceRounded;
            distanceTextView.setText(totalProgress + " meters");
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void displayToast(String message) {

        // Will prevent toast from showing if tapping the button a lot.
        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == resetButton) {
            mMap.clear();
            totalProgress = 0;

        }
    }
}
