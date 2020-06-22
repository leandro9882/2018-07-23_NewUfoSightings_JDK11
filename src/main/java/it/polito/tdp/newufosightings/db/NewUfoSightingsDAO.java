package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Adiacenze;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> getAvvistamenti(int anno,String shape) {
		String sql = "SELECT *\r\n" + 
				"FROM sighting\r\n" + 
				"WHERE year(DATETIME)=?\r\n" + 
				"AND shape=?\r\n" + 
				"ORDER BY DATETIME asc";
		List<Sighting> list = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public void loadAllStates(Map<String, State> stati) {
		String sql = "SELECT * FROM state";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				stati.put(rs.getString("id"), state);

			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> loadAllShape(int anno) {
		String sql = "SELECT distinct shape\r\n" + "FROM sighting\r\n" + "WHERE year(DATETIME)=?";
		List<String> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String shape = rs.getString("shape");
				result.add(shape);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	public List<Adiacenze> getAdiacenze(int anno,String shape,Map<String,State> idMap){
		String sql="SELECT s1.state,s2.state,COUNT(*) AS peso\r\n" + 
				"FROM neighbor AS n,sighting AS s1,sighting AS s2\r\n" + 
				"WHERE s1.state=n.state1 AND s2.state=n.state2\r\n" + 
				"AND s1.shape=? AND s2.shape=?\r\n" + 
				"AND YEAR(s1.datetime)=? AND YEAR(s2.datetime)=?\r\n" + 
				"AND s1.state<s2.state\r\n" + 
				"GROUP BY s1.state,s2.state";
		List<Adiacenze> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,shape);
			st.setString(2,shape);
			st.setInt(3, anno);
			st.setInt(4, anno);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Adiacenze a=new Adiacenze(idMap.get(rs.getString("s1.state").toUpperCase()),idMap.get(rs.getString("s2.state").toUpperCase()),rs.getDouble("peso"));
				result.add(a);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
}
