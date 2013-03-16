package hylinn.minecraft.ElementalWands.item;

import hylinn.minecraft.ElementalWands.ElementalWands;
import hylinn.minecraft.ElementalWands.event.WandCastEvent;
import hylinn.minecraft.ElementalWands.event.WandChargeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ItemWand extends Item {
	
	private final EnumWandMaterial material;
	private final EnumWandElement element;
	private final int maxItemUseDuration = 72000;
	
	public ItemWand(int id, EnumWandMaterial material, EnumWandElement element) {
		super(id);
		
		// Constructor Configuration
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxDamage(material.getMaxCharges());
		this.material = material;
		this.element = element;
	}
	
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount) {
		
		int charge = this.getMaxItemUseDuration(itemStack) - itemInUseCount;

        WandCastEvent event = new WandCastEvent(player, itemStack, charge);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            return;
        
        charge = event.charge;
        
        //TODO Calculate results of charging.
//        float f = (float)charge / 20.0F;
//        f = (f * f + f * 2.0F) / 3.0F;
//
//        if ((double)f < 0.1D)
//        {
//            return;
//        }
//
//        if (f > 1.0F)
//        {
//            f = 1.0F;
//        }

//        EntityArrow entityarrow = new EntityArrow(par2World, par3EntityPlayer, f * 2.0F);

//        if (f == 1.0F)
//        {
//            entityarrow.setIsCritical(true);
//        }

//        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
//
//        if (k > 0)
//        {
//            entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
//        }
//
//        int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);
//
//        if (l > 0)
//        {
//            entityarrow.setKnockbackStrength(l);
//        }
//
//        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
//        {
//            entityarrow.setFire(100);
//        }
        
        //TODO Actually cast a wand's spell.

        itemStack.damageItem(1, player); //TODO Change damage based upon charge time.
//        world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F); //TODO Create and use wand random wand sounds.
	}
	
	public ItemStack onItemRightClick(ItemStack itemStack, World wWorld, EntityPlayer player) {
		
		WandChargeEvent event = new WandChargeEvent(player, itemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            return event.result;   
        
        player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        
	    return itemStack;
	}
	
	public int getItemEnchantability() {
		return this.material.getEnchantability();
	}
	
	public EnumAction getItemUseAction(ItemStack itemStack) {
        return ElementalWands.actionWand;
    }
	
	public boolean getIsRepairable(ItemStack item, ItemStack resource) {
		return this.material.getMaterialID() == resource.itemID ? true : super.getIsRepairable(item, resource);
    }
	
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister) {
		iconIndex = iconRegister.func_94245_a("ElementalWands:blazerod"); //TODO Set ItemWand texture to something dependent upon material and element.
	}
	
	public int getMaxItemUseDuration(ItemStack itemStack) {
        return maxItemUseDuration;
    } 
	
	public boolean isFull3D() {
        return true;
    }

	public Object getWandMaterial() {
		return material.getMaterial();
	}

	public Object getElementMaterial() {
		return element.getMaterial();
	}
}
