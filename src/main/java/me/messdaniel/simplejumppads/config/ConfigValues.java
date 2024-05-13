package me.messdaniel.simplejumppads.config;

import me.messdaniel.simplejumppads.SimpleJumpPads;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValues {

    private static double distanceFallingEntity = 10;
    private static double distanceEndLocation = 10;
    private static boolean removeVelocityAtLaunch = true;
    private static boolean gamemodeSurvival = true;
    private static boolean gamemodeAdventure = true;
    private static boolean gamemodeCreative = true;
    private static boolean gamemodeSpectator = true;

    public static void load() {
        FileConfiguration fc = SimpleJumpPads.getInstance().getConfig();
        distanceFallingEntity = fc.getDouble("distance-falling-entity");
        distanceEndLocation = fc.getDouble("distance-end-location");
        removeVelocityAtLaunch = fc.getBoolean("remove-velocity-at-launch");
        gamemodeSurvival = fc.getBoolean("gamemodes.survival");
        gamemodeAdventure = fc.getBoolean("gamemodes.adventure");
        gamemodeCreative = fc.getBoolean("gamemodes.creative");
        gamemodeSpectator = fc.getBoolean("gamemodes.spectator");
    }


    public static double getDistanceFallingEntity() {
        return distanceFallingEntity;
    }

    public static double getDistanceEndLocation() {
        return distanceEndLocation;
    }

    public static boolean isRemoveVelocityAtLaunch() {
        return removeVelocityAtLaunch;
    }

    public static boolean isGamemodeSurvival() {
        return gamemodeSurvival;
    }

    public static boolean isGamemodeAdventure() {
        return gamemodeAdventure;
    }

    public static boolean isGamemodeCreative() {
        return gamemodeCreative;
    }

    public static boolean isGamemodeSpectator() {
        return gamemodeSpectator;
    }
}
