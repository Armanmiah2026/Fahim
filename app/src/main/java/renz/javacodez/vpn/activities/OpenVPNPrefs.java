package renz.javacodez.vpn.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import app.dev.shapla.vpn.R;

public class OpenVPNPrefs extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        if (OpenVPNClientBase.themeSet) {
            setTheme(OpenVPNClientBase.themeResId);
        }
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
