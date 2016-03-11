package grangerave.GalacticAir.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import glenn.gasesframework.GasesFramework;
import grangerave.GalacticAir.GalacticAir;
import grangerave.GalacticAir.blocks.SpaceFurnace;
import grangerave.GalacticAir.blocks.Space_Fluid;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;

public class TileEntitySpaceFurnace extends TileEntityFurnace{
	
	public boolean oxygen = true;
	private int ticks = 0;
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    
    @Override
    public ItemStack getStackInSlot(int i)
    {
        return this.furnaceItemStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount)
    {
        if (this.furnaceItemStacks[index] != null)
        {
            ItemStack itemstack;

            if (this.furnaceItemStacks[index].stackSize <= amount)
            {
                itemstack = this.furnaceItemStacks[index];
                this.furnaceItemStacks[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.furnaceItemStacks[index].splitStack(amount);

                if (this.furnaceItemStacks[index].stackSize == 0)
                {
                    this.furnaceItemStacks[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack)
    {
        this.furnaceItemStacks[i] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing (int slot) {
        return null;
    }
    
    @Override
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }
    
    public void setOxygen(boolean b){
    	oxygen = b;
    	if(!oxygen){
    		this.furnaceBurnTime = 0;
			this.furnaceCookTime = 0;
			this.currentItemBurnTime=0;
    	}
    }
    
    @Override
    public void updateEntity()	//called on update tick
    {
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;
        
        if (this.furnaceBurnTime > 0)
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
        	ticks++;
        	if(ticks %500 == 0){	//check oxygen
        		ticks = 0;
        		setOxygen(OxygenUtil.isAABBInBreathableAirBlock(this.worldObj, AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2)));
        	}
        	if(ticks%200 == 0 &&  this.furnaceBurnTime !=0){	//if in oxygen, and cooking, make a smoke poof//
        		//spawn smoke particle near front of furnace
        		if(worldObj.rand.nextBoolean()){
        			int i = 0;
        			int j = 0;
        			if(blockMetadata<4){
        				j = -1 + (blockMetadata%2)*2;
        			}else{
        				i = -1 + (blockMetadata%2)*2;
        			}
        			//Space_Fluid.destroyFx(GalacticraftCore.proxy.getClientWorld(),xCoord+i,yCoord,zCoord+j,8);
        			
        			//check in front of
        			if(worldObj.getBlock(xCoord+i, yCoord, zCoord+j).isAir(worldObj, xCoord+i, yCoord, zCoord+j))
        				worldObj.setBlock(xCoord+i, yCoord, zCoord+j,GasesFramework.registry.getGasBlock(GasesFramework.gasTypeSmoke),0,2);
        			else if(worldObj.getBlock(xCoord+i, yCoord-1, zCoord+j).isAir(worldObj, xCoord+i, yCoord-1, zCoord+j))
        				worldObj.setBlock(xCoord+i, yCoord-1, zCoord+j,GasesFramework.registry.getGasBlock(GasesFramework.gasTypeSmoke),0,2);
        			else if(worldObj.getBlock(xCoord+2*i, yCoord, zCoord+2*j).isAir(worldObj, xCoord+2*i, yCoord, zCoord+2*j))
        				worldObj.setBlock(xCoord+2*i, yCoord, zCoord+2*j,GasesFramework.registry.getGasBlock(GasesFramework.gasTypeSmoke),0,2);
        			//else if((worldObj.getBlock(xCoord+i, yCoord, zCoord+j)) instanceof BlockSmoke)
        		}
        	}
        	
            if (this.furnaceBurnTime != 0 || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null)
            {
                if (this.furnaceBurnTime == 0 && this.canSmelt() && oxygen)
                {
                    this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                    if (this.furnaceBurnTime > 0)
                    {
                        flag1 = true;

                        if (this.furnaceItemStacks[1] != null)
                        {
                            --this.furnaceItemStacks[1].stackSize;

                            if (this.furnaceItemStacks[1].stackSize == 0)
                            {
                                this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.furnaceCookTime;

                    if (this.furnaceCookTime == 400)
                    {
                        this.furnaceCookTime = 0;
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.furnaceCookTime = 0;
                }
            }

            if (flag != this.furnaceBurnTime > 0)
            {
                flag1 = true;
                SpaceFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getCookProgressScaled(int i)
    {
        return this.furnaceCookTime * i/ 400;
    }

    //check if smelting is possible with current item configurations
	private boolean canSmelt()
    {
		if(!oxygen)
			return false;
		
        if (this.furnaceItemStacks[0] == null){
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true;
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }


    //Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null)
            {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0)
            {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.furnaceCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            if (this.furnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);

    }
    
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length)
            {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = compound.getShort("BurnTime");
        this.furnaceCookTime = compound.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
    }
	
}
