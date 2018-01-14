package de.wolfi.utils.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.wolfi.utils.ItemBuilder;

public class InventoryConfirmation extends Inventory {

	private String msg;

	public InventoryConfirmation(String title) {
		super("Bitte bestätigen...", 9*3+4, true);
		this.msg = title;
	}

	@Override
	protected boolean isInternSlot(int slot) {

		return slot > 9 * 3;
	}

	@Override
	protected void fillIntern() {
		this.setSelected(new ItemBuilder(Material.PAPER).addLore(this.msg).setName("§cStimmst du zu?").build());
		int row = 9 * 3;
		while (row < 9 * 4) {
			this.inv.setItem(row, Inventory.seperator);
			row++;
		}
		this.inv.setItem(row, Inventory.confirm);
		row += 2;
		this.inv.setItem(row, Inventory.random);
		row += 2;
		row += 4;
		this.inv.setItem(row, Inventory.cancel);
		
	}

	@Override
	protected void slotClicked(ItemStack item, int slot) {
			
	}

}
