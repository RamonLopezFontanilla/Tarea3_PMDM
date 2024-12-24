package com.example.tarea3.pokedex;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea3.PokemonData;
import com.example.tarea3.databinding.PokedexCardBinding;
import com.squareup.picasso.Picasso;

public class PokedexViewHolder extends RecyclerView.ViewHolder {
    private final PokedexCardBinding binding;

    /**
     * Constructor que usa el ViewBinding para simplificar el acceso a las vistas
     * dentro del diseño de cada elemento del RecyclerView
     *
     * @param binding
     */
    public PokedexViewHolder(PokedexCardBinding binding) {
        super(binding.getRoot());
        this.binding = binding;

    }

    /**
     * Método que se usa para enlazar los datos de un objeto Pokemon a las vistas específicas
     * de un item en un RecyclerView
     *
     * @param pokemon Objeto Pokemon
     */
    public void bind(PokemonData pokemon) {
        Picasso.get()
                .load(pokemon.getImage())
                .into(binding.imagePoledex);

        binding.nombrePokedex.setText(pokemon.getName());
    }
}
