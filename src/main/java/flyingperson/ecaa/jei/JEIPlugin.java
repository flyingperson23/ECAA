package flyingperson.ecaa.jei;

import flyingperson.ecaa.Blocks;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new RecipeCategory(helpers.getGuiHelper()));
    }
    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(ExtractorRecipe.class, RecipeWrapper::new, RecipeCategory.NAME);
        registry.addRecipes(ExtractorRecipe.getRecipes(), RecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(Blocks.extractor), RecipeCategory.NAME);
    }
}
