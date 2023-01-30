package presentacio;

import entitats.Response;
import entitats.RowItem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Controlador de la vista 'registres.fxml'. Permet a l'usuari visualitzar,
 * cercar, ordenar i exportar els registres d'un arxiu XML llistats dins una
 * taula.
 *
 * @author Txell Llanas - Creació/Implementació
 */
public class RegistresController implements Initializable {

    private int clauEncriptacio = 0;                                                // Desa la clau d'encriptació que li indica l' usuari
    private ObservableList<RowItem> items;                                          // Llista per desar tots els registres de tipus RowItem recuperats de l'XML importat
    private final Alert alert = new Alert(Alert.AlertType.NONE);                    // Creació-Inicialització 'Alerta d'ajuda'
    private final ButtonType okButton = new ButtonType("Entesos");

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
    private ComboBox<TableColumn<RowItem, ?>> selector_ordenar;
    @FXML
    private CheckBox checkbox_xifrar;
    @FXML
    private RadioButton radioBtn_asc, radioBtn_desc;
    ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private Button btn_exportar, btn_ajuda;
    @FXML
    private TableView<RowItem> llistat;
    @FXML
    private TableColumn<RowItem, String> col_id, col_uuid, col_position, col_address, col_pais, col_any, col_codi, col_homes, col_dones, col_total;
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

        VBox.setVgrow(container, Priority.ALWAYS);                                  // Forçar el responsive de la taula
        btn_ajuda.setTooltip(infoTooltip);

        // RADIOBUTTONS
        radioBtn_asc.setToggleGroup(toggleGroup);                                   // Agrupar radioButtons en un mateix Grup. Així detecten quan des/activar-se
        radioBtn_desc.setToggleGroup(toggleGroup);
        radioBtn_asc.setSelected(true);                                             // Deixar marcat per defecte l'opció ASCENDING

        // OBSERVABLELIST --> Inicialitzar-la
        items = FXCollections.observableArrayList();

        // TABLEVIEW --> Centrar els textes de les columnes + Emplenar la taula amb les dades de l'XML importat
        for (TableColumn col : llistat.getColumns()) {
            col.setStyle("-fx-alignment: CENTER;");
        }
        omplirTaula();

        // BUTTONS --> Deshabilitar botó d'exportar i opció de xifratge si la taula està buida
        btn_exportar.disableProperty().bind(Bindings.isEmpty(llistat.getItems()));
        btn_ajuda.disableProperty().bind(Bindings.isEmpty(llistat.getItems()));
        checkbox_xifrar.disableProperty().bind(Bindings.isEmpty(llistat.getItems()));

        // COLUMNES --> Bloquejar columnes perquè no es puguin reordenar (drag&drop)
        col_any.setReorderable(false);
        col_pais.setReorderable(false);
        col_codi.setReorderable(false);
        col_homes.setReorderable(false);
        col_dones.setReorderable(false);
        col_total.setReorderable(false);

        // COMBOBOX --> Enllaçar l'ordenació del ComboBox amb les columnes del TableView i viceversa
        ordenarRegistresTableView();

