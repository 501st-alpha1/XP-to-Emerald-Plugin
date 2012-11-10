package com.scott_weldon.xp_to_emerald;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class XPtoEmeraldListener implements Listener {
  private XPtoEmerald plugin;

  public void onSignPlace(SignChangeEvent e) {
    Player p = e.getPlayer();
    Block sign = e.getBlock();
    String[] lines = e.getLines();
    if (!lines[0].equalsIgnoreCase("[xptoemerald]")) {
      return;
    }
    if (!lines[1].equalsIgnoreCase("xte") && !lines[1].equalsIgnoreCase("etx")) {
      p.sendMessage("Invalid conversion type! (Second line of sign.)");
      sign.breakNaturally();
      return;
    }
    try {
      Integer.parseInt(lines[2]);
    }
    catch (NumberFormatException ex) {
      p.sendMessage("Invalid conversion amount, must be an integer. (Third line of sign.)");
      sign.breakNaturally();
      return;
    }
  }

  public void onSignInteract(PlayerInteractEvent e) {
    Block b = e.getClickedBlock();
    if (!(b.getType() == Material.SIGN)) {
      return;
    }
    Sign signState = (Sign) b.getState();
    String[] lines = signState.getLines();
    if (!lines[0].equalsIgnoreCase("[xptoemerald]")) {
      return;
    }
    Player p = e.getPlayer();
    int amount = Integer.parseInt(lines[2]);
    if (lines[1].equalsIgnoreCase("xte")) {
      plugin.xtePermCheck(p, p, amount);
    }
    else if (lines[1].equalsIgnoreCase("etx")) {
      plugin.etxPermCheck(p, p, amount);
    }
  }
}
