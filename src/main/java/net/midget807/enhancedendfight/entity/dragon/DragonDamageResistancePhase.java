package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModDamageTypes;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.midget807.enhancedendfight.util.injector.DamageResistance;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;

public class DragonDamageResistancePhase extends AbstractDragonPassiveTickPhase {
    public static final int MAX_TIME = 400;
    public static final float DAMAGE_THRESHOLD = 200.0f;
    private final ServerBossEvent damageResEvent = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
    private float accumulatedMeleeDmg;
    private float accumulatedRangedDmg;
    private float accumulatedMagicDmg;
    private boolean shouldAccumulateMelee;
    private boolean shouldAccumulateRanged;
    private boolean shouldAccumulateMagic;

    public DragonDamageResistancePhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doServerTick() {
        super.doServerTick();

        if (accumulatedMeleeDmg >= DAMAGE_THRESHOLD) {
            this.shouldAccumulateMelee = false;
            ((DamageResistance) this.dragon).setResistanceType(DamageResistanceTypes.MELEE);
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.PASSIVE_TICKING).setStartDamageResistance(false);
        }
        if (accumulatedRangedDmg >= DAMAGE_THRESHOLD) {
            this.shouldAccumulateRanged = false;
            ((DamageResistance) this.dragon).setResistanceType(DamageResistanceTypes.RANGED);
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.PASSIVE_TICKING).setStartDamageResistance(false);
        }
        if (accumulatedMagicDmg >= DAMAGE_THRESHOLD) {
            this.shouldAccumulateMagic = false;
            ((DamageResistance) this.dragon).setResistanceType(DamageResistanceTypes.MAGIC);
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.PASSIVE_TICKING).setStartDamageResistance(false);
        }

        if (this.timer >= MAX_TIME) {
            if (this.damageResEvent.getProgress() < 1.0f){
                this.reset();
            }
        }
    }

    @Override
    public void begin() {
        this.timer = 0;
        this.accumulatedMeleeDmg = 0;
        this.accumulatedRangedDmg = 0;
        this.accumulatedMagicDmg = 0;
        this.shouldAccumulateMelee = true;
        this.shouldAccumulateRanged = true;
        this.shouldAccumulateMagic = true;
        ((DamageResistance) this.dragon).setResistanceType(DamageResistanceTypes.ALL);
    }

    public void reset() {
        this.timer = 0;
        this.shouldAccumulateMelee = true;
        this.shouldAccumulateRanged = true;
        this.shouldAccumulateMagic = true;
        ((DamageResistance) this.dragon).setResistanceType(DamageResistanceTypes.ALL);
    }

    @Override
    public float onHurt(DamageSource damageSource, float amount) {
        if (damageSource.is(ModDamageTypes.Tags.MELEE) && this.shouldAccumulateMelee) {
            this.accumulatedMeleeDmg += amount;
        }
        if (damageSource.is(ModDamageTypes.Tags.RANGED) && this.shouldAccumulateRanged) {
            this.accumulatedRangedDmg += amount;
        }
        if (damageSource.is(ModDamageTypes.Tags.MAGIC) && this.shouldAccumulateMagic) {
            this.accumulatedMagicDmg += amount;
        }
        return super.onHurt(damageSource, amount);
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.DAMAGE_RESISTANCE;
    }
}
