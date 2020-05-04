package flyingperson.ecaa.jei;

import flyingperson.ecaa.block.extractor.ExtractorTileEntity;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;

public class RecipeWrapper implements IRecipeWrapper {

    public static final IRecipeWrapperFactory<ExtractorRecipe> FACTORY = (recipe) -> {
        return new RecipeWrapper(recipe);
    };

    public final ExtractorRecipe theRecipe;

    public RecipeWrapper(ExtractorRecipe recipe) {
        this.theRecipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, this.theRecipe.getItem());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int RF = ExtractorTileEntity.POWER_REQUIRED;
        minecraft.fontRenderer.drawString("Takes "+RF+" RF", 0, 0, 0xFFFFFF, true);
        minecraft.fontRenderer.drawString("Dimension: "+theRecipe.getDim(), 0, 10, 0xFFFFFF, true);
    }

}