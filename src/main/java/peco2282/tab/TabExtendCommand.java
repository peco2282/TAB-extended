package peco2282.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.TabListFormatManager;
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
import java.util.stream.Collectors;

/**
 * めっちゃ見にくくなってる自信あります。
 */
@SuppressWarnings("deprecation")
public class TabExtendCommand implements CommandExecutor, TabCompleter {
  private static final Logger log = LogManager.getLogger(TabExtendCommand.class);

  private static final char COLOR_CHAR = '§';

  private static final String RESET_COLOR = COLOR_CHAR + "r";
  private static final String ROLE = "role";
  private static final String RANK = "point";
  private static final String RELOAD = "reload";

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
      playerData.ifPresent(data -> {
        PlayerMap.PlayerData replaced = data.replaceRole(role);
        map.replace(playerRawName, replaced);
        Config.RoleSet set = roleSet.get();
        manager.setName(player, Constants.joinTAB(set.color(), "%player%", replaced.getRank().toString()));
        sender.sendMessage(ChatColor.GREEN + "ロール" + ChatColor.RESET + " '" + set.toString().replace('&', COLOR_CHAR) + "' " + ChatColor.GREEN + "を" + playerRawName + "に付与しました");
      });
    }
  }

  private static void setPoint(CommandSender sender, TabPlayer player, PlayerMap.PlayerData data, TabListFormatManager manager) {
    Optional<Config.RoleSet> set = TABExtended.config().getRoleSet(data.role());
    if (set.isPresent()) {
      manager.setName(player, Constants.joinTAB(set.get().color(), "%player%", data.getRank().toString()));
      sender.sendMessage(ChatColor.GREEN + Constants.joinClient(set.get().color(), data.player(), data.getRank().toString()));
    } else {
      sender.sendMessage(ChatColor.YELLOW + "ロールが見つかりません。");
    }
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
    if (args[0].equals(RELOAD)) {
      TABExtended.instance().onDisable();
    }
    TabAPI api = TabAPI.getInstance();
    TabListFormatManager manager = api.getTabListFormatManager();
    if (args[0].equals(ROLE)) {
      if (args.length == 1) {
        var roles = TABExtended.config().getRoles().stream().map(Config.RoleSet::name).collect(
            Collectors.joining(ChatColor.RESET + ", " + ChatColor.AQUA,
                ChatColor.AQUA.toString(), ChatColor.RESET.toString())
        );
        sender.sendMessage(ChatColor.GREEN + "有効なロールは " + ChatColor.RESET + roles + " です");
        return true;
      }
      String name = args[1];
      String role = args[2];
      if (!TABExtended.config().isValidRole(role)) {
        sender.sendMessage(ChatColor.RED + role + "というロールは見つかりません");
        return false;
      }
      TabPlayer tp = api.getPlayer(name);
      if (tp == null || !allPlayers(sender).contains(name)) {
        sender.sendMessage(ChatColor.YELLOW + name + " というプレイヤーは見つかりませんでした。");
        log.warn("{} is not found.", name);
        TABExtended.playerMap().putPlayerData(name, new PlayerMap.PlayerData(name, role, 0));
        sender.sendMessage(ChatColor.GREEN + name + " の登録が完了しました。");
        return true;
      }
      setRole(sender, tp, name, role, manager);
      return true;
    } else if (args[0].equals(RANK)) {
      String name = args[1];
      String type = args[2];
      int point = Integer.parseInt(args[3]);
      TabPlayer tp = api.getPlayer(name);
      log.info("{} is not found.", args[1]);
      if (tp == null || !allPlayers(sender).contains(name)) {
        sender.sendMessage(ChatColor.YELLOW + name + " というプレイヤーは見つかりませんでした。");
        return false;
      }
      Optional<PlayerMap.PlayerData> opdata = TABExtended.playerMap().getPlayerData(name);
      if (opdata.isPresent()) {
        PlayerMap.PlayerData data = opdata.get();
        PlayerMap.PlayerData replaced = data;
        replaced = switch (type) {
          case "add" -> data.replacePoint(i -> i + point);
          case "set" -> data.replacePoint($ -> point);
          case "remove" -> {
            var tmp = data;
            try {
              tmp = data.replacePoint(i -> i - point);
            } catch (IllegalArgumentException e) {
              sender.sendMessage(ChatColor.YELLOW + "プレイヤー " + name + " の現ポイント: " + data.point() + " 引こうとしているポイント: " + point);
            }
            yield tmp;
          }
          default -> replaced;
        };
        TABExtended.playerMap().putPlayerData(name, replaced);
        setPoint(sender, tp, replaced, manager);
      }
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
      return List.of(ROLE, RANK, RELOAD);
    }
//    if (label.equalsIgnoreCase(ROLE)) {
//      return allPlayers(sender);
//    }
    return List.of();
  }
}
