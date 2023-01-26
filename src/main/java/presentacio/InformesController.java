/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * FXML Controller class
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
    private BarChart<String, Integer> barChartEvolució;
    @FXML
    private PieChart cake;
    @FXML
    private Label maxPoblacio, minPoblació;

    private ArrayList<RowItem> llista = ImportarController.getDades();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Forço el responsive de la taula
        VBox.setVgrow(container, Priority.ALWAYS);

        fillDropDownList();

        XYChart.Series dada1 = new XYChart.Series();
        dada1.setName("Primera dada");
        //dada1.getData().add(new XYChart.Data("2009", 10000));

        barChartEvolució.getData().addAll(dada1);

        // Importar dades al barChart
        selector_ordenar.setOnAction(event -> {
            dadesBarChart();
        });

        btnExportarInforme.setOnAction(event -> {
            if (selector_ordenar.getSelectionModel().getSelectedItem() != null) {
                exportarDades();
            }
        });
    }

    /**
     * Porta a l'apartat 'Importar XML', que permet a l'usuari obrir un nou
     * arxiu XML amb el que treballar.
     *
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     * @author Izan Jimenez - Implementació
     */
    @FXML
    private void pantalla_importar(ActionEvent event) throws IOException {
        VBox view = FXMLLoader.load(getClass().getResource("/presentacio/importar.fxml"));
        container.getChildren().setAll(view);
    }

    /**
     * Tanca l'aplicació.
     *
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     *
     * @author Izan Jimenez - Implementació
     */
    @FXML
    private void tancar_aplicacio(ActionEvent event) {
        System.out.println(">> L'usuari ha tancat l'aplicació.");
        ((Stage) container.getScene().getWindow()).close();
    }

    /**
     * Porta a l'apartat 'Llistat de registres', que permet a l'usuari
     * visualitzar, cercar, ordenar i exportar els registres de l'XML carrgeat.
     *
     * @param event Acció que afecti al 'MenuItem' (ex: clicar)
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     * @author Izan Jimenez - Implementació
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
     * @author Izan Jimenez - Implementació
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
     * Emplena el ComboBox amb la llista de països del XML
     *
     * @author Víctor García
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
     * Mostra al BarChart l'evolució de la població del país seleccionat durant
     * el temps.
     *
     * @author Víctor García
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

        barChartEvolució.getData().clear();
        XYChart.Series dada1 = new XYChart.Series();
        dada1.setName("Primera dada");

        int homes = 0;
        int dones = 0;

        int minTotal = 0;
        int maxTotal = 0;

        for (int i = 0; i < paisSeleccionat.size(); i++) {
            System.out.println(paisSeleccionat.get(i).getPaisDeResidencia());
            dada1.getData().add(new XYChart.Data(paisSeleccionat.get(i).getAny(), Integer.valueOf(paisSeleccionat.get(i).getTotal())));

            homes += Integer.valueOf(paisSeleccionat.get(i).getHomes());
            dones += Integer.valueOf(paisSeleccionat.get(i).getDones());

            int total = Integer.parseInt(paisSeleccionat.get(i).getTotal());

            if (i == 0) {
                minTotal = total;
                maxTotal = total;
            } else {
                if (minTotal > total) {
                    minTotal = total;
                }
                if (maxTotal < total) {
                    maxTotal = total;
                }
            }
        }
        barChartEvolució.getData().addAll(dada1);
        barChartEvolució.getXAxis().setAnimated(false);
        dadesPieChart(homes, dones);
        maxPoblacio(minTotal, maxTotal);
    }

    /**
     * Mostra al PieChart el percentatge d'homes i dones en una estadística del
     * país seleccionat durant tots el anys.
     *
     * @param homes Nombre d'homes total en els anys que es tenen dades.
     * @param dones Nombre de dones total en els anys que es tenen dades.
     * @author Víctor García
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
     * Mostra les dades de població mínima i màxima en el temps del país
     * seleccionat.
     *
     * @param min Nombre de persones mínim basat en els anys que es tenen
     * registres (anual).
     * @param max Nombre de persones màxim basat en els anys que es tenen
     * registres (anual).
     * @author Víctor García
     * @author Pablo Morante
     */
    private void maxPoblacio(int min, int max) {
        String minim = String.valueOf(min);
        String maxim = String.valueOf(max);

        maxPoblacio.setText(maxim);
        minPoblació.setText(minim);
    }

    /**
     * Exporta a un fitxer les dades del país seleccionat per poder desar-ho en
     * local.
     *
     * @author Víctor García
     * @author Pablo Morante
     */
    private void exportarDades() {
        String userHome = System.getProperty("user.home");
        String paisSeleccionat = selector_ordenar.getSelectionModel().getSelectedItem().getPaisDeResidencia();
        String nomPDF = "estadistica_" + paisSeleccionat + ".pdf";
        String exportDir = userHome + "/" + nomPDF;
        Scene pantalla = container.getScene();
        WritableImage nodeshot = pantalla.snapshot(null);
        File file = new File("chart.png");
        double width = pantalla.getWidth();
        double height = pantalla.getHeight();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
        } catch (IOException e) {

        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        PDImageXObject pdimage;
        PDPageContentStream content;
        try {
            pdimage = PDImageXObject.createFromFile("chart.png", doc);
            content = new PDPageContentStream(doc, page);
            content.drawImage(pdimage, 0, 0, (float)width, (float)height);
            content.close();
            PDRectangle rect = new PDRectangle((float) width, (float) height);
            page.setMediaBox(rect);
            doc.addPage(page);
            doc.save(exportDir);
            doc.close();
        } catch (IOException ex) {
            System.out.println("Error al crear pdf: " + ex);
        }

    }
}
