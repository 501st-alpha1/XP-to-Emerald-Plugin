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

package com.scott_weldon.xp_to_emerald.common.interfaces;

public interface PlayerInterface {
  /**
   * Get the name of the world the player is currently in.
   */
  public String getCurrentWorldName();

  /**
   * Get the display name for the player (for e.g. use in messages).
   */
  public String getDisplayName();

  /**
   * Get the percentage XP the player has towards the next level.
   */
  public float getExp();

  /**
   * Get the player's {@link Inventory}.
   */
  public Inventory getInventory();

  /**
   * Get the player's current level.
   */
  public int getLevel();

  /**
   * Send a message to the player.
   */
  public void sendMessage(String message);

  /**
   * Set the percentage XP the player has towards the next level.
   */
  public void setExp(float exp);

  /**
   * Set the player's current level.
   */
  public void setLevel(int level);
}
