package com.example.tarea3;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tarea3.databinding.FragmentAdjustBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class AdjustFragment extends Fragment {
    private FragmentAdjustBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdjustBinding.inflate(inflater, container, false);

        ((MainActivity)getContext()).ponerTitulo(R.string.ajustes);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // hacer coincidir el switch con la preferencia de borrado
        if (prefs.getBoolean("delete", true)) {
            binding.switchDelete.setChecked(true);
        } else {
            binding.switchDelete.setChecked(false);
        }

        // hacer coincidir el radioGroup con la preferencia de idioma
        if (prefs.getString("language", "en").equals("en")) {
            binding.english.setChecked(true);
        } else {
            binding.spanish.setChecked(true);
        }

        // establecemos listener para los 4 elementos del fragment
        binding.switchDelete.setOnCheckedChangeListener(this::onDeleteSelected);
        binding.radioLanguage.setOnCheckedChangeListener(this::onLanguageSelected);
        binding.closeSesion.setOnClickListener(this::onClickCloseSesion);
        binding.about.setOnClickListener(this::onClickAbout);

        return binding.getRoot();
    }

    /**
     * Método con definir la acción del switch que controla el permiso de borrado de pokemons
     *
     * @param compoundButton
     * @param selected
     */
    private void onDeleteSelected(CompoundButton compoundButton, boolean selected) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (selected) {
            prefs.edit().putBoolean("delete", true).apply();
        } else {
            prefs.edit().putBoolean("delete", false).apply();
        }
    }

    /**
     * Método para guardar en las preferencias el idioma seleccionado en el radioGroup
     *
     * @param radioGroup
     * @param checkedId
     */
    private void onLanguageSelected(RadioGroup radioGroup, int checkedId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (checkedId == R.id.english) {
            changeLanguage("en");
            prefs.edit().putString("language", "en").apply();

        } else {
            changeLanguage("es");
            prefs.edit().putString("language", "es").apply();
        }
    }

    /**
     * Método para establecer el idioma seleccionado
     *
     * @param codeLanguage
     */
    private void changeLanguage(String codeLanguage) {
        Locale locale = new Locale(codeLanguage);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        updateLanguageView();
    }

    /**
     * Método para cambiar el idioma a los elementos del fragment visible
     */
    private void updateLanguageView() {
        ((MainActivity) getContext()).cambiarIdiomaBottomMenu();
        ((MainActivity) getContext()).ponerTitulo(R.string.ajustes);
        binding.about.setText(R.string.button_about);
        binding.closeSesion.setText(R.string.buttom_close_sesion);
        binding.switchDelete.setText(R.string.eliminar_pokemons);
        binding.textLanguage.setText(R.string.radio_language);
        binding.spanish.setText(R.string.radio_spanish);
        binding.english.setText(R.string.radio_english);

    }

    /**
     * Método con definir la acción del botón About
     *
     * @param view
     */
    private void onClickAbout(View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.titulo_cuadro_acerca_de)
                .setMessage(R.string.texto_cuadro_acerca_de)
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Método con definir la acción del botón Cerrar sesión
     *
     * @param view
     */
    private void onClickCloseSesion(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthUI.getInstance()
                    .signOut(getContext())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), R.string.toast_close_sesion, Toast.LENGTH_SHORT).show();
                            // llamamos al método para reiniciar la actividad
                            goToLoginActivity();
                        }
                    });
        }
    }

    /**
     * Método que reinicia la aplicación cuando cerramos sesión
     */
    private void goToLoginActivity() {
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
    }
}