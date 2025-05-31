package xxrexraptorxx.suspicious_pots.main;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xxrexraptorxx.magmacore.config.ConfigHelper;
import xxrexraptorxx.magmacore.main.ModRegistry;
import xxrexraptorxx.suspicious_pots.utils.Config;

/**
 * @author XxRexRaptorxX (RexRaptor)
 * @projectPage <a href="https://www.curseforge.com/minecraft/mc-mods/suspicious_pots">...</a>
 **/
@Mod(References.MODID)
public class SuspiciousPots {

    public static final Logger LOGGER = LogManager.getLogger();

    public SuspiciousPots(ModContainer container) {
        ConfigHelper.registerConfigs(container, References.MODID, false, Config.SERVER_CONFIG, null);
        ModRegistry.register(References.MODID, References.NAME, References.URL);    }


    @Mod(value = References.MODID, dist = Dist.CLIENT)
    public static class SuspiciousPotsClient {

        public SuspiciousPotsClient(ModContainer container) {
            ConfigHelper.registerIngameConfig(container);
        }
    }
}