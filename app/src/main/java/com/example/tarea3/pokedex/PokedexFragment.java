package com.example.tarea3.pokedex;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarea3.MainActivity;
import com.example.tarea3.PokemonData;
import com.example.tarea3.R;
import com.example.tarea3.databinding.FragmentPokedexBinding;

import java.util.ArrayList;

public class PokedexFragment extends Fragment {
    private FragmentPokedexBinding binding;
    private ArrayList<PokemonData> pokemons_list = new ArrayList<PokemonData>();
    private PokedexRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos the layout para este fragment usando el binding
        binding = FragmentPokedexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ponemos título a la Toolbar.
        ((MainActivity) getContext()).ponerTitulo(R.string.pok_dex);

        // pasamos al adaptador los datos de los pokemons extraidos de la API
        ((MainActivity) getContext()).obtenerDatosPokedex(new MainActivity.DataCallback() {
            @Override
            public void onSuccess(ArrayList<PokemonData> data) {
                // Manejamos los datos aquí
                pokemons_list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        // inicializamos el RecyclerView
        adapter = new PokedexRecyclerViewAdapter(getActivity(), pokemons_list);
        binding.pokedexRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.pokedexRv.setAdapter(adapter);

        // rejilla de pokedex
        binding.pokedexRv.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.pokedexRv.setLayoutManager(layoutManager);
    }
}