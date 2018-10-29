package io.github.ohmylob.umbrella.alert.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.degrees_picker_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        final RadioButton useCelsiusRadioButton = rootView.findViewById(R.id.use_celsius);
        final RadioButton useFahrenheitsRadioButton = rootView.findViewById(R.id.use_fahrenheit);

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
    }
}
