package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.util.List;

public class Event implements Comparable<Event> {
	public enum EventType {
		AVVISTAMENTO, CESSATA_ALLERTA
	}

	EventType tipo;
	Sighting a;
	LocalDateTime time;
	List<State> decrem;

	public Event(EventType tipo, Sighting a, LocalDateTime time,List<State> decrem) {
		super();
		this.tipo = tipo;
		this.a = a;
		this.time = time;
		this.decrem=decrem;
	}
	
	

	public List<State> getDecrem() {
		return decrem;
	}



	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	public Sighting getA() {
		return a;
	}

	public void setA(Sighting a) {
		this.a = a;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return tipo + ", a=" + a.getState() + ", time=" + time + "\n";
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.getTime());
	}

}
