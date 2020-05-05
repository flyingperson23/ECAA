package flyingperson.ecaa;

import flyingperson.ecaa.worldgen.ChunkProviderEcaaAsteroids;
import gtc_expansion.GTCXBlocks;
import gtc_expansion.block.GTCXBlockOre;
import gtclassic.common.GTBlocks;
import ic2.core.block.resources.BlockMetal;
import ic2.core.platform.registry.Ic2States;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.SpecialAsteroidBlock;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class Asteroids {

	/**
	 * Method called in Init stage of mod class
	 * 
	 */
	public static void registerData() {
		initCore();
		initShell();
		initOtherBlocks();
	}

	/**
	 * Register your blocks to use in asteroid cores during worldgen <br>
	 * 
	 * These are the default blocks that are set in Galacticraft, remove or add as
	 * needed but recommend to comment them out instead if removing in the case that
	 * you need to revert a change.
	 * 
	 */
	private static void initCore() {

		// asteroid_rock_0
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 2, 2, .3));

		// asteroid_rock_1
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 1, 1, .3));

		// asteroid_rock_2
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 0, 2, .25));

		//Aluminum
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 3, 5, .2));

		//Ilmenite
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 4, 4, .15));

		//Iron
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 5, 3, .2));

		//Copper
		addCore(newSpecialAsteroidBlockMetaless(Ic2States.copperOre.getBlock(), 4, .2));

		//Tin
		addCore(newSpecialAsteroidBlock(Ic2States.tinOre.getBlock(), 1, 4, .2));

		//Lead
		addCore(newSpecialAsteroidBlock(VenusBlocks.venusBlock,8, 4, .2));

		//Silver
		addCore(newSpecialAsteroidBlock(Ic2States.silverOre.getBlock(), 3, 4, .2));

		//Uranium
		addCore(newSpecialAsteroidBlock(Ic2States.uraniumOre.getBlock(), 2,4, .2));

		//Silicon
		addCore(newSpecialAsteroidBlock(GCBlocks.basicBlock, 8, 2, .15));

		// Solid Meteoric Iron
		addCore(newSpecialAsteroidBlock(GCBlocks.basicBlock, 12, 2, .13));

		// Diamond ore
		addCore(newSpecialAsteroidBlock(Blocks.DIAMOND_ORE, 0, 1, .1));

		//Iridium ore
		addCore(newSpecialAsteroidBlockMetaless(GTBlocks.oreIridium, 1, .1));

		//Ruby
		addCore(newSpecialAsteroidBlockMetaless(GTBlocks.oreRuby, 4, .2));

		//Olivine
		addCore(newSpecialAsteroidBlockMetaless(GTCXBlocks.oreOlivine, 4, .2));

	}

	/**
	 * Register your blocks to use in asteroid shells during worldgen <br>
	 * 
	 * These are the default blocks that are set in Galacticraft, remove or add as
	 * needed but recommend to comment them out instead if removing in the case that
	 * you need to revert a change.
	 * 
	 */
	private static void initShell() {
		addShell(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 0, 1, .15));
		addShell(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 1, 3, .15));
		addShell(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 2, 1, .15));
		addShell(newSpecialAsteroidBlock(AsteroidBlocks.blockDenseIce, 0, 1, .15));
	}

	/**
	 * Register your blocks to use in asteroids that isn't part of our special core
	 * or shell parameters for worldgen <br>
	 * 
	 * These are the default blocks that are set in Galacticraft, remove or add as
	 * needed but recommend to comment them out instead if removing in the case that
	 * you need to revert a change.
	 * 
	 */
	private static void initOtherBlocks() {
		
		// if your going to remove this block (3 terrain blocks and ores as well)
		// its best if you make sure to replace them with the same amount of blocks
		// so we dont break worldgen as this is still kind of beta i guess
		
		// blocke metas used with this 1 block are [ 0, 1, 2, 3, 4, 5
		
		asteroidBlocks(AsteroidBlocks.blockBasic);
	}

	/**
	 * Adds the new SpecialAsteroidBlock to the ArrayList of blocks to use in our
	 * ChunkGenerator for asteroid cores
	 *
	 * @param asteroidBlock the asteroid block
	 */



	/**
	 * Easy setter method for building a new special asteroid block without the need
	 * to cast byte to meta <br>
	 * Unless your block is setup in the same style as GC Blocks like
	 * BlockBasicAsteroids.java you might not use this method mainly used to set the
	 * default dimension blocks
	 *
	 * @param block       to use
	 * @param meta        data for block
	 * @param probability chances this block is used in worldgen
	 * @param thickness   expressed as a double (ex: .15 or .30)
	 * @return new SpecialAsteroidBlock
	 */
	private static SpecialAsteroidBlock newSpecialAsteroidBlock(Block block, int meta, int probability,
			double thickness) {
		return new SpecialAsteroidBlock(block, (byte) meta, probability, thickness);
	}

	/**
	 * Easy setter method for building a new special asteroid block that has no
	 * meta, method returns 0 by default
	 *
	 * @param block       to use
	 * @param probability chances this block is used in worldgen
	 * @param thickness   expressed as a double (ex: .15 or .30)
	 * @return new SpecialAsteroidBlock
	 */
	private static SpecialAsteroidBlock newSpecialAsteroidBlockMetaless(Block block, int probability,
			double thickness) {
		return new SpecialAsteroidBlock(block, (byte) 0, probability, thickness);
	}

	/**
	 * Adds the new SpecialAsteroidBlock to the ArrayList of blocks to use in our
	 * ChunkGenerator for asteroid cores
	 *
	 * @param asteroidBlock the asteroid block
	 */
	private static void addCore(SpecialAsteroidBlock asteroidBlock) {
		ChunkProviderEcaaAsteroids.coreBlocks.add(asteroidBlock);
	}

	/**
	 * Adds the new SpecialAsteroidBlock to the ArrayList of blocks to use in our
	 * ChunkGenerator for asteroid shells
	 *
	 * @param asteroidBlock the asteroid block
	 */
	private static void addShell(SpecialAsteroidBlock asteroidBlock) {
		ChunkProviderEcaaAsteroids.shellBlocks.add(asteroidBlock);
	}

	/**
	 * Adds the new SpecialAsteroidBlock to the ArrayList of blocks to use in our
	 * ChunkGenerator for asteroid blocks. Which ironically is used the most
	 *
	 * @param block the block
	 */
	private static void asteroidBlocks(Block block) {
		ChunkProviderEcaaAsteroids.asteroidBlocks.add(block);
	}

}
