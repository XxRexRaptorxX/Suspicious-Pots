package xxrexraptorxx.suspicious_pots.main;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xxrexraptorxx.suspicious_pots.utils.Config;

/**
 * @author XxRexRaptorxX (RexRaptor)
 * @projectPage https://www.curseforge.com/minecraft/mc-mods/suspicious_pots
 **/
@Mod(References.MODID)
public class SuspiciousPots {

    public static final Logger LOGGER = LogManager.getLogger();

    public SuspiciousPots(ModContainer container) {
        Config.init(container);
    }
}