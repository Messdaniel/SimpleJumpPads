package me.messdaniel.simplejumppads.utils;

import me.messdaniel.simplejumppads.SimpleJumpPads;
import me.messdaniel.simplejumppads.jumppad.JumpPad;
import me.messdaniel.simplejumppads.jumppad.JumpPadDirection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class OtherUtils {

    public static String locToString(Location loc) {
        return loc.getWorld().getName() + ", " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    public static JumpPad getJumpPadLookingAt(Player player) {
        Block block = player.getTargetBlockExact(100);
        if (block == null) return null;
        String stringLoc = locToString(block.getLocation());
        return SimpleJumpPads.getInstance().getJumpPadManager().getJumpPad(stringLoc);
    }

    public static Vector getDirectionVector(Player player,JumpPadDirection direction) {
        switch (direction) {
            case UP: return new Vector(0, 1, 0);
            case DOWN: return new Vector(0, -1, 0);
            case FACING: return player.getLocation().getDirection().normalize();
            case NORTH: return new Vector(0, 0, -1);
            case SOUTH: return new Vector(0, 0, 1);
            case EAST: return new Vector(1, 0, 0);
            case WEST: return new Vector(-1, 0, 0);
            default: return player.getLocation().getDirection().clone();
        }
    }

    public static FallingBlock spawnFallingBlock(Entity entity, double yOffset) {
        Material mat = Material.MOVING_PISTON;
        FallingBlock fallingBlock =  entity.getWorld().spawnFallingBlock(entity.getLocation().add(0.0D, yOffset, 0.0D), mat.createBlockData());
        fallingBlock.setDropItem(false);
        fallingBlock.setCustomName("JumppadsEntity");
        return fallingBlock;
    }
}
