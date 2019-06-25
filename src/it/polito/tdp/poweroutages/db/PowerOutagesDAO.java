package it.polito.tdp.poweroutages.db;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;

public class PowerOutagesDAO {
	
	public List<Nerc> loadAllNercs() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	public List<Integer> getVicini(Nerc n) {
		String sql = "SELECT nerc_two " + 
				"FROM nercrelations " + 
				"WHERE nerc_one =?";
		List<Integer> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				nercList.add(res.getInt("nerc_two"));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public int getPeso(Nerc n1, Nerc n2) {
		String sql = "select DISTINCT year(p1.date_event_began), month(p1.date_event_began) " + 
				"FROM poweroutages p1, poweroutages p2 " + 
				"WHERE p1.nerc_id =? AND p2.nerc_id=? AND month(p1.date_event_began)=month(p2.date_event_began) " + 
				"and year(p1.date_event_began)=year(p2.date_event_began)";
		int count= 0;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, n1.getId());
			st.setInt(2, n2.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				count++;
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return count;
	}
	
	public List<PowerOutages> getPowerOutages(){
		String sql = "SELECT * FROM poweroutages ";
		List<PowerOutages> result = new ArrayList<PowerOutages>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new PowerOutages(res.getInt("id"), res.getInt("eventId"), res.getInt("tagId"),
						res.getInt("areaId"), res.getInt("nercId"), res.getInt("responsibleId"), res.getInt("customers_affected"), 
								res.getTimestamp("date_event_began").toLocalDateTime(), res.getTimestamp("date_event_finished").toLocalDateTime(), res.getInt("demand_loss")));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
	
}
