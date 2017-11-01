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
		return false;
	}

	@Override
	protected void fillIntern() {
		this.setSelected(new ItemBuilder(Material.PAPER).addLore(this.msg).setName("§cStimmst du zu?").build());
		
	}

	@Override
	protected void slotClicked(ItemStack item, int slot) {
			
	}

}
