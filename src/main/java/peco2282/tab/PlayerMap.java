package peco2282.tab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.IntUnaryOperator;

/**
 * コンフィグ情報から得られたユーザーデータの保持
 *
 * @author peco2282
 */
public class PlayerMap {
  private final FileConfiguration config;
  private static final Logger log = LogManager.getLogger(PlayerMap.class);
  private final Map<String, PlayerData> playerSet = new HashMap<>(16);

  public PlayerMap(FileConfiguration config) {
    this.config = config;
    config
        .getKeys(false)
        .forEach(key -> playerSet.put(key, playerdata(config, key)));
    log.info("Loaded {} players", playerSet.size());
  }

  private static PlayerData playerdata(FileConfiguration config, String key) {
    return new PlayerData(
        key,
        config.getString(key + ".role"),
        config.getInt(key + ".point")
    );
  }

  public void replace(String player, PlayerData data) {
    playerSet.put(player, data);
  }

  public Optional<PlayerData> getPlayerData(String player) {
    return playerSet.values().stream().filter(playerData -> playerData.player().equals(player)).findFirst();
  }

  public Set<String> getPlayers() {
    return Collections.unmodifiableSet((playerSet.keySet()));
  }

  public void putPlayerData(String player, PlayerData data) {
    playerSet.put(player, data);
    ConfigurationSection section = config.createSection(player);
    section.set("role", data.role());
    section.set("point", data.point());

    TABExtended.instance().savePlayerMap();
  }

  public void save(File path) throws IOException {
    playerSet.forEach((key, data) -> {
      config.set(key + ".role", data.role());
      config.set(key + ".point", data.point());
    });
    config.save(path);
  }

  /**
   * 各々のユーザー情報のレコード
   *
   * @param player プレイヤー名
   * @param role   役割
   * @param point  個人のポイント
   */
  public record PlayerData(String player, String role, int point) {
    public PlayerData replaceRole(String newRole) {
      return new PlayerData(player, newRole, point);
    }

    public PlayerData replacePoint(IntUnaryOperator newPoint) {
      int p = newPoint.applyAsInt(point);
      if (p < 0) {
        throw new IllegalArgumentException("Negative point");
      }
      return new PlayerData(player, role, p);
    }

    public Rank getRank() {
      return Constants.getRank(point);
    }
  }
}
