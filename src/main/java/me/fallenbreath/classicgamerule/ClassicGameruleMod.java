package me.fallenbreath.classicgamerule;

//#if MC >= 11802
//$$ import com.mojang.logging.LogUtils;
//$$ import org.slf4j.Logger;
//#else
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//#endif

//#if FABRIC
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
//#elseif FORGE
//$$ import net.minecraftforge.fml.ModList;
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import net.minecraftforge.forgespi.language.IModInfo;
//#elseif NEOFORGE
//$$ import net.neoforged.fml.ModList;
//$$ import net.neoforged.fml.common.Mod;
//$$ import net.neoforged.neoforgespi.language.IModInfo;
//#endif

//#if FORGE_LIKE
//$$ @Mod(ClassicGameruleMod.MOD_ID)
//#endif
public class ClassicGameruleMod
		//#if FABRIC
		implements ModInitializer
		//#endif
{
	public static final Logger LOGGER =
			//#if MC >= 11802
			//$$ LogUtils.getLogger();
			//#else
			LogManager.getLogger();
			//#endif

	public static final String MOD_ID = "classicgamerule";
	public static String MOD_VERSION = "unknown";
	public static String MOD_NAME = "unknown";

	//#if FABRIC
	@Override
	public void onInitialize()
	{
		ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();
		MOD_NAME = metadata.getName();
		MOD_VERSION = metadata.getVersion().getFriendlyString();
		LOGGER.info("Hello {} v{} from fabric!", MOD_NAME, MOD_VERSION);
		this.init();
	}
	//#elseif FORGE_LIKE
	//$$ public ClassicGameruleMod()
	//$$ {
	//$$ 	IModInfo modInfo = ModList.get().getModContainerById(MOD_ID).orElseThrow(RuntimeException::new).getModInfo();
	//$$ 	MOD_NAME = modInfo.getDisplayName();
	//$$ 	MOD_VERSION = modInfo.getVersion().toString();
	//$$ 	LOGGER.info("Hello {} v{} from forge-like!", MOD_NAME, MOD_VERSION);
	//$$ 	this.init();
	//$$ }
	//#endif

	private void init()
	{
		// common init here
	}
}
