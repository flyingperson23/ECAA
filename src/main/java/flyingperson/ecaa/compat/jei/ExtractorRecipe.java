package flyingperson.ecaa.compat.jei;

import flyingperson.ecaa.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ExtractorRecipe {
    ItemStack item;
    String dim;
    public ExtractorRecipe(ItemStack item, String dim) {
        this.item = item;
        this.dim = dim;
    }
    public ItemStack getItem() {return item;}
    public String getDim() {return dim;}
    public static ArrayList<ExtractorRecipe> getRecipes() {
        ArrayList<ExtractorRecipe> recipes = new ArrayList<ExtractorRecipe>();
        recipes.add(new ExtractorRecipe(new ItemStack(Items.dustAsteroid), "Asteroids"));
        recipes.add(new ExtractorRecipe(new ItemStack(Items.dustMoon), "Moon"));
        recipes.add(new ExtractorRecipe(new ItemStack(Items.dustMars), "Mars"));
        recipes.add(new ExtractorRecipe(new ItemStack(Items.dustVenus), "Venus"));
        return recipes;
    }

}
