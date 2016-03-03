package grangerave.GalacticAir.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSmoke extends Block {

	public BlockSmoke() {
		super(Material.plants);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setBlockName("BlockSmoke");
		//setBlockTextureName("galacticair:ribglass");
	}
	
	//when entity collides with this block
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        //entity.setInWeb();
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
        return 1;
    }
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public Item getItemDropped(int x, Random y, int z)
    {
        return null;
    }
	
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
	
	//block logic
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		if(world.canBlockSeeTheSky(x, y, z)){	//if we're not in an enclosed space, no point in existing
			world.setBlockToAir(x, y, z);
		}
		world.scheduleBlockUpdate(x, y, z, this, world.rand.nextInt(40)+20);
	}
	
	/*
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
		return Blocks.fire.getIcon(side, meta);
		//EnumParticleTypes.SMOKE_LARGE.
    }
    */
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        //this.blockIcon = register.registerIcon("galacticair:ribglass");
    }
	
	
}
