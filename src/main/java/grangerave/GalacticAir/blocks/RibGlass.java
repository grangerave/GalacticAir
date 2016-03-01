package grangerave.GalacticAir.blocks;

import grangerave.GalacticAir.GalacticAir;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class RibGlass extends BlockGlass{

	public RibGlass(Material m, boolean b) {	//constructor
		super(m, b);
		//this.setCreativeTab(tab);
		setBlockName("galacticair.RibGlass");
		setBlockTextureName("galacticair:ribglass");
		setStepSound(soundTypeGlass);
	}
	

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
		//drop block
        return Item.getItemFromBlock(GalacticAir.ribglass);
    }
	
	@Override
	public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }
	
	@SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;	//alpha, not solid
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        this.blockIcon = register.registerIcon("galacticair:ribglass");
    }

}
