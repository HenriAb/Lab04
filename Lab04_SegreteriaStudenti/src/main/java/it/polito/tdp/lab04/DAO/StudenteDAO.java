package it.polito.tdp.lab04.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.lab04.model.Corso;
import it.polito.tdp.lab04.model.Studente;

public class StudenteDAO {

	public List<Studente> getTuttiIStudenti(){
		
		final String sql = "SELECT * FROM studente";
		
		List<Studente> studenti = new LinkedList<>();
		
		try {
			
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				
				Integer matricola = rs.getInt("matricola");
				String cognome = rs.getString("cognome");
				String nome = rs.getString("nome");
				String CDS = rs.getString("CDS");
				
				//System.out.println(matricola + " " + cognome + " " + nome + " " + CDS);
				
				Studente s = new Studente(matricola, cognome, nome, CDS);
				studenti.add(s);
			}
			
			conn.close();
			
			return studenti;
		}catch(SQLException sqle) {
			//sqle.printStackTrace();
			throw new RuntimeException();
		}
	}

	public  Studente getStudente(Studente studente) {

		String sql = "SELECT * FROM studente WHERE matricola = ?";
		Studente s = null;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, studente.getMatricola());
			ResultSet rs = st.executeQuery();

			if(rs.next()) {
				Integer matr = rs.getInt("matricola");
				String cognome = rs.getString("cognome");
				String nome = rs.getString("nome");
				String CDS = rs.getString("CDS");
				s = new Studente(matr, cognome, nome, CDS);
				conn.close();
				return s;
			}
			else {
				conn.close();
				return s;
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		}catch(NullPointerException npe) {
			throw new RuntimeException();
		}
		
	}

	public List<Corso> getCorsiByStudente(Integer matricola){
		
		List<Corso> res = new LinkedList<>();
		
		String sql = "SELECT c.codins, c.crediti, c.nome, c.pd "
				+ "FROM corso c, iscrizione i "
				+ "WHERE c.codins = i.codins AND i.matricola = ?";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matricola);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				String codins = rs.getString("codins");
				int crediti = rs.getInt("crediti");
				String nome = rs.getString("nome");
				int pd = rs.getInt("pd");
				
				Corso c = new Corso(codins, crediti, nome, pd);
				res.add(c);
			}
			conn.close();
		}catch(SQLException sqle) {
			throw new RuntimeException();
		}
		
		return res;
	}
	
	public boolean esisteStudente(Integer matricola) {
		
		String sql = "SELECT * "
				+ "FROM studente "
				+ "WHERE matricola = ?";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matricola);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				conn.close();
				return true;
			}
			else {
				conn.close();
				return false;
			}
		}catch(SQLException sqle) {
			throw new RuntimeException();
		}
		
	}
}
