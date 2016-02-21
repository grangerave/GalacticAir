package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FakeCraftingTable extends BlockWorkbench {

	public FakeCraftingTable ()
    {
            super();
            
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
            EntityPlayer player, int metadata, float what, float these, float are)
    {
		System.out.println("Fake Crafting Table Right Clicked");
		//world.isRemote check used to be here....
        player.displayGUIWorkbench(x, y, z);
        return true;

    }
	
}
