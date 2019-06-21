package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();
		model.creaGrafo();
		System.out.println("Grafo creato di " + model.getGrafo().vertexSet().size() + " vertici e " + model.getGrafo().edgeSet().size() + " archi");
	}

}
