package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;


public class Model {
	SimpleWeightedGraph <Director, DefaultWeightedEdge> grafo;
	public ImdbDAO dao;
	public Map<Integer, Director> idMap;
	private List<Director> migliore;
	
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
		}
	public Set<Director> getVertici(){
	return this.grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> getArchi(){
		return this.grafo.edgeSet();
	}
	
	public String getAdiacenti(Director director) {
		String stringa = "";

		for(Director d : Graphs.neighborListOf(grafo, director)) {
			stringa = stringa + d.toString() + " " + this.grafo.getEdgeWeight(this.grafo.getEdge(director,d)) + "\n";
		}
		return stringa;
	}
	

	

	public List<Director> trovaPercorso(Director director){
		this.migliore = new LinkedList <Director>();
		List<Director> parziale = new LinkedList<Director>();
		//aggiungo il nodo sorgente 
		parziale.add(director);
		ricorsione(parziale, 0);
		return migliore;
	}
	private void ricorsione(List<Director> parziale, int c) {
		int sommaPesi = 0;
		Director ultimo = parziale.get(parziale.size() -1);
		//ottengo i vicini
		List<Director> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		if(sommaPesi > c) {
			return;
		}
		for(Director d : vicini) {
			if(!parziale.contains(d)) {
			parziale.add(d);
			ricorsione(parziale,c);
			sommaPesi += this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, d));
			parziale.remove(d);
			sommaPesi -= this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, d));
			}
		}
		if(parziale.size() > migliore.size()) {
			this.migliore = new LinkedList<Director>(parziale);
		}
		
	}
 	
	
}
