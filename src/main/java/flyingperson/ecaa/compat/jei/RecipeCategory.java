package flyingperson.ecaa.compat.jei;

import flyingperson.ecaa.ECAA;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class RecipeCategory implements IRecipeCategory<RecipeWrapper> {

    public static final String NAME = "ecaa.extractor";
    private final IDrawable background;

    public RecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(new ResourceLocation(ECAA.MODID, "textures/gui/extractor_jei.png"), 0, 0, 112, 49);
    }

    @Override
    public String getUid() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Dust Collector";
    }

    @Override
    public String getModName() {
        return ECAA.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapper wrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(1, false, 47, 23);
        recipeLayout.getItemStacks().set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0).get(0));
    }
}