package com.roma3.infovideo.utility.lessons;

import com.roma3.infovideo.model.Aula;
import com.roma3.infovideo.model.Corso;
import com.roma3.infovideo.model.Lezione;

import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class LessonsHandler extends DefaultHandler {

	private ArrayList<Aula> aule;
	private Aula aula;
	private ArrayList<Corso> corsi = new ArrayList<Corso>();
	private Corso corso;
	private ArrayList<Lezione> lezioni;
	private Lezione lezione;
	
	private boolean in_corso;
	private boolean in_insegnamento;
	private boolean in_periodoAnnoAccademico;

	private String value;
    private StringBuffer buffer;

	@Override
	public void startElement(
			String nameSpaceURI, 
			String localName, 
			String qName, 
			Attributes atts
			) {

        buffer = new StringBuffer();

        if (localName.equals("listaAuleAsservite")) {
            aule = new ArrayList<Aula>();
        }
        if (localName.equals("aula") && !in_insegnamento) {
            aula = new Aula();
        }
        if (localName.equals("corsoLaurea")) {
            corso = new Corso();
            lezioni = new ArrayList<Lezione>();
            in_corso = true;
        }
        if (localName.equals("insegnamento")) {
            in_insegnamento = true;
            lezione = new Lezione();
        }
        if (localName.equals("periodoAnnoAccademico")) {
            in_periodoAnnoAccademico = true;
        }

    }
	
	public void endElement(
			String uri, 
			String localName, 
			String qName) {

        value = buffer.toString();
        buffer.setLength(0);

        if (localName.equals("aula") && !in_insegnamento) {
            if (value.compareTo("") != 0) {
                aula.setNome(value);
                aule.add(aula);
            }
        } else if (localName.equals("capacita")) {
            aula.setCapacita(Integer.parseInt(value));
        } else if (localName.equals("corsoLaurea")) {
            corso.setLezioni(lezioni);
            corsi.add(corso);
        } else if (localName.equals("insegnamento")) {
            in_insegnamento = false;
            lezioni.add(lezione);
        } else if (localName.equals("denominazione") && in_corso && !in_insegnamento)
            corso.setDenominazione(value);
        else if (localName.equals("denominazione") && in_insegnamento && !in_periodoAnnoAccademico)
            lezione.setNomeLezione(value);

        else if (localName.equals("docente"))
            lezione.setProfessore(value);
        else if (localName.equals("aula") && in_insegnamento)
            lezione.setAula(value);
        else if (localName.equals("giorno"))
            lezione.setGiorno(value);
        else if (localName.equals("orarioInizio"))
            lezione.setDataInizio(value);
        else if (localName.equals("orarioFine"))
            lezione.setDataFine(value);
        else if (localName.equals("periodoAnnoAccademico"))
            in_periodoAnnoAccademico = false;
        else if (localName.equals("insegnamento"))
            in_insegnamento = false;
        else if (localName.equals("in_corso"))
            in_corso = false;

    }
	
	public void characters(char[] ch, int start, int end) {
        buffer.append(new String(ch, start, end));
	}

    public ArrayList<Aula> getAule() {
        return this.aule;
    }

    public ArrayList<String> getStringAule() {
        HashSet<String> set = new HashSet<String>();
        if (aule != null) {
            for (Aula a : aule) {
                set.add(a.getNome().trim() + " (" + a.getCapacita() + ")");
            }
        }
        ArrayList<String> listAule = new ArrayList<String>(set);
        Collections.sort(listAule);
        return listAule;
    }

    // TODO we should save everything we download..
	public ArrayList<Lezione> getLessons(Date date) {
		ArrayList<Lezione> lessons = new ArrayList<Lezione>();
		for(Corso c : corsi) {
			for(Lezione l : c.getLezioni()) {
				String today = getDate(date);
				if(l.getGiorno().equals(today))
					lessons.add(l);
			}
		}
        Collections.sort(lessons);
		return lessons;
	}
	
	private String getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return stringify(year) + "-" + stringify(month) + "-" + stringify(day);
	}
	
	private String stringify(int date) {
		if(date < 10)
			return "0" + String.valueOf(date);
		return String.valueOf(date);
	}

	public ArrayList<Lezione> getAllLessons() {
		ArrayList<Lezione> allLessons = new ArrayList<Lezione>();
		for(Corso c : corsi) {
			allLessons.addAll(c.getLezioni());
		}
		return allLessons;
	}

}
