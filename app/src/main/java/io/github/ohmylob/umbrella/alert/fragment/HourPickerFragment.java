package io.github.ohmylob.umbrella.alert.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.activity.MainActivity;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;

public class HourPickerFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private Button hourPickerButton;
    private CheckBox everyDayCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.hour_picker_fragment, container, false);

        everyDayCheckBox = (CheckBox) rootView.findViewById(R.id.every_day);

        String everyDayString = SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.EVERY_DAY);
        if (!everyDayString.equals("0")) {
            everyDayCheckBox.setChecked(Boolean.valueOf(everyDayString));
        }

        everyDayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SharedPreferencesManager.save(getContext(), SharedPreferencesManager.EVERY_DAY, checked ? "true" : "false");
            }
        });

        hourPickerButton = (Button) rootView.findViewById(R.id.set_hour);

        String previousHour = SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.HOUR);
        String previousMinute = SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.MINUTE);

        if (!previousHour.equals("0")) {
            hourPickerButton.setText(previousHour + ":" + previousMinute);
        }

        hourPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                boolean is24Format = DateFormat.is24HourFormat(getContext());

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        HourPickerFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        is24Format
                );
                timePickerDialog.vibrate(false);
                timePickerDialog.show(getActivity().getFragmentManager(), "TAG");
            }
        });
        return rootView;
    }

    private String addZeroIfNeeded(String string) {
        return string.length() == 1
                ? "0" + string
                : string;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = addZeroIfNeeded(String.valueOf(hourOfDay));
        String minuteString = addZeroIfNeeded(String.valueOf(minute));

        hourPickerButton.setText(hourString + ":" + minuteString);

        SharedPreferencesManager.save(getContext(),
                SharedPreferencesManager.buildHashMap(
                        new String[]{
                                SharedPreferencesManager.HOUR,
                                SharedPreferencesManager.MINUTE
                        },
                        new String[]{
                                hourString,
                                minuteString
                        }
                )
        );
        ((MainActivity) getActivity()).nextPage();
    }
}
