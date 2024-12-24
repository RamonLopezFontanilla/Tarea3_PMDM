package com.example.tarea3;

public class PokemonData {

    private int positionInRecycler;
    private int numero;
    private String name;
    private String url;
    private boolean captured=false;
    private String image;
    private int height;
    private int weight;
    private String types;

    public PokemonData(boolean captured, int height, String image, String name, int numero, String url, int weight, int positionInRecycler, String types) {
        this.captured = captured;
        this.height = height;
        this.image = image;
        this.name = name;
        this.numero = numero;
        this.url = url;
        this.weight = weight;
        this.types = types;
        this.positionInRecycler= positionInRecycler;
    }

    public int getPositionInRecycler() {
        return positionInRecycler;
    }

    public void setPositionInRecycler(int positionInRecycler) {
        this.positionInRecycler = positionInRecycler;
    }

    public int getHeight() { return height; }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"+getNumero()+".png";
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumero() {
        // Separamos la url en partes usando como patrón separador "/"
        String[] urlPartes =url.split("/");
        // Devolvemos el entero resultante de convertir el penúltimo String extraido de la cadena
        return Integer.parseInt(urlPartes[urlPartes.length-1]);
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
