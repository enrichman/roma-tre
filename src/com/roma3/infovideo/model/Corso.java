package com.roma3.infovideo.model;

import java.util.ArrayList;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class Corso {

    private String denominazione;

    private ArrayList<Lezione> lezioni;

    public Corso() {
        lezioni = new ArrayList<Lezione>();
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public ArrayList<Lezione> getLezioni() {
        return lezioni;
    }

    public void setLezioni(ArrayList<Lezione> lezioni) {
        this.lezioni = lezioni;
    }



}
