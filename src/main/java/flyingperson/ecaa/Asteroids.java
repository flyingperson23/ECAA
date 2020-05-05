package flyingperson.ecaa;

import flyingperson.ecaa.worldgen.ChunkProviderEcaaAsteroids;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.SpecialAsteroidBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class Asteroids {

	// SET TO TRY TO LOAD THE TESTING METHOD
	private static final boolean doTest = false;

	/**
	 * Method called in Init stage of mod class
	 * 
	 */
	public static void registerData() {
		if (doTest) {
			initTest();
		} else {
			initCore();
			initShell();
			initOtherBlocks();
		}
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
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 2, 5, .3));

		// asteroid_rock_1
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 1, 7, .3));

		// asteroid_rock_2
		addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 0, 11, .25));

		// Remove both lines if these are to be removed
		if (!ConfigManagerAsteroids.disableAluminumGen)
			addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 3, 5, .2));

		if (!ConfigManagerAsteroids.disableIlmeniteGen)
			addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 4, 4, .15));

		if (!ConfigManagerAsteroids.disableIronGen)
			addCore(newSpecialAsteroidBlock(AsteroidBlocks.blockBasic, 5, 3, .2));

		if (ConfigManagerCore.enableSiliconOreGen)
			addCore(newSpecialAsteroidBlock(GCBlocks.basicBlock, 8, 2, .15));

		// Solid Meteoric Iron - has no config to disable
		addCore(newSpecialAsteroidBlock(GCBlocks.basicBlock, 12, 2, .13));

		// Diamond ore - has no config to disable
		addCore(newSpecialAsteroidBlock(Blocks.DIAMOND_ORE, 0, 1, .1));

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
	private static void initTest() {

		// ADD TEST CORE BLOCKS
		addCore(newSpecialAsteroidBlockMetaless(Blocks.BOOKSHELF, 15, .15));
		addCore(newSpecialAsteroidBlockMetaless(Blocks.DIAMOND_BLOCK, 20, .15));
		addCore(newSpecialAsteroidBlockMetaless(Blocks.DIRT, 20, .15));
		addCore(newSpecialAsteroidBlock(Blocks.BEDROCK, 0, 20, .15));
		// ADD TEST SHELL BLOCKS
		addShell(newSpecialAsteroidBlockMetaless(Blocks.STONEBRICK, 15, .15));
		addShell(newSpecialAsteroidBlock(Blocks.COBBLESTONE, 0, 20, .15));
		// ADD TEST ASTEROID BLOCKS
		asteroidBlocks(Blocks.REDSTONE_BLOCK);
		asteroidBlocks(Blocks.IRON_BLOCK);
		asteroidBlocks(Blocks.EMERALD_BLOCK);
	}

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
	 * @param meta        data for block
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
