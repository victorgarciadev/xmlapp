package entitats;

/**
 *
 * @author grupD
 */
public class Demografia {
    
    private int any;
    private String codiPais;
    private String paisResidencia;
    private int homes;
    private int dones;
    private int total;

    public Demografia() {
    }

    public Demografia(int any, String codiPais, String paisResidencia, int homes, int dones, int total) {
        this.any = any;
        this.codiPais = codiPais;
        this.paisResidencia = paisResidencia;
        this.homes = homes;
        this.dones = dones;
        this.total = total;
    }
    
    public int getAny() {
        return any;
    }

    public void setAny(int any) {
        this.any = any;
    }

    public String getCodiPais() {
        return codiPais;
    }

    public void setCodiPais(String codiPais) {
        this.codiPais = codiPais;
    }

    public String getPaisResidencia() {
        return paisResidencia;
    }

    public void setPaisResidencia(String paisResidencia) {
        this.paisResidencia = paisResidencia;
    }

    public int getHomes() {
        return homes;
    }

    public void setHomes(int homes) {
        this.homes = homes;
    }

    public int getDones() {
        return dones;
    }

    public void setDones(int dones) {
        this.dones = dones;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
}
