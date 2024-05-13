package me.messdaniel.simplejumppads.listener;

import me.messdaniel.simplejumppads.SimpleJumpPads;
import me.messdaniel.simplejumppads.config.ConfigValues;
import me.messdaniel.simplejumppads.jumppad.JumpPad;
import me.messdaniel.simplejumppads.JumpPadManager;
import me.messdaniel.simplejumppads.playerdata.PlayerData;
import me.messdaniel.simplejumppads.utils.OtherUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    private JumpPadManager jumpPadManager = SimpleJumpPads.getInstance().getJumpPadManager();

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (!jumpPadManager.worldHasJumpPad(event.getPlayer().getWorld().getName())) return;
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL && !ConfigValues.isGamemodeSurvival()) return;
        if (player.getGameMode() == GameMode.ADVENTURE && !ConfigValues.isGamemodeAdventure()) return;
        if (player.getGameMode() == GameMode.CREATIVE && !ConfigValues.isGamemodeCreative()) return;
        if (player.getGameMode() == GameMode.SPECTATOR && !ConfigValues.isGamemodeSpectator()) return;
        PlayerData playerData = PlayerData.getPlayerData(player);
        if (playerData.isLaunched()) {
            if (playerData.getFallingBlock() != null && player.isFlying()) {
                player.setFlying(false);
            }
            if (playerData.getLaunchTime() != 0 && System.currentTimeMillis() - playerData.getLaunchTime() < 1000) {
                player.setFallDistance(0);
                if (!player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0)).getType().isAir()) {
                    playerData.setLaunched(false);
                    playerData.setLaunchTime(0L);
                }
            }
            return;
        }
        Location playerLoc = player.getLocation();
        String plateLoc = OtherUtils.locToString(playerLoc);
        String blockLoc = OtherUtils.locToString(playerLoc.clone().subtract(0,1,0));

        JumpPad plateJumpPad = jumpPadManager.getJumpPad(plateLoc);
        if (plateJumpPad != null) {
            jumpPadManager.launchPlayer(player,plateJumpPad);
            return;
        }
        JumpPad blockJumpPad = jumpPadManager.getJumpPad(blockLoc);
        if (blockJumpPad != null) {
            jumpPadManager.launchPlayer(player,blockJumpPad);
        }
    }
}
