package autosplittermod;

import autosplittermod.config.AutoSplitterConfig;
import me.contaria.speedrunapi.config.SpeedrunConfigAPI;
import me.contaria.speedrunapi.config.exceptions.NoSuchConfigException;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoSplitterMod implements ClientModInitializer
{
    public static final Logger LOGGER = LogManager.getLogger("autosplittermod");
    public static LiveSplitClient LIVE_SPLIT;
    public static volatile boolean hasStarted = false;

    @Override
    public void onInitializeClient()
    {
        LIVE_SPLIT = new LiveSplitClient();

        LOGGER.info("[AutoSplitterMod] Initialized");
    }

    public static boolean getConfig(String key)
    {
        try
        {
            return (boolean) SpeedrunConfigAPI.getConfigValue("autosplittermod", key);
        } catch (NoSuchConfigException e)
        {
            LOGGER.warn("[AutoSplitterMod] Failed to read config key: {} (Boolean)", key);
            return false;
        }
    }

    public static Object getConfigValue(String key) {
        try {
            return SpeedrunConfigAPI.getConfigValue("autosplittermod", key);
        } catch (NoSuchConfigException e) {
            LOGGER.warn("[AutoSplitterMod] Failed to read config key: {} (Object)", key);
            return null;
        }
    }
}
