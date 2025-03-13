package peco2282.tab;

public enum Rank {
  RANK_0,
  RANK_1,
  RANK_2,
  RANK_3,
  RANK_4,
  RANK_5,
  RANK_6,
  RANK_7,
  RANK_8,
  RANK_9;

  private String rankName;
  private String rankDisplay;
  private String rankColor;

  public static void bind(Config.RankSet[] rankSets) {
    assert rankSets.length == values().length;
    for (Rank rank : values()) {
      rank.setRankName(rankSets[rank.ordinal()].name());
      rank.setRankDisplay(rankSets[rank.ordinal()].display());
      rank.setRankColor(rankSets[rank.ordinal()].color());
    }
  }

  public static Rank getRank(int rank) {
    assert rank >= 0 && rank < values().length;
    return values()[rank];
  }

  public String getRankName() {
    return rankName;
  }

  public void setRankName(String rankName) {
    this.rankName = rankName;
  }

  public String getRankDisplay() {
    return rankDisplay;
  }

  public void setRankDisplay(String rankDisplay) {
    this.rankDisplay = rankDisplay;
  }

  public String getRankColor() {
    return rankColor;
  }

  public void setRankColor(String rankColor) {
    this.rankColor = rankColor;
  }

  @Override
  public String toString() {
    return rankColor + rankDisplay + "&r";
  }
}
