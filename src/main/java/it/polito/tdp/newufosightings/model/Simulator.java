package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;
import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {
	Map<State, Double> defcon; // mappa che contiene tutti i defcon degli stati
	Graph<State, DefaultWeightedEdge> grafo;
	int T1;
	int alpha;
	PriorityQueue<Event> queue;
	NewUfoSightingsDAO dao;
	Map<String, State> map;

	public void init(Graph<State, DefaultWeightedEdge> grafo, int T1, int alpha, int anno, String shape,
			Map<String, State> map) {
		this.dao = new NewUfoSightingsDAO();
		this.grafo = grafo;
		this.map = map;
		this.queue = new PriorityQueue<>();
		this.T1 = T1;
		this.alpha = alpha;

		defcon = new HashMap<>();
		for (State s : grafo.vertexSet()) {
			defcon.put(s, 5.0); // all'inizio hanno tutti valori 5
		}

		for (Sighting s : dao.getAvvistamenti(anno, shape)) { // aggiungo tutti gli eventi alla queue
			Event e = new Event(EventType.AVVISTAMENTO, s, s.getDatetime(),null);
			this.queue.add(e);
		}

	}
	public Map<State,Double> getDefcon(){
		return this.defcon;
	} 

	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.print(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getTipo()) {
		case AVVISTAMENTO:
			double def = 0.0;
			State state = this.map.get(e.getA().getState().toUpperCase());
			List<State> decrementati=new LinkedList<>();
			if (state != null) {
				def = this.defcon.get(state);
			}
			if (def-1 >= 1) {
				this.defcon.replace(state, def, def - 1);
			}
			if (alpha >= (int) (Math.random() * 101)) { // CON PROBABILITà ALPHA
				for (State s : Graphs.neighborListOf(grafo, state)) {
					double defVi = this.defcon.get(s);
					if (defVi-0.5 >= 1) {
						this.defcon.replace(s, defVi, defVi - 0.5);
						decrementati.add(s);
					}

				}
			}

			Event e2 = new Event(EventType.CESSATA_ALLERTA, e.getA(), e.getTime().plusDays(T1),decrementati);
			this.queue.add(e2);

			break;
		case CESSATA_ALLERTA:
			State s = this.map.get(e.getA().getState().toUpperCase());
			double defa = this.defcon.get(s);

			if (defa+1 <= 5) {
				this.defcon.replace(s, defa, defa + 1);
			}
			if (e.getDecrem().size()!=0) { // CON PROBABILITà ALPHA
				for (State s1 : e.getDecrem()) {
					double defVi = this.defcon.get(s1);
					if (defVi+0.5<= 5) {
						this.defcon.replace(s1, defVi, defVi + 0.5);
					}

				}

			break;
		}
	
		}}
}
