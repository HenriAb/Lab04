package it.polito.tdp.lab04.model;

import java.util.List;

import it.polito.tdp.lab04.DAO.CorsoDAO;
import it.polito.tdp.lab04.DAO.StudenteDAO;


public class Model {
	
	private CorsoDAO corsoDAO;
	private StudenteDAO studenteDAO;

	private List<Corso> corsi = null;
	private List<Studente> studenti = null;
	
	public Model() {
		this.corsoDAO = new CorsoDAO();
		this.studenteDAO = new StudenteDAO();
	}
	
	public List<Corso> getCorsi(){
		if(this.corsi == null) {
			CorsoDAO dao = new CorsoDAO();
			this.corsi = dao.getTuttiICorsi();
			corsi.add(0, new Corso("", -1, "", -1));			
		}
		return corsi;
	}
	
	public List<Studente> getStudenti(){
		if(this.studenti == null) {
			StudenteDAO dao = new StudenteDAO();
			this.studenti = dao.getTuttiIStudenti();
		}
		return studenti;
	}

	public Studente getStudente(int matricola) {
		
		return studenteDAO.getStudente(new Studente(matricola, null, null, null));
	}
	
	public List<Studente> getStudentiIscrittiAlCorso(Corso corso) {
		return this.corsoDAO.getStudentiIscrittiAlCorso(corso);
	}
	
	public List<Corso> getCorsiByStudente(Integer matricola){
		return this.studenteDAO.getCorsiByStudente(matricola);
	}
	
	public boolean esisteStudente(Integer matricola) {
		return this.studenteDAO.esisteStudente(matricola);
	}
	
	public boolean inscriviStudenteACorso(Studente studente, Corso corso) {
		return this.corsoDAO.inscriviStudenteACorso(studente, corso);
	}
}
