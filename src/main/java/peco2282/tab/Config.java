package peco2282.tab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * コンフィグファイルの情報
 */
public class Config {
  private static final Logger log = LogManager.getLogger(Config.class);
  private final String crafterColor;
  private final RankSet rank0;
  private final RankSet rank1;
  private final RankSet rank2;
  private final RankSet rank3;
  private final RankSet rank4;
  private final RankSet rank5;
  private final RankSet rank6;
  private final RankSet rank7;
  private final RankSet rank8;
  private final RankSet rank9;
  private final Set<RankSet> ranks = new HashSet<>();

  Config(FileConfiguration config) {
    crafterColor = config.getString("role.crafter.color");
    rank0 = rankset(config, 0);
    rank1 = rankset(config, 1);
    rank2 = rankset(config, 2);
    rank3 = rankset(config, 3);
    rank4 = rankset(config, 4);
    rank5 = rankset(config, 5);
    rank6 = rankset(config, 6);
    rank7 = rankset(config, 7);
    rank8 = rankset(config, 8);
    rank9 = rankset(config, 9);
    log.info("Loaded config. ranks: {}", ranks.size());
  }

  private RankSet rankset(FileConfiguration config, int index) {
    String name = config.getString("rank." + index + ".name");
    String display = config.getString("rank." + index + ".display");
    String color = config.getString("rank." + index + ".color");
    RankSet set = new RankSet(name, display, color);
    ranks.add(set);
    return set;
  }

  public Set<RankSet> getRanks() {
    return ranks;
  }

  public RankSet getRank9() {
    return rank9;
  }

  public RankSet getRank8() {
    return rank8;
  }

  public RankSet getRank7() {
    return rank7;
  }

  public RankSet getRank6() {
    return rank6;
  }

  public RankSet getRank5() {
    return rank5;
  }

  public RankSet getRank4() {
    return rank4;
  }

  public RankSet getRank3() {
    return rank3;
  }

  public RankSet getRank2() {
    return rank2;
  }

  public RankSet getRank1() {
    return rank1;
  }

  public RankSet getRank0() {
    return rank0;
  }

  public String getCrafterColor() {
    return crafterColor;
  }

  public record RankSet(String name, String display, String color) {
    @Override
    public String toString() {
      return color + display + "&r";
    }
  }
}
