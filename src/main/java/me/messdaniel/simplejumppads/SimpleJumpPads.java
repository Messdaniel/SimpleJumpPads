package me.messdaniel.simplejumppads;

import me.messdaniel.simplejumppads.commands.MainCommand;
import me.messdaniel.simplejumppads.config.ConfigValues;
import me.messdaniel.simplejumppads.listener.EntityBlockEvent;
import me.messdaniel.simplejumppads.listener.PlayerMoveEvent;
import me.messdaniel.simplejumppads.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleJumpPads extends JavaPlugin {

    private static SimpleJumpPads instance;

    private JumpPadManager jumpPadManager;

    @Override
    public void onEnable() {
        instance = this;
        createYmlFiles();
        MessagesUtils.load();

        jumpPadManager = new JumpPadManager();
        ConfigValues.load();

        Bukkit.getPluginManager().registerEvents(new PlayerMoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new EntityBlockEvent(), this);

        getCommand("simplejumppads").setExecutor(new MainCommand());

    }

    public void createYmlFiles() {
        saveDefaultResource("config");
        saveDefaultResource("JumpPads");
    }

    void saveDefaultResource(String resource) {
        String path = resource + ".yml";
        File file = new File(getDataFolder(), path);
        if (!file.exists()) {
            this.saveResource(path, false);
        }
    }

    public static SimpleJumpPads getInstance() {
        return instance;
    }

    public JumpPadManager getJumpPadManager() {
        return jumpPadManager;
    }
}
