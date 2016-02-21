package grangerave.GalacticAir.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.block.BlockLiquid;

public class Space_Liquid extends BlockStaticLiquid implements IOxygenReliantBlock {

	protected Space_Liquid(Material mat) {
		super(mat);
		setCreativeTab(CreativeTabs.tabMisc);
		this.setBlockName("Space Lava - Liquid");
		setTickRandomly(true);
		// TODO Auto-generated constructor stub
	}

	@Override
    public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		super.onNeighborBlockChange(world, x, y, z, this);
		System.out.println("added " + world.getBlockMetadata(x, y, z));
		//this.checkOxygen(par1World, par2, par3, par4);
	}
	
	@Override
    public void updateTick(World par1World, int x, int y, int z, Random random)
    {
		//do liquid stuff
		super.updateTick(par1World, x, y, z, random);
		//immediately check if in vacuum
    	this.checkOxygen(par1World, x, y, z);
    }
	
	@Override
	public void onOxygenRemoved(World world, int x, int y, int z) {
		//Oxygen gone, set to air
		if (world.provider instanceof IGalacticraftWorldProvider && !world.isRemote)
		{
			//System.out.println(world.isRemote);
			world.setBlock(x, y, z, Blocks.air, 0, 2);
			world.notifyBlocksOfNeighborChange(x, y, z, this);
			//make a poof sound? kinda useless in vacuum...
			world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 3.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			//make a poof
			world.spawnParticle("smoke", (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 0.0D, 0.0D, 0.0D);
			//world.spawnParticle("largesmoke",(double)x+1D,(double)y+2D,(double)z+1D,0D,0D,0D);
			GalacticraftCore.proxy.spawnParticle("largesmoke",new Vector3(x, y + 0.5D, z), new Vector3(0, 0, 0), new Object[] {});
		    for (int l = 0; l < 8; ++l)
		    {
		          world.spawnParticle("smoke", (double)x + Math.random(), (double)y + Math.random(), (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
		    }
		}
		
	}

	
	
	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
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
        //else
    }

	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
		// TODO switched lava and water flowing tex
		if(this.getMaterial()==Material.water)
			return (side == 0 || side == 1)? Blocks.water.getIcon(1, 0) : Blocks.water.getIcon(2, 1);
		//otherwise lava?
		return (side == 0 || side == 1)? Blocks.lava.getIcon(1, 0) : Blocks.lava.getIcon(2, 1);
    }

	//we use vanilla textures, so don't register textures
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
    }

}
