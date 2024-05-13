package me.messdaniel.simplejumppads.jumppad;

import me.messdaniel.simplejumppads.SimpleJumpPads;
import me.messdaniel.simplejumppads.utils.OtherUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;


public class JumpPad {

    private String name;
    private Material material = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
    private Location location;
    private String stringLoc;
    private double angel = 0;
    private double direction = 0;
    private double power = 1;
    private JumpPadType type = JumpPadType.VECTOR;

    public JumpPad(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStringLoc() {
        return stringLoc;
    }

    public void setStringLoc(String stringLoc) {
        this.stringLoc = stringLoc;
    }

    public double getAngel() {
        return angel;
    }

    public void setAngel(double angel) {
        this.angel = angel;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public JumpPadType getType() {
        return type;
    }

    public void setType(JumpPadType type) {
        this.type = type;
    }

    public void save() {
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(SimpleJumpPads.getInstance().getDataFolder(),"JumpPads.yml");
                YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
                fc.set("jumppads." + name + ".loc.world",location.getWorld().getName());
                fc.set("jumppads." + name + ".loc.x",location.getBlockX());
                fc.set("jumppads." + name + ".loc.y",location.getBlockY());
                fc.set("jumppads." + name + ".loc.z",location.getBlockZ());
                fc.set("jumppads." + name + ".material",material.name());
                fc.set("jumppads." + name + ".power",power);
                fc.set("jumppads." + name + ".angel",angel);
                fc.set("jumppads." + name + ".direction",direction);
                fc.set("jumppads." + name + ".type",type.getName());
                try {
                    fc.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(SimpleJumpPads.getInstance());
    }

    public void delete() {
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(SimpleJumpPads.getInstance().getDataFolder(),"JumpPads.yml");
                YamlConfiguration fc = YamlConfiguration.loadConfiguration(file);
                fc.set("jumppads." + name,null);
                SimpleJumpPads.getInstance().getJumpPadManager().getAllJumpPads().remove(stringLoc);
                try {
                    fc.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(SimpleJumpPads.getInstance());
    }
    public static void createNew(Player player, String name) {
        JumpPad jumpPad = new JumpPad(name);
        Location loc = player.getLocation();
        String stringLoc = OtherUtils.locToString(loc);
        jumpPad.setLocation(loc);
        jumpPad.setStringLoc(stringLoc);
        loc.getWorld().getBlockAt(loc).setType(jumpPad.getMaterial());
        SimpleJumpPads.getInstance().getJumpPadManager().getAllJumpPads().put(stringLoc,jumpPad);
        jumpPad.save();
    }
}
