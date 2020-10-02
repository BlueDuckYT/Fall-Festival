package blueduck.fallfestival;

import blueduck.fallfestival.registry.ModEffects;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("fall_festival")
public class FallFestivalMod {

	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "fall_festival";

	public FallFestivalMod() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		//FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


		ModEffects.init();
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		// some preinit code
		LOGGER.info("HELLO FROM PREINIT");
		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
	}

	private void enqueueIMC(final InterModEnqueueEvent event)
	{
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
	}

	private void processIMC(final InterModProcessEvent event)
	{
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m->m.getMessageSupplier().get()).
				collect(Collectors.toList()));
	}

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
	public static class StaticEvents {

		public static void onTick(final TickEvent.PlayerTickEvent e) {
			int slot = e.player.inventory.getSlotFor(new ItemStack(Items.CARVED_PUMPKIN));
			if (slot == 9 && !e.player.getActivePotionEffects().contains(new EffectInstance(ModEffects.WRATH_EFFECT.get()))) {
				e.player.addPotionEffect(new EffectInstance(ModEffects.WRATH_EFFECT.get()));
			}
		}
	}


}
