package entitats;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que defineix l'etiqueta arrel <response> de l'XML a explotar.
 * 
 * @author Txell Llanas - Creació
 */

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response implements Serializable {
    
    // Definir estructura (etiquetes)
    @XmlElement(name = "row")                                                   // Nivell 1, contenidor únic <row>
    private RowContainer rowContainer;
    
    // Constructors    
    public Response(RowContainer rowContainer) {
        this.rowContainer = rowContainer;
    }
    
     public Response() {        
    }
    
    // getters i setters
    public RowContainer getRowContainer() {
        return rowContainer;
    }

    public void setRowContainer(RowContainer rowContainer) {
        this.rowContainer = rowContainer;
    }
    
}
