package io.github.ohmylob.umbrella.alert.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;

import com.felipecsl.gifimageview.library.GifImageView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.adapter.ViewPagerAdapter;
import io.github.ohmylob.umbrella.alert.fragment.CityFragment;
import io.github.ohmylob.umbrella.alert.fragment.DegreesPickerFragment;
import io.github.ohmylob.umbrella.alert.fragment.DoneFragment;
import io.github.ohmylob.umbrella.alert.fragment.EnableWifiFragment;
import io.github.ohmylob.umbrella.alert.fragment.HourPickerFragment;
import io.github.ohmylob.umbrella.alert.viewpager.ParallaxPageTransformer;
import io.github.ohmylob.umbrella.alert.web.NetworkManager;

public class MainActivity extends AppCompatActivity {

    /**
     * Place AutoComplete Activity Result Code
     */
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 300;

    /**
     * Location Listener
     */
    private static final LocationListener LOCATION_LISTENER = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
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

    /**
     * Main ViewPager
     */
    private ViewPager viewPager;

    /**
     * The Adapter of the ViewPager
     */
    private ViewPagerAdapter viewPagerAdapter;

    /**
     * Application Fragments
     */
    private Fragment cityFragment;
    private Fragment hourFragment;
    private Fragment wifiFragment;
    private Fragment degreesPickerFragment;
    private Fragment doneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        viewPager = (ViewPager) findViewById(R.id.pager);

        Assent.setActivity(MainActivity.this, MainActivity.this);

        if (NetworkManager.isNetworkAvailable(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkLocationPermissions()) {
                    Assent.requestPermissions(new AssentCallback() {
                        @Override
                        public void onPermissionResult(PermissionResultSet result) {
                            setupUi(getCity(getLocation()));
                        }
                    }, 500, Assent.ACCESS_COARSE_LOCATION);
                } else {
                    setupUi(getCity(getLocation()));
                }
            } else {
                setupUi(getCity(getLocation()));
            }
        } else {
            viewPager.setVisibility(View.GONE);
            findViewById(R.id.no_internet_layout).setVisibility(View.VISIBLE);

            try {
                InputStream inputStream = getAssets().open("error.gif");
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();

                GifImageView gifView = (GifImageView) findViewById(R.id.no_internet_umbrella);
                if (gifView != null) {
                    gifView.setBytes(bytes);
                    gifView.startAnimation();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A Method to get Current Location
     *
     * @return the Location object
     */
    private Location getLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            LocationManager locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);

            Location location;

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000,
                            1000 * 60 * 100, LOCATION_LISTENER);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        return location;
                    }
                }
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                            1000 * 60 * 5, LOCATION_LISTENER);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        return location;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get City name giving the Location object
     *
     * @param location the Location object
     * @return An Array of Addresses
     */
    private List<Address> getCity(Location location) {
        if (location == null) {
            return null;
        }

        Geocoder coder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        List<Address> results = null;
        try {
            results = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Setup App UI
     *
     * @param results the List of Addresses
     */
    private void setupUi(List<Address> results) {
        cityFragment = new CityFragment(results);
        hourFragment = new HourPickerFragment();
        wifiFragment = new EnableWifiFragment();
        degreesPickerFragment = new DegreesPickerFragment();
        doneFragment = new DoneFragment();

        Fragment[] fragments = {
                cityFragment,
                hourFragment,
                wifiFragment,
                degreesPickerFragment,
                doneFragment
        };

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, buildParallaxPageTransformer());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * Get the ParallaxPageTransformer
     *
     * @return the ParallaxPageTransformer
     */
    private ParallaxPageTransformer buildParallaxPageTransformer() {
        return new ParallaxPageTransformer()
                // First fragment
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.you_live_in, 0.6f, 0.4f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.city, 0.8f, 0.6f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.right, 1f, 0.8f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.not_my_city, 1.5f, 1.0f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.set_city, 1f, 0.5f))
                // Second fragment
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.every_day, 0.2f, 0.2f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.set_hour, 0.5f, 0.5f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.when_receive_notification, 1.0f, 1.0f))
                // Third fragment
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.enable_wifi, 0.8f, 0.9f))
                // Fourth fragment
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.next, 0.4f, 0.5f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.radio_group, 1.2f, 0.8f))
                // Fifth fragment
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.exit, 0.4f, 0.4f))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.when_receive_notification, 1f, 0.8f));
    }

    /**
     * Scroll to next page
     */
    public void nextPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    /**
     * Check if the app has GPS permissions
     *
     * @return true if the app has GPS permissions
     */
    public boolean checkLocationPermissions() {
        return Assent.isPermissionGranted(Assent.ACCESS_FINE_LOCATION);
    }

    /**
     * @return the ViewPager
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    viewPager.setCurrentItem(1);
                    ((CityFragment) cityFragment).handlePlace(PlaceAutocomplete.getPlace(this, data));
                    break;
                case PlaceAutocomplete.RESULT_ERROR:
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Toast.makeText(MainActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    // The user canceled the operation.
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Leave this empty to fix crash when user denies location permissions
    }

    @Override
    public void onBackPressed() {
        if (cityFragment != null) {
            if (((CityFragment) cityFragment).backPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Assent.setActivity(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Assent.handleResult(permissions, grantResults);
    }
}
