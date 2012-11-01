/* Copyright (c) 2012 Scott Weldon
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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class XPtoEmerald extends JavaPlugin {
  private Server server;

  public static final int SCALE = 20;

  @Override
  public void onEnable() {
    server = Bukkit.getServer();
  }

  @Override
  public void onDisable() {
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (args.length > 2) {
      return false;
    }
    if (cmd.getName().equalsIgnoreCase("xptoemerald")) {
      if (sender instanceof Player) {
        Player player;
        int xp;

        if (args.length == 2) {
          player = server.getPlayer(args[0]);
          xp = Integer.parseInt(args[1]);

          if (!playerOnline(player)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }
          else
            xtePermCheck(sender, player, xp);
        }
        else if (args.length == 1) {
          try {
            xp = Integer.parseInt(args[0]);
            player = (Player) sender;
            return xtePermCheck(sender, player, xp);
          }
          catch (NumberFormatException e) {
            player = server.getPlayer(args[0]);
            if (!playerOnline(player)) {
              sender.sendMessage("Player " + args[0]
                  + " does not exist or is not online.");
              return false;
            }
            return xtePermCheck(sender, player, 0);
          }
        }
        else if (args.length == 0) {
          player = (Player) sender;
          return xtePermCheck(sender, player, 0);
        }
      }
      else { // Command sent from console.
        if (args.length == 2) {
          Player player = server.getPlayer(args[0]);
          int xp = Integer.parseInt(args[1]);

          if (!playerOnline(player)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }

          if (xpToEmerald(player, xp)) {
            sender.sendMessage("Converted " + xp + " of player " + player
                + "'s xp to Emeralds.");
            return true;
          }
          else
            return false;
        }
        else if (args.length == 1) {
          Player player = server.getPlayer(args[0]);

          if (!playerOnline(player)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }

          if (xpToEmerald(player, 0)) {
            sender.sendMessage("Converted all of player " + player
                + "'s xp to Emeralds.");
            return true;
          }
          else
            return false;
        }
      }
    }
    if (cmd.getName().equalsIgnoreCase("emeraldtoxp")) {
      if (sender instanceof Player) {
        Player player;
        int emeralds;

        if (args.length == 2) {
          player = server.getPlayer(args[0]);
          emeralds = Integer.parseInt(args[1]);

          if (!playerOnline(player)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }

          etxPermCheck(sender, player, emeralds);
        }
        else if (args.length == 1) {
          try {
            emeralds = Integer.parseInt(args[0]);
            player = (Player) sender;
            return etxPermCheck(sender, player, emeralds);
          }
          catch (NumberFormatException e) {
            player = server.getPlayer(args[0]);
            if (!playerOnline(player)) {
              sender.sendMessage("Player " + args[0]
                  + " does not exist or is not online.");
              return false;
            }
            return etxPermCheck(sender, player, 0);
          }
        }
        else {
          player = (Player) sender;
          return etxPermCheck(sender, player, 0);
        }
      }
      else { // Command sent from console.
        if (args.length == 2) {
          Player player = server.getPlayer(args[0]);
          int emeralds = Integer.parseInt(args[1]);

          if (!playerOnline(player)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }

          if (emeraldToXP(player, emeralds)) {
            sender.sendMessage("Converted " + emeralds + " of player " + player
                + "'s Emeralds to xp.");
            return true;
          }
          else
            return false;
        }
        else if (args.length == 1) {
          Player player = server.getPlayer(args[0]);

          if (!playerOnline(player)) {
            sender.sendMessage("Player " + args[0]
                + " does not exist or is not online.");
            return false;
          }

          if (emeraldToXP(player, 0)) {
            sender.sendMessage("Converted all of player " + player
                + "'s Emeralds to xp.");
            return true;
          }
          else
            return false;
        }
      }
    }
    return false;
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

  public boolean xtePermCheck(CommandSender sender, Player player, int xp) {
    if (player.equals(sender)) {
      if (sender.hasPermission("xptoemerald.convert")) {
        return xpToEmerald(player, xp);
      }
      else {
        sender.sendMessage("You don't have permission for that!");
        return true;
      }
    }
    else {
      if (sender.hasPermission("xptoemerald.admin")) {
        return xpToEmerald(player, xp);
      }
      else {
        sender.sendMessage("You don't have permission for that!");
        return true;
      }
    }
  }

  public boolean etxPermCheck(CommandSender sender, Player player, int emeralds) {
    if (player.equals(sender)) {
      if (sender.hasPermission("xptoemerald.convert")) {
        return emeraldToXP(player, emeralds);
      }
      else {
        sender.sendMessage("You don't have permission for that!");
        return true;
      }
    }
    else {
      if (sender.hasPermission("xptoemerald.admin")) {
        return emeraldToXP(player, emeralds);
      }
      else {
        sender.sendMessage("You don't have permission for that!");
        return true;
      }
    }
  }

  public boolean xpToEmerald(Player player, int xp) {
    PlayerInventory inventory = player.getInventory();

    int emeralds = xp / SCALE;
    xp -= xp % SCALE;
    int exp = player.getTotalExperience();
    if (xp > exp) {
      player.sendMessage("You only have " + exp + " XP!");
      return true;
    }
    setTotalXP(player, exp - xp);
    inventory.addItem(new ItemStack(Material.EMERALD, emeralds));

    return true;
  }

  public boolean emeraldToXP(Player player, int emeralds) {
    PlayerInventory inventory = player.getInventory();

    int emeraldId = Material.EMERALD.getId();

    int numOfEmeralds = 0;
    for (ItemStack item : inventory) {
      if (item == null) {
        continue;
      }
      if (item.getType() == Material.EMERALD) {
        numOfEmeralds += item.getAmount();
      }
    }
    inventory.remove(emeraldId);

    if (numOfEmeralds < emeralds) {
      player.sendMessage("You only have " + numOfEmeralds + " emeralds!");
      inventory.addItem(new ItemStack(Material.EMERALD, numOfEmeralds));
      return true;
    }

    int exp = player.getTotalExperience() + (emeralds * SCALE);
    setTotalXP(player, exp);
    inventory
        .addItem(new ItemStack(Material.EMERALD, numOfEmeralds - emeralds));

    return true;
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
