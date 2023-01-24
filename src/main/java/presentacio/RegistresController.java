package presentacio;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controlador de la vista 'registres.fxml'. Permet a l'usuari visualitzar,
 * ordenar i exportar els registres d'un arxiu XML des d'una UI.
 *
 * @author Txell Llanas - Creació/Implementació
 */
public class RegistresController implements Initializable {
    
    private int clau = 0;

    private final Alert alert = new Alert(Alert.AlertType.NONE);
//    private final ButtonType yesButton = new ButtonType("Xifrar arxiu");
//    private final ButtonType cancelButton = new ButtonType("Cancel·lar");
    private final ButtonType okButton = new ButtonType("Entesos");  // D'acord

    Tooltip infoTooltip = new Tooltip("Ajuda");

    @FXML
    private MenuBar menu;
    @FXML
    private Menu menu_arxiu, menu_registres, menu_informes;
    @FXML
    private MenuItem menuitem_importar, menuitem_sortir, menuitem_llistar, menuitem_informes;
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
        // Forço el responsive de la taula
        VBox.setVgrow(container, Priority.ALWAYS);

        btn_ajuda.setTooltip(infoTooltip);
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
    private void pantalla_registres(ActionEvent event) throws IOException {
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
     * Permet a l'usuari especificar una Clau de xifrat per encriptar la
     * informació a exportar, si es marca la casella 'Xifrar'.
     *
     * Si aquesta casella ja estava marcada i es desmarca, el valor de la clau
     * es reseteja a 0.
     *
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void indicarClauXifrat(ActionEvent event) {

        final int VALOR_MINIM = 1;
        final int VALOR_MAXIM = 25;
        final String regex = "\\b(0?[1-9]|1[0-9]|2[0-5])\\b";                   // Valors entre [1 i 25]

        /**
         * Comprovar l'estat de la casella 'Xifrar'
         */
        if (checkbox_xifrar.isSelected()) // 1. Si es marca la casella 'Xifrar'
        {
            // Crear diàleg per demanar una clau de xifrat
            System.out.println(">> Obro dialeg");
            TextInputDialog dialegClau = new TextInputDialog();
            dialegClau.setTitle(("Introduir clau de xifrat").toUpperCase());
            dialegClau.setHeaderText("Escrigui la clau de xifrat [ Nombre entre " + VALOR_MINIM + " i " + VALOR_MAXIM + " ]");
            dialegClau.getDialogPane().setContentText("Clau: ");

            // Personalitzar botons
            final Button cancelBtn = (Button) dialegClau.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelBtn.setText("Cancel·lar");
            final Button okBtn = (Button) dialegClau.getDialogPane().lookupButton(ButtonType.OK);
            okBtn.setText("Acceptar");

            Optional<String> resultat = dialegClau.showAndWait();
            TextField input = dialegClau.getEditor();

            System.out.println(">> input: " + input.getText());

            // Validar valor introduït per l'usuari
            boolean correcte = false;
            int i = 1;
            do {

                if (input.getText() != null && input.getText().length() != 0) // hi ha VALOR
                {
                    if (input.getText().matches(regex)) {                        // (CORRECTE)
                        correcte = true;
                        clau = Integer.parseInt(input.getText());
                        System.out.println(">> (Valor: " + clau + " ). Clau correcta!");
                        dialegClau.close();
                        break;

                    } else {                                                    // (INCORRECTE) -> 'blank' o fora de rang                            

                        input.clear();
                        input.setPromptText("Valor incorrecte! Torni-ho a provar...");
                        System.out.println(">> Valor incorrecte!");
                    }

                    i++;
                    dialegClau.showAndWait();                                    // mantinc obert el diàleg fins que la clau sigui correcta o l'usuari el tanqui

                } else {                                                        // sense VALOR
                    System.out.println(">> L'usuari cancel·la l'acció: Uncheck 'Xifrar'.");
                    checkbox_xifrar.setSelected(false);
                    clau = 0;
                    break;
                }

            } while (!correcte);

        } else // 2. Si es desmarca la casella 'Xifrar', netejar el valor de la clau
        {
            checkbox_xifrar.setSelected(false);
            clau = 0;
            System.out.println(">> Anul·lar xifrat. NETEJAR CLAU: " + clau);
        }

        System.out.println(">> (Valor: " + clau + " )");

    }

    /**
     * Permet a l'usuari exportar els registres de l'XML actual.
     *
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void exportarXML(ActionEvent event) {

        // 1. Aplicar filtre d'ordenació, si n'hi ha
        System.out.println(">> Ordenant dades... FET!");

        // 2. Exportar les dades a XML
        if (checkbox_xifrar.isSelected()) // A. L'usuari vol exportar les dades xifrades
        {
//            alert.setAlertType(Alert.AlertType.CONFIRMATION);
//            alert.setTitle(("Confirmi una opció").toUpperCase());
//            alert.setHeaderText("Ha seleccionat l'opció: xifrar.\n" +
//                    "Escrigui la clau de xifrat [nombre entre "+ VALOR_MINIM + " i " + VALOR_MAXIM + "].");
//            alert.setContentText(
//                    "Recordi aquest valor ja que li servirà per desxifrar\n"
//                  + "aquest fitxer quan el vulgui obrir de nou dins l'aplicació.");
//            alert.getButtonTypes().setAll(cancelButton, yesButton);

//            if( alert.showAndWait().get() == yesButton )                        // L'usuari accepta l'acció
//            {
            // (SERIALITZACIÓ)
            String dades = "";  // aplicar marshall a la cadena
            System.out.println(">> Serialitzant fitxer a XML... FET!");

            // Encriptem el fitxer
            if (clau != 0) {
                encriptarCesar(dades, clau);
            }

            // Desar arxiu (File chooser?)
            System.out.println(">> Fitxer XML xifrat desat per l'usuari al disc local... FET!");
//                } else
//                {
//                   // mostrar validació per l'input que està buit 
//                }
//            }                                                                   // L'usuari cancel·la l'acció 
//            else 
//            {
//                System.out.println(">> Xifrat cancel·lat per l'usuari...");
//                alert.close();
//            }
//        } else                                                                  // B. L'usuari vol exportar les dades sense xifrar
        }
        // 3. Procés d'exportació (SERIALITZACIÓ)
        System.out.println(">> Serialitzant fitxer a XML... FET!");

        // 4. Desar arxiu (File chooser?)
        System.out.println(">> Fitxer XML desat per l'usuari al disc local... FET!");
    }

    /**
     * Mostra a l'usuari un diàleg amb instruccions d'ajuda.
     *
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void obrirAjuda(ActionEvent event) {

        // Crear Missatge d'alerta
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle(("Missatge informatiu").toUpperCase());
        alert.setHeaderText("Opció per xifrar les dades de l'arxiu a exportar.");
        alert.setContentText("Seleccioni aquesta opció si desitja protegir el"
                + " contingut\nde l'arxiu exportant-lo xifrat.");
        alert.getButtonTypes().setAll(okButton);
        alert.show();
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
        System.out.println(">> L'usuari ha tancat l'aplicació.");
        ((Stage) container.getScene().getWindow()).close();
    }

    /**
     * Aquest mètode encripta una cadena utilitzant el xifratge del Cèsar.
     *
     * @param s Cadena a encriptar
     * @param clau Clau per encriptar la cadena
     * @return Cadena encriptada
     * @author Víctor García
     * @author Pablo Morante
     */
    public static String encriptarCesar(String s, int clau) {
        char[] arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (Character.isLetter(arr[i])) {
                if ((arr[i] >= 'a' && arr[i] <= 'z') || (arr[i] >= 'A' && arr[i] <= 'Z')) {
                    int character = asciiToString(arr[i]);
                    char c = stringToAscii((character + clau) % 26);
                    if (Character.isUpperCase(arr[i])) {
                        c = Character.toUpperCase(c);
                    } else {
                        c = Character.toLowerCase(c);
                    }
                    arr[i] = c;
                }
            } else if (Character.isDigit(arr[i])) {
                arr[i] = (char) (((arr[i] - '0' + clau) % 10) + '0');
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
