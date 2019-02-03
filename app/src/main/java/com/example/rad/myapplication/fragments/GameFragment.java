package com.example.rad.myapplication.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.api.ApiClient;
import com.example.rad.myapplication.constants.Constants;
import com.example.rad.myapplication.api.ApiException;
import com.example.rad.myapplication.data.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment {

    private static final Logger LOG = LoggerFactory.getLogger(GameFragment.class);

    private OnFragmentInteractionListener listener;
    private MyGameRecyclerViewAdapter adapter;

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Set the adapter
        Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyGameRecyclerViewAdapter(new ArrayList<Game>(), listener);
        RetrieveGameTask retrieveGameTask = new RetrieveGameTask() {
            @Override
            protected void onPreExecute() {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(List<Game> games) {
                adapter.games.clear();
                for (int i = 0; i < games.size(); i++) {
                    adapter.games.add(games.get(i));
                }
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };
        retrieveGameTask.execute();
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) context;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Game item);
    }


    class RetrieveGameTask extends AsyncTask<String, String, List<Game>> {

        private final ApiClient client = ApiClient.getInstance();

        protected List<Game> doInBackground(String... urls) {
            List<Game> list = new ArrayList<>();

            try {
                String jsonResponse = client.getURLWithAuth(Constants.ALL_USER_GAME_URL, String.class);
                if(jsonResponse.length()>5) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    LOG.error(jsonObject.toString());
                    JSONArray games = jsonObject.getJSONArray("data");
                    for (int i = 0; i < games.length(); i++) {
                        JSONObject gamesObject = games.getJSONObject(i);
                        Game item = Game.fromJsonObject(gamesObject,false);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                }

                return list;
            } catch (ApiException | JSONException exp) {
                LOG.error(exp.getMessage(), exp);
            }
            return list;
        }
    }

}
