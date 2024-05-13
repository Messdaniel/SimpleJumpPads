package me.messdaniel.simplejumppads;

import me.messdaniel.simplejumppads.config.ConfigValues;
import me.messdaniel.simplejumppads.jumppad.JumpPadDirection;
import me.messdaniel.simplejumppads.jumppad.JumpPad;
import me.messdaniel.simplejumppads.jumppad.JumpPadType;
import me.messdaniel.simplejumppads.playerdata.PlayerData;
import me.messdaniel.simplejumppads.utils.OtherUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class JumpPadManager {

    private HashMap<String, JumpPad> allJumpPads;
    private ArrayList<UUID> jumpPadsFallingEntityUUIDS;
    private ArrayList <String> jumpPadsWorlds;

    public JumpPadManager() {
        load();
    }

    public boolean load() {
        allJumpPads = new HashMap<>();
        jumpPadsWorlds = new ArrayList<>();
        jumpPadsFallingEntityUUIDS = new ArrayList<>();
        File file = new File(SimpleJumpPads.getInstance().getDataFolder(),"JumpPads.yml");
        YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection cs = fc.getConfigurationSection("jumppads");
        if (cs == null) {
            SimpleJumpPads.getInstance().getLogger().warning("Could not find ConfigurationSection 'jumppads' in file 'JumpPads.yml'");
            SimpleJumpPads.getInstance().getLogger().warning("Delete the file 'JumpPads.yml' to see how it should look");
            return false;
        }
        boolean reloadSuccessfully = true;
        for (String key : cs.getKeys(false)) {
            JumpPad jumpPad = new JumpPad(key);
            boolean setMaterial = true;
            Material material = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
            try {
                material = Material.valueOf(cs.getString(key + ".material").toUpperCase());
            } catch (IllegalArgumentException e) {
                SimpleJumpPads.getInstance().getLogger().warning("Warning: Invalid material for jumppad '" + key + "'.");
                setMaterial = false;
                reloadSuccessfully = false;
            }
            String worldname = cs.getString(key + ".loc.world");
            World world = Bukkit.getWorld(worldname);
            double x = cs.getDouble(key + ".loc.x");
            double y = cs.getDouble(key + ".loc.y");
            double z = cs.getDouble(key + ".loc.z");
            Location loc = new Location(world,x,y,z);

            String stringLoc = OtherUtils.locToString(loc);
            if (world == null) {
                SimpleJumpPads.getInstance().getLogger().warning("Warning: Could not find the world '" + worldname + "' for jumppad '" + key + "'.");
                reloadSuccessfully = false;
            } else {
                world.getBlockAt(loc).setType(material);
            }

            double power = cs.getDouble(key + ".power");
            boolean setDirection = true;
            double angel = cs.getDouble(key + ".angel",0);
            double direction = cs.getDouble(key + ".direction",0);
            boolean setType = true;
            JumpPadType type = JumpPadType.VECTOR;
            try {
                type = JumpPadType.get(cs.getString(key + ".type"));
            } catch (IllegalArgumentException e) {
                SimpleJumpPads.getInstance().getLogger().warning("Warning: Invalid type for jumppad '" + key + "'.");
                setType = false;
                reloadSuccessfully = false;
            }

            if (setMaterial) jumpPad.setMaterial(material);
            jumpPad.setLocation(loc);
            jumpPad.setStringLoc(stringLoc);
            jumpPad.setAngel(angel);
            jumpPad.setDirection(direction);
            jumpPad.setPower(power);
            if (setType) jumpPad.setType(type);

            allJumpPads.put(stringLoc,jumpPad);
            jumpPadsWorlds.add(worldname);
        }

        SimpleJumpPads.getInstance().getLogger().info("Successfully loaded " + allJumpPads.size() + " JumpPads");
        return reloadSuccessfully;
    }

    public boolean worldHasJumpPad(String world) {
        return getJumpPadsWorlds().contains(world);
    }

    public JumpPad getJumpPad(String loc) {
        return getAllJumpPads().get(loc);
    }
    public JumpPad getJumpPadByName(String name) {
        for (JumpPad jumpPad : getAllJumpPads().values()) {
            if (jumpPad.getName().equals(name)) return jumpPad;
        }
        return null;
    }

    public boolean teleportPlayer(Player player,JumpPad jumpPad) {
        Location loc = jumpPad.getLocation().clone();
        if (jumpPad.getMaterial().toString().contains("PRESSURE_PLATE")) loc.subtract(0,1,0);
        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    Location clone = loc.clone().add(x, 0, z);
                    if (clone.getWorld().getBlockAt(clone).getType().isSolid() && clone.getWorld().getBlockAt(clone.add(0, 1, 0)).isEmpty() && clone.getWorld().getBlockAt(clone.add(0, 1, 0)).isEmpty()) {
                        Location destination = clone.add(0.500, -1, 0.500);
                        destination.setDirection(loc.toVector().subtract(destination.toVector().add(new Vector(0, 1, 0))));
                        player.teleport(destination);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void launchPlayer(Player player,JumpPad jumpPad) {
        PlayerData playerData = PlayerData.getPlayerData(player);
        if (playerData.isLaunched()) return;
        playerData.setLaunched(true);
        playerData.setLaunchTime(System.currentTimeMillis());
        if (ConfigValues.isRemoveVelocityAtLaunch()) player.setVelocity(new Vector(0,0,0));


        Location launchLocation = jumpPad.getLocation().clone();
        launchLocation.setPitch((float) jumpPad.getAngel());
        launchLocation.setYaw((float) jumpPad.getDirection());
        Vector velocity = launchLocation.getDirection().normalize().multiply(jumpPad.getPower() / 10);

        if (jumpPad.getType() == JumpPadType.VECTOR) {
            player.setVelocity(velocity);
        } else if (jumpPad.getType() == JumpPadType.FALLINGENTITY) {
            if (!player.getAllowFlight()) {
                playerData.setResetFlight(true);
                player.setAllowFlight(true);
            }
            playerData.setFallingBlock(OtherUtils.spawnFallingBlock(player,1));
            playerData.getFallingBlock().setVelocity(velocity);
            getJumpPadsFallingEntityUUIDS().add(playerData.getFallingBlock().getUniqueId());
            new BukkitRunnable() {
                private boolean endFlight = false;
                private boolean roundOne = true;

                public void run() {
                    player.setFallDistance(0.0F);
                    FallingBlock fBlock = playerData.getFallingBlock();
                    if (fBlock != null) {
                        fBlock.setTicksLived(1);
                        if (fBlock.getWorld() != null && !player.getLocation().getWorld().equals(fBlock.getWorld()) ||
                                fBlock.getLocation().clone().add(fBlock.getLocation().getDirection().clone().multiply(0.5D)).getBlock().isLiquid()) {
                            this.endFlight = true;
                        }

                        if (!fBlock.isDead() && playerData.isLaunched()) {
                            player.setVelocity(fBlock.getVelocity());
                        }

                        if (!fBlock.isOnGround() && player.getLocation().getWorld().equals(fBlock.getLocation().getWorld()) &&
                                player.getLocation().distance(fBlock.getLocation()) >= ConfigValues.getDistanceFallingEntity() && player.getLocation().getWorld().equals(fBlock.getLocation().getWorld())) {
                            player.teleport(new Location(fBlock.getWorld(), fBlock.getLocation().getX(), fBlock.getLocation().getY(), fBlock.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        }
                    }
                    if (player.isOnGround() && !this.roundOne || fBlock == null || fBlock.isOnGround() || fBlock.isDead() || fBlock.getLocation().getY() < -10.0D || fBlock.getVelocity().length() == 0.0D || this.endFlight || player.getLocation().getBlock().getType() == Material.LADDER) {
                        if (fBlock != null) {
                            if (fBlock.isOnGround() && player.getLocation().getWorld().equals(fBlock.getLocation().getWorld()) &&
                                    player.getLocation().distance(fBlock.getLocation()) >= ConfigValues.getDistanceEndLocation() && player.getLocation().getWorld().equals(fBlock.getLocation().getWorld())) {
                                player.teleport(new Location(fBlock.getWorld(), fBlock.getLocation().getX(), fBlock.getLocation().getY(), fBlock.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                            }
                            fBlock.remove();
                            getJumpPadsFallingEntityUUIDS().remove(fBlock.getUniqueId());
                        }

                        if (playerData.isResetFlight()) {
                            playerData.setResetFlight(false);
                            player.setAllowFlight(false);
                        }
                        playerData.setLaunched(false);
                        playerData.setFallingBlock(null);
                        player.setFlying(false);
                        this.cancel();
                    }

                    this.roundOne = false;
                }
            }.runTaskTimer(SimpleJumpPads.getInstance(), 0L, 1L);
        }
    }

    public HashMap<String, JumpPad> getAllJumpPads() {
        return allJumpPads;
    }

    public ArrayList<String> getJumpPadsWorlds() {
        return jumpPadsWorlds;
    }

    public ArrayList<UUID> getJumpPadsFallingEntityUUIDS() {
        return jumpPadsFallingEntityUUIDS;
    }
}
