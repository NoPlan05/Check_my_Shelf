package com.example.imagepro;

public class rowes {
    private int Nummer;
    private String Produkt;
    private int Anzahl;

    public int getNummer() {
        return Nummer;
    }

    public void setNummer(int nummer) {
        Nummer = nummer;
    }

    public String getProdukt() {
        return Produkt;
    }

    public void setProdukt(String produkt) {
        Produkt = produkt;
    }

    public int getAnzahl() {
        return Anzahl;
    }

    public void setAnzahl(int anzahl) {
        Anzahl = anzahl;
    }

    @Override
    public String toString() {
        return "row"+ Nummer +"{" +
                "Produkt='" + Produkt + '\'' +
                ", Anzahl=" + Anzahl +
                '}';
    }
}
