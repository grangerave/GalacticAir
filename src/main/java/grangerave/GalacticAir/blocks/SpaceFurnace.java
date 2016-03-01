package grangerave.GalacticAir.blocks;

import grangerave.GalacticAir.GalacticAir;
import grangerave.GalacticAir.tile.TileEntitySpaceFurnace;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;

public class SpaceFurnace extends BlockFurnace implements IOxygenReliantBlock {
	private boolean lit = false;
	private static boolean softBreak = false;
	
	public SpaceFurnace(boolean b) {
		super(b);
		lit = b;
		setTickRandomly(true);
		if(b)
			setBlockName("litSpaceFurnace");
		else
			setBlockName("SpaceFurnace");
	}


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySpaceFurnace();
    }
    
	/**
     * Update which block the furnace is using depending on whether or not it is burning
     */
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float xx, float yy, float zz)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileEntitySpaceFurnace tileentityfurnace = (TileEntitySpaceFurnace)world.getTileEntity(x, y, z);

            if (tileentityfurnace != null)
            {
                player.func_146101_a(tileentityfurnace);
            }

            return true;
        }
    }
	
    public static void updateFurnaceBlockState(boolean b, World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        
        softBreak = true;	//we're just replacing the block- no need to spit out contents
        if (b)
            world.setBlock(x, y, z, GalacticAir.litfurnace);
        else
            world.setBlock(x, y, z, GalacticAir.furnace);

        softBreak = false;
        System.out.println(l);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null)	//reassociate tile entity related to the furnace
        {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }
	
    @Override
    public void breakBlock(World world, int x, int y, int z, Block b, int rand){
    	if(!softBreak)	//make sure we're not just replacing the block with a version of itself
    		super.breakBlock(world, x, y, z, b, rand);	
    	
    }
	
	@Override
    public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this, world.rand.nextInt(30)+1);
	}
	
	@Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
		this.checkOxygen(world,x,y,z);
    }
	
	private void checkOxygen(World world, int x, int y, int z)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            if (OxygenUtil.checkTorchHasOxygen(world, this, x, y, z))
            {
                this.onOxygenAdded(world, x, y, z);
            }
            else
            {
                this.onOxygenRemoved(world, x, y, z);
            }
        }
    }
	
	@Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, Block par5){
		par1World.scheduleBlockUpdate(x, y, z, this, par1World.rand.nextInt(30)+1);
	}
	
	@Override
	public void onOxygenRemoved(World world, int x, int y, int z) {
		if (world.provider instanceof IGalacticraftWorldProvider){//remove oxygen
			((TileEntitySpaceFurnace) world.getTileEntity(x, y, z)).oxygen=false;
			if(lit)
				this.updateFurnaceBlockState(false, world, x, y, z);
		}
	}

	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		if (world.provider instanceof IGalacticraftWorldProvider){	//reset oxygen
			((TileEntitySpaceFurnace) world.getTileEntity(x, y, z)).oxygen=true;
		}
	}
	
	@SideOnly(Side.CLIENT)@Override
    public IIcon getIcon (int side, int meta) {
		if(lit)
			return Blocks.lit_furnace.getIcon(side, meta);
		else
			return Blocks.furnace.getIcon(side, meta);
		
    }
	
	
	
	@SideOnly(Side.CLIENT)@Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
		//do nothing
    }

}
