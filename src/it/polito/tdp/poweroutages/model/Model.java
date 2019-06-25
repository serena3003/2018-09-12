package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {
	
	private PowerOutagesDAO dao;
	private Graph<Nerc, DefaultWeightedEdge> grafo;
	private Map<Integer, Nerc> nercMap;
	private Simulatore sim;
	private List<PowerOutages> poList;
	
	public Model() {
		dao = new PowerOutagesDAO();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		nercMap = new HashMap<>();
		sim = new Simulatore();
		
		List<Nerc> list = dao.loadAllNercs();
		for(Nerc n : list) {
			nercMap.put(n.getId(), n);
		}
		poList = dao.getPowerOutages();
	}
	
	public void creaGrafo() {
		
		//aggiungo i vertici al grafo
		Graphs.addAllVertices(this.grafo, nercMap.values());
		
		//aggiungo gli archi
		for(Nerc n : nercMap.values()) {
			List<Integer> vicini = dao.getVicini(n);
			for(int id : vicini) {
				if(!this.grafo.containsEdge(n, nercMap.get(id)) && !this.grafo.containsEdge(nercMap.get(id), n)) {
					Nerc n1 = nercMap.get(id);
					int peso = dao.getPeso(n1, n1);
					Graphs.addEdgeWithVertices(this.grafo, n, n1, peso);
					//neigSet.add(new Neighbor(n.getId(), n1.getId(), peso));
				}
			}
		}
	}

	public Graph<Nerc, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public Map<Integer, Nerc> getNercMap() {
		return nercMap;
	}
	
	public List<Nerc> getNercList(){
		return dao.loadAllNercs();
	}

	public List<Neighbor> getVicini(Nerc nerc) {
		List<Neighbor> result = new ArrayList<Neighbor>();
		List<Nerc> vicini = Graphs.neighborListOf(this.grafo, nerc); //prendo tutti i vicini del nodo considerato
		for(Nerc n : vicini) {
			DefaultWeightedEdge e = grafo.getEdge(nerc, n); //arco che li collega
			int peso = (int) grafo.getEdgeWeight(e); //prendo il peso
			result.add(new Neighbor(n, peso)); //aggiungo il vicino
		}
		Collections.sort(result);
		return result;
	}

	public void simula(int k) {
		sim.init(k, poList, nercMap,this.grafo);
		sim.run();
		
	}

}
