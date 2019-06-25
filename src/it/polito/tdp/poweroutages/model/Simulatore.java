package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.poweroutages.model.Evento.Tipo_Evento;

public class Simulatore {
	
	private PriorityQueue<Evento> queue;
	
	//stato del mondo 
	private Graph<Nerc, DefaultWeightedEdge> grafo;
	private List<PowerOutages> poList;
	private Map<Nerc, Set<Nerc>> prestiti;
	
	//parametri
	private int k;
	
	//output
	private int CATASTROFI;
	private Map<Nerc, Long> bonus;
	
	public Simulatore() {
		queue = new PriorityQueue<Evento>();
	}

	public void init(int k, List<PowerOutages> powerOutagesList, Map<Integer, Nerc> nercMap, Graph<Nerc, DefaultWeightedEdge> grafo ) {
		queue.clear();
		this.bonus = new HashMap<Nerc, Long>();
		this.prestiti = new HashMap<Nerc, Set<Nerc>>();
		
		for(Nerc n : nercMap.values()) {
			this.bonus.put(n, Long.valueOf(0));
			this.prestiti.put(n, new HashSet<Nerc>());
		}
		
		this.CATASTROFI = 0;
		this.k = k;
		this.poList = powerOutagesList;
		this.grafo = grafo;
		
		//eventi iniziali
		for(PowerOutages po : poList) { 
			Evento e = new Evento(Evento.Tipo_Evento.INIZIO_INTERRUZIONE, po.getDate_event_began(), nercMap.get(po.getNercId()) , null, po.getDate_event_began(), po.getDate_event_finished());
			queue.add(e);
		}
	}

	public void run() {
		while (!queue.isEmpty()) {
			Evento e = queue.poll();
			switch(e.getTipo()) {
			case INIZIO_INTERRUZIONE:
				Nerc n = e.getNerc();
				//cerco un donatore, altrimenti catastrofe
				Nerc donatore = null;
				//cerco tra i debitori
				if(this.prestiti.get(n).size()>0) {
					//scelgo fra i debitori del nerc
					double min = Long.MAX_VALUE;
					for(Nerc n1 : this.prestiti.get(n)) {
						DefaultWeightedEdge edge = this.grafo.getEdge(n, n1);
						if(this.grafo.getEdgeWeight(edge)< min) { //fra tutti voglio quello con peso minimo
							if(!n.getStaPrestando()) { // solo se il debitore non sta prestando
								donatore = n1;
								min = this.grafo.getEdgeWeight(edge);
							}
						}
					}
				} else {
					//scelgo il vicino con peso minore
					List<Nerc> neighbors = Graphs.neighborListOf(this.grafo, n);
					double min = Long.MAX_VALUE;
					for(Nerc n1 : neighbors) {
						DefaultWeightedEdge edge = this.grafo.getEdge(n, n1);
						if(this.grafo.getEdgeWeight(edge)< min) { //fra tutti voglio quello con peso minimo
							if(!n.getStaPrestando()) { // solo se il debitore non sta prestando
								donatore = n1;
								min = this.grafo.getEdgeWeight(edge);
							}
						}
					}
				}
				if(donatore != null) {
					//donatore inizia a prestare
					donatore.setStaPrestando(true);
					Evento fine = new Evento(Tipo_Evento.FINE_INTERRUZIONE, e.getDataFine(), n, donatore, e.getDataInizio(), e.getDataFine());
					queue.add(fine);
					//aggiungo il nerc ai prestiti del donatore
					this.prestiti.get(donatore).add(n);
					Evento cancella = new Evento(Tipo_Evento.CANCELLA_PRESTITO, e.getDataInizio().plusMonths(k), n, donatore, e.getDataInizio(), e.getDataFine());
					this.queue.add(cancella);
				}else {
					this.CATASTROFI++;
				}
				break;
			case FINE_INTERRUZIONE:
				//assegno il bonus al donatore
				if(e.getDonatore() != null) {
					this.bonus.put(e.getDonatore(), bonus.get(e.getDonatore()) + 
							Duration.between(e.getDataInizio(), e.getDataFine()).toDays());
				}
				//donatore smette di prestare
				e.getDonatore().setStaPrestando(false);
				break;
			case CANCELLA_PRESTITO:
				//dopo k mesi rimuovo il nerc dai prestiti del donatore 
				this.prestiti.remove(e.getDonatore()).remove(e.getNerc());
				break;
			}
		}
	}

}
