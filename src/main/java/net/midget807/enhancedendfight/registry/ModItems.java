package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.item.NodeMarkerItem;
import net.midget807.enhancedendfight.item.PhaseDebuggerItem;
import net.midget807.enhancedendfight.item.PhaseQueryItem;
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

    public static final DeferredItem<PhaseDebuggerItem> ONE_SHOT = registerModItem("one_shot",
            () -> new PhaseDebuggerItem(new Item.Properties(),ModEnderDragonPhases.ONE_SHOT_APPROACH));
    public static final DeferredItem<PhaseDebuggerItem> STUNNED = registerModItem("stunned",
            () -> new PhaseDebuggerItem(new Item.Properties(),ModEnderDragonPhases.STUNNED));
    public static final DeferredItem<PhaseDebuggerItem> TENACITY = registerModItem("tenacity",
            () -> new PhaseDebuggerItem(new Item.Properties(),ModEnderDragonPhases.TENACITY));

    private static<T extends Item> DeferredItem<T> registerModItem(String name, Supplier<T> sup) {
        DeferredItem<T> registered = ITEMS.register(name, sup);
        REGISTERED.add(registered);
        return registered;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
