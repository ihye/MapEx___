package com.cookandroid.mapex___;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;





public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnetcionFailedListener {


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    //위치 정보 얻는 객체
    private FusedLocationProviderClient mFusedLocationClient;


    //권한 체크 요청 코드 정의
    public static final int REQUEST_CODE_PERMISSIONS = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        //GoogleAPIClient 의 인스턴스 생성
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context:this)
                        .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity.this);

    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng kb = new LatLng(37.579736, 126.977170);
        mMap.addMarker(new MarkerOptions().position(kb).title("경복궁"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kb));


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:02-999-1234"));

                startActivity(intent);
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void mCurrentLocation(View v) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(context:this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManger.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context: this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ! = PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( activity: this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                        REQUEST_CODE_PERMISSIONS);
                return;
                    }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity:this,
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location ! = null){
                            //현재 위치
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(myLocation)
                                    .title("현재 위치"));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

                            //카메라 줌
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(v:17.0f));
                        }
                    }
                });
    }



    @Override

    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int
                   super.onRequestPermissionResult(requestCode, permissions, grantResults);


                switch (requestCode) {
        case REQUEST_CODE_PERMISSIONS:
            if (ActivityCompat.checkSelfPermission(context:this,
                android.Manifiest.permission.ACCESS_FINE_LOCATION) ! = PackageManager,PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(context:this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) ! = PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context: this, text = "권한 체크 거부 됨", Toast.LENTH_SHORT).show();
        }
        return;





    }

}

