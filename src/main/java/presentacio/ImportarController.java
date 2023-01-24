package presentacio;

import entitats.Response;
import entitats.RowItem;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Classe que defineix l'objecte Aplicació. Permet configurar la finestra de
 * l'aplicació, carregant la interfície principal i també com mostrar-la.
 *
 * @author Txell Llanas - Implementació
 */
public class ImportarController implements Initializable {

    private File xmlFile;
    
    private static ArrayList<RowItem> temporal = new ArrayList<>();

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
     * Porta a l'apartat 'Importar XML', que permet a l'usuari obrir un nou
     * arxiu XML amb el que treballar.
     *
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void pantalla_importar() throws IOException {
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
    private void pantalla_registres() throws IOException {
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
     * Permet seleccionar un arxiu *.xml dins el Sistema Operatiu i el llista
     * dins una 'listView' connectada a una 'ObservableList'.
     *
     * @author Txell Llanas - Creació
     */
    @FXML
    private void obrirXML(ActionEvent event) {
        importXML();
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
        if (checkbox_desxifrarXML.isSelected()) {
            textfield_clauDesxifrat.setVisible(true);
        } else {
            textfield_clauDesxifrat.setVisible(false);
        }
    }

    /**
     * Permet a l'usuari importar un arxiu l'XML.
     *
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació
     */
    @FXML
    private void importarXML(ActionEvent event) {
        File fitxer;
        JAXBContext context;
        Unmarshaller unmarshaller;

        try {
            context = JAXBContext.newInstance(Response.class);
            unmarshaller = context.createUnmarshaller();
            Response r = (Response) unmarshaller.unmarshal(xmlFile);

            System.out.println(r);
            
            for (RowItem row : r.getPaisos()) {
                System.out.println("CodiPais: " + row.getCodiPais());
                temporal.add(new RowItem(row.getAny(), row.getCodiPais(), row.getPaisDeResidencia(), row.getHomes(), row.getDones(), row.getTotal()));
                
            }

        } catch (JAXBException e) {
            System.out.println(e);
        }

    }
    
    /**
     * Permet retornar la llista
     * 
     * @return 
     */
    public static ArrayList<RowItem> getDades(){
        return temporal;
    }
    

    /**
     * Tanca l'aplicació.
     *
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     *
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void tancar_aplicacio(ActionEvent event) {
        ((Stage) container.getScene().getWindow()).close();
    }

    public File importXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar archivo XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            textfield_arxiuXML.setText(selectedFile.getAbsolutePath());
            xmlFile = selectedFile;
            return selectedFile;
        } else {
            return null;
        }
    }
    
    /**
     * Aquest mètode desencripta una cadena encriptada utilitzant el xifratge
     * del Cèsar.
     *
     * @param s String encriptat
     * @param clau Clau utilitzada per encriptar la cadena original
     * @return Cadena desencriptada
     * @author Víctor García
     * @author Pablo Morante
     */
    public static String desencriptarCesar(String s, int clau) {
        char[] arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (Character.isLetter(arr[i])) {
                if ((arr[i] >= 'a' && arr[i] <= 'z') || (arr[i] >= 'A' && arr[i] <= 'Z')) {
                    int character = asciiToString(arr[i]);
                    char c = stringToAscii((character + 26 - clau % 26) % 26);
                    if (Character.isUpperCase(arr[i])) {
                        c = Character.toUpperCase(c);
                    } else {
                        c = Character.toLowerCase(c);
                    }
                    arr[i] = c;
                }
            } else if (Character.isDigit(arr[i])) {
                arr[i] = (char) (((arr[i] - '0' + 10 - clau % 10) % 10) + '0');
            }
        }
        return String.valueOf(arr);
    }


    /**
     * Aquest mètode converteix un caràcter ASCII a un valor personalitzat.
     *
     * @param ascii Caràcter a convertir
     * @return Valor personalitzat del caràcter
     * @author Víctor García
     * @author Pablo Morante
     */
    public static int asciiToString(char ascii) {
        // Maps 65-90 & 97-122 to 0-51.
        int character;
        if (ascii >= 65 && ascii <= 90) {
            character = ascii - 65;
        } else if (ascii >= 97 && ascii <= 122) {
            character = ascii - 71;
        } else {
            character = ascii;
        }
        return character;
    }

    /**
     * Aquest mètode converteix un valor personalitzat a un caràcter ASCII.
     *
     * @param character Valor personalitzat del caràcter a convertir
     * @return Caràcter ASCII
     * @author Víctor García
     * @author Pablo Morante
     */
    public static char stringToAscii(int character) {
        int ascii;
        if (character >= 0 && character <= 25) {
            ascii = character + 65;
        } else if (character >= 26 && character <= 51) {
            ascii = character + 71;
        } else {
            ascii = character;
        }
        return (char) ascii;
    }
    
}
