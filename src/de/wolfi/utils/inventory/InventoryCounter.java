package de.wolfi.utils.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.wolfi.utils.ItemBuilder;

public class InventoryCounter extends Inventory {

	protected static final ItemStack plus1 = new ItemBuilder(Material.WOOD_BUTTON).setName(ChatColor.GREEN + "+1")
			.build();
	protected static final ItemStack plus5 = new ItemBuilder(Material.STONE_BUTTON).setName(ChatColor.GREEN + "+5")
			.build();
	protected static final ItemStack minus1 = new ItemBuilder(Material.WOOD_BUTTON).setName(ChatColor.RED + "-1")
			.build();
	protected static final ItemStack minus5 = new ItemBuilder(Material.STONE_BUTTON).setName(ChatColor.RED + "-5")
			.build();

	private int amount = 1;

	public InventoryCounter(String title) {
		super(title, 9 * 2 + 4);
	}

	@Override
	protected boolean isInternSlot(int slot) {
		return slot > 9 * 3;
	}

	@Override
	protected void fillIntern() {
		this.inv.setItem(9 * 2 + 2, InventoryCounter.minus5);
		this.inv.setItem(9 * 2 + 3, InventoryCounter.minus1);

		this.inv.setItem(9 * 2 + 5, InventoryCounter.plus1);
		this.inv.setItem(9 * 2 + 6, InventoryCounter.plus5);

		int row = 9 * 3;
		while (row < 9 * 4) {
			this.inv.setItem(row, InventorySelector.seperator);
			row++;
		}
		this.inv.setItem(row, InventorySelector.confirm);
		row += 2;
		this.inv.setItem(row, InventorySelector.random);
		row += 2;
		this.inv.setItem(row, this.selected);
		row += 4;
		this.inv.setItem(row, InventorySelector.cancel);

	}

	@Override
	protected void slotClicked(ItemStack item, int slot) {
		if (item.equals(InventoryCounter.minus5))
			this.changeAmount(-5);
		if (item.equals(InventoryCounter.minus1))
			this.changeAmount(-1);

		if (item.equals(InventoryCounter.plus1))
			this.changeAmount(1);
		if (item.equals(InventoryCounter.plus5))
			this.changeAmount(5);
		
		this.setSelected(new ItemStack(Material.EYE_OF_ENDER, this.amount));
	}

	private void changeAmount(int value) {
		this.amount = Math.max(1, this.amount + value);
	}

}
