package com.github.lorenzopapi;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PumpkinRaidManager extends WorldSavedData {
	private final Map<Integer, Raid> byId = Maps.newHashMap();
	private final ServerWorld world;
	private int nextAvailableId;
	private int tick;

	public static PumpkinRaidManager get(ServerWorld world) {
		return world.getSavedData().getOrCreate(() -> new PumpkinRaidManager(world), FallFestivalMod.MODID + "_pumpkin_raids" + world.func_230315_m_().getSuffix());
	}

	public PumpkinRaidManager(ServerWorld world) {
		super(FallFestivalMod.MODID + "_pumpkin_raids" + world.func_230315_m_().getSuffix());
		this.world = world;
		this.nextAvailableId = 1;
		this.markDirty();
	}

	@Override
	public void read(CompoundNBT nbt) {
		this.nextAvailableId = nbt.getInt("NextAvailableID");
		this.tick = nbt.getInt("Tick");
		ListNBT listnbt = nbt.getList("Raids", 10);

		for(int i = 0; i < listnbt.size(); ++i) {
			CompoundNBT compoundnbt = listnbt.getCompound(i);
			Raid raid = new Raid(this.world, compoundnbt);
			this.byId.put(raid.getId(), raid);
		}
	}

	public void tick() {
		++this.tick;
		Iterator<Raid> iterator = this.byId.values().iterator();

		while(iterator.hasNext()) {
			Raid raid = iterator.next();
			if (this.world.getGameRules().getBoolean(GameRules.DISABLE_RAIDS)) {
				raid.stop();
			}

			if (raid.isStopped()) {
				iterator.remove();
				this.markDirty();
			} else {
				raid.tick();
			}
		}

		if (this.tick % 200 == 0) {
			this.markDirty();
		}
	}

	@Nullable
	public Raid pumpkinTick(ServerPlayerEntity p_215170_1_) {
		if (p_215170_1_.isSpectator()) {
			return null;
		} else if (this.world.getGameRules().getBoolean(GameRules.DISABLE_RAIDS)) {
			return null;
		} else {
			DimensionType dimensiontype = p_215170_1_.world.func_230315_m_();
			if (!dimensiontype.func_241512_l_()) {
				return null;
			} else {
				BlockPos blockpos = p_215170_1_.func_233580_cy_();
				List<PointOfInterest> list = this.world.getPointOfInterestManager().func_219146_b(PointOfInterestType.MATCH_ANY, blockpos, 64, PointOfInterestManager.Status.IS_OCCUPIED).collect(Collectors.toList());
				int i = 0;
				Vector3d vector3d = Vector3d.ZERO;

				for (PointOfInterest pointofinterest : list) {
					BlockPos blockpos2 = pointofinterest.getPos();
					vector3d = vector3d.add(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
					++i;
				}

				BlockPos blockpos1;
				if (i > 0) {
					vector3d = vector3d.scale(1.0D / (double)i);
					blockpos1 = new BlockPos(vector3d);
				} else {
					blockpos1 = blockpos;
				}

				Raid raid = this.findOrCreateRaid(p_215170_1_.getServerWorld(), blockpos1);
				if (!raid.isStarted()) {
					if (!this.byId.containsKey(raid.getId())) {
						this.byId.put(raid.getId(), raid);
					}
				}

				this.markDirty();
				return raid;
			}
		}
	}

	private Raid findOrCreateRaid(ServerWorld p_215168_1_, BlockPos p_215168_2_) {
		Raid raid = p_215168_1_.findRaid(p_215168_2_);
		return raid != null ? raid : new Raid(nextAvailableId++, p_215168_1_, p_215168_2_);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("NextAvailableID", this.nextAvailableId);
		compound.putInt("Tick", this.tick);
		ListNBT listnbt = new ListNBT();

		for(Raid raid : this.byId.values()) {
			CompoundNBT compoundnbt = new CompoundNBT();
			raid.write(compoundnbt);
			listnbt.add(compoundnbt);
		}

		compound.put("Raids", listnbt);
		return compound;
	}
}
