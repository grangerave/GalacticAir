package grangerave.GalacticAir.blocks;

import grangerave.GalacticAir.GalacticAir;

import java.util.Random;

import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPumice extends BlockStone{
	
	private int[] colorMap = new int[]{11842740,13283755,14457740,15768171,15514995,16047250,16575893};
	private int delay = 100;
	
	
	public BlockPumice(){
		super();
		setBlockName("galacticair.Pumice");
		setBlockTextureName("galacticair:pumice");
        setCreativeTab(CreativeTabs.tabBlock);
        setTickRandomly(false);
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(GalacticAir.pumice);
    }
	
	/*
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,EntityPlayer p,int n,float xx,float yy,float zz){
		int l = world.getBlockMetadata(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, l+1, 2);
		return true;
	}
	*/
	
	private boolean checkLava(World world, int x, int y, int z){
		int[] pos = new int[]{x,y,z};
		for(int i=0;i<3;i++){
			for(int j=-1;j<=1;j+=2){
				pos[i]+=j;
				if(world.getBlock(pos[0], pos[1], pos[2]).getMaterial()==Material.lava){
					return true;
				}
				pos[i]-=j;
			}
		}
		return false;
	}
	
	@Override
	public void onBlockAdded(World world,int x,int y,int z){
		
		super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this, delay+world.rand.nextInt(60));
	}
	
	@Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
		int l = world.getBlockMetadata(x, y, z);
		if(l>0){
			
			world.scheduleBlockUpdate(x, y, z, this, delay + world.rand.nextInt(60));
			//if(!checkLava(world,x,y,z))
				world.setBlockMetadataWithNotify(x, y, z, l-1, 2);
		}
    }
	
    
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        //return this.getBlockColor();
    	int l = world.getBlockMetadata(x, y, z);
    	return colorMap[Math.min(l, 6)];
    }
	
	
	/*
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(int side, int meta) {
		return face;
    }



	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        this.blockIcon = register.registerIcon("pumice");
    }
*/	

}
