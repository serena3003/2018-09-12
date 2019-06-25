package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum Tipo_Evento{
		INIZIO_INTERRUZIONE,
		FINE_INTERRUZIONE,
		CANCELLA_PRESTITO
	}

	private Tipo_Evento tipo;
	private LocalDateTime tempo;
	private Nerc nerc;
	private Nerc donatore;
	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;

	public Evento(Tipo_Evento tipo, LocalDateTime tempo, Nerc nerc, Nerc donatore, LocalDateTime dataInizio,
			LocalDateTime dataFine) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
		this.nerc = nerc;
		this.donatore = donatore;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
	}

	public Nerc getNerc() {
		return nerc;
	}

	public Nerc getDonatore() {
		return donatore;
	}

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public LocalDateTime getDataFine() {
		return dataFine;
	}

	public Tipo_Evento getTipo() {
		return tipo;
	}

	public void setTipo(Tipo_Evento tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getTempo() {
		return tempo;
	}

	@Override
	public int compareTo(Evento ev) {
		return this.getTempo().compareTo(ev.getTempo());
	}
	
}
