package de.wolfi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventorySelector implements InventoryHolder, Listener {

	private static final ItemStack cancel = new ItemBuilder(Material.WOOL).setMeta((short) 14)
			.setName(ChatColor.RED + "Cancel").build();

	private static final ItemStack confirm = new ItemBuilder(Material.WOOL).setMeta((short) 5)
			.setName(ChatColor.GREEN + "Confirm").build();
	private static final ItemStack random = new ItemBuilder(Material.PORTAL).setName(ChatColor.LIGHT_PURPLE + "Random")
			.build();
	private static final ItemStack seperator = new ItemBuilder(Material.STAINED_GLASS_PANE).setMeta((short) 9).build();

	private Callback<Boolean, ItemStack> callback = (i) -> false;

	private final Inventory inv;
	private ItemStack selected = new ItemStack(Material.BARRIER);

	public InventorySelector(String title) {
		this.inv = Bukkit.createInventory(this, 9 * 5,title);
		this.fillIntern();
		Bukkit.getPluginManager().registerEvents(this, UtilRegistry.getPlugin());

	}

	private void fillIntern() {
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
	public Inventory getInventory() {

		return this.inv;
	}

	private boolean isInternSlot(int slot) {

		return slot > 9 * 3;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory().getHolder() == null) return;
		if (e.getInventory().getHolder().equals(this)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				final ItemStack item = e.getCurrentItem();
				if (this.isInternSlot(e.getSlot())) {
					if (item.equals(InventorySelector.confirm)) {
						e.getWhoClicked().closeInventory();
						if (!this.callback.call(this.selected))
							e.getWhoClicked().openInventory(this.inv);
					}
				} else
					this.setSelected(item);

			}
		}
	}

	public void addEntry(ItemStack i){
		this.inv.addItem(i);
	}
	
	public void open(Player player) {
		player.openInventory(this.inv);
	}

	public void setCallback(Callback<Boolean, ItemStack> call) {
		this.callback = call;
	}

	private void setSelected(ItemStack item) {
		this.selected = item;
		this.inv.setItem(9*3+4, this.selected);
	}
	
	public void destroy(){
		HandlerList.unregisterAll(this);
		for(HumanEntity e : this.inv.getViewers()){
			e.closeInventory();
		}
	}
}
