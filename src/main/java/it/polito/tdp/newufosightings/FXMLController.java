package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import it.polito.tdp.newufosightings.model.StatoP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	int anno=Integer.parseInt(this.txtAnno.getText());
    	String shape=this.cmbBoxForma.getValue();
    	model.creaGrafo(anno, shape);
    	this.txtResult.appendText("Grafo creato con:\n#Vertici: "+model.nVertici()+"\n#Archi: "+model.nArchi()+"\n");
    	
    	for(StatoP sp:model.getSommaAdiacenti()) {
    		this.txtResult.appendText(sp+"\n");
    	}
    	
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	int anno=Integer.parseInt(this.txtAnno.getText());
    	this.cmbBoxForma.getItems().addAll(model.getShape(anno));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	int anno=Integer.parseInt(this.txtAnno.getText());
    	String shape=this.cmbBoxForma.getValue();
    	int T1=Integer.parseInt(this.txtT1.getText());
    	int alpha=Integer.parseInt(this.txtAlfa.getText());
    	Map<State,Double> map=model.doSimula(T1, alpha, anno, shape);
    	for(State s:map.keySet()) {
    	this.txtResult.appendText(String.format("Stato: %s, relativo edcon=%f\n", s,map.get(s)));
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
