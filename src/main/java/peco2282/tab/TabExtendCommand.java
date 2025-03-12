package peco2282.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.TabListFormatManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class TabExtendCommand implements CommandExecutor, TabCompleter {
  private static final Logger log = LogManager.getLogger(TabExtendCommand.class);

  private static final char COLOR_CHAR = '§';

  private static final String RESET_COLOR = COLOR_CHAR + "r";
  private static final String ROLE = "role";
  private static final String RANK = "rank";

  private static List<String> allPlayers(CommandSender sender) {
    return sender.getServer().getWorlds().stream()
        .map(World::getPlayers)
        .flatMap(Collection::stream)
        .map(Player::getName)
        .toList();
  }

  /**
   * @param sender        コマンド送信者
   * @param player        TabPlayer
   * @param playerRawName プレイヤー名
   * @param role          ロール
   * @param manager       TABのフォーマットマネージャー
   */
  private static void setRole(CommandSender sender, TabPlayer player, String playerRawName, String role, TabListFormatManager manager) {
    final Optional<Config.RoleSet> roleSet = TABExtended.config().getRoleSet(role);
    if (roleSet.isPresent()) {
      PlayerMap map = TABExtended.playerMap();
      final Optional<PlayerMap.PlayerData> playerData = map.getPlayerData(playerRawName);
      playerData.ifPresentOrElse(data -> {
        PlayerMap.PlayerData replaced = data.replaceRole(role);
        map.replace(playerRawName, replaced);
        Config.RoleSet set = roleSet.get();
        manager.setName(player, set.color() + "%player%&r " + replaced.getRank().toString());
        sender.sendMessage("Set role " + set.toString().replace('&', COLOR_CHAR) + " to " + playerRawName);
      }, () -> {
        TABExtended.playerMap().putPlayerData(playerRawName, new PlayerMap.PlayerData(playerRawName, role, 0));
        log.info("Unknown user : {}", playerRawName);
        sender.sendMessage(ChatColor.RED + "Unknown user :" + playerRawName);
      });
    } else {
      sender.sendMessage(ChatColor.RED + "Role not found:" + role);
      log.info("Role not found : {}", role);
    }
  }

  private static void setRank(TabPlayer player, Rank rank) {
  }

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
    if (args.length == 0) {
      sender.sendMessage(ChatColor.AQUA + "/tabextend role <playername> <rolename>");
      return false;
    }
    TabAPI api = TabAPI.getInstance();
    TabListFormatManager manager = api.getTabListFormatManager();
    if (args[0].equals(ROLE)) {
      String name = args[1];
      String role = args[2];
      TabPlayer tp = api.getPlayer(name);
      if (tp == null || !allPlayers(sender).contains(name)) {
        sender.sendMessage(ChatColor.YELLOW + "Player " + name + " is not found (maybe offline).");
        log.warn("{} is not found.", name);
        if (TABExtended.config().isValidRole(role)) {
          TABExtended.playerMap().putPlayerData(name, new PlayerMap.PlayerData(name, role, 0));
          sender.sendMessage(ChatColor.GREEN + "Set role " + role + " to " + name);
          return true;
        } else {
          sender.sendMessage(ChatColor.RED + "Unknown role : " + role);
          return false;
        }
      }
      setRole(sender, tp, name, role, manager);
      return true;
    } else if (args[0].equals(RANK)) {
      log.info("{} is not found.", args[1]);
      return true;
    }
    sender.sendMessage(ChatColor.AQUA + "/tabextend role <playername> <rolename>");
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
    if (label.equalsIgnoreCase("tabextend")) {
      return List.of(ROLE, RANK);
    }
    if (label.equalsIgnoreCase(ROLE)) {
      return allPlayers(sender);
    }
    return List.of();
  }
}
