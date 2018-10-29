package io.github.ohmylob.umbrella.alert.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.activity.MainActivity;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;

public class EnableWifiFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enable_wifi_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        CheckBox checkBox = rootView.findViewById(R.id.enable_wifi);

        String enableWiFiString = SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.ENABLE_WIFI);
        if (!enableWiFiString.equals("0")) {
            checkBox.setChecked(Boolean.valueOf(enableWiFiString));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean switchedOn) {
                SharedPreferencesManager.save(getContext(), SharedPreferencesManager.ENABLE_WIFI,
                        switchedOn ? "true" : "false");
                ((MainActivity) getActivity()).nextPage();
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
