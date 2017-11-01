package de.wolfi.utils.inventory;

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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.wolfi.utils.Callback;
import de.wolfi.utils.ItemBuilder;
import de.wolfi.utils.UtilRegistry;

public abstract class Inventory implements InventoryHolder, Listener {

	protected static final ItemStack cancel = new ItemBuilder(Material.WOOL).setMeta((short) 14)
			.setName(ChatColor.RED + "Cancel").build();
	protected static final ItemStack confirm = new ItemBuilder(Material.WOOL).setMeta((short) 5)
			.setName(ChatColor.GREEN + "Confirm").build();
	protected static final ItemStack random = new ItemBuilder(Material.PORTAL)
			.setName(ChatColor.LIGHT_PURPLE + "Random").build();
	protected static final ItemStack seperator = new ItemBuilder(Material.STAINED_GLASS_PANE).setMeta((short) 9)
			.build();

	protected final org.bukkit.inventory.Inventory inv;

	private final int selectedSlot;
	protected ItemStack selected = new ItemStack(Material.BARRIER);

	private boolean cancelled = false;
	private Callback<Boolean, ItemStack> callback = (i) -> false;
	private final boolean autoDestroy;

	protected abstract boolean isInternSlot(int slot);

	protected abstract void fillIntern();

	protected abstract void slotClicked(ItemStack item, int slot);
	
	protected Inventory(String title, int selectedSlot) {
		this(title,selectedSlot,false);

	}
	
	protected Inventory(String title, int selectedSlot, boolean autoDestroy) {
		this.inv = Bukkit.createInventory(this, 9 * 5, title);
		this.selectedSlot = selectedSlot;
		this.autoDestroy = autoDestroy;
		this.fillIntern();
		Bukkit.getPluginManager().registerEvents(this, UtilRegistry.getPlugin());

	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() == null)
			return;
		if (e.getInventory().getHolder().equals(this)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				final ItemStack item = e.getCurrentItem();
				if (this.isInternSlot(e.getSlot())) {
					if (item.equals(Inventory.confirm) || (this.cancelled = item.equals(Inventory.cancel))) {
						e.getWhoClicked().closeInventory();
						if (!this.callback.call(this.selected))
							e.getWhoClicked().openInventory(this.inv);
						else if(this.autoDestroy)
							this.destroy();
					}
				} else
					this.slotClicked(item, e.getSlot());

			}
		}
	}

	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public final org.bukkit.inventory.Inventory getInventory() {
		return this.inv;
	}

	protected final void setSelected(ItemStack item) {
		this.selected = item;
		this.inv.setItem(this.selectedSlot, this.selected);
	}

	public final void open(Player player) {
		player.openInventory(this.inv);
	}

	public final void setCallback(Callback<Boolean, ItemStack> call) {
		this.callback = call;
	}

	public final void destroy() {
		HandlerList.unregisterAll(this);
		for (HumanEntity e : this.inv.getViewers()) {
			e.closeInventory();
		}
	}
}
