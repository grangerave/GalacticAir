package grangerave.GalacticAir.blocks;


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

public class SpaceFarmland extends BlockFarmland implements IOxygenReliantBlock{
	/* Metadata conventions:
	 * 0: not watered
	 * 1-7: watered
	 * 12: frozen
	 */
	@SideOnly(Side.CLIENT)
    private IIcon iceIcon;
	
	public SpaceFarmland (Material material)
    {
            super();
            setTickRandomly(true);
            setStepSound(soundTypeGravel);
            setBlockName("galacticair.spaceFarmland");
            //setCreativeTab(CreativeTabs.tabBlock);
    }
	
	@Override
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        EnumPlantType plantType = plantable.getPlantType(world, x, y, z);
        if (plantType == EnumPlantType.Crop)
            return (world.getBlockMetadata(x, y, z)!=12) ? true : false; //if not frozen
        return false;
        
    }

	
	@Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
		//immediately check if in vacuum
    	this.checkOxygen(world, x, y, z);
    	/*
    	//update the plant block on top of us
    	Block block = world.getBlock(x, y + 1, z);
        if (block instanceof IPlantable || block instanceof IGrowable)
            block.updateTick(world, x, y + 1, z, random);
        */
        
        int l = world.getBlockMetadata(x, y, z);	//get current metadata
        if(l==12)//if frozen, don't update
        	return;
        //check for water
        if (!this.CheckWater(world, x, y, z) && !world.canLightningStrikeAt(x, y + 1, z)){
        	if (l > 0){
            	//decrease metadata (moisture)
                world.setBlockMetadataWithNotify(x, y, z, l - 1, 2);
            }
            //skip setting to dirt if no crops
        }
        else if(l<=7)
        {
        	//increase metadata
            world.setBlockMetadataWithNotify(x, y, z, l+1, 2);
        }
    }
	
	@Override
    public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this, world.rand.nextInt(40)+100);
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5){
		super.onNeighborBlockChange(world, x, y, z, par5);
		world.scheduleBlockUpdate(x, y, z, this, world.rand.nextInt(30));
	}
	
	
	
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
		//Oxygen gone, set to frozen
		if (world.provider instanceof IGalacticraftWorldProvider)
        {
			world.setBlockMetadataWithNotify(x, y, z, 12, 2);
            world.notifyBlocksOfNeighborChange(x, y, z, this);
        }
	}

	@Override
	public void onOxygenAdded(World world, int x, int y, int z) {
		//do nothing, block should stay
	}
	
	//return true if water found in a 4x4x1 area
	private boolean CheckWater(World world, int x, int y, int z){
        for (int l = x - 4; l <= x + 4; ++l){
            for (int i1 = y; i1 <= y + 1; ++i1){
                for (int j1 = z - 4; j1 <= z + 4; ++j1){
                    if (world.getBlock(l, i1, j1).getMaterial() == Material.water){
                        return true;
                    }
                }
            }
        }

        return false;
    }
	
	@SideOnly(Side.CLIENT)@Override
    public IIcon getIcon (int side, int meta) {
		//sides: dirt, top: dry, wet, or icy farmland icon.
		return side == 1 ? (meta==12 ? iceIcon : Blocks.farmland.getIcon(side,meta)) : Blocks.dirt.getIcon(side,meta);
    }
	
	
	@SideOnly(Side.CLIENT)@Override
    public void registerBlockIcons(IIconRegister register)
    {
		//only register iced texture
		this.iceIcon = register.registerIcon("galacticair:farmland_ice");
    }
	
	
}
