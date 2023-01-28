package entitats;

import java.util.Comparator;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

/**
 * Classe comparadora personalitzada per ordenar els registres d'un llistat 
 * abans de ser exportats a un fitxer XML
 * .
 * S'aplica als radiobuttons 'ASC' i 'DESC' de l'arxiu registresController.java.
 * 
 * @author Txell llanas
 */
public class ComparadorRowItems implements Comparator<RowItem> {

    private  ComboBox<TableColumn<RowItem, ?>> selector_ordenar;
    
    // Constructor
    public ComparadorRowItems(ComboBox<TableColumn<RowItem, ?>> selector_ordenar) {
        this.selector_ordenar = selector_ordenar;
    }
    
    // Criteri comparatiu -> Nom de la columna indicat al ComboBox
    @Override
    public int compare(RowItem o1, RowItem o2) {
        TableColumn<RowItem, ?> selected = selector_ordenar.getValue();
        switch (selected.getText()) {
            case "Any":
                return o1.getAny().compareTo(o2.getAny());
            case "Codi de país":
                return o1.getCodiPais().compareTo(o2.getCodiPais());
            case "Pais de residencia":
                return o1.getPaisDeResidencia().compareTo(o2.getPaisDeResidencia());
            case "Homes":
                return o1.getHomes().compareTo(o2.getHomes());
            case "Dones":
                return o1.getDones().compareTo(o2.getDones());
            case "Total":
                return o1.getTotal().compareTo(o2.getTotal());
            default:
                return 0;
        }
    }

}
