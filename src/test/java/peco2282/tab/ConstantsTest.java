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
    assertSame(Rank.RANK_1, Constants.getRank(2999));
    assertSame(Rank.RANK_2, Constants.getRank(3000));
    assertSame(Rank.RANK_2, Constants.getRank(4499));
    assertSame(Rank.RANK_3, Constants.getRank(6799));
    assertSame(Rank.RANK_4, Constants.getRank(6800));
    assertSame(Rank.RANK_4, Constants.getRank(9999));
    assertSame(Rank.RANK_5, Constants.getRank(10_000));
  }
}
