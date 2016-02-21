package grangerave.GalacticAir.blocks;

import grangerave.GalacticAir.GalacticAir;
import grangerave.GalacticAir.tile.TileEntitySpaceFurnace;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.renderer.texture.IIconRegister;
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
	
	public SpaceFurnace(boolean b) {
		super(b);
		lit = b;
		setTickRandomly(true);
		if(b)
			setBlockName("litSpaceFurnace");
		else
			setBlockName("SpaceFurnace");
	}

	/*
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySpaceFurnace();d
    }
    */
	
	
	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4){
		//immediately check if oxygen or not
		this.checkOxygen(par1World, par2, par3, par4);
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
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5){
		par1World.scheduleBlockUpdate(par2, par3, par4, this, par1World.rand.nextInt(30));
	}
	
	@Override
	public void onOxygenRemoved(World world, int x, int y, int z) {
		if (world.provider instanceof IGalacticraftWorldProvider){
			if(lit)
				world.setBlock(x, y, z, GalacticAir.furnace,world.getBlockMetadata(x, y, z),2);
			//oxygen = false;
			System.out.println("Oxygen Gone!");
		}
	}

	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		if (world.provider instanceof IGalacticraftWorldProvider){
			//oxygen = true;
			//System.out.println("Oxygen Back!");
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
