package com.example.rad.myapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.api.ApiException;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.Game;
import com.example.rad.myapplication.data.Task;
import com.example.rad.myapplication.tasks.LoginUserTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.security.AccessController.getContext;

public class TaskActivity extends AppCompatActivity implements LocationListener  {

    private SharedPreferences sharedPreferences;
    private Button checkButton;
    private TextView textName, textDescription,textGPS;
    private ProgressBar progressBar;
    private String code;
    private Task currentTask;
    protected LocationManager locationManager;
    protected Location location;
    //protected LocationListener locationListener;
    protected Context context;
    private Double longitude=0.0,latitude=0.0;

    private static final Logger LOG = LoggerFactory.getLogger(LoginUserTask.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        sharedPreferences =
                getApplicationContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        checkButton = (Button) findViewById(R.id.checkButton);
        textName = (TextView) findViewById(R.id.textName);
        textGPS = (TextView) findViewById(R.id.textGPS);
        textDescription = (TextView) findViewById(R.id.textDescription);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        code =  intent.getStringExtra("code");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        context = this;
        checkPermission();



        RetrieveTask retrieveTask = new RetrieveTask() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected void onPostExecute(Task task) {
                currentTask = task;
                textName.setText(task.getName());
                textDescription.setText(task.getDescription());
                textName.setVisibility(View.VISIBLE);
                textDescription.setVisibility(View.VISIBLE);
                checkButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };
        retrieveTask.execute();

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){

                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        textGPS.setVisibility(View.VISIBLE);
                        textGPS.setText("Latitude:" +latitude + ", Longitude:" + longitude);
                    }
//                    LOG.error(longitude.toString());
//                    LOG.error(latitude.toString());

                    RetrieveTaskCheck retrieveTaskCheck = new RetrieveTaskCheck() {
                        @Override
                        protected void onPreExecute() {
                            progressBar.setVisibility(View.VISIBLE);
                            checkButton.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        protected void onPostExecute(Task task) {
                            if (task.getStatus()) {// end task

                                if (task.getEnd()) {//end game
                                    textName.setText("Gratulacje! Koniec gry!");
                                    textDescription.setVisibility(View.GONE);
                                    checkButton.setVisibility(View.INVISIBLE);
                                } else {
                                    currentTask = task;
                                    textName.setText(task.getName());
                                    textDescription.setText(task.getDescription());
                                    textName.setVisibility(View.VISIBLE);
                                    textDescription.setVisibility(View.VISIBLE);
                                    checkButton.setVisibility(View.VISIBLE);
                                }
                            } else {
                                checkButton.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),
                                        "Niestety to nie tu:(.", Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    };
                    retrieveTaskCheck.execute();
                }
            }
        });

    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},99);
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
            // milliseconds,meters

            return true;
        }
        return false;
    }

    private void logout() {
        sharedPreferences.edit().clear().apply();
        ApiClient.getInstance().unauthorize();
        finishAffinity();
        Intent mIntent = new Intent(this, LoginRegisterActivity.class);
        startActivity(mIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.logout) {
            logout();

            return true;
        }
        if (id == R.id.gamesAll) {
            startMainActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startMainActivity() {
        Intent mIntent = new Intent(TaskActivity.this, ChoiceSaveActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        //txtLat = (TextView) findViewById(R.id.textview1);
        textGPS.setVisibility(View.VISIBLE);
        textGPS.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        //LOG.info("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        LOG.info("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        LOG.info("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LOG.info("Latitude","status");
    }

    class RetrieveTask extends AsyncTask<String, String, Task> {

        private final ApiClient client = ApiClient.getInstance();

        protected Task doInBackground(String... urls) {
            Task task = new Task();

            try {

                String jsonResponse = client.getURLWithAuth(Constants.GET_CURRENT_TASK_URL+"?code="+code, String.class);
                if(jsonResponse.length()>5) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject tasks = jsonObject.getJSONObject("data");
                    task = Task.fromJsonObject(tasks);
                }

                return task;
            } catch (ApiException | JSONException exp) {
                LOG.error(exp.getMessage(), exp);
            }
            return task;
        }
    }

    class RetrieveTaskCheck extends AsyncTask<String, String, Task> {

        private final ApiClient client = ApiClient.getInstance();

        protected Task doInBackground(String... urls) {
            Task task = new Task();

            try {
                String jsonResponse = client.getURLWithAuth(
                        Constants.CHECK_TASK_URL+"?code="+code+"&task_id="+currentTask.geTaskId()+"" +
                                "&longitude="+longitude+"&latitude="+latitude
                        , String.class);
                if(jsonResponse.length()>5) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject tasks = jsonObject.getJSONObject("data");

                    if(tasks.has("end") ){
                        if(!tasks.getBoolean("end")) {
                            task = Task.fromJsonObject(tasks);
                            task.setStatus(tasks.getBoolean("status"));
                            task.setEnd(tasks.getBoolean("end"));
                        } else {
                            task.setStatus(tasks.getBoolean("status"));
                            task.setEnd(tasks.getBoolean("end"));
                        }
                    } else if(tasks.has("status")) {
                        task.setStatus(tasks.getBoolean("status"));
                        task.setEnd(false);
                    }

                }

                return task;
            } catch (ApiException | JSONException exp) {
                LOG.error(exp.getMessage(), exp);
            }
            return task;
        }
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
        finish();
    }
    
}
