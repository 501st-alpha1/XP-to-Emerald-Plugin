/* Copyright (c) 2017 Scott Weldon
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

package com.scott_weldon.xp_to_emerald.bukkit1_6;

import com.scott_weldon.xp_to_emerald.common.interfaces.PlayerInterface;
import com.scott_weldon.xp_to_emerald.common.interfaces.InventoryInterface;

public class Player1_6 implements PlayerInterface {
  private org.bukkit.entity.Player bukkitPlayer;

  public Player1_6(org.bukkit.entity.Player bukkitPlayer) {
    this.bukkitPlayer = bukkitPlayer;
  }

  public String getCurrentWorldName() {
    return this.bukkitPlayer.getLocation().getWorld().getName();
  }

  public String getDisplayName() {
    return this.bukkitPlayer.getDisplayName();
  }

  public float getExp() {
    return this.bukkitPlayer.getExp();
  }

  public InventoryInterface getInventory() {
    return new Inventory1_6(bukkitPlayer.getInventory());
  }

  public int getLevel() {
    return this.bukkitPlayer.getLevel();
  }

  public boolean isOnline() {
    return this.bukkitPlayer != null && this.bukkitPlayer.isOnline();
  }

  public void sendMessage(String message) {
    this.bukkitPlayer.sendMessage(message);
  }

  public void setExp(float exp) {
    this.bukkitPlayer.setExp(exp);
  }

  public void setLevel(int level) {
    this.bukkitPlayer.setLevel(level);
  }
}
