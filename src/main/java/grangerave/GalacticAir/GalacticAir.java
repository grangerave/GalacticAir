package grangerave.GalacticAir;

import grangerave.GalacticAir.blocks.*;
import grangerave.GalacticAir.handlers.*;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = GalacticAir.MODID, version = GalacticAir.VERSION,name = GalacticAir.MODNAME)
public class GalacticAir
{
    public static final String MODID = "galacticair";
    //Set the "Name" of the mod.
    public static final String MODNAME = "Galactic Air";
    public static final String VERSION = "1.0";
    
    @Instance(value = GalacticAir.MODID) //Tell Forge what instance to use.
    public static GalacticAir instance;
    
    
    
    //block definitions
    public static Block spaceFarmland;
    public static Block fakeWorkbench;
    
    public static Block sapling;
    public static Block furnace;
    public static Block litfurnace;
    //cosmetic blocks
    public static Block pumice;
    public static Block ribglass;
    public static Block panel;
    //liquids/gases
    public static Block space_water_block;
	public static Block space_lava_block;
	//public static Block smoke;
	
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// print during initialization
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        System.out.println("-----------------");
        
        //define blocks
        //space_lava = new Space_Liquid(Material.lava);
        //liquids
        space_water_block = new Space_Fluid(FluidRegistry.WATER, Material.water);
        space_lava_block = new Space_Fluid(FluidRegistry.LAVA, Material.lava);
        //util
        spaceFarmland = new SpaceFarmland(Material.ground);
        sapling = new SpaceSapling();
        furnace = new SpaceFurnace(false);
        litfurnace = new SpaceFurnace(true);
        //smoke = new BlockSmoke();
        //cosmetic stuff
        pumice = new BlockPumice();
        ribglass = new RibGlass(Material.glass,false);
        panel = new BlockPanel(Material.rock);

        
        //Register Blocks with forge
        //liquids
        GameRegistry.registerBlock(space_water_block, "spaceWaterBlock").setHardness(100F);
        GameRegistry.registerBlock(space_lava_block, "spaceLavaBlock").setHardness(100F);
        //utility
        GameRegistry.registerBlock(spaceFarmland, "spaceFarmland").setHardness(0.5F).setHarvestLevel("shovel", 0);
        GameRegistry.registerBlock(sapling, "Space Sapling").setHardness(0.0F);
        GameRegistry.registerBlock(furnace, "Space Furnace").setHardness(3.5F).setCreativeTab(CreativeTabs.tabDecorations).setResistance(2.0F).setHarvestLevel("pickaxe", 0);
        GameRegistry.registerBlock(litfurnace, "Lit Space Furnace").setHardness(3.5F).setLightLevel(0.875F).setResistance(2.0F).setHarvestLevel("pickaxe", 0);
        //GameRegistry.registerBlock(fakeWorkbench, "FakeCraftingTable");
        //cosmetic
        GameRegistry.registerBlock(pumice, "Pumice").setHardness(1.5F).setResistance(2.0F).setHarvestLevel("pickaxe", 0);
        GameRegistry.registerBlock(ribglass, "Ribbed Glass").setHardness(0.5F);
        GameRegistry.registerBlock(panel, "Plastic Panel").setHardness(0.8F);
        //GameRegistry.registerBlock(smoke, "BlockSmoke").setLightOpacity(1).setHardness(4.0F);
        
        
        //Register Tile entities
        GameRegistry.registerTileEntity(grangerave.GalacticAir.tile.TileEntitySpaceFurnace.class, "spaceFurnace");
        
        //Register Event Listeners
        MinecraftForge.EVENT_BUS.register(new GalacticAirEventHandler());
        
        // End Basic Blocks

    }
    

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	GCBlocks.blockMoon.setStepSound(Block.soundTypeSand);
    	GCBlocks.fallenMeteor.setHarvestLevel("pickaxe", 1);
    	/*
    	String[] sealIDs = ConfigManagerCore.sealableIDs;
    	boolean flag = false;
    	for(int i=0;i<sealIDs.length;i++){
    		System.out.println(ConfigManagerCore.sealableIDs[i]);
    		if(sealIDs[i]=="galacticair:RibGlass"){
    			flag = true;
    			break;
    		}
    	}
    	if(!flag)
    		ConfigManagerCore.sealableIDs= sealIDs.
    		*/
    }

}
