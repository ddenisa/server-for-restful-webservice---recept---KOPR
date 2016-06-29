
package kopr.recept;


public class Surovina {

   
    private String nazov;
    private double mnozstvo;
    private String jednotka;

    public Surovina(String nazov, double mnozstvo, String jednotka) {
        this.nazov = nazov;
        this.mnozstvo = mnozstvo;
        this.jednotka = jednotka;
    }

    public String print() {
        return "Nazov suroviny: " + nazov + "; Mnozstvo: " + mnozstvo + "; " + jednotka + " ";
    }
    
    public Surovina(){
        
    }
    
    public String getNazov(){
        return this.nazov;
    }

    public double getMnozstvo(){
        return this.mnozstvo;
    }
    
    public String getJednotka(){
        return this.jednotka;
    }
    
    public void setJednotka(String jednotka){
        this.jednotka=jednotka;
    }
    
    public void setNazov(String nazov){
        this.nazov = nazov;
    }
    
    public void setMnozstvo(double mnozstvo){
        this.mnozstvo = mnozstvo;
    }

}
