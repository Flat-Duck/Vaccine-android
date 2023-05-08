package ly.smarthive.vaccine.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.Objects;

import ly.smarthive.vaccine.COMMON;
import ly.smarthive.vaccine.R;
import ly.smarthive.vaccine.util.Helper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = MapActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private DatabaseReference mDatabase;
    LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;
    float zoom = 17;
    Helper helper;
    boolean cameraMoved;
    LatLng cameraLocation, myLocation;
    FusedLocationProviderClient mFusedLocationClient;
    boolean alerted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        helper = new Helper();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ImageView imageBack = findViewById(R.id.overflow_back);
        imageBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnCameraMoveListener(() -> cameraMoved = true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mLocationRequest  = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
                //  myLocation
            } else {
                checkLocationPermission();
            }
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            zoom = mGoogleMap.getCameraPosition().zoom;
            cameraLocation = mGoogleMap.getCameraPosition().target;
            Location location = locationResult.getLastLocation();
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12));
            COMMON.CURRENT_LAT = Double.toString(location.getLatitude());
            COMMON.CURRENT_LNG = Double.toString(location.getLongitude());
            helper.updateFbUser();
            loadCasesLocations();
        }
    };

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create().show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void loadCasesLocations() {
        mDatabase.child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            if (!Objects.equals(d.getKey(), COMMON.CURRENT_USER_ID)) {
                                LatLng latLng1 = new LatLng(Float.parseFloat(d.child("lat").getValue().toString()), Float.parseFloat(d.child("lng").getValue().toString()));
                                calculateDistance(latLng1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });
    }

    @SuppressLint("DefaultLocale")
    void calculateDistance(LatLng caseLocation) {

        double distance = SphericalUtil.computeDistanceBetween(myLocation, caseLocation);
      //  if (distance <= 100) {
         //   Toast.makeText(this, "المسافة بينك وبين الحالة هي \n " + String.format("%.2f", distance ) + "متر", Toast.LENGTH_LONG).show();
//            if (distance <= 10 && !alerted) {
//                alerted = true;
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Warning!");
//                builder.setMessage("هناك حالة بالقرب منك");
//                builder.setCancelable(false);
//                builder.setPositiveButton(getResources().getString(R.string.ok), (dialog, which) ->{
//                    dialog.cancel();
//                    alerted = false;});
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
            addCaseMarker(caseLocation);
     //   }
    }

    private void addCaseMarker(LatLng markerLocation) {
        MarkerOptions options = new MarkerOptions();
        options.title(getString(R.string.Case)).position(markerLocation);
        mGoogleMap.addMarker(options);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(markerLocation);
        circleOptions.radius(700);
        circleOptions.fillColor(Color.argb(20,250,47,47));
        circleOptions.strokeWidth(1);
        mGoogleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(markerLocation);
        mGoogleMap.addMarker(markerOptions.title(getString(R.string.Case)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                    //L//ocation currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    ///      mGoogleMap.addMarker(currentLocation.getLatitude(),)
                }
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, getText(R.string.permission_denied), Toast.LENGTH_LONG).show();
            }
        }
    }
}