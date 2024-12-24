package com.example.tarea3.captured;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarea3.MainActivity;
import com.example.tarea3.PokemonData;
import com.example.tarea3.R;
import com.example.tarea3.databinding.FragmentCapturedBinding;

import java.util.ArrayList;

public class CapturedFragment extends Fragment {
    private FragmentCapturedBinding binding;
    private ArrayList<PokemonData> captured_list = new ArrayList<PokemonData>();
    private CapturedRecyclerViewAdapter adapter;
    // Etiqueta para mensajes de consola
    private String TAG = "CAPTURES";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos the layout para este fragment usando el binding
        binding = FragmentCapturedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ponemos título a la Toolbar
        ((MainActivity)getContext()).ponerTitulo(R.string.capturas);

        // Llamamos al método para obtener los datos de los pokemons de la base de datos
        ((MainActivity)getContext()).obtenerDatos(new MainActivity.DataCallback() {
            @Override
            public void onSuccess(ArrayList<PokemonData> data) {
                // Manejamos los datos aquí
                captured_list.addAll(data);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error al obtener los datos", e);
            }
        });

        // inicializa el RecyclerView
        adapter = new CapturedRecyclerViewAdapter(getActivity(), captured_list);
        binding.capturedRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.capturedRv.setAdapter(adapter);
    }
}