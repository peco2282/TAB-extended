package peco2282.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import me.neznamy.tab.api.event.plugin.PlaceholderRegisterEvent;
import me.neznamy.tab.api.event.plugin.TabLoadEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.Objects;
import java.util.Optional;

public class EventListener implements Listener {
  private static final Logger log = LogManager.getLogger(EventListener.class);

  private static final Runnable PLAYER_LOAD = () -> {
    Objects.requireNonNull(TabAPI.getInstance()
            .getEventBus())
        .register(PlayerLoadEvent.class, ple -> {
          log.info("Starting Player loading");
          Player player = (Player) ple.getPlayer().getPlayer();
          TabPlayer tp = TabAPI.getInstance().getPlayer(player.getName());
          Optional<PlayerMap.PlayerData> data = TABExtended.playerMap().getPlayerData(player.getName());
          data.ifPresent(playerData -> {
            String role = TABExtended.config().getRoleSet(playerData.role()).map(Config.RoleSet::color).orElse("");
            Objects.requireNonNull(TabAPI.getInstance().getTabListFormatManager()).setName(
                tp != null ? tp : new WrapTABPlayer(player),
                Constants.joinTAB(role, "%player%", playerData.getRank().toString())
            );
            log.info("Player loaded");
          });
        });
  };

  private static final Runnable TAB_LOAD = () -> {
    Objects.requireNonNull(TabAPI.getInstance().getEventBus())
        .register(TabLoadEvent.class, tle -> {
          log.info("All player loaded");
        });
  };

  @EventHandler
  public void onServerLoad(ServerLoadEvent event) {
    if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
      TABExtended.instance().reloadPlayerMap();
    }
    PLAYER_LOAD.run();
    TAB_LOAD.run();
  }
}
