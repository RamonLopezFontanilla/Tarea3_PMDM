package com.example.tarea3.pokedex;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea3.MainActivity;
import com.example.tarea3.PokemonData;
import com.example.tarea3.databinding.PokedexCardBinding;

import java.util.ArrayList;

public class PokedexRecyclerViewAdapter extends RecyclerView.Adapter<PokedexViewHolder> {
    private final ArrayList<PokemonData> pokemons;
    private final Context context;

    public PokedexRecyclerViewAdapter(Context context, ArrayList<PokemonData> pokemons) {
        this.context = context;
        this.pokemons = pokemons;
    }

    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PokedexCardBinding binding = PokedexCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new PokedexViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        PokemonData currentPokemon = this.pokemons.get(position);

        // Cambiar el color de fondo si el ítem está seleccionado o no
        if (((MainActivity) context).obtenerPosicionesDePreferencias().contains(position)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Fondo normal
        }

        holder.bind(currentPokemon);
        // ponemos un listener para controlar el click sobre un cardview
        holder.itemView.setOnClickListener(view -> itemClicked(currentPokemon, view, position));
    }

    /**
     * LLamada al método que almacena en la base de datos los datos del pokemon seleccionado.
     *
     * @param currentPokemon
     * @param view
     * @param position
     */
    private void itemClicked(PokemonData currentPokemon, View view, int position) {
        ((MainActivity) context).itemClicked(currentPokemon, view, position);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }
}
