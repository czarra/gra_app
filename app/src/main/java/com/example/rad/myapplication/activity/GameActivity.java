package com.example.rad.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.api.ApiException;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.Game;
import com.example.rad.myapplication.tasks.LoginUserTask;
import com.example.rad.myapplication.tasks.SaveGameTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity  {

    private SharedPreferences sharedPreferences;
    private Button goToTaskButton;
    private TextView textName, textDescription, textTasksGame;
    private ProgressBar progressBar;
    private String code;
  //  private Integer id;
    private static final Logger LOG = LoggerFactory.getLogger(LoginUserTask.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        sharedPreferences =
                getApplicationContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        goToTaskButton = (Button) findViewById(R.id.goToTaskButton);
        textName = (TextView) findViewById(R.id.textName);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textTasksGame = (TextView) findViewById(R.id.textTasksGame);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        code =  intent.getStringExtra("code");

        RetrieveGame retrieveGame = new RetrieveGame() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected void onPostExecute(Game game) {
                textName.setText(game.getName());
                textDescription.setText(game.getDescription());
                textName.setVisibility(View.VISIBLE);
                textDescription.setVisibility(View.VISIBLE);
                //show only if is more task
                if(game.getIsCurrentTask()) {
                    textTasksGame.setText("Zadanie "+game.getUserTask()+" z "+game.getAllTask());
                    goToTaskButton.setVisibility(View.VISIBLE);
                    textTasksGame.setVisibility(View.VISIBLE);
                }else{
                    textTasksGame.setText("Gra ukończona");
                }

                progressBar.setVisibility(View.GONE);
            }
        };
        retrieveGame.execute();

        goToTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTaskActivity();
            }
        });

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
        Intent mIntent = new Intent(GameActivity.this, ChoiceSaveActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void startTaskActivity() {
        Intent mIntent = new Intent(GameActivity.this, TaskActivity.class);
        mIntent.putExtra("code", code);
        startActivity(mIntent);
       // finish();
    }

    class RetrieveGame extends AsyncTask<String, String, Game> {

        private final ApiClient client = ApiClient.getInstance();

        protected Game doInBackground(String... urls) {
            Game game = new Game();

            try {

                String jsonResponse = client.getURLWithAuth(Constants.GET_INFO_GAME_URL+"?code="+code, String.class);
                if(jsonResponse.length()>5) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject games = jsonObject.getJSONObject("data");
                    game = Game.fromJsonObject(games,true);
                }

                return game;
            } catch (ApiException | JSONException exp) {
                LOG.error(exp.getMessage(), exp);
            }
            return game;
        }
    }

    @Override
    public void onBackPressed() {

        startMainActivity();
        finish();
    }
    
}
