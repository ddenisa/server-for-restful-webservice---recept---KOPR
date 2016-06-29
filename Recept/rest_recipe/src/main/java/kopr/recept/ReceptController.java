package kopr.recept;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recepty")
public class ReceptController {

    private  List<Recept> recepty = new CopyOnWriteArrayList<Recept>();
    private  int nextId = 0;

    public ReceptController() {

        List<Surovina> suroviny = new ArrayList<Surovina>();
        suroviny.add(new Surovina("zemiaky", 0.5, "kg"));
        suroviny.add(new Surovina("mlieko", 100, "ml"));
        Recept recept = new Recept(suroviny, "zemiakova kasa", "uvar zemiaky, popuc, pridaj mlieko", "danka");
        recept.setId(++nextId);
        recepty.add(recept);

        suroviny = new ArrayList<Surovina>();
        suroviny.add(new Surovina("paradajka", 3, "ks"));
        suroviny.add(new Surovina("paprika", 1, "ks"));
        suroviny.add(new Surovina("uhorka", 1, "ks"));
        suroviny.add(new Surovina("slany syr", 150, "g"));
        recept = new Recept(suroviny, "zeleninovy salat", "nakrajaj vsetko, zmiesaj", "janka");
        recept.setId(++nextId);
        recepty.add(recept);

        suroviny = new ArrayList<Surovina>();
        suroviny.add(new Surovina("jablko", 10, "kg"));
        suroviny.add(new Surovina("muka", 100, "g"));
        suroviny.add(new Surovina("prasok na pecenie", 1, "ks"));
        recept = new Recept(suroviny, "jablkovy kolac", "zrob cesto pridaj jablko upec", "hanka");
        recept.setId(++nextId);
        recepty.add(recept);
    }

    @RequestMapping("/{id}")
    public Recept get(@PathVariable int id) {
        Recept recept = findById(id);
        if (recept == null) {
            throw new ReceptNotFound();
        }
        return recept;
    }

    @RequestMapping
    public List<Recept> list() {
        return recepty;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public int add(@RequestBody Recept recept) {
//        if (recept == null) {
//            throw new ReceptProblem();
//        }
//        if (recept.getNazov() == null || recept.getAutor() == null || recept.getPostup() == null
//                || recept.getSuroviny() == null) {
//            throw new ReceptLittleProblem();
//        }
        
        recept.setId(++nextId);
        recepty.add(recept);
        return nextId;
    }

    @RequestMapping(value = "/findByIngredients", method = RequestMethod.POST, produces = "application/json")
    public List<Recept> findByIngredients(@RequestBody List<Surovina> suroviny) {
        List<Recept> receptPom = new CopyOnWriteArrayList<Recept>(recepty);
        List<Recept> filteredRecipes = new CopyOnWriteArrayList<Recept>();
        for (Recept recept : receptPom) {
            if (recept.canMake(suroviny)) {
                filteredRecipes.add(recept);
            }
        }
        return filteredRecipes;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteById(@RequestBody int id) {
        synchronized (this) {
            int index = -1;
            for (final Recept recPom : recepty) {
                if (recPom.getId() == id) {
                    index = recepty.indexOf(recPom);
                    break;
                }
            }
            if (index != -1) {
                recepty.remove(index);
            } else {
                throw new ReceptNotFound();
            }
        }
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST, produces = "application/json")
    public void updateById(@RequestBody Recept recept) {

        synchronized (this) {
            int id = recept.getId();
            int index = -1;
            for (final Recept recPom : recepty) {
                if (recPom.getId() == id) {
                    index = recepty.indexOf(recPom);
                    break;
                }
            }
            if (index != -1) {
                recepty.remove(index);
                recepty.add(recept);
            } else {
                throw new ReceptNotFound();
            }
        }
    }

    @RequestMapping(value = "/findByKeyword", method = RequestMethod.POST, produces = "application/json")
    public List<Recept> findByKeyword(@RequestBody String keyword) {
        List<Recept> receptyPom = new CopyOnWriteArrayList<Recept>(recepty);
        List<Recept> result = new ArrayList<Recept>();
        if (keyword.equals("")) {
            return receptyPom;
        }
        for (Recept toFind : receptyPom) {
            if (toFind.getNazov().toLowerCase().contains(keyword.toLowerCase()) || toFind.getAutor().toLowerCase().contains(keyword.toLowerCase()) || toFind.getPostup().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(toFind);
            }
        }
        return result;
    }

    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public Recept findById(int id) {
        for (Recept recept : recepty) {
            if (recept.getId() == id) {
                return recept;
            }
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ReceptNotFound extends RuntimeException {

    }

}
