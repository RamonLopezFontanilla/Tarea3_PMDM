package com.example.tarea3.captured;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tarea3.PokemonData;
import com.example.tarea3.databinding.CapturedCardBinding;
import com.squareup.picasso.Picasso;

public class CapturedViewHolder extends RecyclerView.ViewHolder {
    private final CapturedCardBinding binding;

    /**
     * Constructor que usa el ViewBinding para simplificar el acceso a las vistas
     * dentro del diseño de cada elemento del RecyclerView
     *
     * @param binding
     */
    public CapturedViewHolder(CapturedCardBinding binding) {
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
                .into(binding.imagePokemon);

        binding.pokemonName.setText(pokemon.getName());
        binding.indice.setText(String.valueOf(pokemon.getNumero()));
        binding.weight.setText(String.valueOf(pokemon.getWeight()));
        binding.height.setText(String.valueOf(pokemon.getHeight()));
    }
}
