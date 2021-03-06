package com.example.rad.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.data.Game;
import com.example.rad.myapplication.data.SaveGame;
import com.example.rad.myapplication.fragments.GameFragment;
import com.example.rad.myapplication.tasks.LoginUserTask;
import com.example.rad.myapplication.tasks.SaveGameTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ChoiceSaveActivity extends AppCompatActivity implements GameFragment.OnFragmentInteractionListener {

    private SharedPreferences sharedPreferences;
    private Button saveToGameButton;
    private EditText gameCode;
    private ProgressBar progressBar;
    private static final Logger LOG = LoggerFactory.getLogger(LoginUserTask.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choise_save);
        sharedPreferences =
                getApplicationContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
        saveToGameButton = (Button) findViewById(R.id.saveToGameButton);
        gameCode = (EditText) findViewById(R.id.gameCode);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        loadFragment(GameFragment.newInstance());

        saveToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameCode.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),
                            "Pola nie mogę być puste!", Toast.LENGTH_SHORT).show();

                }else {
                    saveToGameButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    SaveGame game = new SaveGame(gameCode.getText().toString());
                    SaveGameTask gameTask = new SaveGameTask(game) {
                        @Override
                        protected void onPostExecute(Boolean success) {
                            super.onPostExecute(success);
                            if (success) {
                                Game lockGame = new Game(super.getGameCode());
                                starGameActivity(lockGame);
                            } else if( !super.getMessage().isEmpty()){
                                Toast.makeText(getApplicationContext(),
                                        super.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Błąd. Nie udało się zapisać!", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                            saveToGameButton.setEnabled(true);
                        }
                    };
                    gameTask.execute();
                }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (id == R.id.endGame) {
            startEndGamesActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startEndGamesActivity() {
        Intent mIntent = new Intent(ChoiceSaveActivity.this, EndGameActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Game game) {
        starGameActivity(game);
    }

    private void starGameActivity(Game game) {
        Intent mIntent = new Intent(this, GameActivity.class);
        mIntent.putExtra("code", game.getCode());
        startActivity(mIntent);
        finish();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.commit();
    }

}
