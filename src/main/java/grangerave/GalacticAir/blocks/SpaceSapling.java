package grangerave.GalacticAir.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;

public class SpaceSapling extends BlockSapling implements IOxygenReliantBlock {

	public SpaceSapling ()
    {
            super();
            setStepSound(soundTypeGrass);
            setTickRandomly(true);
            setBlockName("galacticair.spaceSapling");
    }
	
	/*
	@Override
    public void updateTick(World par1World, int x, int y, int z, Random random)
    {
		//immediately check if in vacuum
    	this.checkOxygen(par1World, x, y, z);
    }
    */
	
	private void checkOxygen(World world, int x, int y, int z)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            if (OxygenUtil.checkTorchHasOxygen(world, this, x, y, z))
                this.onOxygenAdded(world, x, y, z);
            else
                this.onOxygenRemoved(world, x, y, z);
        }
    }
	
	@Override
	public void onOxygenRemoved(World world, int x, int y, int z) {
		//Oxygen gone, set to dirt
		if (world.provider instanceof IGalacticraftWorldProvider)
		{
			world.setBlock(x, y, z, Blocks.deadbush, 0, 2);
		    world.notifyBlocksOfNeighborChange(x, y, z, this);
		}
	}

	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}
	
	@SideOnly(Side.CLIENT)@Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
		for(int i=0;i<=5;i++){
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	@SideOnly(Side.CLIENT)@Override
    public IIcon getIcon (int side, int meta) {
		//use parent texture
		return Blocks.sapling.getIcon(side, meta);
    }
	
	@SideOnly(Side.CLIENT)@Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
		//do nothing
    }

}
