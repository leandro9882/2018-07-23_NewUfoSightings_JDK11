package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	NewUfoSightingsDAO dao;
	SimpleWeightedGraph<State,DefaultWeightedEdge> grafo;
	Map<String,State> stati;
	
	public Model() {
		dao=new NewUfoSightingsDAO();
		stati=new HashMap<>();
	}
	public List<String> getShape(int anno){
		return dao.loadAllShape(anno);
	}
	public void creaGrafo(int anno,String shape) {
		dao.loadAllStates(stati);
		grafo=new SimpleWeightedGraph<State,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, stati.values());
		//aggiungo gli archi
		for(Adiacenze a:dao.getAdiacenze(anno, shape, stati)) {
			Graphs.addEdge(this.grafo, a.getS1(), a.getS2(), a.getPeso());
		}
	}
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	public List<StatoP> getSommaAdiacenti() {
		List<StatoP> lista=new LinkedList<>();
		for(State s:stati.values()) {
			int peso=0;
			for(State d:Graphs.neighborListOf(grafo, s)) {
				if(this.grafo.getEdge(s, d)!=null) {
				peso+=this.grafo.getEdgeWeight(this.grafo.getEdge(s, d));
				}
			}
			StatoP sp=new StatoP(s,peso);
			lista.add(sp);
		}
	return lista;
	}
	public Map<State,Double> doSimula(int T1,int alpha,int anno,String shape){
		Simulator sim=new Simulator();
		sim.init(grafo, T1, alpha, anno, shape, stati);
		sim.run();
		return sim.getDefcon();
	}
	
}
