package com.roma3.infovideo.model;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class Aula {
	
	private String nome;

    private int capacita;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacita() {
        return capacita;
    }

    public void setCapacita(int capacita) {
        this.capacita = capacita;
    }

    @Override
    public String toString() {
        return "Aula{" +
                "nome='" + nome + '\'' +
                ", capacita='" + capacita + '\'' +
                '}';
    }
}
