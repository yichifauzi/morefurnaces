package pw.smto.morefurnaces.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pw.smto.morefurnaces.block.CustomFurnaceBlockEntity;

@Mixin(FurnaceOutputSlot.class)
public abstract class FurnaceOutputSlotMixin extends Slot {
    @Final
    @Shadow
    private PlayerEntity player;

    protected FurnaceOutputSlotMixin(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(at = @At("TAIL"), method = "onCrafted(Lnet/minecraft/item/ItemStack;)V")
    public void canInsert(ItemStack stack, CallbackInfo ci) {
        if (this.player instanceof ServerPlayerEntity serverPlayerEntity && this.inventory instanceof CustomFurnaceBlockEntity abstractFurnaceBlockEntity) {
            abstractFurnaceBlockEntity.dropExperienceForRecipesUsed(serverPlayerEntity);
        }
    }
}
