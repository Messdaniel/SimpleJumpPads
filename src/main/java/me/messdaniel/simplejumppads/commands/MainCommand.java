package me.messdaniel.simplejumppads.commands;

import me.messdaniel.simplejumppads.SimpleJumpPads;
import me.messdaniel.simplejumppads.config.ConfigValues;
import me.messdaniel.simplejumppads.jumppad.JumpPadDirection;
import me.messdaniel.simplejumppads.jumppad.JumpPad;
import me.messdaniel.simplejumppads.jumppad.JumpPadType;
import me.messdaniel.simplejumppads.JumpPadManager;
import me.messdaniel.simplejumppads.playerdata.PlayerData;
import me.messdaniel.simplejumppads.utils.MessagesUtils;
import me.messdaniel.simplejumppads.utils.OtherUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private JumpPadManager jumpPadManager = SimpleJumpPads.getInstance().getJumpPadManager();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("simplejumppads.reload")) {
                MessagesUtils.sendMessage(player,"dont-have-perms");
                return true;
            }
            if (args.length == 0) {
                MessagesUtils.sendMessage(player,"command-usage");
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "reload": {
                    SimpleJumpPads.getInstance().reloadConfig();
                    SimpleJumpPads.getInstance().createYmlFiles();
                    MessagesUtils.load();
                    PlayerData.reload();
                    boolean reload = SimpleJumpPads.getInstance().getJumpPadManager().load();
                    if (!reload) {
                        MessagesUtils.sendMessage(player,"reload-not-successful");
                    } else {
                        MessagesUtils.sendMessage(player,"reload-successful");
                    }
                    ConfigValues.load();
                    return true;
                }
                case "list": {
                    TextComponent top = new TextComponent("§a----------- Page: §f" + 1 + "/" + 1 + "§a-----------");
                    for (JumpPad jumpPad : jumpPadManager.getAllJumpPads().values()) {
                        TextComponent info = new TextComponent("\n §7* §e" + jumpPad.getName());
                        info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                                "§aInfo about JumpPad: \n" +
                                "§eLocation: §f" + jumpPad.getStringLoc() + "\n" +
                                "§eMaterial: §f" + jumpPad.getMaterial() + "\n" +
                                "§ePower: §f" + jumpPad.getPower() + "\n" +
                                "§eAngel: §f" + jumpPad.getAngel() + "\n" +
                                "§eDirection: §f" + jumpPad.getDirection() + "\n" +
                                "§eType: §f" + jumpPad.getType().getName() + "\n" +
                                "\n" +
                                "§7Click to teleport")));
                        info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sjp Teleport " + jumpPad.getName()));
                        top.addExtra(info);
                    }
                    player.spigot().sendMessage(top);
                    return true;
                }
                case "info": {
                    JumpPad jumpPad = OtherUtils.getJumpPadLookingAt(player);
                    if (jumpPad == null) {
                        MessagesUtils.sendMessage(player,"no-jumppad-found");
                        return true;
                    }
                    TextComponent top = new TextComponent("§aInfo about JumpPad: \n");
                    TextComponent name = new TextComponent("§eName: §f" + jumpPad.getName() + "\n");
                    TextComponent loc = new TextComponent("§eLocation: §f" + jumpPad.getStringLoc() + "\n");
                    TextComponent material = new TextComponent("§eMaterial: §f" + jumpPad.getMaterial() + "\n");
                    TextComponent power = new TextComponent("§ePower: §f" + jumpPad.getPower() + "\n");
                    TextComponent angel = new TextComponent("§eAngel: §f" + jumpPad.getAngel() + "\n");
                    TextComponent direction = new TextComponent("§eDirection: §f" + jumpPad.getDirection() + "\n");
                    TextComponent type = new TextComponent("§eType: §f" + jumpPad.getType().getName() + "\n");
                    top.addExtra(name);
                    top.addExtra(loc);
                    top.addExtra(material);
                    top.addExtra(power);
                    top.addExtra(angel);
                    top.addExtra(direction);
                    top.addExtra(type);
                    player.spigot().sendMessage(top);
                    return true;
                }
                case "help": {
                    TextComponent top = new TextComponent("§a----------- §fAll Commands §a-----------\n ");
                    TextComponent help = new TextComponent("§7* §e/sjp Help" +  "\n ");
                    help.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eSends a list of all sjp commands with a description about what the command does \n  " +
                            "\n" +
                            "§7Click to run command")));
                    help.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sjp Help"));
                    TextComponent list = new TextComponent("§7* §e/sjp List" +  "\n ");
                    list.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eSends a list of all JumpPads created\n  " +
                             "\n" +
                            "§7Click to run command")));
                    list.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sjp List"));
                    TextComponent reload = new TextComponent("§7* §e/sjp Reload" +  "\n ");
                    reload.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eReloads this plugin\n  " +
                            "\n" +
                            "§7Click to run command")));
                    reload.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sjp Reload"));
                    TextComponent info = new TextComponent("§7* §e/sjp Info" +  "\n ");
                    info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eShows info about a JumpPad the player is looking at\n" +
                             "\n" +
                             "§7Click to run command")));
                    info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sjp Info"));
                    TextComponent set = new TextComponent("§7* §e/sjp Set <attribute> <value>" +  "\n ");
                    set.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eSets or changes a attribute about a targeted JumpPad\n" +
                            "§eAttributes:\n  " +
                            "§7* §fMaterial\n  " +
                            "§7* §fLocation\n  " +
                            "§7* §fPower\n  " +
                            "§7* §fDirection\n  " +
                            "§7* §fAngel\n  " +
                            "§7* §fType\n  " +
                            "\n" +
                            "§7Click to run command")));
                    set.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sjp Set "));
                    TextComponent teleport = new TextComponent("§7* §e/sjp Teleport <JumpPad>" +  "\n ");
                    teleport.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eTeleports to a JumpPad\n" +
                            "\n" +
                            "§7Click to run command")));
                    teleport.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sjp Teleport "));
                    TextComponent create = new TextComponent("§7* §e/sjp Create <name>" +  "\n ");
                    create.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eCreates a new JumpPad at your current location\n" +
                            "\n" +
                            "§7Click to run command")));
                    create.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sjp Create "));
                    TextComponent delete = new TextComponent("§7* §e/sjp Delete" +  "\n ");
                    delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(
                            "§eDeletes the targeted JumpPad\n" +
                            "\n" +
                            "§7Click to run command")));
                    delete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sjp Delete"));

                    top.addExtra(help);
                    top.addExtra(list);
                    top.addExtra(reload);
                    top.addExtra(info);
                    top.addExtra(set);
                    top.addExtra(teleport);
                    top.addExtra(create);
                    top.addExtra(delete);
                    player.spigot().sendMessage(top);
                    return true;
                }
                case "set": {
                    if (args.length == 2 && !args[1].equalsIgnoreCase("location")) {
                        MessagesUtils.sendMessage(player,"command-usage");
                        return true;
                    } else if (args.length < 3) {
                        MessagesUtils.sendMessage(player,"command-usage");
                        return true;
                    }
                    JumpPad jumpPad = OtherUtils.getJumpPadLookingAt(player);
                    if (jumpPad == null) {
                        MessagesUtils.sendMessage(player,"no-jumppad-found");
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "power": {
                            if (args[2].matches("-?\\d+(\\.\\d+)?")) {
                                jumpPad.setPower(Double.parseDouble(args[2]));
                                jumpPad.save();
                                MessagesUtils.sendMessage(player,"updated-jumppad","name",jumpPad.getName());
                            } else {
                                MessagesUtils.sendMessage(player,"invalid-number-format");
                            }
                            return true;
                        }
                        case "angel": {
                            if (args[2].matches("-?\\d+(\\.\\d+)?")) {
                                jumpPad.setAngel(Double.parseDouble(args[2]));
                                jumpPad.save();
                                MessagesUtils.sendMessage(player,"updated-jumppad","name",jumpPad.getName());
                            } else {
                                MessagesUtils.sendMessage(player,"invalid-number-format");
                            }
                            return true;
                        }
                        case "direction": {
                            if (args[2].matches("-?\\d+(\\.\\d+)?")) {
                                jumpPad.setDirection(Double.parseDouble(args[2]));
                                jumpPad.save();
                                MessagesUtils.sendMessage(player,"updated-jumppad","name",jumpPad.getName());
                            } else {
                                MessagesUtils.sendMessage(player,"invalid-number-format");
                            }
                            return true;
                        }
                        case "type": {
                            JumpPadType type = JumpPadType.get(args[2]);
                            if (type == null) {
                                MessagesUtils.sendMessage(player,"invalid-type");
                                return true;
                            }
                            MessagesUtils.sendMessage(player,"updated-jumppad","name",jumpPad.getName());
                            jumpPad.setType(type);
                            jumpPad.save();
                            return true;
                        }
                        case "material": {
                            Material material;
                            try {
                                material = Material.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                MessagesUtils.sendMessage(player,"invalid-material");
                                return true;
                            }
                            MessagesUtils.sendMessage(player,"updated-jumppad","name",jumpPad.getName());
                            jumpPad.setMaterial(material);
                            jumpPad.getLocation().getWorld().getBlockAt(jumpPad.getLocation()).setType(material);
                            jumpPad.save();
                            return true;
                        }
                        case "location": {
                            Location loc = player.getLocation();
                            String stringLoc = OtherUtils.locToString(loc);

                            MessagesUtils.sendMessage(player,"updated-jumppad","name",jumpPad.getName());
                            jumpPad.setLocation(loc);
                            jumpPadManager.getAllJumpPads().remove(jumpPad.getStringLoc());
                            jumpPad.setStringLoc(stringLoc);
                            jumpPadManager.getAllJumpPads().put(jumpPad.getStringLoc(),jumpPad);

                            jumpPad.getLocation().getWorld().getBlockAt(jumpPad.getLocation()).setType(jumpPad.getMaterial());
                            jumpPad.save();
                            return true;
                        }
                        default: {
                            MessagesUtils.sendMessage(player,"command-usage");
                            return true;
                        }
                    }
                }
                case "teleport": {
                    if (args.length < 2) {
                        MessagesUtils.sendMessage(player,"command-usage");
                        return true;
                    }
                    JumpPad jumpPad = jumpPadManager.getJumpPadByName(args[1]);
                    if (jumpPad == null) {
                        MessagesUtils.sendMessage(player,"invalid-jumppad","name",args[1]);
                        return true;
                    }
                    boolean teleport = jumpPadManager.teleportPlayer(player,jumpPad);
                    if (teleport) MessagesUtils.sendMessage(player,"teleport-successful","name",args[1]);
                    else MessagesUtils.sendMessage(player,"teleport-not-successful","name",args[1]);
                    return true;
                }
                case "delete": {
                    JumpPad jumpPad = OtherUtils.getJumpPadLookingAt(player);
                    if (jumpPad == null) {
                        MessagesUtils.sendMessage(player,"no-jumppad-found");
                        return true;
                    }
                    jumpPad.delete();
                    MessagesUtils.sendMessage(player,"deleted-jumppad","name",jumpPad.getName());
                    return true;
                }
                case "create": {
                    String name = args.length == 1 ? String.valueOf(jumpPadManager.getAllJumpPads().size() + 1) : args[1];
                    JumpPad.createNew(player,name);
                    MessagesUtils.sendMessage(player,"jumppad-created","name",name);
                    return true;
                }

                default: {
                    MessagesUtils.sendMessage(player,"command-usage");
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("Reload");
            completions.add("Info");
            completions.add("List");
            completions.add("Set");
            completions.add("Teleport");
            completions.add("Create");
            completions.add("Delete");
            completions.add("Help");
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "set": {
                    completions.add("Material");
                    completions.add("Location");
                    completions.add("Power");
                    completions.add("Angel");
                    completions.add("Direction");
                    completions.add("Type");
                    break;
                }
                case "teleport": {
                    for (JumpPad jumpPad : jumpPadManager.getAllJumpPads().values()) {
                        completions.add(jumpPad.getName());
                    }
                    break;
                }
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "set": {
                    switch (args[1].toLowerCase()) {
                        case "material": {
                            for (Material material : Material.values()) {
                                if (!material.isBlock()) continue;
                                completions.add(material.name());
                            }
                            break;
                        }
                        case "direction": {
                            for (JumpPadDirection direction : JumpPadDirection.values()) {
                                completions.add(direction.getName());
                            }
                            break;
                        }
                        case "type": {
                            for (JumpPadType type : JumpPadType.values()) {
                                completions.add(type.getName());
                            }
                        }
                    }
                }
            }
        }
        completions.removeIf(completion -> !completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        Collections.sort(completions);
        return completions;
    }
}
