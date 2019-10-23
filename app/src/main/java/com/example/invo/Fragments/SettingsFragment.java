package com.example.invo.Fragments;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.invo.MainActivity;
import com.example.invo.R;

public class SettingsFragment extends PreferenceFragment {
    MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = new MainActivity();
        addPreferencesFromResource(R.xml.settings);
        final SwitchPreference onoffSwitch = (SwitchPreference) findPreference("enabled");
        onoffSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (onoffSwitch.isChecked()){

                    onoffSwitch.setChecked(false);
                }
                else {
                    onoffSwitch.setChecked(true);
                }
                return false;
            }
        });

        Preference preference = (Preference) findPreference("enabled1");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                About about = new About();

                getActivity().getFragmentManager().beginTransaction().replace(
                        R.id.fragment_conteiner, about, "findThisFragment"
                ).addToBackStack(null).commit();
                return false;
            }
        });
        final ListPreference listPreference = (ListPreference) getPreferenceManager().findPreference("list_preference_1");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listPreference.setValue(newValue.toString());
                preference.setTitle(listPreference.getEntry());
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        return view;
    }
}