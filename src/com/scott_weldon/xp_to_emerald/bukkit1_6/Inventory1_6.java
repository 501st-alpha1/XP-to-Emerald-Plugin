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

import java.lang.IllegalArgumentException;
import com.scott_weldon.xp_to_emerald.common.interfaces.PlayerInterface;
import com.scott_weldon.xp_to_emerald.common.interfaces.InventoryInterface;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;

public class Inventory1_6 implements InventoryInterface {
  private PlayerInventory bukkitInventory;

  public Inventory1_6(PlayerInventory bukkitInventory) {
    this.bukkitInventory = bukkitInventory;
  }

  public void addItems(String name, int number) {
    Material material = Material.getMaterial(name.toUpperCase());
    if (material == null) {
      throw new IllegalArgumentException("Invalid material.");
    }

    this.bukkitInventory.addItem(new ItemStack(material, number));
  }

  public int getItems(String name) {
    Material material = Material.getMaterial(name.toUpperCase());
    if (material == null) {
      throw new IllegalArgumentException("Invalid material.");
    }

    int numOfEmeralds = 0;
    for (ItemStack item : bukkitInventory) {
      if (item == null) {
        continue;
      }

      if (item.getType() == material) {
        numOfEmeralds += item.getAmount();
      }
    }

    return numOfEmeralds;
  }

  public void removeItems(String name, int number) {
    Material material = Material.getMaterial(name.toUpperCase());
    if (material == null) {
      throw new IllegalArgumentException("Invalid material.");
    }

    int currentItems = this.getItems(name);
    if (number < currentItems) {
      throw new IllegalArgumentException("Cannot remove more items than exist.");
    }

    int itemsLeft = currentItems - number;

    if (itemsLeft > 0) {
      // To remove given number of items, remove all of type and add correct
      // number back.
      bukkitInventory.remove(material.getId());
      this.addItems(material.toString(), itemsLeft);
    }
  }
}
