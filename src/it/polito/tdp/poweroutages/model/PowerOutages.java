package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class PowerOutages {
	
	private int id;
	private int eventTypeId;
	private int tagId;
	private int areaId;
	private int nercId;
	private int responsibleId;
	private int customers_affected;
	private LocalDateTime date_event_began;
	private LocalDateTime date_event_finished;
	private int demand_loss;
	
	public PowerOutages(int id, int eventId, int tagId, int areaId, int nercId, int responsibleId,
			int customers_affected, LocalDateTime date_event_began, LocalDateTime date_event_finished,
			int demand_loss) {
		super();
		this.id = id;
		this.eventTypeId = eventId;
		this.tagId = tagId;
		this.areaId = areaId;
		this.nercId = nercId;
		this.responsibleId = responsibleId;
		this.customers_affected = customers_affected;
		this.date_event_began = date_event_began;
		this.date_event_finished = date_event_finished;
		this.demand_loss = demand_loss;
	}

	public int getId() {
		return id;
	}

	public int getEventId() {
		return eventTypeId;
	}

	public int getTagId() {
		return tagId;
	}

	public int getAreaId() {
		return areaId;
	}

	public int getNercId() {
		return nercId;
	}

	public int getResponsibleId() {
		return responsibleId;
	}

	public int getCustomers_affected() {
		return customers_affected;
	}

	public LocalDateTime getDate_event_began() {
		return date_event_began;
	}

	public LocalDateTime getDate_event_finished() {
		return date_event_finished;
	}

	public int getDemand_loss() {
		return demand_loss;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		PowerOutages other = (PowerOutages) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
