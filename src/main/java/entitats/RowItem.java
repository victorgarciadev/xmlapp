package entitats;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Classe que defineix les etiquetes filles <row> de l'XML a explotar.
 * Es diu igual que la seva etiqueta mare, però es diferencia d'ella perquè 
 * aquesta conté atributs.
 * 
 * @author Txell Llanas - Creació
 */

public class RowItem {

    // Definir estructura (atributs/etiquetes filles)    
    @XmlAttribute(name = "_id")
    private String id;
    @XmlAttribute(name = "_uuid")
    private String uuid;
    @XmlAttribute(name = "_position")
    private String position;
    @XmlAttribute(name = "_address")
    private String address;
    
    @XmlElement(name = "any")
    private String any;
    @XmlElement(name = "codi_pa_s")
    private String codiPais;
    @XmlElement(name = "pa_s_de_resid_ncia")
    private String paisDeResidencia;
    @XmlElement(name = "homes")
    private String homes;
    @XmlElement(name = "dones")
    private String dones;
    @XmlElement(name = "total")
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

    public RowItem() {
    }
    
    // Getters i setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAny() {
        return any;
    }

    public void setAny(String any) {
        this.any = any;
    }

    public String getCodiPais() {
        return codiPais;
    }

    public void setCodiPais(String codiPais) {
        this.codiPais = codiPais;
    }

    public String getPaisDeResidencia() {
        return paisDeResidencia;
    }

    public void setPaisDeResidencia(String paisDeResidencia) {
        this.paisDeResidencia = paisDeResidencia;
    }

    public String getHomes() {
        return homes;
    }

    public void setHomes(String homes) {
        this.homes = homes;
    }

    public String getDones() {
        return dones;
    }

    public void setDones(String dones) {
        this.dones = dones;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
        
}
