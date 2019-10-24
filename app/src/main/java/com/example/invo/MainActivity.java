package com.example.invo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;

import com.example.invo.Direction.Dijkstra;
import com.example.invo.Fragments.BottomSheetFragment;
import com.example.invo.Interface.ButtonPressInterface;
import com.example.invo.Interface.Coordinates;
import com.example.invo.Interface.latLong;
import com.example.invo.Parsing.Retrofit.ModelRecycler;
import com.example.invo.Parsing.Retrofit.RetrofitAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.invo.Fragments.SettingsFragment;



public class MainActivity extends AppCompatActivity implements ButtonPressInterface,latLong, RetrofitAdapter.OnItemClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, Coordinates {

    public ArrayList<String> phLatti, phLongi;
    public int cords;
    public View bottomShet;
    public BottomSheetBehavior bottomSheetBehavior;
    public ArrayList<ModelRecycler> modelRecyclerArrayList;
    public MapView mMapView;
    public GoogleMap gMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String mapsApiKey = "AIzaSyBvyUAtQk-9AaDF-U9VOwFCi3jnntOkPY8";
    private List<com.google.android.gms.maps.model.LatLng> places = new ArrayList<>();
    private int width;
    String lattitude,longitude;
    public int proverka=0;
    public double latti=0.0;
    public double longi=0.0;
    public double navLatti=0.0;
    public double navLongi=0.0;
    int check =0;
    boolean enabled;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    FloatingActionButton myFab;
    FloatingActionButton navigationFab;
    FloatingActionButton minusFab;
    Button btnProv;
    FloatingActionButton plusFab;
    public int position;
    FloatingActionButton dorectionFab;
    BottomSheetFragment bottomSheetFragment;
    private FragmentManager fragmentManager;
    com.google.android.gms.maps.model.LatLng minsk;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        phLatti = new ArrayList<>();
        phLongi = new ArrayList<>();
        width = getResources().getDisplayMetrics().widthPixels;
        FloatingActionButton dorectionFab = findViewById(R.id.fab5);
        bottomShet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomShet);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_nov);
        myFab = (FloatingActionButton) findViewById(R.id.fab);
        navigationFab = (FloatingActionButton) findViewById(R.id.fab2);
        minusFab = (FloatingActionButton) findViewById(R.id.fab3);
        plusFab = (FloatingActionButton) findViewById(R.id.fab4);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        dorectionFab.setVisibility(View.INVISIBLE);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            myFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
            dorectionFab.getBackground().setColorFilter(0xffFFC107, PorterDuff.Mode.MULTIPLY);
            minusFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
            plusFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
        }
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            myFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
            navigationFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
            minusFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
            plusFab.getBackground().setColorFilter(0xff000000, PorterDuff.Mode.MULTIPLY);
        }

        navigationFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dorectionFab.setVisibility(View.INVISIBLE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                dorectionFab.setVisibility(View.INVISIBLE);
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                }
                else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                    com.google.android.gms.maps.model.LatLng current = new com.google.android.gms.maps.model.LatLng(latti, longi);
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                }
            }
        });


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        getFragmentManager().beginTransaction().replace(R.id.fragment_conteiner,
                new SettingsFragment()).commit();

        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.isHideable()) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if(check==1){
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_list).setChecked(true);
                    }
                    if (check==2){
                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_account).setChecked(true);
                    }
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_COLLAPSED:


                        bottomNavigationView.getMenu().findItem(R.id.bottom_nav_map).setChecked(true);

                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:



                        if (check == 0) {

                            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_map).setChecked(true);
                            check = 0;
                        }
                        if (check == 1) {
                            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_list).setChecked(true);
                            check = 1;
                        }
                        if (check == 2) {
                            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_account).setChecked(true);
                            check = 2;
                        }


                        if (bottomNavigationView.getMenu().findItem(R.id.bottom_nav_map).isChecked()) {

                            check = 0;
                            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_map).setChecked(true);
                            break;
                        }

                        if (bottomNavigationView.getMenu().findItem(R.id.bottom_nav_list).isChecked()) {
                            check = 1;
                            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_list).setChecked(true);
                            break;
                        }

                        if (bottomNavigationView.getMenu().findItem(R.id.bottom_nav_account).isChecked()) {
                            check = 2;
                            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_account).setChecked(true);
                            break;
                        }
                        if(cords==2){
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                        }


                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                click();
                click2();
                if (!bottomSheetBehavior.isHideable()) {
                    myFab.hide();
                }
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        gMap = googleMap;
        click2();
        MarkerOptions[] markers = new MarkerOptions[places.size()];
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_nov);
         dorectionFab = findViewById(R.id.fab5);

        dorectionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(minsk, 11));
                googleMap.clear();
                getLocation();
                setPreCoordinates();
                setLine(googleMap);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                click();
                android.app.Fragment lol = null;
                switch (menuItem.getItemId()) {
                    case R.id.bottom_nav_map:



                        check=0;
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        lol = new com.example.invo.Fragments.ListFragment();
                        break;

                    case R.id.bottom_nav_list:
                        check = 1;
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


                        lol = new com.example.invo.Fragments.ListFragment();
                        break;

                    case R.id.bottom_nav_account:
                        check = 2;
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                        lol = new SettingsFragment();
                        break;
                    default:
                        break;
                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_conteiner,
                        lol).commit();
                return true;
            }
        });



        minusFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        plusFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#
            return;

        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                navLatti=latLng.latitude;
                navLongi=latLng.longitude;
                markerOptions.title(navLatti + " : " + navLongi);

                //Clears the previously touched position
                googleMap.clear();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                } else
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        dorectionFab.setVisibility(View.VISIBLE);

