package com.scott_weldon.xp_to_emerald;

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

        if (!player.hasPermission("xptoemerald.convert")) {
          player.sendMessage("You don't have permission for that!");
          return true;
        }

        int expToUse = Integer.parseInt(args[0]);
        int emeralds = expToUse / SCALE;
        expToUse -= expToUse % SCALE;
        int exp = player.getTotalExperience();
        if (expToUse > exp) {
          player.sendMessage("You only have " + exp + " XP!");
          return true;
        }
        setTotalXP(player, exp - expToUse);
        inventory.addItem(new ItemStack(Material.EMERALD, emeralds));

        return true;
      }
      else {
        sender.sendMessage("This command may not be used from the console.");
        return true;
      }
    }
    return false;
  }

  public void setTotalXP(Player player, int xp) {
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
    player.setTotalExperience(xp);
    return;
  }
}
