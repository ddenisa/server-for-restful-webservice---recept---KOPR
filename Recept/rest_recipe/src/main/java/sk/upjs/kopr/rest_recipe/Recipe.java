package sk.upjs.kopr.rest_recipe;


import java.util.ArrayList;
import java.util.List;

public class Recipe {
    
    private int id = -1;
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();
    private String recipe_name;
    private String directions;
    private String author;
    
    public Recipe(List<Ingredient> ingredients, String recipe_name, String directions, String author) {
        this.ingredients = ingredients;
        this.recipe_name = recipe_name;
        this.directions = directions;
        this.author = author;
    }
    
    public Recipe(int id, List<Ingredient> ingredients, String recipe_name, String directions, String author) {
        this.id = id;
        this.ingredients = ingredients;
        this.recipe_name = recipe_name;
        this.directions = directions;
        this.author = author;
    }
    
    public Recipe() {
        
    }

    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    
    public String getRecipe_name() {
        return recipe_name;
    }

   
    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    
    public String getDirections() {
        return directions;
    }

    
    public void setDirections(String directions) {
        this.directions = directions;
    }

    
    public String getAuthor() {
        return author;
    }

   
    public void setAuthor(String author) {
        this.author = author;
    }
    
    
    public boolean canMake (List<Ingredient> ingredientClient){
        for(Ingredient ing : ingredients) {
            for(int i=0; i<ingredientClient.size(); i++){
                if(ing.getName().toLowerCase().equals(ingredientClient.get(i).getName().toLowerCase())){
                    if(ing.getUnit().toLowerCase().equals(ingredientClient.get(i).getUnit().toLowerCase())) {
                        if(ing.getQuantity() <= ingredientClient.get(i).getQuantity()){
                            break;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } 
        }
        return true;
    }
    
}
