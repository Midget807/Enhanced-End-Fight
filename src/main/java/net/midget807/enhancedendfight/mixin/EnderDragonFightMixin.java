package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.entity.OneShotPhaseCrystal;
import net.midget807.enhancedendfight.util.injector.OneShotPhaseCrystals;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.phys.AABB;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(EndDragonFight.class)
public abstract class EnderDragonFightMixin implements OneShotPhaseCrystals {
    @Unique
    private int oneShotPhaseCrystalsAlive;
    @Unique
    private List<OneShotPhaseCrystal> oneShotPhaseCrystals = new ArrayList<>();
    @Shadow
    private boolean dragonKilled;
    @Shadow
    @Final
    private ServerLevel level;
    @Unique
    private Map<BlockPos, EndCrystal> vanillaEndCrystals = new HashMap<>();

    @Override
    public int getOneShotCrystalsAlive() {
        this.oneShotPhaseCrystalsAlive = oneShotPhaseCrystals.size();
        return this.oneShotPhaseCrystalsAlive;
    }

    @Override
    public List<OneShotPhaseCrystal> getOneShotPhaseCrystals() {
        return oneShotPhaseCrystals;
    }

    @Override
    public Map<BlockPos, EndCrystal> getVanillaEndCrystals() {
        return this.vanillaEndCrystals;
    }

    @Override
    public void updateOneShotPhaseCrystals() {
        List<SpikeFeature.EndSpike> spikes = SpikeFeature.getSpikesForLevel((ServerLevel) level);
        List<OneShotPhaseCrystal> buffer = new ArrayList<>();
        for (SpikeFeature.EndSpike spike : spikes) {
            BlockPos pos = new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ());
            List<OneShotPhaseCrystal> onShotCrystalPresentAtTower = level.getEntitiesOfClass(OneShotPhaseCrystal.class, new AABB(pos).inflate(1));
            buffer.addAll(onShotCrystalPresentAtTower);
        }
        this.oneShotPhaseCrystals = buffer;
        System.out.println("crystalSize: " + this.oneShotPhaseCrystals.size());
    }

    @Inject(method = "updateCrystalCount", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private void enhancedEndFight$mapVanillaCrystals(CallbackInfo ci, @Local SpikeFeature.EndSpike spikefeature$endspike) {
        this.level.getEntitiesOfClass(EndCrystal.class, spikefeature$endspike.getTopBoundingBox()).forEach(endCrystal -> {
            this.vanillaEndCrystals.put(endCrystal.blockPosition(), endCrystal);
        });
    }

    @ModifyExpressionValue(method = "updateDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;getMaxHealth()F"))
    private float enhancedEndFight$getMaxHealthFromBase(float original, @Local(argsOnly = true) EnderDragon dragon) {
        return (float) dragon.getAttributeBaseValue(Attributes.MAX_HEALTH);
    }

    @Inject(method = "updateDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
    private void enhancedEndFight$setTenacityProgress(EnderDragon dragon, CallbackInfo ci) {

    }

    @Inject(method = "updatePlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;addPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void enhancedEndFight$addPlayersToCustomBar(CallbackInfo ci, @Local ServerPlayer serverplayer) {

    }
    @Inject(method = "updatePlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;removePlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void enhancedEndFight$removePlayersToCustomBar(CallbackInfo ci, @Local ServerPlayer serverplayer) {

    }
    @Inject(method = "setDragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
    private void enhancedEndFight$resetCustomBar(EnderDragon dragon, CallbackInfo ci) {

    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void enhancedEndFight$tick(CallbackInfo ci) {
    }
}
