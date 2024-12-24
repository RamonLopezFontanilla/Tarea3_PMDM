package com.example.tarea3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import com.example.tarea3.databinding.ActivityMainBinding;
import com.example.tarea3.retrofit.ApiService;
import com.example.tarea3.retrofit.PokemonRespuesta;
import com.example.tarea3.retrofit.PokemonRespuestaDetalle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private String TAG = "MAIN";

    // inicializamos variables para el acceso a la API
    private Retrofit retrofit;
    private final String URL_API = "https://pokeapi.co/api/v2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //inicialización del binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //buscamos en contenedor de fragmentos (navHostFragment)
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        //buscamos el controlador de la pila de fragmentos que se van mostrando y ocultando
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
        }

        //establecemos el idioma predeterminado.
        cambiarIdiomaApp();
        cambiarIdiomaBottomMenu();

        //establecemos la barra de menú.
        setSupportActionBar(binding.toolbar);

        //ponemos listener para la selección de un botón del menú.
        binding.bottomNavigation.setOnItemSelectedListener(this::selectedBottomMenu);

    }

    /************************************************************************************
     * Método para navegar con el bottom menú.
     *************************************************************************************/
    private boolean selectedBottomMenu(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.button_capturas) {
            navController.navigate(R.id.capturedFragment);
            binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_my_calendar);
        }
        if (menuItem.getItemId() == R.id.button_pokedex) {
            navController.navigate(R.id.pokedexFragment);
            binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_today);
        }
        if (menuItem.getItemId() == R.id.button_ajustes) {
            navController.navigate(R.id.adjustFragment);
            binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_manage);
        }
        return true;
    }

    /************************************************************************************
     * Método para enviar los datos del pokemon seleccionado de la lista de capturados,
     * al Fragment de detalles de dicho pokemon.
     * @param pokemon
     * @param view
     *************************************************************************************/
    public void pokemonSelectioned(PokemonData pokemon, View view) {
        Bundle bundle = new Bundle();
        bundle.putString("name", pokemon.getName());
        bundle.putString("image", pokemon.getImage());
        bundle.putInt("positionInPokedexRecycler", pokemon.getPositionInRecycler());
        bundle.putInt("index", pokemon.getNumero());
        bundle.putString("types", pokemon.getTypes());
        bundle.putInt("weight", pokemon.getWeight());
        bundle.putInt("height", pokemon.getHeight());

        //mostrar fragment de detalle del personaje
        Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
    }

    /************************************************************************************
     * Método para cargar los pokemons capturados al eliminar uno.
     ************************************************************************************/
    public void actualizarCaptured() {
        navController.navigate(R.id.pokedexFragment);
    }

    /************************************************************************************
     * Método para cambiar el idioma a los botones del botom menú.
     ************************************************************************************/
    public void cambiarIdiomaBottomMenu() {
        binding.bottomNavigation.getMenu().getItem(0).setTitle(R.string.capturas);
        binding.bottomNavigation.getMenu().getItem(1).setTitle(R.string.pok_dex);
        binding.bottomNavigation.getMenu().getItem(2).setTitle(R.string.ajustes);
    }

    /************************************************************************************
     * Método para poner el idioma almacenado en las SharedPreferences.
     ************************************************************************************/
    public void cambiarIdiomaApp() {
        // Obtener las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String languageCode;

        // Comprobar la preferencia de idioma
        String code = prefs.getString("language", "en");
        if (code.equals("en")) {
            languageCode = "en";
        } else {
            languageCode = "es";
        }

        // Crear el nuevo Locale
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    /************************************************************************************
     * Método para poner titulos a la Toolbar.
     *
     * @param title
     ************************************************************************************/
    public void ponerTitulo(int title) {
        binding.toolbar.setTitle(title);
    }

    /************************************************************************************
     * Método que extrae los campos de cada registro de la API y crea un
     * objeto pokemon por cada registro, almacenandolos en el array que se pasa al
     * adaptador del recyclerview.
     ************************************************************************************/
    public void obtenerDatosPokedex(final DataCallback callback) {
        // cargamos lista de nombres de Pokemons
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon();
        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                if (response.isSuccessful()) {
                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<PokemonData> listaPokemons = pokemonRespuesta.getResults();

                    // llamamos al método del adaptador para pasarle la lista de pokemons
                    callback.onSuccess(listaPokemons); // Devolver los datos mediante el callback

                } else {
                    Log.e(TAG, " onResponse: " + response.errorBody());
                    callback.onFailure(new Exception());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                Log.e(TAG, " onFailure: " + t.getMessage());
            }
        });
    }


    /************************************************************************************
     * Método para almacenar en la base de datos los datos del pokemon que acabamos
     * de seleccionar, trayendo de la API el resto de campos necesarios.
     ************************************************************************************/
    public void itemClicked(PokemonData currentPokemon, View view, int position) {
        // Guardar la posición seleccionada en SharedPreferences
        guardarPosicionEnPreferencias(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pokemonsCapturados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean capturadoAnteriormente = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Comprobamos si el pokemon recibido ya estaba capturado anteriormente
                                String name = document.getString("name");
                                if (name.equals(currentPokemon.getName().toString())) {
                                    capturadoAnteriormente = true;
                                }
                            }
                            // Si una vez recorrida la base de datos no hemos encontrado el nombre lo añadimos
                            if (!capturadoAnteriormente) {
                                // Traemos de la API los datos complementarios (peso, altura,...)
                                retrofit = new Retrofit.Builder()
                                        .baseUrl(URL_API)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                ApiService serviceDetalle = retrofit.create(ApiService.class);
                                Call<PokemonRespuestaDetalle> pokemonRespuestaDetalleCall = serviceDetalle.find(currentPokemon.getName().toString());
                                pokemonRespuestaDetalleCall.enqueue(new Callback<PokemonRespuestaDetalle>() {
                                    @Override
                                    public void onResponse(Call<PokemonRespuestaDetalle> call, Response<PokemonRespuestaDetalle> response) {
                                        if (response.isSuccessful()) {
                                            PokemonRespuestaDetalle pokemonRespuestaDetalle = response.body();

                                            currentPokemon.setPositionInRecycler(position); // para cuando lo eliminemos que lo quite de sharedPreferences
                                            currentPokemon.setCaptured(true);
                                            currentPokemon.setName(pokemonRespuestaDetalle.getName());
                                            currentPokemon.setNumero(pokemonRespuestaDetalle.getId());
                                            currentPokemon.setImage(pokemonRespuestaDetalle.getImagen());
                                            currentPokemon.setWeight(pokemonRespuestaDetalle.getWeight());
                                            currentPokemon.setHeight(pokemonRespuestaDetalle.getHeight());
                                            ArrayList<PokemonRespuestaDetalle.PokemonType> types = response.body().getTypes();
                                            for (PokemonRespuestaDetalle.PokemonType type : types) {
                                                String typeName = type.getType().getName();
                                                if (currentPokemon.getTypes() == null) {
                                                    currentPokemon.setTypes(typeName);
                                                } else {
                                                    currentPokemon.setTypes(currentPokemon.getTypes() + ", " + typeName);
                                                }

                                                Log.d("PokemonType", "Type: " + typeName);
                                            }
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            db.collection("pokemonsCapturados").add(currentPokemon)
                                                    .addOnSuccessListener(runnable ->
                                                            Log.e(TAG, " Captured: " + response.body().getName()))
                                                    //Toast.makeText(view.getContext(), "Capturado correctamente", Toast.LENGTH_SHORT).show())
                                                    .addOnFailureListener(runnable ->
                                                            Toast.makeText(view.getContext(), "Error. Pokemon no capturado", Toast.LENGTH_SHORT).show()
                                                    );
                                        } else {
                                            Log.e(TAG, " onResponse: " + response.errorBody());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PokemonRespuestaDetalle> call, Throwable t) {
                                        Log.e(TAG, " onFailure: " + t.getMessage());
                                    }
                                });
                            }
                        }
                    }
                });
    }


    /************************************************************************************
     * Método que extrae los campos de cada registro de la base de datos y crea un
     * objeto pokemon por cada registro, almacenandolos en el array que se pasa al
     * adaptador del recyclerview.
     ************************************************************************************/
    public void obtenerDatos(final DataCallback callback) {
        ArrayList<PokemonData> captured_list = new ArrayList<PokemonData>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pokemonsCapturados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // procesamos cada documento recibido de la consulta a la base de datos
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                int numero = (document.getLong("numero")).intValue();
                                String url = document.getString("url");
                                String image = document.getString("image");
                                int weight = (document.getLong("weight")).intValue();
                                int height = (document.getLong("height")).intValue();
                                int positionInRecycler = (document.getLong("positionInRecycler")).intValue();
                                String types = document.getString("types");

                                // añadimos al array de adaptador un objeto por cada registro leido de la base de datos
                                captured_list.add(new PokemonData(true, height, image, name, numero, url, weight, positionInRecycler, types));
                            }
                            callback.onSuccess(captured_list); // Devolver los datos mediante el callback
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    /************************************************************************************
     * Método para obtener las posiciones seleccionadas desde SharedPreferences.
     *
     * @return
     *************************************************************************************/
    public Set<Integer> obtenerPosicionesDePreferencias() {
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Set<String> stringPositions = sharedPref.getStringSet("selected_positions", new HashSet<>());
        Set<Integer> positions = new HashSet<>();
        for (String pos : stringPositions) {
            positions.add(Integer.parseInt(pos));
        }
        return positions;
    }

    /************************************************************************************
     * Método para guardar la posición seleccionada en SharedPreferences
     *
     * @param position
     *************************************************************************************/
    private void guardarPosicionEnPreferencias(int position) {
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Guardamos la posición seleccionada en SharedPreferences
        Set<Integer> selectedPositions = obtenerPosicionesDePreferencias();
        selectedPositions.add(position);

        // Convertir Set<Integer> a Set<String> para guardarlo en SharedPreferences
        Set<String> stringPositions = new HashSet<>();
        for (Integer pos : selectedPositions) {
            stringPositions.add(String.valueOf(pos));
        }
        editor.putStringSet("selected_positions", stringPositions);
        editor.apply();
    }

    /************************************************************************************
     * Método para eliminar un pokemon seleccionado de la base de datos.
     *
     * @param view
     * @param nameSelected
     * @param borradoPokemon
     * @param position
     **************************************************************************************/
    public void eliminarPokemon(View view, String nameSelected, boolean borradoPokemon, int position) {
        if (borradoPokemon) {
            // quitar la posición seleccionada de la lista de sharedPreferences
            eliminarPosicionDePreferencias(position);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("pokemonsCapturados")
                    .whereEqualTo("name", nameSelected)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("pokemonsCapturados").document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Document with ID " + document.getId() + " successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });
                                }
                            } else {
                                Log.w(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            actualizarCaptured();
        } else {
            Toast.makeText(this, R.string.toast_no_delete, Toast.LENGTH_SHORT).show();
        }
    }

    /************************************************************************************
     * Método para eliminar el fondo gris al eliminar un pokemon seleccionado.
     *
     * @param position
     *************************************************************************************/
    private void eliminarPosicionDePreferencias(int position) {
        // Obtener instancia de SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Recuperar una copia de las posiciones seleccionadas previamente
        Set<String> stringPositions = new HashSet<>(sharedPref.getStringSet("selected_positions", new HashSet<>()));

        // Eliminar la posición seleccionada si existe
        stringPositions.remove(String.valueOf(position));

        // Guardar el conjunto actualizado en SharedPreferences
        editor.putStringSet("selected_positions", stringPositions);
        editor.apply();
    }

    /************************************************************************************
     * Interfaz para controlar las respuestas de las consultas asíncronas
     ************************************************************************************/
    public interface DataCallback {
        // Mmanejamos los datos exitosos
        void onSuccess(ArrayList<PokemonData> data);

        // Manejamos los errores
        void onFailure(Exception e);
    }


    /************************************************************************************
     * Método que separa el String de tipos recibidos y los manda uno a uno a buscar en el
     * recurso String del idioma de la aplicación.
     * @param types
     * @return
     ************************************************************************************/
    public String traducirTipos(String types){
        String tiposTraducidos="";
        String[] tipos = types.split(", ");
        for(int i=0;i<tipos.length;i++){
            String traduccion = buscarTextoEnStringRes(this, tipos[i]);
            if(tiposTraducidos.isEmpty()){
                tiposTraducidos=traduccion;
            }else{
                tiposTraducidos=tiposTraducidos+", "+traduccion;
            }
        }
        return tiposTraducidos;
    }

    /************************************************************************************
     * Método para buscar el texto recibido dentro del atributo name del xml String correspondiente
     * al idioma de la aplicación.
     * @param context
     * @param textoBuscado palabra recibida correspondiente a un tipo del pokemon que buscaremos en las etiquetas names.
     * @return palabra extraida del contenido del recurso correspondiente.
     ************************************************************************************/
    public static String buscarTextoEnStringRes(Context context, String textoBuscado) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        // Obtener todas las claves de strings definidos en R.string
        Field[] fields = R.string.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // Obtener el ID del recurso
                int resId = field.getInt(null);
                // Obtener el valor asociado al recurso
                String texto = resources.getString(resId);
                // Comparar si contiene el texto buscado
                if (field.getName().toLowerCase().contains(textoBuscado.toLowerCase())) {
                    return texto;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null; // No encontrado
    }
}