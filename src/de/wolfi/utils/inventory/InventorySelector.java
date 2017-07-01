package de.wolfi.utils.inventory;

import org.bukkit.inventory.ItemStack;

public class InventorySelector extends de.wolfi.utils.inventory.Inventory {

	public InventorySelector(String title) {
		super(title,9*3+4);
	}

	@Override
	protected void fillIntern() {
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

	// @Override
	// public Inventory getInventory() {
	//
	// return this.inv;
	// }

	@Override
	protected boolean isInternSlot(int slot) {

		return slot > 9 * 3;
	}

//	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onClick(InventoryClickEvent e) {
//		if(e.getInventory().getHolder() == null) return;
//		if (e.getInventory().getHolder().equals(this)) {
//			e.setCancelled(true);
//			if (e.getCurrentItem() != null) {
//				final ItemStack item = e.getCurrentItem();
//				if (this.isInternSlot(e.getSlot())) {
//					if (item.equals(InventorySelector.confirm)) {
//						e.getWhoClicked().closeInventory();
//						if (!this.callback.call(this.selected))
//							e.getWhoClicked().openInventory(this.inv);
//					}
//				} else
//					this.setSelected(item);
//
//			}
//		}
//	}

	public void addEntry(ItemStack i){
		this.inv.addItem(i);
	}

	@Override
	protected void slotClicked(ItemStack item, int slot) {
		this.setSelected(item);
	}
	
//	public void open(Player player) {
//		player.openInventory(this.inv);
//	}

//	public void setCallback(Callback<Boolean, ItemStack> call) {
//		this.callback = call;
//	}
//
//	private void setSelected(ItemStack item) {
//		this.selected = item;
//		this.inv.setItem(9*3+4, this.selected);
//	}
//	
//	public void destroy(){
//		HandlerList.unregisterAll(this);
//		for(HumanEntity e : this.inv.getViewers()){
//			e.closeInventory();
//		}
//	}
}
