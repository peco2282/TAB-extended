package peco2282.tab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TABExtended extends JavaPlugin {
  private static final Logger log = LogManager.getLogger(TABExtended.class);
  public static TABExtended instance;
  private final File playerMapFile = new File(getDataFolder(), "playermap.yml");
  private Config config;
  private PlayerMap playerMap;

  public static TABExtended instance() {
    return instance;
  }

  public static Config config() {
    return instance.config;
  }

  public static PlayerMap playerMap() {
    return instance.playerMap;
  }

  @Override
  public void onEnable() {
    // Plugin startup logic
    log.info("Starting up");
    saveDefaultConfig();

    instance = this;

    log.info("Loading config");
    this.config = new Config(getConfig());
    this.playerMap = new PlayerMap(loadPlayerMapConfig());
    log.info("Successfully loaded config");

    log.info("Loading commands");
    PluginCommand command = getCommand("tabextend");
    if (command != null) {
      TabExtendCommand tabExtendCommand = new TabExtendCommand();
      command.setExecutor(tabExtendCommand);
      command.setTabCompleter(tabExtendCommand);
      command.setUsage("/tabextend");
    }

    log.info("Successfully loaded plugin `TAB-EXTENDED`");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  /**
   * playermap.yml を読み込む
   */
  private YamlConfiguration loadPlayerMapConfig() {

    if (!playerMapFile.exists()) {
      saveResource("playermap.yml", false);
    }

    return YamlConfiguration.loadConfiguration(playerMapFile);
  }
}
