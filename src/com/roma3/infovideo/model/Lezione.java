package com.roma3.infovideo.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class Lezione implements Comparable<Lezione> {

    private GregorianCalendar dataInizio;
    private GregorianCalendar dataFine;
    private String giorno;
    private String edificio;
    private String aula;
    private String professore;
    private String nomeLezione;

    public GregorianCalendar getDataInizio() { return this.dataInizio; }
    public String getDataInizioString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.dataInizio.getTime());
    }
    public void setDataInizio(String oraInizio) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date data = sdf.parse(oraInizio);
            GregorianCalendar dataInizio = new GregorianCalendar();
            dataInizio.setTime(data);
            this.dataInizio = dataInizio;
        }
        catch (Exception e) {
            Log.e("LEZIONI ROMA TRE", "Something went wrong while setting this oraInizio [" + oraInizio + "]");
        }
    }

    public GregorianCalendar getDataFine() { return this.dataFine; }
    public String getDataFineString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.dataFine.getTime());
    }
    public void setDataFine(String oraFine) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date data = df.parse(oraFine);
            GregorianCalendar dataFine = new GregorianCalendar();
            dataFine.setTime(data);
            this.dataFine = dataFine;
        }
        catch (Exception e) {
            Log.e("LEZIONI ROMA TRE", "Something went wrong while setting this oraFine [" + oraFine + "]");
        }
    }

    public String getEdificio() { return this.edificio; }
    public void setEdificio(String edificio) { this.edificio = edificio; }

    public String getAula() { return this.aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getProfessore() { return this.professore; }
    public void setProfessore(String professore) { this.professore = professore; }

    public String getNomeLezione() { return this.nomeLezione; }
    public void setNomeLezione(String nomeLezione) { this.nomeLezione = nomeLezione; }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return this.professore +" ["+this.nomeLezione+"] dalle "
                +sdf.format(this.dataInizio.getTime())+ " alle "
                +sdf.format(this.dataFine.getTime())+" in aula "+this.aula;
    }

    public void setEmptyStrings() {
        if(this.edificio==null)
            this.edificio="";
        if(this.aula==null)
            this.aula="";
        if(this.professore==null)
            this.professore="";
        if(this.nomeLezione==null)
            this.nomeLezione="";
    }
    public String getGiorno() {
        return giorno;
    }
    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    @Override
    public int compareTo(Lezione lezione) {
        return this.getDataInizio().compareTo(lezione.getDataInizio());
    }
}
