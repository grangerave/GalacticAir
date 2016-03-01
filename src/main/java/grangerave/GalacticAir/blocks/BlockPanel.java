package grangerave.GalacticAir.blocks;

import grangerave.GalacticAir.GalacticAir;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockPanel extends Block{

	@SideOnly(Side.CLIENT)
    private IIcon top;
	@SideOnly(Side.CLIENT)
    private IIcon side;
	
	public BlockPanel(Material m) {
		super(m);
		setStepSound(soundTypePiston);
		setBlockName("galacticair.Panel");
		setBlockTextureName("galacticair:panel");
		
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(GalacticAir.panel);
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return (side==1||side==0) ? this.top : this.side;

    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register){
        this.side = register.registerIcon(this.getTextureName());
        this.top = register.registerIcon(this.getTextureName()+"_top");
    }
}
