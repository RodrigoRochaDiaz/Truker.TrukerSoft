package com.example.rodrigo.trukertrukersoft.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.rodrigo.trukertrukersoft.models.Geolocalization;
import com.example.rodrigo.trukertrukersoft.models.Person;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.navigation.v5.AlertLevelChangeListener;
import com.mapbox.services.android.navigation.v5.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.NavigationEventListener;
import com.mapbox.services.android.navigation.v5.ProgressChangeListener;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.navigation.v5.RouteProgress;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.mapboxsdk.maps.MapboxMap.OnMapClickListener;
import static com.mapbox.services.android.Constants.ARRIVE_ALERT_LEVEL;
import static com.mapbox.services.android.Constants.DEPART_ALERT_LEVEL;
import static com.mapbox.services.android.Constants.HIGH_ALERT_LEVEL;
import static com.mapbox.services.android.Constants.LOW_ALERT_LEVEL;
import static com.mapbox.services.android.Constants.MEDIUM_ALERT_LEVEL;
import static com.mapbox.services.android.Constants.NONE_ALERT_LEVEL;

import com.example.rodrigo.trukertrukersoft.R;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;


public class MyMapBoxActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener,
        ProgressChangeListener, NavigationEventListener, AlertLevelChangeListener, LocationEngineListener {

    private static final String LOG_TAG = "MyMapBoxActivity";
    private GeocoderAutoCompleteView autocomplete;

    // Map variables
    private MapView mapView;
    private Polyline routeLine;
    private MapboxMap mapboxMap;
    private Marker destinationMarker;
    private Position position;

    // Navigation related variables
    private LocationEngine locationEngine;
    private MapboxNavigation navigation;
    private Button startRouteButton;
    private DirectionsRoute route;
    private int geolocalizationId = 1;
    private int userid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_my_map_box);


        startRouteButton = (Button) findViewById(R.id.startRouteButton);
        startRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navigation != null && route != null) {

                    // Hide the start button
                    startRouteButton.setVisibility(View.INVISIBLE);

                    // Attach all of our navigation listeners.
                    navigation.setNavigationEventListener(MyMapBoxActivity.this);
                    navigation.setProgressChangeListener(MyMapBoxActivity.this);
                    navigation.setAlertLevelChangeListener(MyMapBoxActivity.this);

                    // Adjust location engine to force a gps reading every second. This isn't required but gives an overall
                    // better navigation experience for users. The updating only occurs if the user moves 3 meters or further
                    // from the last update.
                    //locationEngine.setInterval(0);
                    //locationEngine.setSmallestDisplacement(3.0f);
                    locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
                    //locationEngine.setFastestInterval(1000);

                    navigation.setLocationEngine(locationEngine);
                    navigation.startNavigation(route);
                }
            }
        });

        // Set up autocomplete widget
        autocomplete = (GeocoderAutoCompleteView) findViewById(R.id.query);
        autocomplete.setAccessToken(Utils.getMapboxAccessToken(this));
        autocomplete.setType(GeocodingCriteria.TYPE_POI);
        autocomplete.setLimit(10);
        autocomplete.setVisibility(View.VISIBLE);
        autocomplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            public void onFeatureClick(CarmenFeature feature) {
                position = feature.asPosition();
                updateMap(position.getLatitude(), position.getLongitude());
            }
        });

        // Set up map
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(MapboxMap mapboxMapReady) {
//                mapboxMap = mapboxMapReady;
//                mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);
//            }
//        });

        // Set up location services to improve accuracy
        locationEngine = LostLocationEngine.getLocationEngine(this);
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();


        navigation = new MapboxNavigation(this, Mapbox.getAccessToken());


        Intent myIntent = getIntent();
        Person person = new Person();
        person.setId(myIntent.getIntExtra("userid", geolocalizationId));
        person.setName(myIntent.getStringExtra("name"));
        userid = person.getId();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person");
        myRef.child("" + person.getId()).setValue(person);
    }

    @Override
    public void onConnected() {
        Log.d(LOG_TAG, "\n" + "Conectado al motor");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(LOG_TAG, "Nueva ubicación perdida: " + location.toString());
            autocomplete.setProximity(Position.fromCoordinates(
                    location.getLongitude(), location.getLatitude()));
        }
    }

    private void updateMap(double latitude, double longitude) {

        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }

        // Marker
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude)));

        // Animate map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(14)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);

        startRouteButton.setVisibility(View.VISIBLE);
        calculateRoute(Position.fromCoordinates(longitude, latitude));

        hideSoftKeyboard(this);
        startRouteButton.requestFocus();
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setOnMapClickListener(this);
        Snackbar.make(mapView, "\n" + "Toca el mapa para ubicar el destino", BaseTransientBottomBar.LENGTH_LONG).show();

        mapboxMap.moveCamera(CameraUpdateFactory.zoomBy(14));

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapboxMap.setMyLocationEnabled(true);
            mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(point));

        startRouteButton.setVisibility(View.VISIBLE);
        calculateRoute(Position.fromCoordinates(point.getLongitude(), point.getLatitude()));
    }

    private void drawRouteLine(DirectionsRoute route) {
        List<Position> positions = LineString.fromPolyline(route.getGeometry(), Constants.PRECISION_6).getCoordinates();
        List<LatLng> latLngs = new ArrayList<>();
        for (Position position : positions) {
            latLngs.add(new LatLng(position.getLatitude(), position.getLongitude()));
        }

        // Remove old route if currently being shown on map.
        if (routeLine != null) {
            mapboxMap.removePolyline(routeLine);
        }

        routeLine = mapboxMap.addPolyline(new PolylineOptions()
                .addAll(latLngs)
                .color(Color.parseColor("#56b881"))
                .width(5f));
    }

    private void calculateRoute(Position destination) {
        Location userLocation = mapboxMap.getMyLocation();
        if (userLocation == null) {
            Timber.d("calculateRoute: \n" + "La ubicación del usuario es nula, por lo tanto, no se puede establecer el origen.");
            return;
        }

        navigation.setOrigin(Position.fromCoordinates(userLocation.getLongitude(), userLocation.getLatitude()));
        navigation.setDestination(destination);
        navigation.getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                DirectionsRoute route = response.body().getRoutes().get(0);
                MyMapBoxActivity.this.route = route;
                drawRouteLine(route);

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.e("onFailure: navigation.getRoute()", throwable);
            }
        });
    }

  /*
   * Navigation listeners
   */

    @Override
    public void onRunning(boolean running) {
        if (running) {
            Timber.d("onRunning: Started");
        } else {
            Timber.d("onRunning: Stopped");
        }
    }

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
        //Timber.d("onProgressChange: fraction of route traveled: %d", routeProgress.getFractionTraveled());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person");
        Geolocalization geolocalization = new Geolocalization();
        geolocalization.setLongitude(location.getLongitude());
        geolocalization.setLatitude(location.getLatitude());
        myRef.child("" + userid).child("geolocalization").child("" + geolocalizationId).setValue(geolocalization);
        calculateRoute(position);
        //geolocalizationId++;
    }

    @Override
    public void onAlertLevelChange(int alertLevel, RouteProgress routeProgress) {

        switch (alertLevel) {
            case HIGH_ALERT_LEVEL:
                //Toast.makeText(MyMapBoxActivity.this, "HIGH", Toast.LENGTH_LONG).show();
                break;
            case MEDIUM_ALERT_LEVEL:
                //Toast.makeText(MyMapBoxActivity.this, "MEDIUM", Toast.LENGTH_LONG).show();
                break;
            case LOW_ALERT_LEVEL:
                //Toast.makeText(MyMapBoxActivity.this, "LOW", Toast.LENGTH_LONG).show();
                break;
            case ARRIVE_ALERT_LEVEL:
                //Toast.makeText(MyMapBoxActivity.this, "ARRIVE", Toast.LENGTH_LONG).show();
                break;
            case DEPART_ALERT_LEVEL:
                //Toast.makeText(MyMapBoxActivity.this, "DEPART", Toast.LENGTH_LONG).show();
                break;
            default:
            case NONE_ALERT_LEVEL:
                //Toast.makeText(MyMapBoxActivity.this, "NONE", Toast.LENGTH_LONG).show();
                break;
        }
    }

  /*
   * Activity lifecycle methods
   */

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationEngine != null && locationEngine.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationEngine.requestLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
            locationEngine.removeLocationEngineListener(this);
            locationEngine.deactivate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigation.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        navigation.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autocomplete.cancelApiCall();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
