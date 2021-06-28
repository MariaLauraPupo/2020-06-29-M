package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;


public class Model {
	SimpleWeightedGraph <Director, DefaultWeightedEdge> grafo;
	public ImdbDAO dao;
	public Map<Integer, Director> idMap;
	
	public Model() {
		idMap = new HashMap<Integer, Director>();
		dao = new ImdbDAO();
		dao.listAllDirectors(idMap);
	}
	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph <Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        //vertici
		Graphs.addAllVertices(grafo, dao.getDirectorByAnno(anno, idMap));
        //archi
		for(Arco a : dao.getArchi( anno, idMap)) {
			if(grafo.containsVertex(a.getD1()) && grafo.containsVertex(a.getD2())) {
				DefaultWeightedEdge e = this.grafo.addEdge(a.getD1(), a.getD2());
				if(e==null) {
					Graphs.addEdgeWithVertices(grafo, a.getD1(), a.getD2());
				}
			}
		}
		System.out.println("vertici " + this.grafo.vertexSet().size());
		System.out.println("archi " + this.grafo.edgeSet().size());

				
		}
	public Set<Director> getVertici(){
	return this.grafo.vertexSet();
	}
	public Set<DefaultWeightedEdge> getArchi(){
		return this.grafo.edgeSet();
	}
	public List<Director> getAdiacenti(Director director) {
		List<Director> adiacenti = new LinkedList<>();
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(director)) {
			adiacenti.add(this.grafo.getEdgeTarget(e));
		}
		return adiacenti;
	}

}
