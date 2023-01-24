package entitats;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe que defineix les etiquetes filles <row> de l'XML a explotar. Es diu
 * igual que la seva etiqueta mare, però es diferencia d'ella perquè aquesta
 * conté atributs.
 *
 * @author Txell Llanas - Creació
 */
@XmlRootElement(name = "row")
public class RowItem {

    // Definir estructura (atributs/etiquetes filles)    
    private String id;
    private String uuid;
    private String position;
    private String address;

    private String any;
    private String codiPais;
    private String paisDeResidencia;
    private String homes;
    private String dones;
    private String total;

    // Constructors
    public RowItem(String id, String uuid, String position, String address, String any, String codiPais, String paisDeResidencia, String homes, String dones, String total) {
        this.id = id;
        this.uuid = uuid;
        this.position = position;
        this.address = address;
        this.any = any;
        this.codiPais = codiPais;
        this.paisDeResidencia = paisDeResidencia;
        this.homes = homes;
        this.dones = dones;
        this.total = total;
    }
    
    public RowItem(String any, String codiPais, String paisDeResidencia, String homes, String dones, String total) {
        this.any = any;
        this.codiPais = codiPais;
        this.paisDeResidencia = paisDeResidencia;
        this.homes = homes;
        this.dones = dones;
        this.total = total;
    }

    public RowItem() {
    }

    // Getters i setters
    @XmlAttribute(name = "_id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "_uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @XmlAttribute(name = "_position")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @XmlAttribute(name = "_address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlElement(name = "any")
    public String getAny() {
        return any;
    }

    public void setAny(String any) {
        this.any = any;
    }

    @XmlElement(name = "codi_pa_s")
    public String getCodiPais() {
        return codiPais;
    }

    public void setCodiPais(String codiPais) {
        this.codiPais = codiPais;
    }

    @XmlElement(name = "pa_s_de_resid_ncia")
    public String getPaisDeResidencia() {
        return paisDeResidencia;
    }

    public void setPaisDeResidencia(String paisDeResidencia) {
        this.paisDeResidencia = paisDeResidencia;
    }

    @XmlElement(name = "homes")
    public String getHomes() {
        return homes;
    }

    public void setHomes(String homes) {
        this.homes = homes;
    }

    @XmlElement(name = "dones")
    public String getDones() {
        return dones;
    }

    public void setDones(String dones) {
        this.dones = dones;
    }

    @XmlElement(name = "total")
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
