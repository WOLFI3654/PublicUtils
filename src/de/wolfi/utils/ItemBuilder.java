package de.wolfi.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemBuilder {

	public static class MyEnchantment extends Enchantment {

		private String name;

		public MyEnchantment(String name) {
			super(65);
			this.name = name;
		}

		@Override
		public boolean canEnchantItem(ItemStack paramItemStack) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean conflictsWith(Enchantment paramEnchantment) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public EnchantmentTarget getItemTarget() {
			// TODO Auto-generated method stub
			return EnchantmentTarget.ALL;
		}

		@Override
		public int getMaxLevel() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return this.name;
		}

		@Override
		public int getStartLevel() {
			// TODO Auto-generated method stub
			return 1;
		}

	}

	private static String trollLore = "§7[§0Troll§4Mode§7]";

	public static boolean isTrollItem(ItemStack itemInHand) {
		boolean is = true;
		is &= itemInHand.hasItemMeta();
		if (!is) {
			return false;
		}
		is &= itemInHand.getItemMeta().hasLore();
		if (!is) {
			return false;
		}
		is &= itemInHand.getItemMeta().getLore().contains(ItemBuilder.trollLore);
		return is;
	}

	public static ItemBuilder skullFromPlayer(String name) {
		ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM);
		builder.setName(name);
		builder.setMeta((short) 3);
		builder.setSkullOwner(name);
		return builder;
	}

	private Material m;

	private ItemStack stack;

	public ItemBuilder(ItemStack stack) {
		this.stack = stack;
		this.m = stack.getType();
	}

	public ItemBuilder(Material m) {
		this.m = m;
		this.stack = new ItemStack(this.m);
	}

	public ItemBuilder addLore(String text) {
		ItemMeta meta = this.stack.getItemMeta();
		List<String> lore = meta.getLore();
		if (!meta.hasLore() || lore == null) {
			lore = Arrays.asList(text);
		} else {
			lore.add(text);
		}
		meta.setLore(lore);
		this.stack.setItemMeta(meta);

		return this;
	}

	public ItemStack build() {
		return this.stack;
	}

	public ItemBuilder dye(Color d) {
		ItemMeta meta = this.stack.getItemMeta();
		if (meta instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) meta).setColor(d);
		}
		this.stack.setItemMeta(meta);
		return this;
	}
	

	public ItemBuilder dye(DyeColor d) {
		ItemMeta meta = this.stack.getItemMeta();
		if (meta instanceof Colorable) {
			((Colorable) meta).setColor(d);
		}
		this.stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder potionColor(PotionEffectType type){
		if(this.stack.getItemMeta() instanceof PotionMeta){
			PotionMeta meta = (PotionMeta) this.stack.getItemMeta();	
			meta.setMainEffect(type);
			this.stack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder potion(PotionEffectType type, int durability, int amp){
		if(this.stack.getItemMeta() instanceof PotionMeta){
			PotionMeta meta = (PotionMeta) this.stack.getItemMeta();	
			meta.addCustomEffect(new PotionEffect(type, durability, amp), false);
			this.stack.setItemMeta(meta);
		}
		return this;
	}
	
	public ItemBuilder enchant(Enchantment ench, int level) {
		this.stack.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		this.stack.setAmount(amount);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = this.stack.getItemMeta();
		meta.setLore(lore);
		this.stack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setMeta(short meta) {

		this.stack.setDurability(meta);
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta meta = this.stack.getItemMeta();
		meta.setDisplayName(name);
		this.stack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setSkullOwner(String owner) {
		if (this.stack.getItemMeta() instanceof SkullMeta) {
			SkullMeta meta = (SkullMeta) this.stack.getItemMeta();
			meta.setOwner(owner);
			this.stack.setItemMeta(meta);
		}

		return this;
	}

	public ItemBuilder trollItem() {
		this.addLore(ItemBuilder.trollLore);
		return this;
	}
	
	public static ItemBuilder copyFromItemstack(ItemStack i){
		return new ItemBuilder(i.clone());
	}

}
