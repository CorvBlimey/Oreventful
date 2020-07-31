package com.github.corvblimey.oreventful;

import com.github.corvblimey.oreventful.block.*;
import com.github.corvblimey.oreventful.entity.SpawnerRuneBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Oreventful implements ModInitializer {
	ItemGroup ITEMGROUP = ItemGroup.MISC;

	public static final Block ASCENSION_STONE = new AscensionStoneBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
	//public static final Block CRITTER_NEST = new Block(FabricBlockSettings.of(Material.STONE).hardness(2.0f));
	public static final Block CRUMBLING_STONE = new CrumblingStoneBlock(FabricBlockSettings.of(Material.STONE).hardness(0.2f));
	public static final Block GAS_DEPOSIT = new GasDepositBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f));
	public static final Block LAVALOGGED_STONE = new LavaloggedStoneBlock(FabricBlockSettings.of(Material.STONE).hardness(2.0f).lightLevel(2));
	public static final Block RICH_SEAM = new Block(FabricBlockSettings.of(Material.METAL).hardness(4.0f));
	public static final Block SPAWNER_RUNE = new SpawnerRuneBlock(FabricBlockSettings.of(Material.STONE).hardness(8.0f));
	public static final Block WISDOLOMITE = new WisdolomiteBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f));

	public static BlockEntityType<SpawnerRuneBlockEntity> SPAWNER_RUNE_BLOCK_ENTITY;

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "ascension_stone"), ASCENSION_STONE);
		//Registry.register(Registry.BLOCK, new Identifier("oreventful", "critter_nest"), CRITTER_NEST);
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "crumbling_stone"), CRUMBLING_STONE);
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "gas_deposit"), GAS_DEPOSIT);
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "lavalogged_stone"), LAVALOGGED_STONE);
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "spawner_rune"), SPAWNER_RUNE);
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "rich_seam"), RICH_SEAM);
		Registry.register(Registry.BLOCK, new Identifier("oreventful", "wisdolomite"), WISDOLOMITE);

		SPAWNER_RUNE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "oreventful:spawner_rune", BlockEntityType.Builder.create(SpawnerRuneBlockEntity::new, SPAWNER_RUNE).build(null));

		Registry.register(Registry.ITEM, new Identifier("oreventful", "ascension_stone"), new BlockItem( ASCENSION_STONE, new Item.Settings().group(ITEMGROUP)));
		//Registry.register(Registry.ITEM, new Identifier("oreventful", "critter_nest"), new BlockItem(CRITTER_NEST, new Item.Settings().group(ITEMGROUP)));
		Registry.register(Registry.ITEM, new Identifier("oreventful", "crumbling_stone"), new BlockItem( CRUMBLING_STONE, new Item.Settings().group(ITEMGROUP)));
		Registry.register(Registry.ITEM, new Identifier("oreventful", "gas_deposit"), new BlockItem( GAS_DEPOSIT, new Item.Settings().group(ITEMGROUP)));
		Registry.register(Registry.ITEM, new Identifier("oreventful", "lavalogged_stone"), new BlockItem( LAVALOGGED_STONE, new Item.Settings().group(ITEMGROUP)));
		Registry.register(Registry.ITEM, new Identifier("oreventful", "rich_seam"), new BlockItem( RICH_SEAM, new Item.Settings().group(ITEMGROUP)));
		Registry.register(Registry.ITEM, new Identifier("oreventful", "spawner_rune"), new BlockItem(SPAWNER_RUNE, new Item.Settings().group(ITEMGROUP)));
		Registry.register(Registry.ITEM, new Identifier("oreventful", "wisdolomite"), new BlockItem( WISDOLOMITE, new Item.Settings().group(ITEMGROUP)));
	}
}
