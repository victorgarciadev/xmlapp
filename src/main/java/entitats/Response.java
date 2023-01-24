package entitats;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que defineix l'etiqueta arrel <response> de l'XML a explotar.
 *
 * @author Txell Llanas - Creació
 */
@XmlRootElement(name = "response")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Response implements Serializable {

    // Definir estructura (etiquetes)                                                   // Nivell 1, contenidor únic <row>
    private ArrayList<RowItem> paisos = new ArrayList();

    // Constructors    
    public Response(ArrayList<RowItem> paisos) {
        this.paisos = paisos;
    }

    public Response() {
    }

    @XmlElementWrapper(name = "row")
    @XmlElement(name = "row")
    public ArrayList<RowItem> getPaisos() {
        return paisos;
    }

    public void setPaisos(ArrayList<RowItem> paisos) {
        this.paisos = paisos;
    }

}
