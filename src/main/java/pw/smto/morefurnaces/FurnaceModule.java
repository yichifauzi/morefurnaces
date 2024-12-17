package pw.smto.morefurnaces;

import net.minecraft.item.ItemStack;

import java.util.function.Function;

public enum FurnaceModule {
    NO_MODULE(f -> f, m -> m),
    DOUBLE_SPEED_AND_FUEL(f -> f / 2, m -> m * 2),
    HALF_SPEED_AND_FUEL(f -> f * 2, m -> m / 2);
    private final Function<Integer, Integer> fuelAdjustment;
    private final Function<Integer, Integer> speedMultiplierAdjustment;

    FurnaceModule(Function<Integer, Integer> fuelAdjustment, Function<Integer, Integer> speedMultiplierAdjustment) {
        this.fuelAdjustment = fuelAdjustment;
        this.speedMultiplierAdjustment = speedMultiplierAdjustment;
    }

    public int adjustFuelTime(int originalTime) {
        return this.fuelAdjustment.apply(originalTime);
    }

    public int adjustSpeedMultiplier(int originalSpeedMultiplier) {
        return this.speedMultiplierAdjustment.apply(originalSpeedMultiplier);
    }

    public ItemStack getItemStack() {
        switch(this) {
            case DOUBLE_SPEED_AND_FUEL -> {
                return MoreFurnaces.Items.DOUBLE_FURNACE_MODULE.getDefaultStack();
            }
            case HALF_SPEED_AND_FUEL -> {
                return MoreFurnaces.Items.HALF_FURNACE_MODULE.getDefaultStack();
            }
        }
        return ItemStack.EMPTY;
    }
}
