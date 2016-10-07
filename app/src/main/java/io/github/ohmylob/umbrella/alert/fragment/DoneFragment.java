package io.github.ohmylob.umbrella.alert.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.ohmylob.umbrella.alert.R;
import io.github.ohmylob.umbrella.alert.activity.MainActivity;
import io.github.ohmylob.umbrella.alert.alarm.AlarmSetter;
import io.github.ohmylob.umbrella.alert.preference.SharedPreferencesManager;

public class DoneFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.done_fragment, container, false);

        rootView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesManager.getValue(getContext(),
                        SharedPreferencesManager.CITY).equals("0")) {
                    Snackbar.make(view, R.string.select_city, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.select, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((MainActivity) getActivity()).getViewPager().setCurrentItem(0);
                                }
                            }).show();
                    return;
                }

                if (SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.HOUR).equals("0")) {
                    Snackbar.make(view, R.string.set_hour, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.select, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((MainActivity) getActivity()).getViewPager().setCurrentItem(1);
                                }
                            }).show();
                    return;
                }

                String isAlarmSetString = SharedPreferencesManager.getValue(getContext(), SharedPreferencesManager.IS_ALARM_SET);
                if (!isAlarmSetString.equals("0") && Boolean.valueOf(isAlarmSetString)) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.warning)
                            .setMessage(R.string.alarm_already_set)
                            .setPositiveButton(R.string.keep, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setAndFinish();
                                }
                            })
                            .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AlarmSetter.getInstance(getContext()).destroyAlarms();
                                    setAndFinish();
                                }
                            })
                            .create()
                            .show();
                } else {
                    setAndFinish();
                }
            }
        });
        return rootView;
    }

    private void setAndFinish() {
        AlarmSetter.getInstance(getContext()).set();
        getActivity().finish();
    }
}
