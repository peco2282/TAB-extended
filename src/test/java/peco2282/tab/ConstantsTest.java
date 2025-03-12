package peco2282.tab;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getRank() {
    assertSame(Rank.RANK_0, Constants.getRank(900));
    assertSame(Rank.RANK_1, Constants.getRank(1000));
    assertSame(Rank.RANK_1, Constants.getRank(3999));
    assertSame(Rank.RANK_2, Constants.getRank(4000));
    assertSame(Rank.RANK_2, Constants.getRank(8499));
    assertSame(Rank.RANK_3, Constants.getRank(8500));
    assertSame(Rank.RANK_3, Constants.getRank(9999));
    assertSame(Rank.RANK_3, Constants.getRank(10_000));
    assertSame(Rank.RANK_5, Constants.getRank(34_000));
    assertSame(Rank.RANK_8, Constants.getRank(100_000));
    assertSame(Rank.RANK_8, Constants.getRank(146_799));
    assertSame(Rank.RANK_9, Constants.getRank(146_800));
  }
}
