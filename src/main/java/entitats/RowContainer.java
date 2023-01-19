package entitats;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Classe que defineix l'etiqueta contenidora única <row> de l'XML a explotar.
 * S'especifica amb l'anotació @XmlElementWrapper(name = "row") per 
 * diferenciar-la dels elements fills que també es diuen igual.
 * 
 * @author Txell Llanas - Creació
 */

public class RowContainer {

    // Definir estructura (etiquetes)
    @XmlElementWrapper(name = "row")                                            // Nivell 1, contenidor únic <row>
    @XmlElement(name = "row")                                                   // Nivell 2, conjunt d'ítems <row _id="" ...> 
    private List<RowItem> rowItems;

    // Constructors
    public RowContainer(List<RowItem> rowItems) {
        this.rowItems = rowItems;
    }

    public RowContainer() {
    }
    
    // Getters i setters
    public List<RowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(List<RowItem> rowItems) {
        this.rowItems = rowItems;
    }
    
}
