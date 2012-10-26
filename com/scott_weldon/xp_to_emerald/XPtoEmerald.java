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
        // Permissions stuff here?
        int expToUse = Integer.parseInt(args[0]);
        int emeralds = expToUse / SCALE;
        if ((expToUse % SCALE) != 0) {
          expToUse -= expToUse % SCALE;
        }
        int exp = (int) player.getTotalExperience();
        if (expToUse > exp) {
          sender.sendMessage("You only have " + exp + " XP!");
          return true;
        }
        sender.sendMessage("You previously had " + exp + " XP!");
        player.setTotalExperience(exp - expToUse);
        sender.sendMessage("You should have " + (exp - expToUse) + " XP.");
        sender.sendMessage("You actually have " + player.getTotalExperience()
            + " XP.");
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
}
