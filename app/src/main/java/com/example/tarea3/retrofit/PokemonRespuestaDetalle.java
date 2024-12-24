package com.example.tarea3.retrofit;

/**
 * Clase que define la consulta a la API para obtener los datos de un pokemon concreto
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class PokemonRespuestaDetalle {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    @SerializedName("positionInRecycler")
    private int positionInRecycler;

    @SerializedName("types")
    private ArrayList<PokemonType> types;

    public ArrayList<PokemonType> getTypes() {
        return types;
    }

    public static class PokemonType {
        @SerializedName("slot")
        private int slot;

        @SerializedName("type")
        private TypeInfo type;

        public TypeInfo getType() {
            return type;
        }

        public static class TypeInfo {
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }
        }
    }
    public int getPositionInRecycler() {
        return positionInRecycler;
    }

    public void setPositionInRecycler(int positionInRecycler) {
        this.positionInRecycler = positionInRecycler;
    }

    private String imagen;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
