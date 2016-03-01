package grangerave.GalacticAir.handlers;

import grangerave.GalacticAir.GalacticAir;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;


public class GalacticAirEventHandler {

	private int[] map = new int[]{-1,1,-1,1,-1,1};
	private int[] indx = new int[]{1,1,2,2,0,0};
	/*
	 * Gets called when player right clicks block. If player is hoe-ing a dirt block in galacticraft world,
	 * then cancel event, and replace targeted dirt with space-farmland (oxygen dependent)
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void HoeGround(UseHoeEvent event){
		//early out if not in galacticraft world
		if(!(event.world.provider instanceof IGalacticraftWorldProvider)){
			return;
		}
		//early out if block isn't dirt
		if(event.world.getBlock(event.x,event.y,event.z)!=Blocks.dirt && event.world.getBlock(event.x,event.y,event.z)!=GalacticAir.spaceFarmland){
			return;
		}
		//set the dirt to space farmland
		event.world.setBlock(event.x, event.y, event.z, GalacticAir.spaceFarmland, 0, 2);
		event.world.playSoundEffect((double)((float)event.x + 0.5F), (double)((float)event.y + 0.5F), (double)((float)event.z + 0.5F), "step.gravel", 1.0F, 0.05F);
		//finish the event: return damaged item, etc.
		event.setResult(Result.ALLOW);
		return;
	}
	
	/* Gets called on bonemeal event. Checks if item is bonemeal, and cancels event if so 
	*/
	/*
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void NoBonemeal(BonemealEvent event){
		//early out if not in galacticraft world
		if(!(event.world.provider instanceof IGalacticraftWorldProvider)){
			return;
		}
		//check configs, etc here
		event.setResult(Result.DENY);	//no bonemealing
		//event.useItem = Result.DENY;
        event.setCanceled(true);
		return;
	}
	*/
	
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void NoSapling(SaplingGrowTreeEvent event){
		System.out.println("tried to sapling grow");
		//early out if not in galacticraft world
		if(!(event.world.provider instanceof IGalacticraftWorldProvider))
			return;
		event.world.setBlock(event.x, event.y, event.z, Blocks.deadbush,0,2);
		event.setResult(Result.DENY);	//no growing
		//event.useItem = Result.DENY;
        event.setCanceled(true);
		return;
		
	}
	
	
	/* Called when using bucket. If lava, place space lava block; if water bucket, place space water block  
	*/
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void waterBucket(FillBucketEvent event){
		if(event.world.isRemote){//don't want client doing things
			return;
		}
		//early out if not in galacticraft world
		if(!(event.world.provider instanceof IGalacticraftWorldProvider)){
			return;
		}
		//early out if bucket missed
		if(event.target.typeOfHit != MovingObjectType.BLOCK){
			return;
		}
		
		int[] pos = new int[3];
		pos[0] = event.target.blockX;
		pos[1] = event.target.blockY;
		pos[2] = event.target.blockZ;
		
		FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(event.current);
		boolean empty = (liquid==null);	//is the bucket empty?
		if(empty){	//Empty
			return;
		}//not empty then.
		//offset position by 1 towards clicked face
		pos[indx[event.target.sideHit]] += map[event.target.sideHit];
		
		if(liquid.getFluid().equals(FluidRegistry.WATER)){	//WATER
			event.world.setBlock(pos[0],pos[1],pos[2], GalacticAir.space_water_block, 0, 2);
			//return empty bucket
			event.result = new ItemStack(Items.bucket, event.current.stackSize);
		}else if(liquid.getFluid().equals(FluidRegistry.LAVA)){		//LAVA
			//set to fake water
			event.world.setBlock(pos[0],pos[1],pos[2], GalacticAir.space_lava_block, 0, 2);
			//return empty bucket
			event.result = new ItemStack(Items.bucket, event.current.stackSize);
		}

		event.setResult(Result.ALLOW);
		return;
	}
	
	/*
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void tick(WorldTickEvent event){
		event.world.loadedTileEntityList.contains(TileEntityFurnace);
		
	}*/
}
