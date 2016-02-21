package com.example.examplemod;


import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GenericBlock extends BlockFarmland implements IOxygenReliantBlock{
	
	Random rand;
	
	public GenericBlock (Material material)
    {
            super();
            this.setTickRandomly(true);
            setStepSound(soundTypeGravel);
            //setCreativeTab(CreativeTabs.tabBlock);
            rand = new Random();
    }
	
	@Override
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        EnumPlantType plantType = plantable.getPlantType(world, x, y, z);
        if (plantType == EnumPlantType.Crop)
            return true;

        return false;
    }
	
	@Override
    public void updateTick(World par1World, int x, int y, int z, Random random)
    {
		//immediately check if in vacuum
    	this.checkOxygen(par1World, x, y, z);
    	/*
    	//update the plant block on top of us
    	Block block = par1World.getBlock(x, y + 1, z);
        if (block instanceof IPlantable || block instanceof IGrowable)
            block.updateTick(par1World, x, y + 1, z, random);
            */
        //check for water
        int l = par1World.getBlockMetadata(x, y, z);	//get current metadata
        if (!this.CheckWater(par1World, x, y, z) && !par1World.canLightningStrikeAt(x, y + 1, z))
        {
        	if (l > 0)
            {
            	//decrease metadata (moisture)
                par1World.setBlockMetadataWithNotify(x, y, z, l - 1, 2);
            }
            //skip setting to dirt if no crops
        }
        else
        {
        	//increase metadata
            par1World.setBlockMetadataWithNotify(x, y, z, l+1, 2);
        }
    }
	
	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4){
		par1World.scheduleBlockUpdate(par2, par3, par4, this, par1World.rand.nextInt(40)+100);
		//this.checkOxygen(par1World, par2, par3, par4);
	}
	
	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5){
		
		par1World.scheduleBlockUpdate(par2, par3, par4, this, par1World.rand.nextInt(30));
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
	
	
	
	
	@Override
	public void onOxygenRemoved(World world, int x, int y, int z) {
		//Oxygen gone, set to dirt
		if (world.provider instanceof IGalacticraftWorldProvider)
        {
			//System.out.println(world.isRemote);
			world.setBlock(x, y, z, Blocks.dirt, 0, 2);
            world.notifyBlocksOfNeighborChange(x, y, z, this);
        }
	}

	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		//do nothing, block should stay
	}
	
	//return true if water found
	private boolean CheckWater(World p_149821_1_, int p_149821_2_, int p_149821_3_, int p_149821_4_)
    {
        for (int l = p_149821_2_ - 4; l <= p_149821_2_ + 4; ++l)
        {
            for (int i1 = p_149821_3_; i1 <= p_149821_3_ + 1; ++i1)
            {
                for (int j1 = p_149821_4_ - 4; j1 <= p_149821_4_ + 4; ++j1)
                {
                    if (p_149821_1_.getBlock(l, i1, j1).getMaterial() == Material.water)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }
	
	@SideOnly(Side.CLIENT)@Override
    public IIcon getIcon (int side, int meta) {
		return side == 1 ? (meta>0 ? Blocks.farmland.getIcon(1,1) : Blocks.farmland.getBlockTextureFromSide(1)) : Blocks.dirt.getBlockTextureFromSide(side);
    }
	
	
	@SideOnly(Side.CLIENT)@Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
		//do nothing
    }
	
	
}
