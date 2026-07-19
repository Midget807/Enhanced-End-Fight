package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;

public class DragonOneShotAttackPhase extends AbstractDragonOneShotPhase {
    private static final int ROAR_DURATION = 20;
    private int attackingTicks;

    public DragonOneShotAttackPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doClientTick() {
        this.dragon
                .level()
                .playLocalSound(
                        this.dragon.getX(),
                        this.dragon.getY(),
                        this.dragon.getZ(),
                        SoundEvents.ENDER_DRAGON_GROWL,
                        this.dragon.getSoundSource(),
                        2.5F,
                        0.8F + this.dragon.getRandom().nextFloat() * 0.3F,
                        false
                );
    }

    @Override
    public void doServerTick() {
        super.doServerTick();
        if (this.attackingTicks++ >= ROAR_DURATION) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_FIREBALL);
        }
    }

    @Override
    public void begin() {
        this.attackingTicks = 0;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_ATTACK;
    }
}
