package com.school.shopbudd.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.school.shopbudd.R;
import com.school.shopbudd.base.BaseActivity;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * Help tab to get started within the application.
 *
 * @author NightPlex
 */
public class HelpActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID()
    {
        return R.id.nav_help;
    }

    public static class HelpFragment extends PreferenceFragment
    {

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.help);
        }
    }

}