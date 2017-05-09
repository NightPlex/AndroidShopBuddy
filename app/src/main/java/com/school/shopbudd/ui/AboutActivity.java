package com.school.shopbudd.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.school.shopbudd.R;
import com.school.shopbudd.base.BaseActivity;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * About the application and it's development
 *
 * @author NightPlex
 */
public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        //Fire the data from super.
        super.onCreate(savedInstanceState);
        //Change the page
        setContentView(R.layout.activity_about);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        //Fetch the content
        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            //Changing opacity to 0
            mainContent.setAlpha(0);
            //Nice looking fading when changing the activities
            mainContent.animate().alpha(1).setDuration(BaseActivity.MAIN_CONTENT_FADEIN_DURATION);
        }

        overridePendingTransition(0, 0);

        //TODO: Change to my github
        ((TextView) findViewById(R.id.secusoWebsite)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.githubURL)).setMovementMethod(LinkMovementMethod.getInstance());
    }

}

