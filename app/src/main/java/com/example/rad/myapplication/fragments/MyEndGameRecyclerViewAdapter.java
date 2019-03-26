package com.example.rad.myapplication.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rad.myapplication.R;
import com.example.rad.myapplication.data.Game;

import java.util.List;

public class MyEndGameRecyclerViewAdapter extends RecyclerView.Adapter<MyEndGameRecyclerViewAdapter.ViewHolder> {

    public final List<Game> games;
    private Context context;
    private final EndGameFragment.OnFragmentInteractionListener listener;

    public MyEndGameRecyclerViewAdapter(List<Game> games, EndGameFragment.OnFragmentInteractionListener listener) {
        this.games = games;
        this.listener =listener;
    }

    @Override
    public MyEndGameRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_end_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.game = games.get(position);
        holder.gameName.setText( games.get(position).getName());

        holder.buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onFragmentInteraction(games.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        final TextView gameName;
        final Button buttonGo;
        Game game;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            gameName = (TextView) view.findViewById(R.id.textNameGame);
            buttonGo = (Button) view.findViewById(R.id.buttonGo);
        }
    }
}
