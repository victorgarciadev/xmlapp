package presentacio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe que defineix l'objecte Aplicació. Permet configurar la finestra de
 * l'aplicació, carregant la interfície principal i també com mostrar-la.
 *
 * @author Txell Llanas - Implementació
 */
public class ImportarController implements Initializable {

    @FXML
    private TextField textfield_arxiuXML, textfield_clauDesxifrat;
    @FXML
    private Button btn_obrir, btn_ajuda, btn_importar;
    @FXML
    private CheckBox checkbox_desxifrarXML;
    @FXML
    private VBox container;
    @FXML
    private MenuBar menu;
    @FXML
    private Menu menu_arxiu, menu_registres, menu_informes;
    @FXML
    private MenuItem menuitem_importar, menuitem_sortir, menuitem_llistar, menuitem_informes;

    /**
     * Inicialitza el controlador.
     * 
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized. 
     */  
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        
        // Forço el responsive de la taula
        VBox.setVgrow(container, Priority.ALWAYS);
    }    

    /**
     * Porta a l'apartat 'Importar XML', que permet a l'usuari obrir un nou arxiu 
     * XML amb el que treballar.
     * 
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void pantalla_importar(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/importar.fxml"));
        container.getChildren().setAll(view);
    }

    /**
     * Porta a l'apartat 'Llistat de registres', que permet a l'usuari 
     * visualitzar, cercar, ordenar i exportar els registres de l'XML carrgeat.
     * 
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void pantalla_registres(ActionEvent event)  throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/registres.fxml"));
        container.getChildren().setAll(view);
    }
    
    /**
     * Porta a l'apartat 'Informes', que permet a l'usuari visualitzar un 
     * conjunt de gràfiques i estadístiques de l'XML carrgeat.
     * 
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void pantalla_informes(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/informes.fxml"));
        container.getChildren().setAll(view);
    }

    /**
     * Permet seleccionar un arxiu *.xml dins el Sistema Operatiu i el
     * llista dins una 'listView' connectada a una 'ObservableList'.
     *
     * @author Txell Llanas - Creació
     */
    @FXML
    private void obrirXML(ActionEvent event) {
    }

    /**
     * Mostra a l'usuari un diàleg amb instruccions d'ajuda.
     * 
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació
     */
    @FXML
    private void obrirAjuda(ActionEvent event) {
    }

    /**
     * Permet a l'usuari especificar una Clau de desxifrat per desencriptar la 
     * informació a importar.
     * 
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació
     */
    @FXML
    private void indicarClauDesxifrat(ActionEvent event) {
    }
     /**
     * Permet a l'usuari importar un arxiu l'XML.
     * 
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació
     */
    @FXML
    private void importarXML(ActionEvent event) {
    }

    /**
     * Tanca l'aplicació.
     * 
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)     * 
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void tancar_aplicacio(ActionEvent event) {
        ((Stage)container.getScene().getWindow()).close();
    }
    
}
