package me.messdaniel.simplejumppads.playerdata;

import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private static HashMap<UUID, PlayerData> allPlayerData = new HashMap<>();

    public static void reload() {
        allPlayerData = new HashMap<>();
    }
    private boolean launched = false;
    private Long launchTime = 0L;
    private FallingBlock fallingBlock = null;
    private boolean resetFlight = false;

    public static PlayerData getPlayerData(Player player) {
        if (allPlayerData.containsKey(player.getUniqueId())) return allPlayerData.get(player.getUniqueId());
        else {
            PlayerData playerData = new PlayerData();
            allPlayerData.put(player.getUniqueId(),playerData);
            return playerData;
        }
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    public Long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Long launchTime) {
        this.launchTime = launchTime;
    }

    public FallingBlock getFallingBlock() {
        return fallingBlock;
    }

    public void setFallingBlock(FallingBlock fallingBlock) {
        this.fallingBlock = fallingBlock;
    }

    public boolean isResetFlight() {
        return resetFlight;
    }

    public void setResetFlight(boolean resetFlight) {
        this.resetFlight = resetFlight;
    }
}
