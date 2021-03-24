package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;

public class CorsoDAO {

	public List<Corso> getCorsiByPeriodo(Integer periodo){
		String sql = "SELECT * FROM corso WHERE pd = ?"; // ? perchè è un parametro
		
		List<Corso> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			
			PreparedStatement st = conn.prepareStatement(sql); // usando gli preparedStatement evito SQLINJECTION
			
			st.setInt(1, periodo); // 1 indica il primo parametro nella quella query che deve assumere il valore di periodo
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				result.add(c);
			}
			
			rs.close();
			st.close();
			conn.close(); // qs è l'operazione impo delle 3
			
			// è il modello ad usare questo -> Model
			
		}catch(SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		return result;
	}
	

	public Map<Corso, Integer> getIscrittiByPeriodo(Integer periodo){
		String sql = "SELECT c.codins, c.nome, c.crediti, c.pd, COUNT(*) AS tot\r\n"
				+ "FROM corso c, iscrizione i\r\n"
				+ "WHERE c.codins = i.codins AND c.pd = ?\r\n"
				+ "GROUP BY c.codins, c.nome, c.crediti, c.pd"; // ? perchè è un parametro
		
		Map<Corso, Integer> result = new HashMap<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			
			PreparedStatement st = conn.prepareStatement(sql); // usando gli preparedStatement evito SQLINJECTION
			
			st.setInt(1, periodo); // 1 indica il primo parametro nella quella query 
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				Integer n = rs.getInt("tot");
				result.put(c, n);
			}
			
			rs.close();
			st.close();
			conn.close(); // qs è l'operazione impo delle 3
			
			// è il modello ad usare questo -> Model
			
		}catch(SQLException sqle) {
			throw new RuntimeException(sqle);
		}
		
		return result;
	}
}
