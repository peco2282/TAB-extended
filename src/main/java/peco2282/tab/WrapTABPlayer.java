package peco2282.tab;

import me.neznamy.tab.api.TabPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record WrapTABPlayer(Player player) implements TabPlayer {

  /**
   * Returns player's name
   *
   * @return Player's name
   */
  @Override
  public @NotNull String getName() {
    return this.player.getName();
  }

  /**
   * Returns player's UUID
   *
   * @return Player's UUID
   */
  @Override
  public @NotNull UUID getUniqueId() {
    return this.player.getUniqueId();
  }

  /**
   * Returns platform-specific entity
   *
   * @return platform's player object
   */
  @Override
  public @NotNull Object getPlayer() {
    return this.player;
  }

  /**
   * Returns true once the player is successfully loaded (onJoin method ran through all methods)
   *
   * @return {@code true} if player is fully loaded, {@code false} otherwise
   */
  @Override
  public boolean isLoaded() {
    return true;
  }

  /**
   * Returns player's primary permission group. If group has been changed using
   * {@link #setTemporaryGroup(String)}, returns that value. Otherwise, returns group
   * detected by standard group assign logic
   *
   * @return player's primary permission group
   */
  @Override
  public @NotNull String getGroup() {
    return "";
  }

  /**
   * Temporarily overrides player's group and applies all changes coming from new group.
   * This includes all properties and sorting, if used.
   * Set to {@code null} to reset back.
   *
   * @param group New group to use
   * @see #hasTemporaryGroup()
   */
  @Override
  public void setTemporaryGroup(@Nullable String group) {

  }

  /**
   * Returns {@code true} if a temporary group was applied to the player using {@link #setTemporaryGroup(String)}.
   * If no group was set, returns {@code false}
   *
   * @return {@code true} if group is set, {@code false} if not
   * @see #setTemporaryGroup(String)
   */
  @Override
  public boolean hasTemporaryGroup() {
    return false;
  }

  /**
   * Changes expected profile name of the player. This adapts all name-bound features
   * to use this new profile name if another plugin changed profile name of the player. <p>
   * Automatic profile name change detection is available on Bukkit, BungeeCord and Fabric,
   * therefore using this is redundant there. This function is only needed on Sponge and Velocity,
   * where the detection is not available. <p>
   * Warning: This function does NOT change player's profile name, it only updates the tracked
   * name inside the plugin.
   *
   * @param profileName New expected profile name
   */
  @Override
  public void setExpectedProfileName(@NotNull String profileName) {

  }

  /**
   * Returns player's expected profile name. This defaults to player's username, but may
   * get changed by other plugins and then detected by TAB.
   *
   * @return Player's expected profile name
   */
  @Override
  public @NotNull String getExpectedProfileName() {
    return "";
  }

  /**
   * Returns name of the server the player is connected to.
   * This requires TAB on proxy.
   * On backend installation, this method returns {@code "N/A"}.
   *
   * @return Player's server on proxy or {@code "N/A"} on backend installation
   */
  @Override
  public @NotNull String getServer() {
    return this.player.getServer().getName();
  }

  /**
   * Returns name of the world the player is in.
   * On proxy installation, TAB-Bridge must be installed to forward this info to the proxy.
   * Otherwise, it will return {@code "N/A"}.
   *
   * @return Player's world or {@code "N/A"} on proxy installation without TAB-Bridge installed
   */
  @Override
  public @NotNull String getWorld() {
    return this.player.getWorld().getName();
  }

  /**
   * Returns {@code true} if this player is a bedrock player, {@code false} if not.
   *
   * @return {@code true} if this player is a bedrock player, {@code false} if not
   */
  @Override
  public boolean isBedrockPlayer() {
    return false;
  }
}
