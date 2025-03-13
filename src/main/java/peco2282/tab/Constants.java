package peco2282.tab;

public class Constants {
  public static final int[] POINT = {
      1000,
      2000,
      1500,
      2300,
      3200,
      5000,
      7500,
      11_500,
      16_000,
  };
  public static final char COLOR_CHAR = '§';

  public static String joinTAB(String color, String name, String rank) {
    return color + name + "&r&r  " + rank + "&r";
  }

  public static String joinClient(String color, String name, String rank) {
    return joinTAB(color, name, rank).replace('&', COLOR_CHAR);
  }



  public static Rank getRank(final int totalPoints) {
    int rank = 0;
    int accumulatedPoints = 0;

    for (int j : POINT) {
      accumulatedPoints += j;
      if (totalPoints < accumulatedPoints) {
        break;
      }
      rank++;
    }

    return Rank.getRank(rank);
  }
}