//                        AddPhotoBottomDialogFragment addPhotoBottomDialogFragment =
//                                AddPhotoBottomDialogFragment.newInstance();
//                        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
//                                "add_photo_dialog_fragment");
                        return false;
                    }
                });
            }
            });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
            }
        });




        googleMap.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(52.953847, 27.064607)).title(" Гараж моего внедорожника"));
        //googleMap.getUiSettings().setMyLocationButtonEnabled(false);
       minsk = new com.google.android.gms.maps.model.LatLng(53.902016, 27.559901);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(minsk, 11));
        navigationFab.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                dorectionFab.setVisibility(View.INVISIBLE);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                }
                else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    googleMap.clear();
                    getLocation();
                    com.google.android.gms.maps.model.LatLng current = new com.google.android.gms.maps.model.LatLng(latti, longi);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                }
            }
        });
    }





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Текущее месторасположение " + cords, Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
               Toast.makeText(this, "Текущее месторасположение:\n" + lattitude + longitude, Toast.LENGTH_LONG).show();

            } else if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Toast.makeText(this, "Текущее месторасположение:\n" + lattitude + longitude, Toast.LENGTH_LONG).show();

            } else if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                Toast.makeText(this, "Текущее месторасположение:\n" + lattitude + longitude, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Невозможно найти ваше месторасположение", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void click() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        enabled = preferences.getBoolean("enabled", false);
        if (enabled) {
            plusFab.show();
            minusFab.show();
        } else {
            plusFab.hide();
            minusFab.hide();
        }
    }

    public void click2(){
        SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(this);
        String regular = preferences2.getString(getString(R.string.list), "");
        if (regular.equals("Схема")){
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (regular.equals("Гибрид")){
            gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        if (regular.equals("Спутник")){
            gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        gMap.getMapType();
    }

    private void setLine(GoogleMap googleMap){
        PolylineOptions line = new PolylineOptions();
        line.width(4f).color(R.color.color_select);
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < places.size(); i++) {
            if (i == 0) {
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .position(places.get(i));
                googleMap.addMarker(startMarkerOptions);
            } else if (i == places.size() - 1) {
                MarkerOptions endMarkerOptions = new MarkerOptions()
                        .position(places.get(i));
                googleMap.addMarker(endMarkerOptions);
            }
            line.add(places.get(i));
            latLngBuilder.include(places.get(i));
        }
        googleMap.addPolyline(line);
        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
    }

    public void setList(){
        places.clear();
        for (int i = 0; i < Dijkstra.buildMarker.length; i++) {
            places.add(coordinatesMap.get(Dijkstra.buildMarker[i]));
        }
    }

    public void setPreCoordinates() {
        pls.clear();
        coordinatesMap.clear();
        pls.add(new LatLng(latti, longi));//0
        setCoordinates();
        pls.add(new LatLng(navLatti, navLongi));//6
        for (int i = 0; i < pls.size(); i++) {
            coordinatesMap.put(i, pls.get(i));
        }
        Dijkstra.doNavigation();
        setList();
    }


    @Override
    public void onItemClick(int position)  {
        this.position=position;
        bottomSheetFragment= new BottomSheetFragment();
        Bundle bundle = new Bundle();

        bundle.putString("address", modelRecyclerArrayList.get(position).getCountry());
        bundle.putString("img", modelRecyclerArrayList.get(position).getImgURL());
        bundle.putString("name", modelRecyclerArrayList.get(position).getName());
        bundle.putString("site", modelRecyclerArrayList.get(position).getSite());
        bundle.putString("phone", modelRecyclerArrayList.get(position).getPhone());

        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());


    }

    @Override
    public void seeee(ArrayList<String> lat, ArrayList<String> longi, ArrayList<ModelRecycler> modelRecyclerArrayList) {
        phLatti=lat;
        phLongi=longi;
        this.modelRecyclerArrayList = modelRecyclerArrayList;

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void buttonClick() {

        bottomSheetFragment.dismiss();
        gMap.clear();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        navLatti=Double.parseDouble(phLatti.get(position));
        navLongi=Double.parseDouble(phLongi.get(position));



        Marker marker =gMap.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(navLatti, navLongi)));
        marker.showInfoWindow();
        com.google.android.gms.maps.model.LatLng ph = new com.google.android.gms.maps.model.LatLng(Double.parseDouble(phLatti.get(position)), Double.parseDouble(phLongi.get(position)));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ph, 17));
        dorectionFab.setVisibility(View.VISIBLE);
    }
}