        // RADIOBUTTONS --> Enllaçar radioButtons amb l'ordenació de la taula (radiobutton -> columnes )
        toggleGroup.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                    if (toggleGroup.getSelectedToggle() != null) {
                        if (radioBtn_desc.equals(toggleGroup.getSelectedToggle())) {
                            col_any.setSortType(TableColumn.SortType.DESCENDING);
                            col_pais.setSortType(TableColumn.SortType.DESCENDING);
                            col_codi.setSortType(TableColumn.SortType.DESCENDING);
                            col_homes.setSortType(TableColumn.SortType.DESCENDING);
                            col_dones.setSortType(TableColumn.SortType.DESCENDING);
                            col_total.setSortType(TableColumn.SortType.DESCENDING);
                        } else {
                            col_any.setSortType(TableColumn.SortType.ASCENDING);
                            col_pais.setSortType(TableColumn.SortType.ASCENDING);
                            col_codi.setSortType(TableColumn.SortType.ASCENDING);
                            col_homes.setSortType(TableColumn.SortType.ASCENDING);
                            col_dones.setSortType(TableColumn.SortType.ASCENDING);
                            col_total.setSortType(TableColumn.SortType.ASCENDING);
                        }
                    }
                });

        // COMBOBOX: Deixar seleccionat el primer element per defecte
        selector_ordenar.getSelectionModel().select(0);

        // BOTÓ EXPORTAR -> Assignar acció
        btn_exportar.setOnAction(event -> {
            exportarXML();
        });

        // TABLEVIEW --> Actualitzar dades TableView
        llistat.refresh();
    }

    /**
     * Porta a l'apartat 'Importar XML', que permet a l'usuari obrir un nou
     * arxiu XML amb el què treballar.
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
     * informació a exportar quan es marca la casella 'Xifrar'.
     *
     * Si aquesta casella ja estava prèviament marcada i es desmarca, el valor
     * de la clau es reseteja a 0.
     *
     * @param event Acció que afecti al 'Button' (ex: clicar)
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void indicarClauXifrat(ActionEvent event) {

        final int VALOR_MINIM = 1;
        final int VALOR_MAXIM = 25;
        final String regex = "\\b(0?[1-9]|1[0-9]|2[0-5])\\b";                       // Valors entre [1 i 25]

        // 1. Si es marca la casella 'Xifrar'...
        if (checkbox_xifrar.isSelected()) {
            System.out.println(">> Obro dialeg per demanar la clau de xifrat a l'usuari...");

            // Crear diàleg per demanar una clau de xifrat
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

            // validar valor introduït per l'usuari
            boolean correcte = false;
            do {
                if (input.getText() != null && input.getText().length() != 0) // (SÍ) hi ha VALOR
                {
                    if (input.getText().matches(regex)) {                           // (CORRECTE)
                        correcte = true;
                        clauEncriptacio = Integer.parseInt(input.getText());
                        System.out.println(">> (Valor: " + clauEncriptacio + " ). Clau correcta!");
                        dialegClau.close();
                        break;
                    } else {                                                        // (INCORRECTE) -> 'blank' o fora de rang                            
                        input.clear();
                        input.setPromptText("Valor incorrecte! Torni-ho a provar...");
                        System.out.println(">> Valor incorrecte!");
                    }

                    dialegClau.showAndWait();                                       // Mantenir obert el diàleg fins que la clau sigui correcta o l'usuari el tanqui

                } else {                                                            // (NO) hi ha VALOR
                    System.out.println(">> L'usuari cancel·la l'acció: Uncheck 'Xifrar'.");
                    checkbox_xifrar.setSelected(false);
                    clauEncriptacio = 0;
                    break;
                }

            } while (!correcte);

        } else // 2. Si es desmarca la casella 'Xifrar', resetejar el valor de la clau a 0
        {
            checkbox_xifrar.setSelected(false);
            clauEncriptacio = 0;
            System.out.println(">> Anul·lar xifrat. NETEJAR CLAU: " + clauEncriptacio);
        }

        System.out.println(">> (Valor clau d'encriptació: " + clauEncriptacio + " )");

    }

    /**
     * Permet a l'usuari exportar tots els registres del llistat a un fitxer de
     * tipus *.xml.
     *
     * @author Txell Llanas - Creació/Implementació
     */
    @FXML
    private void exportarXML() {

        ArrayList<RowItem> registres = new ArrayList();                             // Instància <RoWItem> per recuperar les dades del llistat                                                        
        Response response = new Response(registres);                                // Contenidor principal (root), conté tots els registres de tipus RowItem
        StringBuilder xmlXifrat = new StringBuilder();                              // Contenidor per desar l'XML xifrat

        System.out.println(">> Ordenant registres...\n" + items.toString());
        System.out.println(">> Ordenant registres... FET!");

        // 2. Desar llistat de dades ordenades
        registres.addAll(items);
        System.out.println("Registres del llistat: " + registres.toString());

        // 3. Serialitzar (de memòria a fitxer XML)
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Desar arxiu");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // Assignar directori inicial 
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml*"));            // Definir tipus d'extensió admesa pel selector d'arxius
        File fitxer = fileChooser.showSaveDialog(btn_exportar.getScene().getWindow());  // Mostra el selector d'arxius i espera que l'usuari desi l'arxiu

        if (fitxer != null) {

            OutputStreamWriter osw;                                             // Contenidor per desar dades des d'un StringBuilder
            String fileName;                                                    // String per validar el nom de l'arxiu a desar

            try {
                // Validar que l'usuari escriu una extensió per l'arxiu i que aquesta sigui correcta
                if (!fitxer.getName().endsWith(".xml")) {
                    if (fitxer.getAbsolutePath().contains(".")) // Si té una extensió incorrecta, canviar-la 
                    {
                        int indexExtensio = fitxer.getAbsolutePath().lastIndexOf('.');
                        fileName = fitxer.getAbsolutePath().substring(0, indexExtensio) + ".xml";
                    } else {                                                    // Si no té extensió, afegir-la
                        fileName = fitxer.getAbsolutePath() + ".xml";
                    }

                    File file = new File(fileName);                             // Crear nou fitxer amb el nou nom de l'arxiu a desar                        
                    osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);  // Crear fitxer contenidor amb codificació UTF_8 per admetre caracters especials que contenen les dades a desar

                } else {
                    osw = new OutputStreamWriter(new FileOutputStream(fitxer), StandardCharsets.UTF_8);  // Crear fitxer contenidor amb codificació UTF_8 per admetre caracters especials que contenen les dades a desar
                }

                JAXBContext context = JAXBContext.newInstance(Response.class);  // context: l'etiqueta contenidora <response>
                Marshaller marshaller = context.createMarshaller();             // Serialitzador (passem d'Objecte ----> XML)
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");      // Exporta amb codificació per caràcters especials
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);         // Exporta sense capçalera

                StringWriter sw = new StringWriter();                           // Crear un objecte StringWriter per poder escriure l'arxiu amb .write()
                marshaller.marshal(response, sw);                               // Serialitzar dades

                xmlXifrat = encriptarCesar(new StringBuilder(sw.toString()), clauEncriptacio);   // Desar dades encriptades 

                // Verificar si cal encriptar el fitxer
                if (clauEncriptacio != 0) {
                    osw.write(xmlXifrat.toString());                            // Escriure dades encriptades convertides a String perquè el mètode .write() les processi correctament
                } else {
                    osw.write(sw.toString());                                   // Escriure dades
                }
                osw.close();                                                    // Tancar fitxer

                Alert alertaExportar = new Alert(Alert.AlertType.NONE);         // Creació-Inicialització 'Alerta exportar arxiu'
                alertaExportar.setAlertType(Alert.AlertType.INFORMATION);       // Mostra un AVÍS si l'arxiu s'ha desat correctament
                alertaExportar.setTitle(("Missatge informatiu").toUpperCase());
                alertaExportar.setHeaderText("Arxiu exportat correctament.");
                alertaExportar.getButtonTypes().setAll(okButton);
                alertaExportar.show();
                System.out.println(">> Arxiu XML desat correctament.");

            } catch (JAXBException e) {                                         // Error en serialitzar l'objecte
                System.out.println(e.toString());
            } catch (IOException e) {                                           // Error en l'escriptura de l'arxiu
                System.out.println(e.toString());
                if (!fitxer.canWrite()) {
                    alert.setAlertType(Alert.AlertType.WARNING);                // Mostra un AVÍS si l'usuari no disposa de permisos per escriure al directori actual
                    alert.setTitle(("Error d'escriptura").toUpperCase());
                    alert.setHeaderText("Accés denegat.");
                    alert.setContentText("No es tenen els permisos necessaris per "
                            + "desar l'arxiu dins el directori seleccionat.\n"
                            + "Posi's en contacte amb l'Administrador.");
                    alert.getButtonTypes().setAll(okButton);
                    alert.show();
                    System.out.println(">> L'usuari no té permisos d'escriptura.");
                }
            }

        } else {                                                                    // Mostra un AVÍS si l'usuari no desa l'arxiu (fitxer == null)
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle(("Missatge informatiu").toUpperCase());
            alert.setHeaderText("Acció cancel·lada.");
            alert.setContentText("No s'ha desat cap arxiu.");
            alert.getButtonTypes().setAll(okButton);
            alert.show();
            System.out.println(">> L'usuari ha CANCEL·LAT l'exportació.");
        }

    }

    /**
     * Mostra a l'usuari un diàleg informatiu.
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
     * Mètode que retorna la columna que està seleccionada en un TableView. Es
     * crida dins el mètode d'ordenació: ordenarRegistresTableView() per canviar
     * el valor del ComboBox amb el nom de la columna seleccionada.
     *
     * @return Objecte ListCell que es troba dins la columna seleccionada
     * @author Txell Llanas - Creació/Implementació
     */
    private ListCell<TableColumn<RowItem, ?>> createListCell() {
        final ListCell<TableColumn<RowItem, ?>> cell = new ListCell<>();
        cell.itemProperty().addListener(new ChangeListener<TableColumn<RowItem, ?>>() {
            @Override
            public void changed(
                    ObservableValue<? extends TableColumn<RowItem, ?>> obs,
                    TableColumn<RowItem, ?> oldColumn, TableColumn<RowItem, ?> newColumn) {
                if (newColumn == null) {
                    cell.setText(null);                                             // No es retorna cap nom
                } else {
                    cell.setText(newColumn.getText());                              // Recuperar nom de la columna
                }
            }
        });
        return cell;
    }

    /**
     * Mètode que omple el TableView amb els registres de l'XML importat.
     *
     * @author Txell Llanas - Creació/Implementació
     */
    private void omplirTaula() {

        // 1. Vincular els atributs de RowItem amb cada columna del TableView:
        // atributs
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_uuid.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        // <etiquetes>
        col_codi.setCellValueFactory(new PropertyValueFactory<>("codiPais"));
        col_pais.setCellValueFactory(new PropertyValueFactory<>("paisDeResidencia"));
        col_any.setCellValueFactory(new PropertyValueFactory<>("any"));
        col_homes.setCellValueFactory(new PropertyValueFactory<>("homes"));
        col_dones.setCellValueFactory(new PropertyValueFactory<>("dones"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));

        // 2. Assignar el valor (nom) a cada columna
        col_any.setText("Any");
        col_codi.setText("Codi de país");
        col_pais.setText("País de residència");
        col_homes.setText("Homes");
        col_dones.setText("Dones");
        col_total.setText("Total");

        // 3. Emplenear taula amb els registres de l'XML: <etiquetes> i atributs
        for (RowItem item : ImportarController.getDades()) {
            items.add(item);
        }

        // 4. Afegir noms columnes al selector
        selector_ordenar.getItems().add(col_any);
        selector_ordenar.getItems().add(col_pais);
        selector_ordenar.getItems().add(col_codi);
        selector_ordenar.getItems().add(col_homes);
        selector_ordenar.getItems().add(col_dones);
        selector_ordenar.getItems().add(col_total);

        // 5. Afegir els registres a la taula
        llistat.setItems(items);

    }

    /**
     * Mètode que enllaça les columnes d'un TableView amb un desplegable
     * (ComboBox) per ordenar els registres d'un llistat.
     *
     * @author Txell Llanas - Creació/Implementació
     */
    private void ordenarRegistresTableView() {

        // A. Ordenació ( selector --> columnes )
        final Callback<ListView<TableColumn<RowItem, ?>>, ListCell<TableColumn<RowItem, ?>>> cellFactory = (ListView<TableColumn<RowItem, ?>> listView) -> createListCell();

        selector_ordenar.setCellFactory(cellFactory);
        selector_ordenar.setButtonCell(createListCell());
        selector_ordenar.valueProperty().addListener(new ChangeListener<TableColumn<RowItem, ?>>() {
            @Override
            public void changed(ObservableValue<? extends TableColumn<RowItem, ?>> obs,
                    TableColumn<RowItem, ?> oldCol, TableColumn<RowItem, ?> newCol) {

                if (newCol != null) {

                    llistat.getSortOrder().setAll(Collections.singletonList(newCol));

                    // Resetejar ordenació i radiobuttons a ASCENDENT
                    newCol.setSortType(TableColumn.SortType.ASCENDING);
                    radioBtn_asc.setSelected(true);

                } else {
                    llistat.getSortOrder().clear();
                }
            }
        });

        // B. Ordenació ( columnes --> selector )
        llistat.getSortOrder().addListener(new ListChangeListener<TableColumn<RowItem, ?>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TableColumn<RowItem, ?>> change) {
                while (change.next()) {
                    if (change.wasAdded()) {

                        selector_ordenar.setValue(llistat.getSortOrder().get(0));

                        // Enllaçar ordenació columnes amb estat radioButtons (columnes --> radiobutton)                        
                        col_codi.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue == TableColumn.SortType.DESCENDING) {
                                radioBtn_desc.setSelected(true);
                            } else {
                                radioBtn_desc.setSelected(false);
                                radioBtn_asc.setSelected(true);
                            }
                        });
                        col_pais.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue == TableColumn.SortType.DESCENDING) {
                                radioBtn_desc.setSelected(true);
                            } else {
                                radioBtn_desc.setSelected(false);
                                radioBtn_asc.setSelected(true);
                            }
                        });
                        col_any.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue == TableColumn.SortType.DESCENDING) {
                                radioBtn_desc.setSelected(true);
                            } else {
                                radioBtn_desc.setSelected(false);
                                radioBtn_asc.setSelected(true);
                            }
                        });
                        col_homes.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue == TableColumn.SortType.DESCENDING) {
                                radioBtn_desc.setSelected(true);
                            } else {
                                radioBtn_desc.setSelected(false);
                                radioBtn_asc.setSelected(true);
                            }
                        });
                        col_dones.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue == TableColumn.SortType.DESCENDING) {
                                radioBtn_desc.setSelected(true);
                            } else {
                                radioBtn_desc.setSelected(false);
                                radioBtn_asc.setSelected(true);
                            }
                        });
                        col_total.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue == TableColumn.SortType.DESCENDING) {
                                radioBtn_desc.setSelected(true);
                            } else {
                                radioBtn_desc.setSelected(false);
                                radioBtn_asc.setSelected(true);
                            }
                        });
                    }
                }
            }
        });

        // COMBOBOX: Mostrar el nom de la columna seleccionada com a primer valor per defecte
        selector_ordenar.getSelectionModel().select(0);
    }

    /**
     * Aquest mètode encripta una cadena utilitzant el xifratge del Cèsar.
     *
     * @param s Cadena a encriptar
     * @param clau Clau per encriptar la cadena
     * @return Cadena encriptada
     * @author Víctor García
     * @author Pablo Morante, Txell Llanas
     */
    public static StringBuilder encriptarCesar(StringBuilder s, int clau) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i))) {
                if ((s.charAt(i) >= 'a' && s.charAt(i) <= 'z') || (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')) {
                    int character = asciiToString(s.charAt(i));
                    char c = stringToAscii((character + clau) % 26);
                    if (Character.isUpperCase(s.charAt(i))) {
                        c = Character.toUpperCase(c);
                    } else {
                        c = Character.toLowerCase(c);
                    }
                    s.setCharAt(i, c);
                }
            } else if (Character.isDigit(s.charAt(i))) {
                s.setCharAt(i, (char) (((s.charAt(i) - '0' + clau) % 10) + '0'));
            }
        }
        return s;
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
