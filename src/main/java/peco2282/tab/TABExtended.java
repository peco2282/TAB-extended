package peco2282.tab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;

public final class TABExtended extends JavaPlugin {
  private static final Logger log = LogManager.getLogger(TABExtended.class);
  public static TABExtended instance;
  private final File playerMapFile = new File(getDataFolder(), "playermap.yml");
  private Config config;
  private PlayerMap playerMap;

  @Contract(pure = true)
  public static TABExtended instance() {
    return instance;
  }

  @Contract(pure = true)
  public static Config config() {
    return instance.config;
  }

  @Contract(pure = true)
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
    }
    getServer().getPluginManager().registerEvents(new EventListener(), this);

    log.info("Successfully loaded plugin `TAB-EXTENDED`");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    savePlayerMap();
  }

  public void savePlayerMap() {
    try {
      playerMap.save(playerMapFile);
    } catch (IOException e) {
      log.error(e);
    }
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

  public void reloadPlayerMap() {
    this.playerMap = new PlayerMap(loadPlayerMapConfig());
  }
}
