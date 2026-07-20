package net.midget807.enhancedendfight.item;

import net.midget807.enhancedendfight.util.BlockPosHighlights;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneShotCrystalSpawnerItem extends SuccessItem {
    public OneShotCrystalSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        List<EnderDragon> dragons = level.getEntitiesOfClass(EnderDragon.class, player.getBoundingBox().inflate(500));
        dragons.forEach(dragon -> {
            EndDragonFight fight = dragon.getDragonFight();
            if (fight != null) {
                if (!level.isClientSide) {
                    List<SpikeFeature.EndSpike> spikes = SpikeFeature.getSpikesForLevel((ServerLevel) level);
                    List<BlockPos> towerCrystalPositions = new ArrayList<>();
                    Map<Integer, BlockPos> oneShotCrystalPositions = new HashMap<>();
                    for (SpikeFeature.EndSpike spike : spikes) {
                        BlockPos pos = new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ());
                        if (player.isShiftKeyDown()) {
                            towerCrystalPositions.remove(pos);
                        } else {
                            towerCrystalPositions.add(pos);
                        }
                    }
                    List<Integer> allowedPositions = new ArrayList<>();
                    for (int i = 0; i < towerCrystalPositions.size(); i++) {
                        allowedPositions.add(i);
                    }
                    //Debug
                    System.out.println("allowed position: " + allowedPositions);

                    for (int i = 0; i < 4; i++) {
                        int random = allowedPositions.get(level.random.nextInt(allowedPositions.size()));
                        //Debug
                        System.out.println("random: " + random);

                        BlockPos pos = towerCrystalPositions.get(random);
                        oneShotCrystalPositions.put(random, pos);
                        allowedPositions.remove((Object) (random));

                        //Debug
                        System.out.println("allowed position: " + allowedPositions);

                        if (allowedPositions.contains(random + 1)) {
                            allowedPositions.remove((Object) (random + 1));
                            //Debug
                            System.out.println("increment allowed");
                        } else if (allowedPositions.contains(random - 1)) {
                            allowedPositions.remove((Object) (random - 1));
                            //Debug
                            System.out.println("decrement allowed");
                        }


                        //Debug
                        System.out.println("allowed position: " + allowedPositions);
                    }
                    for (Map.Entry<Integer, BlockPos> entry : oneShotCrystalPositions.entrySet()) {
                        BlockPosHighlights.add(entry.getValue());
                        List<EndCrystal> crystalPresentAtTower = level.getEntitiesOfClass(EndCrystal.class, new AABB(entry.getValue()));
                        if (!crystalPresentAtTower.isEmpty()) {
                            //todo spawn a custom crystal with the boolean to replace on discard
                        } else {
                            //todo spawn a custom crystal with no replace on discard
                        }
                    }
                }
            }
        });
        return super.use(level, player, usedHand);
    }
}
