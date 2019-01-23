package com.example.patrick.ediety;

public class Posilek {
    String bialko,wartosc,wegle,tluszcze;


    public  Posilek(){

    }

    public Posilek(String bialko, String wartosc, String wegle, String tluszcze) {
        this.bialko = bialko;
        this.wartosc = wartosc;
        this.wegle = wegle;
        this.tluszcze = tluszcze;
    }

    public String getBialko() {
        return bialko;
    }

    public void setBialko(String bialko) {
        this.bialko = bialko;
    }

    public String getWartosc() {
        return wartosc;
    }

    public void setWartosc(String wartosc) {
        this.wartosc = wartosc;
    }

    public String getWegle() {
        return wegle;
    }

    public void setWegle(String wegle) {
        this.wegle = wegle;
    }

    public String getTluszcze() {
        return tluszcze;
    }

    public void setTluszcze(String tluszcze) {
        this.tluszcze = tluszcze;
    }
}
