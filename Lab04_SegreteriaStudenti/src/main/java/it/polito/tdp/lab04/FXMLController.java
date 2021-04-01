package it.polito.tdp.lab04;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.lab04.DAO.ConnectDB;
import it.polito.tdp.lab04.model.Corso;
import it.polito.tdp.lab04.model.Model;
import it.polito.tdp.lab04.model.Studente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Corso> cbScelta;

    @FXML
    private Button btnAutocomplete;
    
    @FXML
    private HBox layout;

    @FXML
    private TextField txtMatricola;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCognome;

    @FXML
    private HBox layoutBottoni;

    @FXML
    private TextArea txtOutput;

    @FXML
    void DoCercaIscrittiCorso(ActionEvent event) {

    	Corso corso = this.cbScelta.getValue();
    	if(corso == null || corso.equals(new Corso("", -1, "", -1))) {
    		this.txtOutput.setText("Errore non hai selezionato il corso");
    		return;
    	}
    	
    	List<Studente> res = this.model.getStudentiIscrittiAlCorso(corso);
    	StringBuilder sb = new StringBuilder();
    	for(Studente s : res) {
    		sb.append(String.format("%-6d ", s.getMatricola())); 
    		sb.append(String.format("%-15s ", s.getCognome())); 
    		sb.append(String.format("%-15s ", s.getNome())); 
    		sb.append(String.format("%-6s\n", s.getCDS())); 
    	}
    	this.txtOutput.setText(sb.toString());
    	return;
    }

    @FXML
    void doCercaCorsi(ActionEvent event) {

    	// data una matricola cerca i corsi a cui è iscritto senno errore
    	List<Corso> res = new ArrayList<>();
    	
    	String matricolaS = this.txtMatricola.getText();
    	int matricola;
    	
    	if(matricolaS == null) {
    		this.txtOutput.setText("Errore! Non hai inserito la maticola");
    		return;
    	}
    	
    	try {
    		matricola = Integer.parseInt(matricolaS);
    		
    		StringBuilder sb = new StringBuilder();
    		res = this.model.getCorsiByStudente(matricola);
    		
    		// distinguere caso di matricola non presente nel DB
    		if(!this.model.esisteStudente(matricola)) {
    			this.txtOutput.setText("Errore! Studente non presente nel db");
    			return;
    		}
    		
    		if(res.isEmpty()) {
    			// lo studente non ha corsi che segue
    			this.txtOutput.setText("Lo studente non ha corsi");
    			return;
    		}
    		
    		for(Corso c : res) {
    			sb.append(String.format("%-7s ", c.getCodins()));
    			sb.append(String.format("%-2d ", c.getCrediti()));
    			sb.append(String.format("%-30s ", c.getNome()));
    			sb.append(String.format("%-2d\n", c.getPd()));
    		}
    		this.txtOutput.setText(sb.toString());
    		
    	}catch(NumberFormatException nfe) {
    		this.txtOutput.setText("Errore! Devi inserire solo caratteri numerici");
    		return;
    	}
    	
    	return;
    }

    @FXML
    void doIscrivi(ActionEvent event) {

    	//metodo che selezionato un corso e uno studente -> iscrive -> lo inserisce nel DB
    	
    	// selezionato un corso e selezionata una matricola -> dice se se è iscritto o meno a quel corso
    	Corso c = this.cbScelta.getValue();
    	String matr = this.txtMatricola.getText();
    	int matricola;
    	Studente s = null;
    	
    	String sql = "INSERT INTO 'iscrizione' ('matricola', 'codins') VALUES (?, ?)";
    	
    	if( c== null){
    		this.txtOutput.setText("Errore! Devi selezionare un corso");
    		return;
    	}
    	
    	if(matr.equals("")) {
    		this.txtOutput.setText("Errore! devi inserire una matricola");
    		return;
    	}
    	
    	try {
    		matricola = Integer.parseInt(matr);
    		
    		s = this.model.getStudente(matricola);
    		if(s == null) {
    			this.txtOutput.setText("Errore! Studente non presente nel DB");
    			return;
    		}
    		
//    		List<Studente> stud = this.model.getStudentiIscrittiAlCorso(c);
//    		for(Studente si : stud) {
//    			if(s.equals(s)) {
//    				// esiste
//    				this.txtOutput.setText("Studente già iscritto al corso");
//    				return;
//    			}
//    		}
    		
//    		if(!stud.contains(s)) {
//    			this.txtOutput.setText("Studente non iscritto al corso");
//    			return;
//    		}
    		
    		if(this.model.inscriviStudenteACorso(s, c)) {
    			this.txtOutput.setText("Studente iscritto al corso in questione");
    			this.txtOutput.appendText("\n" + s + "\n" + c);
    			return;
    		}
    		else {
    			this.txtOutput.setText("Studente già iscritto al corso in questione");
    			this.txtOutput.appendText("\n" + s + "\n" + c);
    			return;
    		}
    		
    	}catch(NumberFormatException nfe) {
    		this.txtOutput.setText("Errore! Devi inserire solo caratteri numerici");
    	}
    }

    @FXML
    void doAutocomplete(ActionEvent event) {
    	
    	String matricolaS = this.txtMatricola.getText();
    	int matricola;
    	
    	if(matricolaS == null || matricolaS.equals("")) {
    		this.txtOutput.setText("Errore! Non hai inserito la matricola");
    		return;
    	}
    	
    	try {
    		matricola = Integer.parseInt(matricolaS);
    		
    		if(this.model.getStudente(matricola) == null) {
    			this.txtOutput.setText("Matricola non presente nel DB");
    			return;
    		}
    		else {
    			this.txtCognome.setText(this.model.getStudente(matricola).getCognome());
    			this.txtNome.setText(this.model.getStudente(matricola).getNome());
    			return;
    		}
    		
    		
    	}catch(NumberFormatException nfe) {
    		this.txtOutput.setText("Errore! La matricola deve contenere solo numeri");
    		return;
    	}
    }

	private void ripulisci() {
		
		this.txtCognome.clear();
		this.txtMatricola.clear();
		this.txtNome.clear();
		this.txtOutput.clear();
		this.cbScelta.setValue(null);
	}

	@FXML
    void doReset(ActionEvent event) {

    	//this.cbScelta.setValue(null);
    	this.cbScelta.getSelectionModel().clearSelection();
    	this.txtMatricola.setText(null);
    	this.txtCognome.setText(null);
    	this.txtNome.setText(null);
    	this.txtOutput.setText(null);
    	
    }

    @FXML
    void doScelta(ActionEvent event) {

    }
    
    public void setModel(Model model) {
    	this.model = model;

    	this.cbScelta.getItems().addAll(this.model.getCorsi());
    	
    	this.txtOutput.setStyle("-fx-font-family: moospace");
    }

    @FXML
    void initialize() {
        assert cbScelta != null : "fx:id=\"cbScelta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert layout != null : "fx:id=\"layout\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMatricola != null : "fx:id=\"txtMatricola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCognome != null : "fx:id=\"txtCognome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert layoutBottoni != null : "fx:id=\"layoutBottoni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtOutput != null : "fx:id=\"txtOutput\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAutocomplete != null : "fx:id=\"btnAutocomplete\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}
