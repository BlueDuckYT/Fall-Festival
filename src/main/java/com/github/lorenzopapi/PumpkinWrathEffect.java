package com.github.lorenzopapi;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

public class PumpkinWrathEffect extends Effect {

	protected PumpkinWrathEffect() {
		super(EffectType.NEUTRAL, 0xff8c00);
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		if (!entityLivingBaseIn.isSpectator() && entityLivingBaseIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLivingBaseIn;
			ServerWorld serverworld = serverplayerentity.getServerWorld();
			if (serverworld.getDifficulty() == Difficulty.PEACEFUL) {
				return;
			}

			if (serverworld.isVillage(entityLivingBaseIn.func_233580_cy_())) {
				PumpkinRaidManager manager = new PumpkinRaidManager(serverworld);
				manager.pumpkinTick(serverplayerentity);
			}
		}
	}
}
