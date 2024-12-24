package com.example.tarea3;

import java.util.ArrayList;

public class PokemonType {

    private String slot;
    private ArrayList<PokemonTypeData> type;

    public PokemonType(String slot) {
        this.slot = slot;
        this.type = type;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public ArrayList<PokemonTypeData> getType() {
        return type;
    }

    public void setType(ArrayList<PokemonTypeData> type) {
        this.type = type;
    }
}
