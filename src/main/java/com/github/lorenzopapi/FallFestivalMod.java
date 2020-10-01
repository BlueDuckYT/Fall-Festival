package com.github.lorenzopapi;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fall_festival")
public class FallFestivalMod {

	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "fall_festival";

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
	public static class StaticEvents {

		public static void onTick(final TickEvent.PlayerTickEvent e) {
			int slot = e.player.inventory.getSlotFor(new ItemStack(Items.CARVED_PUMPKIN));
			if (slot == 9) {

			}
		}
	}
}
