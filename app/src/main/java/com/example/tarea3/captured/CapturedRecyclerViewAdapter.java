package com.example.tarea3.captured;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea3.MainActivity;
import com.example.tarea3.PokemonData;
import com.example.tarea3.databinding.CapturedCardBinding;

import java.util.ArrayList;


public class CapturedRecyclerViewAdapter extends RecyclerView.Adapter<CapturedViewHolder> {
    private final ArrayList<PokemonData> pokemons;
    private final Context context;

    public CapturedRecyclerViewAdapter(Context context, ArrayList<PokemonData> pokemons) {
        this.context = context;
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    public CapturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CapturedCardBinding binding = CapturedCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CapturedViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CapturedViewHolder holder, int position) {
        PokemonData currentPokemon = this.pokemons.get(position);

        holder.bind(currentPokemon);
        // ponemos un listener para controlar el click sobre un cardview
        holder.itemView.setOnClickListener(view -> ((MainActivity) context).pokemonSelectioned(currentPokemon, view));
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

}
