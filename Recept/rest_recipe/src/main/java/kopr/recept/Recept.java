package kopr.recept;

import java.util.ArrayList;
import java.util.List;

public class Recept {

    int id = -1;
    List<Surovina> suroviny = new ArrayList<Surovina>();
    private String nazov;
    private String postup;
    private String autor;

    public Recept(List<Surovina> suroviny, String nazov, String postup, String autor) {
        this.suroviny = suroviny;
        this.nazov = nazov;
        this.postup = postup;
        this.autor = autor;
    }

    public Recept(int id,List<Surovina> suroviny, String nazov, String postup, String autor) {
        this.id = id;
        this.suroviny = suroviny;
        this.nazov = nazov;
        this.postup = postup;
        this.autor = autor;
    }

    public Recept() {

    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNazov() {
        return this.nazov;
    }

    public List<Surovina> getSuroviny() {
        return this.suroviny;
    }

    public String getPostup() {
        return this.postup;
    }

    public void setSuroviny(List<Surovina> suroviny) {
        this.suroviny = suroviny;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public void setPostup(String postup) {
        this.postup = postup;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    
    public boolean canMake(List<Surovina> ingredientsHad){
      
        List<Surovina> ingredientsNeeded = suroviny;
        List<Surovina> ing = new ArrayList<Surovina>();
        for(int i=0; i<ingredientsHad.size(); i++){
            for(int j=0; j<ingredientsNeeded.size(); j++){
                if(ingredientsHad.get(i).getNazov().toLowerCase().equals(ingredientsNeeded.get(j).getNazov().toLowerCase())){
                    ing.add(ingredientsNeeded.get(j));
                }
            }
        }
        return ing.equals(ingredientsNeeded);
    }
}
