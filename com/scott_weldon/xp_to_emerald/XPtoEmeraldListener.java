package com.scott_weldon.xp_to_emerald;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class XPtoEmeraldListener implements Listener {
  private XPtoEmerald plugin;
  private Logger debug;

  public XPtoEmeraldListener(XPtoEmerald plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    debug = plugin.getLogger();
  }

  public void onSignPlace(SignChangeEvent e) {
    debug.log(Level.INFO, "onSignPlace!");
    Player p = e.getPlayer();
    Block sign = e.getBlock();
    String[] lines = e.getLines();
    if (!lines[0].equalsIgnoreCase("[xptoemerald]")) {
      debug.log(Level.INFO, "ignore this");
      return;
    }
    if (!lines[1].equalsIgnoreCase("xte") && !lines[1].equalsIgnoreCase("etx")) {
      debug.log(Level.INFO, "bad conversion");
      p.sendMessage("Invalid conversion type! (Second line of sign.)");
      sign.breakNaturally();
      return;
    }
    try {
      Integer.parseInt(lines[2]);
    }
    catch (NumberFormatException ex) {
      debug.log(Level.INFO, "bad number");
      p.sendMessage("Invalid conversion amount, must be an integer. (Third line of sign.)");
      sign.breakNaturally();
      return;
    }
  }

  public void onSignInteract(PlayerInteractEvent e) {
    debug.log(Level.INFO, "onSignInteract!");
    Block b = e.getClickedBlock();
    if (!(b.getType() == Material.SIGN)) {
      debug.log(Level.INFO, "not a sign");
      return;
    }
    Sign signState = (Sign) b.getState();
    String[] lines = signState.getLines();
    if (!lines[0].equalsIgnoreCase("[xptoemerald]")) {
      debug.log(Level.INFO, "not my sign");
      return;
    }
    Player p = e.getPlayer();
    int amount = Integer.parseInt(lines[2]);
    if (lines[1].equalsIgnoreCase("xte")) {
      debug.log(Level.INFO, "xte");
      plugin.xtePermCheck(p, p, amount);
    }
    else if (lines[1].equalsIgnoreCase("etx")) {
      debug.log(Level.INFO, "etx");
      plugin.etxPermCheck(p, p, amount);
    }
  }
}
