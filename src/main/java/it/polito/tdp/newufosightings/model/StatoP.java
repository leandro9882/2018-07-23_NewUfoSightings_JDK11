package it.polito.tdp.newufosightings.model;

public class StatoP {
State s;
int peso;
public StatoP(State s, int peso2) {
	super();
	this.s = s;
	this.peso = peso2;
}
public State getS() {
	return s;
}
public void setS(State s) {
	this.s = s;
}
public int getPeso() {
	return peso;
}

@Override
public String toString() {
	return s + ", peso=" + peso;
}

}
