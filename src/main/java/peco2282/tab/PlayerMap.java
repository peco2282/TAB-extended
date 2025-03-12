package peco2282.tab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * コンフィグ情報から得られたユーザーデータの保持
 *
 * @author peco2282
 */
public class PlayerMap {
  private static final Logger log = LogManager.getLogger(PlayerMap.class);
  private final Set<PlayerData> playerSet = new HashSet<>(16);

  public PlayerMap(FileConfiguration config) {
    config
        .getKeys(false)
        .forEach(key -> playerSet.add(playerdata(config, key)));
    log.info("Loaded {} players", playerSet.size());
  }

  private static PlayerData playerdata(FileConfiguration config, String key) {
    return new PlayerData(
        key,
        config.getString(key + ".role"),
        config.getInt(key + ".point")
    );
  }

  /**
   * 各々のユーザー情報のレコード
   * @param player プレイヤー名
   * @param role 役割
   * @param point 個人のポイント
   */
  public record PlayerData(String player, String role, int point) {
  }
}
