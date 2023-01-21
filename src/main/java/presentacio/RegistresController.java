package presentacio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controlador de la vista 'registres.fxml'. Permet a l'usuari visualitzar, 
 * ordenar i exportar els registres d'un arxiu XML des d'una UI.
 * 
 * @author Txell Llanas - Creació/Implementació
 */
public class RegistresController implements Initializable {
    
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private MenuBar menu;
    @FXML
    private Menu menu_arxiu, menu_registres, menu_informes;
    @FXML
    private MenuItem menuitem_importar, menuitem_sortir, menuitem_llistar, menuitem_informes;;
    @FXML
    private TextField textfield_cercador;
    @FXML
    private ComboBox<?> selector_ordenar;
    @FXML
    private CheckBox checkbox_xifrar;
    @FXML
    private Button btn_exportar, btn_ajuda;
    @FXML
    private TableView<?> llistat;
    @FXML
    private TableColumn<?, ?> col_pais;
    @FXML
    private TableColumn<?, ?> col_any;
    @FXML
    private TableColumn<?, ?> col_codi;
    @FXML
    private TableColumn<?, ?> col_homes;
    @FXML
    private TableColumn<?, ?> col_dones;
    @FXML
    private TableColumn<?, ?> col_total;
    @FXML
    private VBox container;

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
        // TODO
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
        root = FXMLLoader.load(getClass().getResource("/presentacio/importar.fxml"));
        stage = (Stage)(container.getScene().getWindow()); 
        scene = new Scene(root);
        stage.setScene(scene);        
        stage.setTitle("Dades demogràfiques de catalans a l'estranger > Importar XML");
        stage.sizeToScene();
        stage.show();
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
        root = FXMLLoader.load(getClass().getResource("/presentacio/registres.fxml"));
        stage = (Stage)(container.getScene().getWindow()); 
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dades demogràfiques de catalans a l'estranger > Llistat de registres");
        stage.sizeToScene();
        stage.show();
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
        root = FXMLLoader.load(getClass().getResource("/presentacio/informes.fxml"));
        stage = (Stage)(container.getScene().getWindow()); 
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("DEMOGRAFIA A L'EXTRANGER. Informes");
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Permet a l'usuari especificar una Clau de xifrat per encriptar la 
     * informació a exportar.
     * 
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void indicarClauXifrat(ActionEvent event) {
    }

    /**
     * Permet a l'usuari exportar els registres de l'XML actual.
     * 
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void exportarXML(ActionEvent event) {
    }
    
    /**
     * Mostra a l'usuari un diàleg amb instruccions d'ajuda.
     * 
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void obrirAjuda(ActionEvent event) {
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
