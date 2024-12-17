package pw.smto.morefurnaces;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.LoggerFactory;
import pw.smto.morefurnaces.api.MoreFurnacesContent;
import pw.smto.morefurnaces.block.CustomFurnaceBlock;
import pw.smto.morefurnaces.block.CustomFurnaceBlockEntity;
import pw.smto.morefurnaces.item.FurnaceModuleItem;
import pw.smto.morefurnaces.item.GenericBlockItem;

import java.lang.reflect.Field;
import java.util.*;

public class MoreFurnaces implements ModInitializer {
	public static final String MOD_ID = "morefurnaces";
	public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MoreFurnaces.MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MoreFurnaces.MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(MoreFurnaces.MOD_ID);
		PolymerResourcePackUtils.markAsRequired();

		for (Field field : Blocks.class.getFields()) {
			try {
				if (field.get(null) instanceof MoreFurnacesContent block) {
					Registry.register(Registries.BLOCK, block.getIdentifier(), (Block)block);
				}
			} catch (Throwable ignored) {
                MoreFurnaces.LOGGER.error("Failed to register block: {}", field.getName());
			}
		}

		for (Field field : BlockEntities.class.getFields()) {
			try {
				Registry.register(Registries.BLOCK_ENTITY_TYPE, MoreFurnaces.id(field.getName().toLowerCase(Locale.ROOT)), (BlockEntityType<?>) field.get(null));
				PolymerBlockUtils.registerBlockEntity((BlockEntityType<?>) field.get(null));
			} catch (Throwable ignored) {
                MoreFurnaces.LOGGER.error("Failed to register block entity type: {}", field.getName());
			}
		}

		for (Field field : Items.class.getFields()) {
            try {
				if (field.get(null) instanceof Item item) {
					Identifier id = MoreFurnaces.id(field.getName().toLowerCase(Locale.ROOT));
					if (item instanceof MoreFurnacesContent b) {
						if (b.getIdentifier().equals(id)) {
							Registry.register(Registries.ITEM, id, item);
						}
					}
				}
            } catch (Exception ignored) {
                MoreFurnaces.LOGGER.error("Failed to register item: {}", field.getName());
			}
        }

		PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(MoreFurnaces.MOD_ID,"items"), PolymerItemGroupUtils.builder()
				.icon(() -> new ItemStack(Items.IRON_FURNACE))
				.displayName(Text.of("More Furnaces"))
				.entries((context, entries) -> {
					entries.add(Items.IRON_FURNACE);
					entries.add(Items.GOLD_FURNACE);
					entries.add(Items.DIAMOND_FURNACE);
					entries.add(Items.NETHERITE_FURNACE);
					entries.add(Items.DOUBLE_FURNACE_MODULE);
					entries.add(Items.HALF_FURNACE_MODULE);
				}).build());

        MoreFurnaces.LOGGER.info("MoreFurnaces loaded!");
	}
	public static class Blocks {
		public static Block IRON_FURNACE = new CustomFurnaceBlock(MoreFurnaces.id("iron_furnace"), 2, BlockSoundGroup.METAL);
		public static Block GOLD_FURNACE = new CustomFurnaceBlock(MoreFurnaces.id("gold_furnace"), 3, BlockSoundGroup.METAL);
		public static Block DIAMOND_FURNACE = new CustomFurnaceBlock(MoreFurnaces.id("diamond_furnace"), 4, BlockSoundGroup.METAL);
		public static Block NETHERITE_FURNACE = new CustomFurnaceBlock(MoreFurnaces.id("netherite_furnace"), 6, BlockSoundGroup.NETHERITE);
	}

	public static class Items {
		public static BlockItem IRON_FURNACE = new GenericBlockItem(MoreFurnaces.id("iron_furnace"), Blocks.IRON_FURNACE);
		public static BlockItem GOLD_FURNACE = new GenericBlockItem(MoreFurnaces.id("gold_furnace"), Blocks.GOLD_FURNACE);
		public static BlockItem DIAMOND_FURNACE = new GenericBlockItem(MoreFurnaces.id("diamond_furnace"), Blocks.DIAMOND_FURNACE);
		public static BlockItem NETHERITE_FURNACE = new GenericBlockItem(MoreFurnaces.id("netherite_furnace"), Blocks.NETHERITE_FURNACE);

		public static Item DOUBLE_FURNACE_MODULE = new FurnaceModuleItem(MoreFurnaces.id("double_furnace_module"), FurnaceModule.DOUBLE_SPEED_AND_FUEL);
		public static Item HALF_FURNACE_MODULE = new FurnaceModuleItem(MoreFurnaces.id("half_furnace_module"), FurnaceModule.HALF_SPEED_AND_FUEL);
	}

	public static class BlockEntities {
		public static BlockEntityType<CustomFurnaceBlockEntity> CUSTOM_FURNACE_ENTITY = FabricBlockEntityTypeBuilder
				.create(CustomFurnaceBlockEntity::new, Blocks.IRON_FURNACE, Blocks.GOLD_FURNACE, Blocks.DIAMOND_FURNACE, Blocks.NETHERITE_FURNACE).build();
	}
}