package autosplittermod.config;

import me.contaria.speedrunapi.config.api.SpeedrunConfig;
import me.contaria.speedrunapi.config.api.SpeedrunConfigStorage;
import me.contaria.speedrunapi.config.api.annotations.Config;

public class AutoSplitterConfig implements SpeedrunConfig
{
    @Config.Category("Connection")
    public ConnectionSettings connection = new ConnectionSettings();

    @Config.Category("Splits")
    public SplitSettings splits = new SplitSettings();

    @Config.Category("General")
    public GeneralSettings general = new GeneralSettings();

    @Override
    public String modID()
    {
        return "autosplittermod";
    }

    public static class ConnectionSettings implements SpeedrunConfigStorage
    {
        public String host = "localhost";
        @Config.Numbers.Whole.Bounds(min = 1, max = 65535)
        public int port = 16834;
        public boolean autoReconnect = true;
    }

    public static class SplitSettings implements SpeedrunConfigStorage
    {
        public boolean enterNether = false;
        public boolean enterBastion = false;
        public boolean enterFortress = false;
        public boolean blind = false;
        public boolean eyeSpy = false;
        public boolean endEnter = false;
        public boolean credits = false;
    }

    public static class GeneralSettings implements SpeedrunConfigStorage
    {
        public boolean resetDetection = true;
        public boolean showHud = true;
        public boolean debugLogging = false;
    }

}
