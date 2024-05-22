package org.example;

public class Barva {
    private String nazev, hex;
    private int r, g, b;

    public Barva(String nazev, int r, int g, int b, String hex) {
        this.nazev = nazev;
        this.r = r;
        this.g = g;
        this.b = b;
        this.hex = hex;
    }

    public String getNazev() {
        return nazev;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String getHex() {
        return hex;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setHex() {
        this.hex = hex;
    }
}
