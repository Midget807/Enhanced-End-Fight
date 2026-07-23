package net.midget807.enhancedendfight.event;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.client.OneShotTargetModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = EnhancedEndFightMain.MODID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OneShotTargetModel.LAYER_LOCATION, OneShotTargetModel::createBodyLayer);
    }
}
