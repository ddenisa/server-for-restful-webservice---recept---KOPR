
package sk.upjs.kopr.rest_recipe;


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
@RequestMapping("/recipes")
public class RecipeController {
    
    private List<Recipe> recipes = new CopyOnWriteArrayList<Recipe>();
    private int id = 0;
    
    public RecipeController() {

        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("zemiaky", 1, "kg"));
        ingredients.add(new Ingredient("muka", 500, "g"));
        ingredients.add(new Ingredient("cesnak", 3, "ks"));
        Recipe recipe = new Recipe(ingredients, "zemiakove placky", "nastruhaj zemiaky, zmiesaj, vypec", "danka");
        recipe.setId(++id);
        recipes.add(recipe);

        ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("zemiaky", 0.5, "kg"));
        ingredients.add(new Ingredient("mlieko", 100, "ml"));
        recipe = new Recipe(ingredients, "zemiakove pyre", "uvar zemiaky, popuc, zmiesaj", "janka");
        recipe.setId(++id);
        recipes.add(recipe);

        ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("paradajka", 3, "ks"));
        ingredients.add(new Ingredient("uhorka", 1, "ks"));
        ingredients.add(new Ingredient("paprika", 1, "ks"));
        ingredients.add(new Ingredient("slany syr", 200, "g"));
        recipe = new Recipe(ingredients, "zeleninovy salat", "nakrajaj vsetko, zmiesaj", "branko");
        recipe.setId(++id);
        recipes.add(recipe);
    }

    @RequestMapping
    public List<Recipe> list() {
        return recipes;
    }
    
    @RequestMapping("/{id}")
    public Recipe get(@PathVariable int id) {
        Recipe recipe = findById(id);
        if (recipe == null) {
            throw new RecipeNotFound();
        }
        return recipe;
    }
    
    @RequestMapping(value = "/findById", method = RequestMethod.POST, produces = "application/json")
    public Recipe findById(int id) {
        for (final Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public int add(@RequestBody Recipe recipe) {
        if (recipe == null) {
            throw new RecipeNotFound();
        }
        if (recipe.getRecipe_name() == null || recipe.getAuthor() == null || recipe.getDirections()== null
                || recipe.getIngredients() == null) {
            throw new RecipeNotFound();
        }
        
        recipe.setId(++id);
        recipes.add(recipe);
        return id;
    }
    
    @RequestMapping(value = "/findByResources", method = RequestMethod.POST, produces = "application/json")
    public List<Recipe> findByResources(@RequestBody List<Ingredient> suroviny) {
        List<Recipe> receptPom = new CopyOnWriteArrayList<Recipe>(recipes);
        List<Recipe> returnRecept = new CopyOnWriteArrayList<Recipe>();
        for (Recipe recept : receptPom) {
            if (recept.canMake(suroviny)) {
                returnRecept.add(recept);
            }
        }
        return returnRecept;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteById(@RequestBody int uid) {
        synchronized (this) {
            int index = -1;
            for (final Recipe recPom : recipes) {
                if (recPom.getId() == uid) {
                    index = recipes.indexOf(recPom);
                    break;
                }
            }
            if (index != -1) {
                recipes.remove(index);
            } else {
                throw new RecipeNotFound();
            }
        }
    }
    
    @RequestMapping(value = "/updateById", method = RequestMethod.POST, produces = "application/json")
    public void updateById(@RequestBody Recipe recept) {

        synchronized (this) {
            int uid = recept.getId();
            int index = -1;
            for (final Recipe recPom : recipes) {
                if (recPom.getId() == uid) {
                    index = recipes.indexOf(recPom);
                    break;
                }
            }
            if (index != -1) {
                recipes.remove(index);
                recipes.add(recept);
            } else {
                throw new RecipeNotFound();
            }
        }
    }
    
    @RequestMapping(value = "/findByName", method = RequestMethod.POST, produces = "application/json")
    public List<Recipe> findByName(@RequestBody String keyword) {
        //System.out.println(find);
        List<Recipe> receptyPom = new CopyOnWriteArrayList<Recipe>(recipes);
        List<Recipe> result = new ArrayList<Recipe>();
        if (keyword.equals("")) {
            return receptyPom;
        }
        for (final Recipe toFind : receptyPom) {
            if (toFind.getRecipe_name().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(toFind);
            }
        }
        return result;
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class RecipeNotFound extends RuntimeException {

    }
}
