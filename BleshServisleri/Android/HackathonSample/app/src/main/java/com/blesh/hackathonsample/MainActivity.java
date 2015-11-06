package com.blesh.hackathonsample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.blesh.sdk.classes.BleshInstance;
import com.blesh.sdk.common.BleshIntent;
import com.blesh.sdk.models.BleshTemplateResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Define a callback reference to be used by the Blesh service
        // in order to push the user's action results to your application
        BleshTemplateResult result = new BleshTemplateResult() {
            @Override
            public void bleshTemplateResultCallback(String actionType,
                                                    String actionValue) {
                if (actionType != null && actionValue != null) {
                    Log.i(TAG, "I received type:" + actionType + " value:"
                            + actionValue);
                    // Check for the action type and value you want to use
                    // You may wish to load a web page here using the action value
                } else {
                    Log.w(TAG, "bleshTemplateResultCallback() result is empty!");
                }
            }
        };

        // Register your callback function, named as "result" for this example
        BleshInstance.sharedInstance().setTemplateResult(result);

        // This is how you start Blesh service
        startBlesh("YOUR_API_USER", "YOUR_API_KEY", "INTEGRATION_ID");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startBlesh(String apiUser, String apiKey, String integrationId) {
        startService(new BleshIntent.Builder(apiUser, apiKey, integrationId).optionalKey("M").getIntent(this));
    }
}
