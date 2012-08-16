package com.roma3.infovideo.activities.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.roma3.infovideo.R;
import com.roma3.infovideo.model.Lezione;

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
public class LezioneAdapter extends ArrayAdapter<Lezione> {

    private ArrayList<Lezione> lezioni;

    public LezioneAdapter(Context context, int textViewResourceId, ArrayList<Lezione> lezioni) {
        super(context, textViewResourceId, lezioni);
        this.lezioni = lezioni;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater li = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.row, null);
        }

        Lezione l = this.lezioni.get(position);

        if (l != null) {

            l.setEmptyStrings();

            TextView oraInizio = (TextView) v.findViewById(R.id.row_orarioInizio);
            oraInizio.setText(l.getDataInizioString());

            TextView oraFine = (TextView) v.findViewById(R.id.row_orarioFine);
            oraFine.setText(l.getDataFineString());

            TextView aula = (TextView) v.findViewById(R.id.row_aula);
            aula.setText(l.getAula());

            TextView edificio = (TextView) v.findViewById(R.id.row_edificio);
            edificio.setText(l.getEdificio());

            TextView professore = (TextView) v.findViewById(R.id.row_professore);
            professore.setText(l.getProfessore());

            TextView nomeLezione = (TextView) v.findViewById(R.id.row_nomeLezione);
            nomeLezione.setText(l.getNomeLezione());

        }

        return v;
    }
}
