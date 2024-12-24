package com.example.tarea3;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarea3.databinding.FragmentDetailsBinding;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {
    private FragmentDetailsBinding binding;
    private boolean borradoPokemon = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getContext()).ponerTitulo(R.string.detalles);

        if (getArguments() != null) {
            //guardamos en variables los datos recibidos del bundle
            String name = getArguments().getString("name");
            String image = getArguments().getString("image");
            int positionInPokedexRecycler = getArguments().getInt("positionInPokedexRecycler");
            int index = getArguments().getInt("index");
            String types = getArguments().getString("types");
            int weight = getArguments().getInt("weight");
            int height = getArguments().getInt("height");

            //traducimos los tipos al idioma seleccionado usando un método del MainActivity
            String tiposTraducidos=((MainActivity)getContext()).traducirTipos(types);

            //rellenamos los campos de la vista con el valor de las variables anteriores
            binding.pokemonName.setText(name);
            Picasso.get()
                    .load(image)
                    .into(binding.pokemonImage);
            binding.positionInPokedexRecycler.setText(String.valueOf(positionInPokedexRecycler));
            binding.indice.setText(String.valueOf(index));
            binding.type.setText(tiposTraducidos);
            binding.weight.setText(String.valueOf(weight));
            binding.height.setText(String.valueOf(height));

            // Obtener las preferencias
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

            // Comprobar la preferencia de activar borrado de pokemons
            if (prefs.getBoolean("delete", true)) {
                borradoPokemon = true;
            } else {
                binding.buttonDelete.setBackgroundColor(Color.LTGRAY);
                binding.buttonDelete.setTextColor(Color.DKGRAY);
                borradoPokemon = false;
            }
            binding.buttonDelete.setOnClickListener(this::eliminarPokemon);
        }
    }

    private void eliminarPokemon(View view) {
        ((MainActivity) getContext()).eliminarPokemon(view, binding.pokemonName.getText().toString(), borradoPokemon, Integer.parseInt(binding.positionInPokedexRecycler.getText().toString()));
    }
}