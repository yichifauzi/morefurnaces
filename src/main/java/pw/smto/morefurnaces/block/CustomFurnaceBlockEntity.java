package pw.smto.morefurnaces.block;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import pw.smto.morefurnaces.MoreFurnaces;

public class CustomFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private int speedMultiplier = 1;
    private String titleTranslationKey = "";
    private final Random random = Random.create();
    public CustomFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(MoreFurnaces.BlockEntities.CUSTOM_FURNACE_ENTITY, pos, state, RecipeType.SMELTING);
    }

    public CustomFurnaceBlockEntity(BlockPos pos, BlockState state, String titleTranslationKey, int speedMultiplier) {
        super(MoreFurnaces.BlockEntities.CUSTOM_FURNACE_ENTITY, pos, state, RecipeType.SMELTING);
        this.titleTranslationKey = titleTranslationKey;
        this.speedMultiplier = speedMultiplier;
    }

    @SuppressWarnings("unused")
    public static void tick(ServerWorld world, BlockPos pos, BlockState state, CustomFurnaceBlockEntity t) {
        for (int i = 0; i < t.speedMultiplier; i++) {
            AbstractFurnaceBlockEntity.tick(world, pos, state, t);
        }
        if (t.random.nextInt(20) >= 18) {
            if ((Boolean)state.get(AbstractFurnaceBlock.LIT)) {
                double d = (double)pos.getX() + 0.5;
                double e = (double)pos.getY();
                double f = (double)pos.getZ() + 0.5;
                if (t.random.nextDouble() < 0.1) {
                    ((ServerWorld)world).playSound(null, d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, 0);
                }
                Direction direction = state.get(AbstractFurnaceBlock.FACING);
                Direction.Axis axis = direction.getAxis();
                double g = 0.52;
                double h = t.random.nextDouble() * 0.6 - 0.3;
                double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52 : h;
                double j = t.random.nextDouble() * 6.0 / 16.0;
                double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52 : h;
                world.getServer().getPlayerManager()
                        .sendToAround(
                                null,
                                d + i, e + j, f + k,
                                10,
                                world.getRegistryKey(),
                                new ParticleS2CPacket(ParticleTypes.SMOKE, false, d + i, e + j, f + k, 0, 0, 0, 0.0f, 1)
                        );
                SimpleParticleType particle = ParticleTypes.FLAME;
                if (t.speedMultiplier == 6) particle = ParticleTypes.SOUL_FIRE_FLAME;
                world.getServer().getPlayerManager()
                        .sendToAround(
                                null,
                                d + i, e + j, f + k,
                                10,
                                world.getRegistryKey(),
                                new ParticleS2CPacket(particle, false, d + i, e + j, f + k, 0, 0, 0, 0.0f, 1)
                        );
            }
        }
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable(this.titleTranslationKey);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        if(nbt.contains("multiplier")) {
            this.speedMultiplier = nbt.getInt("multiplier");
        }
        if(nbt.contains("titleTranslationKey")) {
            this.titleTranslationKey = nbt.getString("titleTranslationKey");
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt("multiplier", this.speedMultiplier);
        nbt.putString("titleTranslationKey", this.titleTranslationKey);
        super.writeNbt(nbt, registries);
    }
}
