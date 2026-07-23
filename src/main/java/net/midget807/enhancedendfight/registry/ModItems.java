package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.item.NodeMarkerItem;
import net.midget807.enhancedendfight.item.OneShotCrystalSpawnerItem;
import net.midget807.enhancedendfight.item.OneShotTargetSpawnerItem;
import net.midget807.enhancedendfight.item.PhaseDebuggerItem;
import net.midget807.enhancedendfight.item.PhaseQueryItem;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EnhancedEndFightMain.MODID);
    public static final List<DeferredItem<? extends Item>> REGISTERED = new ArrayList<>();

    public static final DeferredItem<PhaseQueryItem> PHASE_QUERY = registerModItem("phase_query",
            () -> new PhaseQueryItem(new Item.Properties()));
    public static final DeferredItem<NodeMarkerItem> NODE_MARKER = registerModItem("node_marker",
            () -> new NodeMarkerItem(new Item.Properties()));
    public static final DeferredItem<OneShotCrystalSpawnerItem> ONE_SHOT_CRYSTAL_SPAWNER = registerModItem("one_shot_crystal_spawner",
            () -> new OneShotCrystalSpawnerItem(new Item.Properties()));
    public static final DeferredItem<OneShotTargetSpawnerItem> ONE_SHOT_TARGET_SPAWNER = registerModItem("one_shot_target_spawner",
            () -> new OneShotTargetSpawnerItem(new Item.Properties()));

    public static final DeferredItem<PhaseDebuggerItem> ONE_SHOT = registerModItem("one_shot",
            () -> new PhaseDebuggerItem(new Item.Properties(),ModEnderDragonPhases.ONE_SHOT_APPROACH));
    public static final DeferredItem<PhaseDebuggerItem> STUNNED = registerModItem("stunned",
            () -> new PhaseDebuggerItem(new Item.Properties(), ModEnderDragonPhases.STUNNED));
    public static final DeferredItem<PhaseDebuggerItem> TENACITY = registerModItem("tenacity",
            () -> new PhaseDebuggerItem(new Item.Properties(), ModEnderDragonPhases.TENACITY));
    public static final DeferredItem<PhaseDebuggerItem> DEATH = registerModItem("death",
            () -> new PhaseDebuggerItem(new Item.Properties(), EnderDragonPhase.DYING));
    public static final DeferredItem<PhaseDebuggerItem> PERCH = registerModItem("perch",
            () -> new PhaseDebuggerItem(new Item.Properties(), EnderDragonPhase.LANDING_APPROACH));

    private static<T extends Item> DeferredItem<T> registerModItem(String name, Supplier<T> sup) {
        DeferredItem<T> registered = ITEMS.register(name, sup);
        REGISTERED.add(registered);
        return registered;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
