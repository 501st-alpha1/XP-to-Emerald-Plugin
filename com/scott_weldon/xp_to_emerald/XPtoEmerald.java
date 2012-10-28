package com.scott_weldon.xp_to_emerald;

import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class XPtoEmerald extends JavaPlugin {
  public static final int SCALE = 20;

  @Override
  public void onEnable() {
  }

  @Override
  public void onDisable() {
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (args.length < 1 || args.length > 1) {
      return false;
    }
    if (cmd.getName().equalsIgnoreCase("xptoemerald")) {
      if (sender instanceof Player) {
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        // Permissions stuff here?
        int expToUse = Integer.parseInt(args[0]);
        int emeralds = expToUse / SCALE;
        expToUse -= expToUse % SCALE;
        int exp = getTotalXP(player);
        if (expToUse > exp) {
          player.sendMessage("You only have " + exp + " XP!");
          return true;
        }
        player.sendMessage("You previously had " + exp + " XP!");
        setTotalXP(player, exp - expToUse);
        player.sendMessage("You should have " + (exp - expToUse) + " XP.");
        player.sendMessage("You actually have " + getTotalXP(player) + " XP.");
        inventory.addItem(new ItemStack(Material.EMERALD, emeralds));

        return true;
      }
      else {
        sender.sendMessage("This command may not be used from the console.");
        return false;
      }
    }
    return false;
  }

  public int getTotalXP(Player player) {
    int level = player.getLevel();
    int currentXP = (int) player.getExp();
    int totalXP = 0;

    if (level <= 15) {
      totalXP = 17 * level;
    }
    else if (level <= 30) {
      totalXP = (int) (1.5 * Math.pow(level, 2) - 29.5 * level + 360);
    }
    else { // Levels above 30
      totalXP = (int) (3.5 * Math.pow(level, 2) - 151.5 * level + 2220);
    }

    totalXP += currentXP;

    return totalXP;
  }

  public void setTotalXP(Player player, int xp) {
    int level = 0;

    getLogger().log(Level.INFO, "Level: " + level + " XP: " + xp);
    while (level < 15) {
      int xpToNext = 17;
      if (xp > xpToNext) {
        xp -= xpToNext;
        level++;
      }
      else {
        break;
      }
      getLogger().log(Level.INFO, "Level: " + level + " XP: " + xp);
    }
    while (level < 30) {
      int xpToNext = 3 * level - 28;
      if (xp > xpToNext) {
        xp -= xpToNext;
        level++;
      }
      else {
        break;
      }
      getLogger().log(Level.INFO, "Level: " + level + " XP: " + xp);
    }
    while (true) {
      int xpToNext = 7 * level - 148;
      if (xp > xpToNext) {
        xp -= xpToNext;
        level++;
      }
      else {
        break;
      }
      getLogger().log(Level.INFO, "Level: " + level + " XP: " + xp);
    }

    player.setLevel(level);
    player.setExp(0);
    player.setExp(xp);
    return;
  }
}
