package presentacio;

import entitats.RowItem;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.WritableImage;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * FXML Controller class. Controla i permet a l'usuari agafar un fitxer XML
 * encriptat o no i guardar-ho en memoria per utilitzar-ho posteriorment
 *
 * @author Izan
 */
public class InformesController implements Initializable {

    @FXML
    private VBox container;
    @FXML
    private MenuBar menu;
    @FXML
    private MenuItem menuitem_importar, menuitem_sortir, menuitem_llistar, menuitem_informes;
    @FXML
    private Menu menu_registres, menu_informes, menu_arxiu;
    @FXML
    private ComboBox<RowItem> selector_ordenar = new ComboBox<RowItem>();
    @FXML
    private Button btnExportarInforme;
    @FXML
    private BarChart<String, Integer> barChartEvoluci??;
    @FXML
    private PieChart cake;
    @FXML
    private Label maxPoblacio, minPoblaci??, anyInici, anyFinal;

    private ArrayList<RowItem> llista = ImportarController.getDades();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // For??o el responsive de la taula
        VBox.setVgrow(container, Priority.ALWAYS);

        fillDropDownList();

        // Importar dades al barChart
        selector_ordenar.setOnAction(event -> {
            dadesBarChart();
        });

        btnExportarInforme.setOnAction(event -> {
            if (selector_ordenar.getSelectionModel().getSelectedItem() != null) {
                exportarDades();
            } else {
                Alert done = new Alert(Alert.AlertType.WARNING);
                done.setTitle("ATENCI??");
                done.setHeaderText("No has seleccionat cap pa??s.");
                ButtonType acceptButton = new ButtonType("Acceptar");
                done.getButtonTypes().setAll(acceptButton);
                done.show();
            }
        });
    }

    /**
     * Porta a l'apartat 'Importar XML', que permet a l'usuari obrir un nou
     * arxiu XML amb el que treballar.
     *
     * @param event Acci?? que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepci?? a mostrar en cas que no es trobi el Layout
     * @author Izan Jimenez - Implementaci??
     */
    @FXML
    private void pantalla_importar(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/importar.fxml"));
        container.getChildren().setAll(view);
    }

    /**
     * Tanca l'aplicaci??.
     *
     * @param event Acci?? que afecti al 'MenuItem' (ex: clicar)
     *
     * @author Izan Jimenez - Implementaci??
     */
    @FXML
    private void tancar_aplicacio(ActionEvent event) {
        System.out.println(">> L'usuari ha tancat l'aplicaci??.");
        ((Stage) container.getScene().getWindow()).close();
    }

    /**
     * Porta a l'apartat 'Llistat de registres', que permet a l'usuari
     * visualitzar, cercar, ordenar i exportar els registres de l'XML carrgeat.
     *
     * @param event Acci?? que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepci?? a mostrar en cas que no es trobi el Layout
     * @author Izan Jimenez - Implementaci??
     */
    @FXML
    private void pantalla_registres(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/registres.fxml"));
        container.getChildren().setAll(view);
    }

    /**
     * Porta a l'apartat 'Informes', que permet a l'usuari visualitzar un
     * conjunt de gr??fiques i estad??stiques de l'XML carrgeat.
     *
     * @param event Acci?? que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepci?? a mostrar en cas que no es trobi el Layout
     * @author Izan Jimenez - Implementaci??
     */
    @FXML
    private void pantalla_informes(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/informes.fxml"));
        container.getChildren().setAll(view);
    }

    @FXML
    private void btnExportarInformePressed(ActionEvent event) {
    }

    /**
     * Emplena el ComboBox amb la llista de pa??sos del XML
     *
     * @author V??ctor Garc??a
     * @author Pablo Morante
     */
    private void fillDropDownList() {
        Collection<RowItem> filtered = llista.stream()
                .collect(Collectors.toMap(
                        RowItem::getPaisDeResidencia, p -> p, (p1, p2) -> p2))
                .values();
        ArrayList<RowItem> filteredList = new ArrayList<>(filtered);
        for (int i = 0; i < filteredList.size(); i++) {
            if (filteredList.get(i).getPaisDeResidencia() == null) {
                filteredList.remove(i);
            }
        }
        Collections.sort(filteredList, Comparator.comparing(RowItem::getPaisDeResidencia));

        ObservableList<RowItem> olc = (FXCollections.observableList(filteredList));
        selector_ordenar.setItems(olc);

        selector_ordenar.setConverter(new StringConverter<RowItem>() {
            @Override
            /**
             * {@inheritDoc}
             */
            public String toString(RowItem rowItem) {
                if (rowItem == null) {
                    return null;
                } else {
                    return rowItem.getPaisDeResidencia();
                }
            }

            @Override
            /**
             * {@inheritDoc}
             */
            public RowItem fromString(String id) {
                return null;
            }
        });
    }

    /**
     * Mostra al BarChart l'evoluci?? de la poblaci?? del pa??s seleccionat durant
     * el temps.
     *
     * @author V??ctor Garc??a
     * @author Pablo Morante
     */
    private void dadesBarChart() {
        ArrayList<RowItem> paisSeleccionat = new ArrayList<>();
        String selectedItem = selector_ordenar.getSelectionModel().getSelectedItem().getPaisDeResidencia();
        paisSeleccionat.addAll(llista.stream().filter(p -> {
            try {
                return p.getPaisDeResidencia().contains(selectedItem);
            } catch (NullPointerException e) {
                return false;
            }
        }).collect(Collectors.toList()));

        barChartEvoluci??.getData().clear();
        XYChart.Series dada1 = new XYChart.Series();
        dada1.setName("Poblaci?? anual");

        int homes = 0;
        int dones = 0;

        int minTotal = 0;
        int anyMin = 0;
        int maxTotal = 0;
        int anyMax = 0;

        RowItem item = Collections.min(paisSeleccionat, Comparator.comparing(a -> a.getAny()));
        anyInici.setText(item.getAny());
        item = Collections.max(paisSeleccionat, Comparator.comparing(a -> a.getAny()));
        anyFinal.setText(item.getAny());

        for (int i = 0; i < paisSeleccionat.size(); i++) {
            dada1.getData().add(new XYChart.Data(paisSeleccionat.get(i).getAny(), Integer.valueOf(paisSeleccionat.get(i).getTotal())));

            homes += Integer.valueOf(paisSeleccionat.get(i).getHomes());
            dones += Integer.valueOf(paisSeleccionat.get(i).getDones());

            int total = Integer.parseInt(paisSeleccionat.get(i).getTotal());
            int any = Integer.parseInt(paisSeleccionat.get(i).getAny());

            if (i == 0) {
                minTotal = total;
                maxTotal = total;
                anyMin = any;
                anyMax = any;
            } else {
                if (minTotal > total) {
                    minTotal = total;
                    anyMin = any;
                }
                if (maxTotal < total) {
                    maxTotal = total;
                    anyMax = any;
                }
            }
        }
        barChartEvoluci??.getData().addAll(dada1);
        barChartEvoluci??.getXAxis().setAnimated(false);
        dadesPieChart(homes, dones);
        maxPoblacio(minTotal, maxTotal, anyMin, anyMax);
    }

    /**
     * Mostra al PieChart el percentatge d'homes i dones en una estad??stica del
     * pa??s seleccionat durant tots el anys.
     *
     * @param homes Nombre d'homes total en els anys que es tenen dades.
     * @param dones Nombre de dones total en els anys que es tenen dades.
     * @author V??ctor Garc??a
     * @author Pablo Morante
     */
    private void dadesPieChart(int homes, int dones) {
        double total = homes + dones;
        double h = ((double) homes / total) * 100;
        double d = ((double) dones / total) * 100;

        System.out.println(h);
        System.out.println(d);
        ObservableList<PieChart.Data> cackeData = FXCollections.observableArrayList(new PieChart.Data("HOMES", h), new PieChart.Data("DONES", d));
        cake.setData(cackeData);
    }

    /**
     * Mostra les dades de poblaci?? m??nima i m??xima en el temps del pa??s
     * seleccionat.
     *
     * @param min Nombre de persones m??nim basat en els anys que es tenen
     * registres (anual).
     * @param max Nombre de persones m??xim basat en els anys que es tenen
     * registres (anual).
     * @param anyMin Any en que es registra el m??nim de poblaci??
     * @param anyMax Any en que es registra el m??xim de poblaci??
     * @author V??ctor Garc??a
     * @author Pablo Morante
     */
    private void maxPoblacio(int min, int max, int anyMin, int anyMax) {

        maxPoblacio.setText(max + " (any " + anyMax + ")");
        minPoblaci??.setText(min + " (any " + anyMin + ")");
    }

    /**
     * Exporta a un fitxer PDF l'informe del pa??s seleccionat per poder desar-ho
     * en local.
     *
     * @author V??ctor Garc??a
     * @author Pablo Morante
     */
    private void exportarDades() {
        String exportDir = System.getProperty("user.home");
        Scene pantalla = container.getScene();
        WritableImage nodeshot = pantalla.snapshot(null);
        File file = new File("chart.png");
        double width = pantalla.getWidth();
        double height = pantalla.getHeight();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
        } catch (IOException ex) {
            System.out.println("Error al crear imatge: " + ex);
        }

        // Obrir finestra 
        String nombreDefecto = "Informe " + selector_ordenar.getSelectionModel().getSelectedItem().getPaisDeResidencia() + ".pdf";
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(nombreDefecto);
        fileChooser.setTitle("Guardar informe");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(container.getScene().getWindow());
        
        if (selectedFile != null) {
            exportDir = selectedFile.getAbsolutePath();
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            PDImageXObject pdimage;
            PDPageContentStream content;
            try {
                pdimage = PDImageXObject.createFromFile("chart.png", doc);
                content = new PDPageContentStream(doc, page);
                content.drawImage(pdimage, 0, 0, (float) width, (float) height);
                content.close();
                PDRectangle rect = new PDRectangle((float) width, (float) height);
                page.setMediaBox(rect);
                doc.addPage(page);
                doc.save(exportDir);
                doc.close();

                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("GUARDAT");
                done.setHeaderText("Informe guardat correctament a la carpeta seleccionada.");

                ButtonType acceptButton = new ButtonType("Acceptar");

                done.getButtonTypes().setAll(acceptButton);
                done.show();

            } catch (IOException ex) {
                System.out.println("Error al crear pdf: " + ex);
            }
        }
        
        file.delete();
    }

}
