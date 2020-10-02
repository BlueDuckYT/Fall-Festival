package blueduck.fallfestival.registry;

import blueduck.fallfestival.PumpkinWrathEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static blueduck.fallfestival.FallFestivalMod.MODID;

public class ModEffects {
    public static DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, MODID);

    public static final RegistryObject<Effect> WRATH_EFFECT = EFFECTS.register("pumpkin_wrath", PumpkinWrathEffect::new);

    public static void init() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}