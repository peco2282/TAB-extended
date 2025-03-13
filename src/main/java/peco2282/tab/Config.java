package peco2282.tab;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.TabListFormatManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * コンフィグファイルの情報
 */
public class Config {
  private static final Logger log = LogManager.getLogger(Config.class);
  private final Set<RoleSet> roleSets = new HashSet<>(4);
  private final List<RankSet> ranks = new ArrayList<>();

  Config(FileConfiguration config) {
    initRoles(config);
    initRanks(config);

    Rank.bind(ranks.toArray(RankSet[]::new));
    log.info("Loaded config. ranks: {}", ranks.size());
  }

  private void initRoles(FileConfiguration config) {
    ConfigurationSection section = config.getConfigurationSection("role");
    if (section != null) {
      section.getKeys(false).forEach(key -> roleSets.add(new RoleSet(
          key,
          config.getString("role." + key + ".color")
      )));
      log.info("Loaded roles: {}", roleSets.size());
      return;
    }
    throw new IllegalArgumentException("role section is not found.");
  }

  private void initRanks(FileConfiguration config) {
    ConfigurationSection section = config.getConfigurationSection("rank");
    if (section != null) {
      final int size = section.getKeys(false).size();
      for (int index = 0; index < size; index++) {
        String name = section.getString(index + ".name");
        String display = section.getString(index + ".display");
        String color = section.getString(index + ".color");
        RankSet set = new RankSet(name, display, color);
        ranks.add(set);
      }
      return;
    }
    throw new IllegalArgumentException("rank section is not found.");
  }

  public Set<RoleSet> getRoles() {
    return roleSets;
  }

  public boolean isValidRole(String role) {
    return roleSets.stream().map(RoleSet::name).anyMatch(role::equals);
  }

  public Optional<RoleSet> getRoleSet(String name) {
    return roleSets.stream()
        .filter(rs -> rs.name().equalsIgnoreCase(name))
        .findFirst();
  }

  public List<RankSet> getRanks() {
    return ranks;
  }

  public record RoleSet(String name, String color) {
    public void withPrefix(TabListFormatManager manager, TabPlayer p) {
      manager.setPrefix(p, color);
    }

    @Override
    public String toString() {
      return color + name + "&r";
    }
  }

  public record RankSet(String name, String display, String color) {
    @Override
    public String toString() {
      return color + display + "&r";
    }
  }
}
