package io.github.ohmylob.umbrella.alert.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.activity.MainActivity;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;

public class DegreesPickerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.degrees_picker_fragment, container, false);

        final RadioButton useCelsiusRadioButton = (RadioButton) rootView.findViewById(R.id.use_celsius);
        final RadioButton useFahrenheitsRadioButton = (RadioButton) rootView.findViewById(R.id.use_fahrenheit);

        boolean useCelsius = Boolean.valueOf(SharedPreferencesManager.getValue(getContext(),
                SharedPreferencesManager.USE_CELSIUS));

        useCelsiusRadioButton.setChecked(useCelsius);
        useFahrenheitsRadioButton.setChecked(!useCelsius);

        useCelsiusRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SharedPreferencesManager.save(getContext(), SharedPreferencesManager.USE_CELSIUS,
                        checked ? "true" : "false");
                useFahrenheitsRadioButton.setChecked(!checked);
            }
        });

        rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).nextPage();
            }
        });

        return rootView;
    }
}
