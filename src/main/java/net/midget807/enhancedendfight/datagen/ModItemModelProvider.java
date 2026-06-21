package net.midget807.enhancedendfight.datagen;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EnhancedEndFightMain.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.REGISTERED.forEach(item -> {
            handheldItem(item.get());
        });
    }
}
