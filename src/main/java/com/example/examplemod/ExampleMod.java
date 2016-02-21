package com.example.examplemod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION,name = ExampleMod.MODNAME)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    //Set the "Name" of the mod.
    public static final String MODNAME = "Example Mod";
    public static final String VERSION = "1.0";
    
    @Instance(value = ExampleMod.MODID) //Tell Forge what instance to use.
    public static ExampleMod instance;
    
    /*
    
    
    //block definitions
    public static Block spaceFarmland;
    public static Block fakeWorkbench;
    public static Block pumice;
    public static Fluid space_water;
    public static Block space_water_block;
	public static Block space_lava_block;
	//public static Block space_lava;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// print during initialization
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        System.out.println("-----------------");
        
        //define blocks
        //space_lava = new Space_Liquid(Material.lava);
        
        space_water_block = new Space_Fluid(FluidRegistry.WATER, Material.water);
        space_lava_block = new Space_Fluid(FluidRegistry.LAVA, Material.lava);
        spaceFarmland = new GenericBlock(Material.ground);
        pumice = new BlockPumice();
        //fakeWorkbench = new FakeCraftingTable().setBlockName("FakeCraftingTable");
        //Register Blocks with forge
        

		//GameRegistry.registerBlock(space_lava, "spaceLava");
        GameRegistry.registerBlock(space_water_block, "spaceWaterBlock").setHardness(100F);
        GameRegistry.registerBlock(space_lava_block, "spaceLavaBlock").setHardness(100F);
        GameRegistry.registerBlock(spaceFarmland, "spaceFarmland").setHardness(0.5F).setHarvestLevel("shovel", 0);
        GameRegistry.registerBlock(pumice, "Pumice").setHardness(1.5F).setResistance(2.0F).setHarvestLevel("pickaxe", 0);
        //GameRegistry.registerBlock(fakeWorkbench, "FakeCraftingTable");
        
        //Register Event Listeners
        MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
        
        // End Basic Blocks
    }
    
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	GCBlocks.blockMoon.setStepSound(Block.soundTypeSand);
    }
    */

}
