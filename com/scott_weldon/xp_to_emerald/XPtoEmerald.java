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
        // Permissions stuff here?
        int expToUse = Integer.parseInt(args[0]);
        int emeralds = expToUse / SCALE;
        PlayerInventory inventory = player.getInventory();
        int exp = (int) player.getExp();
        if (expToUse > exp) {
          sender.sendMessage("You don't have enough XP!");
          return true;
        }
        else {
          player.setExp(exp - expToUse);
          while (emeralds > 0) {
            ItemStack emeraldStack;
            if (emeralds > 64) {
              emeraldStack = new ItemStack(Material.EMERALD, 64);
            }
            else {
              emeraldStack = new ItemStack(Material.EMERALD, emeralds);
            }
            emeralds -= 64;
            inventory.addItem(emeraldStack);
          }
        }
        return true;
      }
      else {
        sender.sendMessage("This command may not be used from the console.");
        return false;
      }
    }
    return false;
  }
}
