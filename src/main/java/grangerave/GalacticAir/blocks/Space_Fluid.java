package grangerave.GalacticAir.blocks;

import grangerave.GalacticAir.GalacticAir;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Space_Fluid extends BlockFluidClassic implements IOxygenReliantBlock {

	public Space_Fluid(Fluid fluid, Material material) {
		super(fluid, material);
		setCreativeTab(CreativeTabs.tabMisc);
		this.setBlockName("Space " + fluid.getName());
		setTickRandomly(true);
	}
	
	
	
	
	@Override
    public void updateTick(World par1World, int x, int y, int z, Random random)
    {
		//do liquid stuff
		super.updateTick(par1World, x, y, z, random);
    	//do vanilla stuff only for lava(like cobble gen)
    	if(this.getMaterial()==Material.lava)
    		Blocks.lava.updateTick(par1World, x, y, z, random);
    	//immediately check if in vacuum
    	this.checkOxygen(par1World, x, y, z);
    	
    }
	
	
	
	//fluid stuff
	@Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {

		World thisWorld = (World)world;
		
		if (thisWorld.provider instanceof IGalacticraftWorldProvider){
			//if(super.canDisplace(world, x, y, z))
			if(this.getMaterial()==Material.water){
				return OxygenUtil.checkTorchHasOxygen(thisWorld,this,x,y,z) ? super.canDisplace(world, x, y, z) : false;
			}
		}
        return super.canDisplace(world, x, y, z);
    }
	
	@Override
	//called when actively trying to displace
    public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.provider instanceof IGalacticraftWorldProvider){
			if(this.getMaterial()==Material.water){
				if(OxygenUtil.checkTorchHasOxygen(world,this,x,y,z)){
					return super.displaceIfPossible(world, x, y, z);
				}
				destroyFx(GalacticraftCore.proxy.getClientWorld(),x,y,z,3);
				return false;
			}
		}
        return super.displaceIfPossible(world, x, y, z);
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
	

	private boolean checkWater(World world, int x, int y, int z){
		int[] pos = new int[]{x,y,z};
		for(int i=0;i<3;i++){
			for(int j=-1;j<=1;j+=2){
				pos[i]+=j;
				//System.out.println("checking: "+pos[0]+","+pos[1]+","+pos[2]);
				if(world.getBlock(pos[0], pos[1], pos[2]).getMaterial()==Material.water){
					if(j<0&&i==1){	//if water below us, return false 
						//world.setBlock(pos[0], pos[1], pos[2], GalacticAir.pumice, 5, 2);
						return false;
					}
					return true;
				}
				pos[i]-=j;
			}
		}
		return false;
	}
	
	
	
	@Override 
	public void onNeighborBlockChange(World world, int x, int y,int z,Block block){
		super.onNeighborBlockChange(world, x, y, z, block);
		if(this.getMaterial()==Material.lava){//if lava
			if(checkWater(world,x,y,z)){
				destroyFx(world,x,y,z,6);
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 3.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
				if(world.getBlockMetadata(x, y, z)==0)
					world.setBlock(x, y, z, Blocks.obsidian, 0, 2);
				else
					world.setBlock(x, y, z, Blocks.cobblestone, 0, 2);
			}
		}
	}
	
	@Override
	public void onBlockAdded(World world,int x,int y,int z){
		
		super.onBlockAdded(world, x, y, z);
		if(this.getMaterial()==Material.lava){
			if(checkWater(world,x,y,z)){
				destroyFx(world,x,y,z,6);
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 3.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
				if(world.getBlockMetadata(x, y, z)==0)
					world.setBlock(x, y, z, Blocks.obsidian, 0, 2);
				else
					world.setBlock(x, y, z, GalacticAir.pumice,5,2);
			}
		}
	}
	
	private void destroyFx(World world,int x, int y, int z,int num){
		if(world.isRemote){//client side only
			for (int l = 0; l < num; ++l)
	        {
	            world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 0.7D, (double)z + Math.random(), -0.05D + 0.1D*Math.random(), 0.1D, -0.05D + 0.1D*Math.random());
	        }
		}

	}
	
	/*
	public void updateOxygenState(World par1World, int x, int y, int z, boolean valid)
    {
        if (!valid)
        {
        	System.out.println("oxygen not Valid!");
        	//par1World.setBlockMetadataWithNotify(x, y, z, 1, 3);
        }
        
    }
	*/
	
	@Override
	public void onOxygenRemoved(World world, int x, int y, int z) {
		//Oxygen gone, set to air
		if (world.provider instanceof IGalacticraftWorldProvider)
		{
				destroyFx(GalacticraftCore.proxy.getClientWorld(),x,y,z,8);
				if(this.getMaterial()==Material.water){
					//freeze water if we're in atmosphere
					System.out.println("daytime: " + world.provider.isDaytime());
					boolean flag = y < 0.8*world.provider.getAverageGroundLevel() && ((IGalacticraftWorldProvider) world.provider).getWindLevel()>0.1F && (!world.provider.isDaytime() || !world.canBlockSeeTheSky(x, y, z));
					if(flag){
						world.setBlock(x, y, z, Blocks.ice, 0, 2);
					}
					else{
						world.setBlock(x, y, z, Blocks.air,0,2);
					}
				}
				else
					world.setBlock(x, y, z, GalacticAir.pumice,5,2);
				world.notifyBlocksOfNeighborChange(x, y, z, this);
				//make a poof sound? kinda useless in vacuum...
				world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 3.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
		}		

	}
	
	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		// Do nothing, block should stay
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand){
		if(this.getMaterial()==Material.lava)
			Blocks.lava.randomDisplayTick(world, x, y, z, rand);
		else
			Blocks.water.randomDisplayTick(world, x, y, z, rand);
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
