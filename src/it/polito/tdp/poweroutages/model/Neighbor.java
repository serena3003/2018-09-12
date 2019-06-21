package it.polito.tdp.poweroutages.model;

public class Neighbor implements Comparable<Neighbor>{
	
	private Nerc nerc;
	private int peso;
	
	public Neighbor(Nerc nerc, int peso) {
		this.nerc = nerc;
		this.peso = peso;
	}

	public Nerc getNerc() {
		return nerc;
	}

	public int getPeso() {
		return peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nerc == null) ? 0 : nerc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Neighbor other = (Neighbor) obj;
		if (nerc == null) {
			if (other.nerc != null)
				return false;
		} else if (!nerc.equals(other.nerc))
			return false;
		return true;
	}

	@Override
	public int compareTo(Neighbor neig) {
		return -(this.getPeso()-neig.getPeso());
	}

	@Override
	public String toString() {
		return peso + " - " + nerc.getValue();
	}
	
	

}
