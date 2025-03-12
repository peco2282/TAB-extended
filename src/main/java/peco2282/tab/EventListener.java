package peco2282.tab;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import java.util.Objects;
import java.util.Optional;

public class EventListener implements Listener {
  @EventHandler
  public void onPlayerJoin(ServerLoadEvent event) {
    Objects.requireNonNull(TabAPI.getInstance()
            .getEventBus())
        .register(PlayerLoadEvent.class, ple -> {
          Player player = (Player) ple.getPlayer().getPlayer();
          TabPlayer tp = TabAPI.getInstance().getPlayer(player.getName());
          Optional<PlayerMap.PlayerData> data = TABExtended.playerMap().getPlayerData(player.getName());
          data.ifPresent(playerData -> {
            String role = TABExtended.config().getRoleSet(playerData.role()).map(Config.RoleSet::color).orElse("");
            Objects.requireNonNull(TabAPI.getInstance().getTabListFormatManager()).setName(
                tp,
                Constants.joinTAB(role, "%player%", playerData.getRank().toString())
            );
          });
        });
  }
}
