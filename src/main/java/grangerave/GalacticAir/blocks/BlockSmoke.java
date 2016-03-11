package grangerave.GalacticAir.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSmoke extends Block {

	public BlockSmoke() {
		super(Material.web);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setBlockName("BlockSmoke");
		this.setTickRandomly(false);
	}
	
	//when entity collides with this block
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
		if(!(entity instanceof EntityPlayer) || world.isRemote)
			return;
		EntityPlayer player = (EntityPlayer)entity;
		player.addPotionEffect(new PotionEffect(Potion.blindness.id, 90,0));
		if(player.hurtResistantTime==0 && world.rand.nextInt(10)>7){	//hurt the player if he doesn't have air
			GCPlayerStats g = GCPlayerStats.get((EntityPlayerMP)player);
			if(!g.oxygenSetupValid || ( g.airRemaining<=0 && g.airRemaining2<=0)){	//check if no air
				player.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage * 0.5F);
			}
		}
    }
	
	public boolean isOpaqueCube()	
    {
        return false;	//not opaque cube
    }
	
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;	//no collider here
    }
	
	public int getRenderType()
    {
        return -1;	//don't render at all
    }
	
	public boolean renderAsNormalBlock()
    {
        return true;
    }
	
	public Item getItemDropped(int x, Random y, int z)
    {
        return Item.getItemFromBlock(Blocks.air);	//drop nothing
    }
	
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

	@Override
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return true;
    }
	
	public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_)
    {
        return false;
    }
	
	//block logic
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		if(world.canBlockSeeTheSky(x, y, z)){	//if we're not in an enclosed space, no point in existing
			world.setBlockToAir(x, y, z);	//this check doesn't seem to work....
		}
		world.scheduleBlockUpdate(x, y, z, this, world.rand.nextInt(40)+40);	//we schedule our own ticks
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
		//spawn particles with chance corresponding to stagnant block age
		if(world.rand.nextInt(20)>world.getBlockMetadata(x, y, z)){
			world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 0.2D, (double)z + Math.random(), -0.025D + 0.05D*Math.random(), 0.0D, -0.025D + 0.05D*Math.random());
		}
    }
	
	@Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
		if(world.canBlockSeeTheSky(x, y, z)){	//if we're not in an enclosed space, no point in existing
			world.setBlockToAir(x, y, z);
		}
		int l = world.getBlockMetadata(x, y, z);
		if(l>10){
			world.setBlockToAir(x, y, z);
			return;
		}
		if(world.isAirBlock(x, y+1, z)){	//empty above us
			world.setBlock(x, y+1, z, this,l,2);
			world.setBlockToAir(x, y, z);
			world.scheduleBlockUpdate(x, y+1, z, this, world.rand.nextInt(40)+30);
			return;
		}
		//check above, horizontally (x)
		for(int i=-1;i<2;i++){
			if(world.isAirBlock(x+i, y+1, z)){
				world.setBlock(x+i, y+1, z, this,l,2);
				world.setBlockToAir(x, y, z);
				world.scheduleBlockUpdate(x+i, y+1, z, this, world.rand.nextInt(40)+30);
				return;
			}
		}//check above, vertically (z)
		for(int j=-1;j<2;j++){
			if(world.isAirBlock(x, y+1, z+j)){
				world.setBlock(x, y+1, z+j, this,l,2);
				world.setBlockToAir(x, y, z);
				world.scheduleBlockUpdate(x, y+1, z+j, this, world.rand.nextInt(40)+30);
				return;
			}
				
		} 
		//increase metadata timer
		world.setBlockMetadataWithNotify(x, y, z, l+1, 2);
		world.scheduleBlockUpdate(x, y, z, this, world.rand.nextInt(40)+80);	//tick slower when dissappating
    }
	
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
		//We're invisible!!
		return GCBlocks.brightBreatheableAir.getIcon(side, meta);
    }
    
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        //do nothing
    }
	
	
}
