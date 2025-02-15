package com.example.etlap;

public class Etel {
    private int id;
    private String nev;
    private String kategoria;
    private int ar;

    public Etel(int id, String nev, String kategoria, int ar) {
        this.id = id;
        this.nev = nev;
        this.kategoria = kategoria;
        this.ar = ar;
    }

    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

    public String getKategoria() {
        return kategoria;
    }

    public int getAr() {
        return ar;
    }
}
