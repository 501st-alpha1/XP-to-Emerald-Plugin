/* Copyright (c) 2012-2015 Scott Weldon
 *
 *  The XP to Emerald Plugin is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  The XP to Emerald Plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.scott_weldon.xp_to_emerald;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.scott_weldon.xp_to_emerald.bukkit1_6.Player1_6;
import com.scott_weldon.xp_to_emerald.common.interfaces.PlayerInterface;

public final class XPtoEmerald extends JavaPlugin {
  private Server server;
  private FileConfiguration config;

  private int scale;
  private Material material;
  private String convXP;
  private String convEm;
  private HashMap<String, Integer> worldScales;
  private HashMap<String, Material> worldMaterials;

  protected static final int COMMAND = 0;
  protected static final int SIGN = 1;

  private static final String SCALE = "conversion_scale";
  private static final String MATERIAL = "material";
  private static final String SET_SCALE = "setscale";
  private static final String SET_MATERIAL = "setmaterial";
  private static final String CONV_XP = "convert_xp_using";
  private static final String CONV_EM = "convert_emeralds_using";

  @Override
  public void onEnable() {
    server = Bukkit.getServer();
    config = this.getConfig();

    worldScales = new HashMap<String, Integer>();
    worldMaterials = new HashMap<String, Material>();

    reload(server.getConsoleSender());

    new XPtoEmeraldListener(this);

    getLogger().log(Level.INFO, "XP to Emerald enabled!");
  }

  @Override
  public void onDisable() {
    getLogger().log(Level.INFO, "XP to Emerald disabled. Bye!");
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (args.length > 3) {
      return false;
    }

    if (cmd.getName().equalsIgnoreCase("xptoemerald")
        || cmd.getName().equalsIgnoreCase("xp2em")) {
      return xteCommand(sender, args);
    }
    else if (cmd.getName().equalsIgnoreCase("emeraldtoxp")
        || cmd.getName().equalsIgnoreCase("em2xp")) {
      return etxCommand(sender, args);
    }
    else {
      return false;
    }
  }

  public boolean xteCommand(CommandSender sender, String[] args) {
    if (sender instanceof Player) {
      Player bukkitPlayer;
      int xp;

      if (args.length == 3) {
        if (args[0].equalsIgnoreCase(SET_SCALE)) {
          return setScale(sender, Integer.parseInt(args[1]), args[2]);
        }
        else if (args[0].equalsIgnoreCase(SET_MATERIAL)) {
          return setMaterial(sender, args[1], args[2]);
        }
        else {
          return false;
        }
      }
      else if (args.length == 2) {
        if (args[0].equalsIgnoreCase(SET_SCALE)) {
          return setScale(sender, Integer.parseInt(args[1]));
        }
        else if (args[0].equalsIgnoreCase(SET_MATERIAL)) {
          return setMaterial(sender, args[1]);
        }
        else {
          bukkitPlayer = server.getPlayer(args[0]);
          xp = Integer.parseInt(args[1]);

          if (!playerOnline(bukkitPlayer)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }
          else
            return xtePermCheck(sender, bukkitPlayer, xp, COMMAND);
        }
      }
      else if (args.length == 1) {
        try {
          xp = Integer.parseInt(args[0]);
          bukkitPlayer = (Player) sender;
          return xtePermCheck(sender, bukkitPlayer, xp, COMMAND);
        }
        catch (NumberFormatException e) {
          if (args[0].equalsIgnoreCase("reload")) {
            return reload(sender);
          }
          bukkitPlayer = server.getPlayer(args[0]);
          if (!playerOnline(bukkitPlayer)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }
          return xtePermCheck(sender, bukkitPlayer, 0, COMMAND);
        }
      }
      else if (args.length == 0) {
        bukkitPlayer = (Player) sender;
        return xtePermCheck(sender, bukkitPlayer, 0, COMMAND);
      }
    }
    else { // Command sent from console.
      if (args.length == 3) {
        if (args[0].equalsIgnoreCase(SET_SCALE)) {
          return setScale(sender, Integer.parseInt(args[1]), args[2]);
        }
        else if (args[0].equalsIgnoreCase(SET_MATERIAL)) {
          return setMaterial(sender, args[1], args[2]);
        }
        else {
          return false;
        }
      }
      else if (args.length == 2) {
        if (args[0].equalsIgnoreCase(SET_SCALE)) {
          return setScale(sender, Integer.parseInt(args[1]));
        }
        else if (args[0].equalsIgnoreCase(SET_MATERIAL)) {
          return setMaterial(sender, args[1]);
        }
        else {
          Player bukkitPlayer = server.getPlayer(args[0]);
          int xp = Integer.parseInt(args[1]);

          if (!playerOnline(bukkitPlayer)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }

          if (xpToEmerald(bukkitPlayer, xp)) {
            sender.sendMessage("Converted " + xp + " of player "
                + bukkitPlayer.getDisplayName() + "'s xp to Emeralds.");
            return true;
          }
          else
            return false;
        }
      }
      else if (args.length == 1) {
        if (args[0].equalsIgnoreCase("reload")) {
          return reload(sender);
        }

        Player bukkitPlayer = server.getPlayer(args[0]);

        if (!playerOnline(bukkitPlayer)) {
          sender.sendMessage("Player " + args[0]
              + " does not exist or is not online.");
          return false;
        }

        if (xpToEmerald(bukkitPlayer, 0)) {
          sender.sendMessage("Converted all of player "
              + bukkitPlayer.getDisplayName() + "'s xp to Emeralds.");
          return true;
        }
        else
          return false;
      }
    }

    return false;
  }

  public boolean etxCommand(CommandSender sender, String[] args) {
    if (sender instanceof Player) {
      Player bukkitPlayer;
      int emeralds;

      if (args.length == 2) {
        bukkitPlayer = server.getPlayer(args[0]);
        emeralds = Integer.parseInt(args[1]);

        if (!playerOnline(bukkitPlayer)) {
          sender.sendMessage("Player " + args[0]
              + " does not exist or is not online.");
          return false;
        }

        etxPermCheck(sender, bukkitPlayer, emeralds, COMMAND);
      }
      else if (args.length == 1) {
        try {
          emeralds = Integer.parseInt(args[0]);
          bukkitPlayer = (Player) sender;
          return etxPermCheck(sender, bukkitPlayer, emeralds, COMMAND);
        }
        catch (NumberFormatException e) {
          bukkitPlayer = server.getPlayer(args[0]);
          if (!playerOnline(bukkitPlayer)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }
          return etxPermCheck(sender, bukkitPlayer, 0, COMMAND);
        }
      }
      else {
        bukkitPlayer = (Player) sender;
        return etxPermCheck(sender, bukkitPlayer, 0, COMMAND);
      }
    }
    else { // Command sent from console.
      if (args.length == 2) {
        Player bukkitPlayer = server.getPlayer(args[0]);
        int emeralds = Integer.parseInt(args[1]);

        if (!playerOnline(bukkitPlayer)) {
          sender.sendMessage("Player " + args[0]
              + " does not exist or is not online.");
          return false;
        }

        if (emeraldToXP(bukkitPlayer, emeralds)) {
          sender.sendMessage("Converted " + emeralds + " of player "
              + bukkitPlayer.getDisplayName() + "'s Emeralds to xp.");
          return true;
        }
        else
          return false;
      }
      else if (args.length == 1) {
        Player bukkitPlayer = server.getPlayer(args[0]);

        if (!playerOnline(bukkitPlayer)) {
          sender.sendMessage("Player " + args[0]
              + " does not exist or is not online.");
          return false;
        }

        if (emeraldToXP(bukkitPlayer, 0)) {
          sender.sendMessage("Converted all of player "
              + bukkitPlayer.getDisplayName() + "'s Emeralds to xp.");
          return true;
        }
        else
          return false;
      }
    }
    return false;
  }

  public boolean setScale(CommandSender sender, int scale) {
    return setScale(sender, scale, null);
  }

  public boolean setScale(CommandSender sender, int scale, String world) {
    if (sender.hasPermission("xptoemerald.admin")
        || sender instanceof ConsoleCommandSender) {
      if (world == null) {
        this.config.set(SCALE, scale);
        this.scale = scale;
      }
      else {
        this.config.set(world + ".conversion_scale", scale);
        this.worldScales.put(world, scale);
      }
      this.saveConfig();
      String worldName = (world == null) ? "all (default) worlds."
          : ("world \"" + world + "\"");
      sender.sendMessage("Scale set to " + scale + " for " + worldName);
      return true;
    }
    else {
      sender.sendMessage("You don't have permission for that!");
      return true;
    }
  }

  public boolean setMaterial(CommandSender sender, String material) {
    return setMaterial(sender, material, null);
  }

  public boolean setMaterial(CommandSender sender, String material, String world) {
    if (sender.hasPermission("xptoemerald.admin")
        || sender instanceof ConsoleCommandSender) {
      Material m = Material.getMaterial(material.toUpperCase());
      if (m == null) {
        sender.sendMessage("That isn't a valid material!");
        return true;
      }
      else {
        if (world == null) {
          this.config.set(MATERIAL, material);
          this.material = m;
        }
        else {
          this.config.set(world + ".material", material);
          this.worldMaterials.put(world, m);
        }
        this.saveConfig();
        String worldName = (world == null) ? "all (default) worlds."
            : ("world \"" + world + "\"");
        sender.sendMessage("Material set to " + m.toString() + " for "
            + worldName);
        return true;
      }
    }
    else {
      sender.sendMessage("You don't have permission for that!");
      return true;
    }
  }

  public boolean reload(CommandSender sender) {
    if (sender.hasPermission("xptoemerald.admin")
        || sender instanceof ConsoleCommandSender) {
      this.reloadConfig();
      this.config = this.getConfig();
      Set<String> configKeys = this.config.getKeys(true);

      // Set up global options
      this.scale = config.getInt(SCALE);

      Material m = Material.getMaterial(config.getString(MATERIAL)
          .toUpperCase());
      if (m == null) {
        getLogger().log(Level.WARNING,
            "Material found in config is not valid! Defaulting to Emeralds.");
        m = Material.EMERALD;
        this.config.set(MATERIAL, "emerald");
      }
      this.material = m;

      String convXP = config.getString(CONV_XP);
      if (!convXP.equalsIgnoreCase("emeralds")
          && !convXP.equalsIgnoreCase("xp")) {
        getLogger().log(Level.WARNING,
            "Invalid value for " + CONV_XP + ". Defaulting to Emeralds.");
        this.convXP = "Emeralds";
      }
      else {
        this.convXP = convXP;
      }

      String convEm = config.getString(CONV_EM);
      if (!convEm.equalsIgnoreCase("emeralds")
          && !convEm.equalsIgnoreCase("target_level")) {
        getLogger().log(Level.WARNING,
            "Invalid value for " + CONV_EM + ". Defaulting to Emeralds.");
        this.convEm = "Emeralds";
      }
      else {
        this.convEm = convEm;
      }

      // Set up world-specific options
      for (String fullPath : configKeys) {
        String[] splitPath = fullPath.split("\\.");
        if (splitPath.length < 2) {
          continue;
        }
        else {
          if (splitPath[1].equalsIgnoreCase(SCALE)) {
            int scale = this.config.getInt(fullPath);
            this.worldScales.put(splitPath[0], scale);
          }
          else if (splitPath[1].equalsIgnoreCase(MATERIAL)) {
            String material = this.config.getString(fullPath);
            Material mat = Material.getMaterial(material.toUpperCase());
            if (mat == null) {
              getLogger().log(
                  Level.WARNING,
                  "Material found in config for world " + splitPath[0]
                      + " is not valid! Defaulting to Emeralds.");
              mat = Material.EMERALD;
              this.config.set(fullPath, mat);
            }
            this.worldMaterials.put(splitPath[0], mat);
          }
          else {
            getLogger().log(
                Level.WARNING,
                "Invalid config key " + splitPath[1] + " for world "
                    + splitPath[0] + ". Skipping.");
          }
        }
      }

      this.saveConfig();
      sender.sendMessage("Configuration reloaded!");
      return true;
    }
    else {
      sender.sendMessage("You don't have permission for that!");
      return true;
    }
  }

  public boolean playerOnline(Player p) {
    if (p == null) {
      return false;
    }
    else if (!p.isOnline()) {
      return false;
    }
    else
      return true;
  }

  public boolean xtePermCheck(CommandSender sender, Player bukkitPlayer, int xp,
      int src) {
    if (bukkitPlayer.equals(sender)) {
      if (src == COMMAND) {
        if (sender.hasPermission("xptoemerald.convert.xte")) {
          return xpToEmerald(bukkitPlayer, xp);
        }
        else {
          sender.sendMessage("You don't have permission for that!");
          return true;
        }
      }
      else if (src == SIGN) {
        if (sender.hasPermission("xptoemerald.sign")) {
          return xpToEmerald(bukkitPlayer, xp);
        }
        else {
          sender.sendMessage("You don't have permission for that!");
          return true;
        }
      }
      else {
        sender.sendMessage("Something went wrong. Invalid argument src (coder"
            + "error).");
        return true;
      }
    }
    else {
      if (sender.hasPermission("xptoemerald.admin")) {
        return xpToEmerald(bukkitPlayer, xp);
      }
      else {
        sender.sendMessage("You don't have permission for that!");
        return true;
      }
    }
  }

  public boolean etxPermCheck(CommandSender sender, Player bukkitPlayer,
      int emeralds, int src) {
    if (bukkitPlayer.equals(sender)) {
      if (src == COMMAND) {
        if (sender.hasPermission("xptoemerald.convert.etx")) {
          return emeraldToXP(bukkitPlayer, emeralds);
        }
        else {
          sender.sendMessage("You don't have permission for that!");
          return true;
        }
      }
      else if (src == SIGN) {
        if (sender.hasPermission("xptoemerald.sign")) {
          return emeraldToXP(bukkitPlayer, emeralds);
        }
        else {
          sender.sendMessage("You don't have permission for that!");
          return true;
        }
      }
      else {
        sender.sendMessage("Something went wrong. Invalid argument src (coder"
            + "error).");
        return true;
      }
    }
    else {
      if (sender.hasPermission("xptoemerald.admin")) {
        return emeraldToXP(bukkitPlayer, emeralds);
      }
      else {
        sender.sendMessage("You don't have permission for that!");
        return true;
      }
    }
  }

  private boolean xpToEmerald(Player bukkitPlayer, int amount) {
    PlayerInterface player = new Player1_6(bukkitPlayer);
    String worldName = player.getCurrentWorldName();
    int scale = (worldScales.containsKey(worldName)) ? worldScales
        .get(worldName) : this.scale;
    Material material = (worldMaterials.containsKey(worldName)) ? worldMaterials
        .get(worldName) : this.material;
    int xp = (this.convXP.equalsIgnoreCase("emeralds")) ? (amount * scale)
        : amount;
    int exp = getTotalXP(player);
    if (xp == 0) {
      xp = exp;
    }

    InventoryInterface inventory = player.getInventory();
    int emeralds = xp / scale;
    xp -= xp % scale;

    if (xp > exp) {
      player.sendMessage("You only have " + exp + " XP!");
      return true;
    }
    else if (xp == 0) {
      player.sendMessage("You need at least " + scale + " XP!");
      return true;
    }
    setTotalXP(player, exp - xp);
    inventory.addItems(material.toString(), emeralds);

    return true;
  }

  private boolean emeraldToXP(Player bukkitPlayer, int value) {
    PlayerInterface player = new Player1_6(bukkitPlayer);
    String worldName = player.getCurrentWorldName();
    int scale = (worldScales.containsKey(worldName)) ? worldScales
        .get(worldName) : this.scale;
    Material material = (worldMaterials.containsKey(worldName)) ? worldMaterials
        .get(worldName) : this.material;
    PlayerInventory inventory = bukkitPlayer.getInventory();

    int emeralds = this.convEm.equalsIgnoreCase("target_level") ? (int) Math
        .ceil((getXPatLevel(value) - getXPatLevel(player.getLevel())) / scale)
        : value;

    int numOfEmeralds = 0;
    for (ItemStack item : inventory) {
      if (item == null) {
        continue;
      }
      if (item.getType() == material) {
        numOfEmeralds += item.getAmount();
      }
    }

    if (numOfEmeralds == 0) {
      player.sendMessage("You don't have any " + material.toString() + "s!");
      return true;
    }
    if (emeralds == 0) {
      emeralds = numOfEmeralds;
    }

    inventory.remove(material.getId());

    if (numOfEmeralds < emeralds) {
      player.sendMessage("You only have " + numOfEmeralds + " "
                         + material.toString() + "s!");
      inventory.addItem(new ItemStack(material, numOfEmeralds));
      return true;
    }

    int exp = getTotalXP(player) + (emeralds * scale);
    setTotalXP(player, exp);
    if ((numOfEmeralds - emeralds) > 0) {
      inventory.addItem(new ItemStack(material, numOfEmeralds - emeralds));
    }

    return true;
  }

  private int getXPtoNext(int level) {
    if (level < 16) {
      return 17;
    }
    else if (level < 31) {
      return 3 * level - 28;
    }
    else {
      return 7 * level - 148;
    }
  }

  private int getXPatLevel(int level) {
    if (level < 16) {
      return 17 * level;
    }
    else if (level < 31) {
      return (int) (1.5 * level * level - 29.5 * level + 360);
    }
    else {
      return (int) (3.5 * level * level - 151.5 * level + 2220);
    }
  }

  private int getTotalXP(PlayerInterface player) {
    int xp = 0;
    int level = player.getLevel();
    float xpPct = player.getExp();
    int xpToNext;

    xp = getXPatLevel(level);
    xpToNext = getXPtoNext(level);

    xp += (int) (xpToNext * xpPct);

    return xp;
  }

  private void setTotalXP(PlayerInterface player, int xp) {
    int level = 0;
    int currXP = xp;
    int xpToNext = 17;

    while (level < 15) {
      if (currXP > xpToNext) {
        currXP -= xpToNext;
        level++;
      }
      else {
        break;
      }
    }
    while ((level >= 15) && (level < 30)) {
      xpToNext = 3 * level - 28;
      if (currXP > xpToNext) {
        currXP -= xpToNext;
        level++;
      }
      else {
        break;
      }
    }
    while (level >= 30) {
      xpToNext = 7 * level - 148;
      if (currXP > xpToNext) {
        currXP -= xpToNext;
        level++;
      }
      else {
        break;
      }
    }

    float xpPct = (float) (currXP * 1.0 / xpToNext);

    player.setLevel(level);
    player.setExp(xpPct);
    return;
  }
}
