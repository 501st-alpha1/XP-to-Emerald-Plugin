package com.scott_weldon.xp_to_emerald;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class XPtoEmeraldListener implements Listener {
  private XPtoEmerald plugin;

  public XPtoEmeraldListener(XPtoEmerald plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onSignPlace(SignChangeEvent e) {
    Player p = e.getPlayer();
    Block sign = e.getBlock();
    String[] lines = e.getLines();
    if (!lines[0].equalsIgnoreCase("[xptoemerald]")) {
      return;
    }
    if (!p.hasPermission("xptoemerald.admin")) {
      p.sendMessage("You don't have permission for that!");
      sign.breakNaturally();
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

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onSignInteract(PlayerInteractEvent e) {
    Block b = e.getClickedBlock();
    if ((b.getType() != Material.SIGN_POST)
        && (b.getType() != Material.WALL_SIGN)) {
      return;
    }
    if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
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
    p.updateInventory();
  }
}
