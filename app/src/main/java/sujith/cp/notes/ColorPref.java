package sujith.cp.notes;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by sujith on 6/10/15.
 */
public class ColorPref extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_main);
    }
}
