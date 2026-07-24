package net.midget807.enhancedendfight.datagen;

import net.midget807.enhancedendfight.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ModItems.REGISTERED.forEach((item) -> {
            stonecutterResultFromBase(recipeOutput, RecipeCategory.MISC, item, Items.DIAMOND);
            ModItems.REGISTERED.forEach((item2) -> {
               if (!item.equals(item2)) {
                   stonecutterResultFromBase(recipeOutput, RecipeCategory.MISC, item, item2);
               }
            });
        });
    }
}
