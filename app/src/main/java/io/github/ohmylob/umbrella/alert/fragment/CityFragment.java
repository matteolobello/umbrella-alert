package io.github.ohmylob.umbrella.alert.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.List;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.activity.MainActivity;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;

@SuppressLint("ValidFragment")
public class CityFragment extends Fragment {

    private final List<Address> addresses;

    private final boolean initialPermissions;

    private View rootView;
    private Button setCityButton;
    private TextView youLiveIn;
    private TextView notMyCity;

    private boolean forceFindCity;

    public CityFragment(List<Address> addresses) {
        this.addresses = addresses;
        this.initialPermissions = addresses == null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(
                addresses != null && !forceFindCity
                        ? R.layout.city_fragment
                        : R.layout.city_fragment_find, container, false
        );

        if (forceFindCity) {
            loadWithoutPermissions();
        } else {
            setupUi();
        }

        return rootView;
    }

    private void setupUi() {
        if (addresses != null && addresses.size() > 0 && !forceFindCity) {
            Address address = addresses.get(0);

            String city = address.getLocality();

            youLiveIn = (TextView) rootView.findViewById(R.id.city);
            notMyCity = (TextView) rootView.findViewById(R.id.not_my_city);

            youLiveIn.setText(Html.fromHtml(city + ","));

            SharedPreferencesManager.save(getContext(),
                    SharedPreferencesManager.buildHashMap(
                            new String[]{
                                    SharedPreferencesManager.LATITUDE,
                                    SharedPreferencesManager.LONGITUDE,
                                    SharedPreferencesManager.CITY
                            },
                            new String[]{
                                    String.valueOf(address.getLatitude()),
                                    String.valueOf(address.getLongitude()),
                                    address.getLocality()
                            }
                    )
            );

            notMyCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reload(true);
                }
            });
        } else {
            reload(true);
        }
    }

    private void reload(boolean force) {
        forceFindCity = force;

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .detach(CityFragment.this)
                .attach(CityFragment.this)
                .commit();
    }

    public boolean backPressed() {
        if (forceFindCity && !initialPermissions) {
            reload(false);
            return false;
        }
        return true;
    }

    private void loadWithoutPermissions() {
        setCityButton = (Button) rootView.findViewById(R.id.set_city);

        String cityString = SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.CITY);
        if (!cityString.equals("0")) {
            setCityButton.setText(cityString);
        }

        setCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    getActivity().startActivityForResult(intent, MainActivity.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handlePlace(Place place) {
        setCityButton.setText(place.getAddress());

        SharedPreferencesManager.save(getContext(),
                SharedPreferencesManager.buildHashMap(
                        new String[]{
                                SharedPreferencesManager.LATITUDE,
                                SharedPreferencesManager.LONGITUDE,
                                SharedPreferencesManager.CITY
                        },
                        new String[]{
                                String.valueOf(place.getLatLng().latitude),
                                String.valueOf(place.getLatLng().longitude),
                                String.valueOf(place.getAddress())
                        }
                )
        );
    }
}
