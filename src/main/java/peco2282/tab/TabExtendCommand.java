package peco2282.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.TabListFormatManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TabExtendCommand implements CommandExecutor, TabCompleter {
  private static final Logger log = LogManager.getLogger(TabExtendCommand.class);

  /**
   * Executes the given command, returning its success.
   * <br>
   * If false is returned, then the "usage" plugin.yml entry for this command
   * (if defined) will be sent to the player.
   *
   * @param sender  Source of the command
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    Passed command arguments
   * @return true if a valid command, otherwise false
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    TabAPI api = TabAPI.getInstance();
    TabListFormatManager manager = api.getTabListFormatManager();
    if (manager == null) return false;
    TabPlayer player = api.getPlayer(sender.getName());
    if (player == null) return false;
    Config config = TABExtended.config();
    if (args.length != 3) return false;
    String playerName = args[0];
    String playerRole = args[1];
    String playerRank = args[2];

    Config.RankSet rank = config.getRanks().stream().filter(rs -> rs.name().equalsIgnoreCase(playerRank)).findFirst().orElse(null);

    if (rank != null) {
      manager.setPrefix(player, config.getCrafterColor());
      manager.setSuffix(player, "&r " + rank);
      return true;
    }
    return false;
  }

  /**
   * Requests a list of possible completions for a command argument.
   *
   * @param sender  Source of the command.  For players tab-completing a
   *                command inside of a command block, this will be the player, not
   *                the command block.
   * @param command Command which was executed
   * @param label   Alias of the command which was used
   * @param args    The arguments passed to the command, including final
   *                partial argument to be completed
   * @return A List of possible completions for the final argument, or null
   * to default to the command executor
   */
  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      List<String> playerNames = player.getWorld().getPlayers().stream().map(Player::getName).toList();
      if (!playerNames.isEmpty()) return playerNames;
    }
    return List.of();
  }
}
