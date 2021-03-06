package flyingperson.ecaa.worldgen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import flyingperson.ecaa.ECAA;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeAdaptive;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Billowed;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.SpecialAsteroidBlock;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.SpecialAsteroidBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;



/**
 * This class is from Galacticraft Planets and modified to allow 
 * customization of asteroid blocks used for worldgen
 * 
 * 
 * all credit for the original class go to the Galacticraft developement team
 * 
 * Modified by: ROMVoid95
 * Created: 5/4/2020
 *
 */
public class ChunkProviderEcaaAsteroids extends ChunkProviderBase {

	final Block DIRT = Blocks.DIRT;
	final byte DIRT_META = 0;
	final Block GRASS = Blocks.GRASS;
	final byte GRASS_META = 0;
	final Block LIGHT = Blocks.GLOWSTONE;
	final byte LIGHT_META = 0;
	BlockTallGrass.EnumType GRASS_TYPE = BlockTallGrass.EnumType.GRASS;
	final BlockFlower FLOWER = Blocks.RED_FLOWER;

	final Block LAVA = Blocks.LAVA;
	final byte LAVA_META = 0;
	final Block WATER = Blocks.WATER;
	final byte WATER_META = 0;

	public static final List<SpecialAsteroidBlock> coreBlocks = new ArrayList<SpecialAsteroidBlock>();
	public static final List<SpecialAsteroidBlock> shellBlocks = new ArrayList<SpecialAsteroidBlock>();

	public static final List<Block> asteroidBlocks = new ArrayList<Block>();

	private final Random rand;

	private final World world;

	private final NoiseModule asteroidDensity;

	private final NoiseModule asteroidTurbulance;

	private final NoiseModule asteroidSkewX;
	private final NoiseModule asteroidSkewY;
	private final NoiseModule asteroidSkewZ;

	private final SpecialAsteroidBlockHandler coreHandler;
	private final SpecialAsteroidBlockHandler shellHandler;

	// DO NOT CHANGE
	private static final int CHUNK_SIZE_X = 16;
	private static final int CHUNK_SIZE_Y = 256;
	private static final int CHUNK_SIZE_Z = 16;

	private static final int MAX_ASTEROID_RADIUS = 25;
	private static final int MIN_ASTEROID_RADIUS = 5;

	private static final int MAX_ASTEROID_SKEW = 8;

	private static final int MIN_ASTEROID_Y = 48;
	private static final int MAX_ASTEROID_Y = ChunkProviderEcaaAsteroids.CHUNK_SIZE_Y - 48;

	private static final int ASTEROID_CHANCE = 800; // About 1 / n chance per XZ pair

	private static final int ASTEROID_CORE_CHANCE = 2; // 1 / n chance per asteroid
	private static final int ASTEROID_SHELL_CHANCE = 2; // 1 / n chance per asteroid

	private static final int MIN_BLOCKS_PER_CHUNK = 50;
	private static final int MAX_BLOCKS_PER_CHUNK = 200;

	private static final int ILMENITE_CHANCE = 400;
	private static final int IRON_CHANCE = 300;
	private static final int ALUMINUM_CHANCE = 250;

	private static final int RANDOM_BLOCK_FADE_SIZE = 32;
	private static final int FADE_BLOCK_CHANCE = 5; // 1 / n chance of a block being in the fade zone

	private static final int NOISE_OFFSET_SIZE = 256;

	private static final float MIN_HOLLOW_SIZE = .6F;
	private static final float MAX_HOLLOW_SIZE = .8F;
	private static final int HOLLOW_CHANCE = 10; // 1 / n chance per asteroid
	private static final int MIN_RADIUS_FOR_HOLLOW = 15;
	private static final float HOLLOW_LAVA_SIZE = .12F;

	// Per chunk per asteroid
	private static final int TREE_CHANCE = 2;
	private static final int TALL_GRASS_CHANCE = 2;
	private static final int FLOWER_CHANCE = 2;
	private static final int WATER_CHANCE = 2;
	private static final int LAVA_CHANCE = 2;
	private static final int GLOWSTONE_CHANCE = 20;

	private LinkedList<AsteroidData> largeAsteroids = new LinkedList<AsteroidData>();
	private int largeCount = 0;
	private static HashSet<BlockVec3> chunksDone = new HashSet<BlockVec3>();
	private int largeAsteroidsLastChunkX;
	private int largeAsteroidsLastChunkZ;
	private final MapGenAbandonedBase dungeonGenerator = new MapGenAbandonedBase();

	public ChunkProviderEcaaAsteroids(World par1World, long par2, boolean par4) {
		this.world = par1World;
		this.rand = new Random(par2);

		this.asteroidDensity = new Billowed(this.rand.nextLong(), 2, .25F);
		this.asteroidDensity.setFrequency(.009F);
		this.asteroidDensity.amplitude = .6F;

		this.asteroidTurbulance = new Gradient(this.rand.nextLong(), 1, .2F);
		this.asteroidTurbulance.setFrequency(.08F);
		this.asteroidTurbulance.amplitude = .5F;

		this.asteroidSkewX = new Gradient(this.rand.nextLong(), 1, 1);
		this.asteroidSkewX.amplitude = ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW;
		this.asteroidSkewX.frequencyX = 0.005F;

		this.asteroidSkewY = new Gradient(this.rand.nextLong(), 1, 1);
		this.asteroidSkewY.amplitude = ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW;
		this.asteroidSkewY.frequencyY = 0.005F;

		this.asteroidSkewZ = new Gradient(this.rand.nextLong(), 1, 1);
		this.asteroidSkewZ.amplitude = ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW;
		this.asteroidSkewZ.frequencyZ = 0.005F;

		this.coreHandler = new SpecialAsteroidBlockHandler();
		for (SpecialAsteroidBlock core : coreBlocks) {
			this.coreHandler.addBlock(core);
			ECAA.logger.info("## Registering [" + core.block.getUnlocalizedName() + "] as Asteroid Core Block");
		}

		this.shellHandler = new SpecialAsteroidBlockHandler();
		for (SpecialAsteroidBlock shell : shellBlocks) {
			this.shellHandler.addBlock(shell);
			ECAA.logger.info("## Registering [" + shell.block.getUnlocalizedName() + "] as Asteroid Shell Block");
		}
	}

	public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer primer, boolean flagDataOnly) {
		this.largeAsteroids.clear();
		this.largeCount = 0;
		final Random random = new Random();
		final int asteroidChance = ChunkProviderEcaaAsteroids.ASTEROID_CHANCE;
		final int rangeY = ChunkProviderEcaaAsteroids.MAX_ASTEROID_Y - ChunkProviderEcaaAsteroids.MIN_ASTEROID_Y;
		final int rangeSize = ChunkProviderEcaaAsteroids.MAX_ASTEROID_RADIUS
				- ChunkProviderEcaaAsteroids.MIN_ASTEROID_RADIUS;

		for (int i = chunkX - 3; i < chunkX + 3; i++) {
			int minX = i * 16;
			int maxX = minX + ChunkProviderEcaaAsteroids.CHUNK_SIZE_X;
			for (int k = chunkZ - 3; k < chunkZ + 3; k++) {
				int minZ = k * 16;
				int maxZ = minZ + ChunkProviderEcaaAsteroids.CHUNK_SIZE_Z;

				for (int x = minX; x < maxX; x += 2) {
					for (int z = minZ; z < maxZ; z += 2) {
						if (this.randFromPointPos(x, z) < (this.asteroidDensity.getNoise(x, z) + .4) / asteroidChance) {
							random.setSeed(x + z * 3067);
							int y = random.nextInt(rangeY) + ChunkProviderEcaaAsteroids.MIN_ASTEROID_Y;
							int size = random.nextInt(rangeSize) + ChunkProviderEcaaAsteroids.MIN_ASTEROID_RADIUS;

							this.generateAsteroid(random, x, y, z, chunkX << 4, chunkZ << 4, size, primer,
									flagDataOnly);
							this.largeCount++;
						}
					}
				}
			}
		}
	}

	private void generateAsteroid(Random rand, int asteroidX, int asteroidY, int asteroidZ, int chunkX, int chunkZ,
			int size, ChunkPrimer primer, boolean flagDataOnly) {
		SpecialAsteroidBlock core = this.coreHandler.getBlock(rand, size);

		SpecialAsteroidBlock shell = null;
		if (rand.nextInt(ChunkProviderEcaaAsteroids.ASTEROID_SHELL_CHANCE) == 0) {
			shell = this.shellHandler.getBlock(rand, size);
		}

		boolean isHollow = false;
		final float hollowSize = rand.nextFloat()
				* (ChunkProviderEcaaAsteroids.MAX_HOLLOW_SIZE - ChunkProviderEcaaAsteroids.MIN_HOLLOW_SIZE)
				+ ChunkProviderEcaaAsteroids.MIN_HOLLOW_SIZE;
		if (rand.nextInt(ChunkProviderEcaaAsteroids.HOLLOW_CHANCE) == 0
				&& size >= ChunkProviderEcaaAsteroids.MIN_RADIUS_FOR_HOLLOW) {
			isHollow = true;
			shell = new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce, (byte) 0, 1, .15);
		}

		// Add to the list of asteroids for external use
		((WorldProviderEcaaAsteroids) this.world.provider).addAsteroid(asteroidX, asteroidY, asteroidZ, size,
				isHollow ? -1 : core.index);

		final int xMin = this.clamp(
				Math.max(chunkX, asteroidX - size - ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW - 2) - chunkX, 0, 16);
		final int zMin = this.clamp(
				Math.max(chunkZ, asteroidZ - size - ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW - 2) - chunkZ, 0, 16);
		final int yMin = asteroidY - size - ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW - 2;
		final int yMax = asteroidY + size + ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW + 2;
		final int xMax = this.clamp(
				Math.min(chunkX + 16, asteroidX + size + ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW + 2) - chunkX, 0,
				16);
		final int zMax = this.clamp(
				Math.min(chunkZ + 16, asteroidZ + size + ChunkProviderEcaaAsteroids.MAX_ASTEROID_SKEW + 2) - chunkZ, 0,
				16);
		final int xSize = xMax - xMin;
		final int ySize = yMax - yMin;
		final int zSize = zMax - zMin;

		if (xSize <= 0 || ySize <= 0 || zSize <= 0) {
			return;
		}

		final float noiseOffsetX = this.randFromPoint(asteroidX, asteroidY, asteroidZ)
				* ChunkProviderEcaaAsteroids.NOISE_OFFSET_SIZE + chunkX;
		final float noiseOffsetY = this.randFromPoint(asteroidX * 7, asteroidY * 11, asteroidZ * 13)
				* ChunkProviderEcaaAsteroids.NOISE_OFFSET_SIZE;
		final float noiseOffsetZ = this.randFromPoint(asteroidX * 17, asteroidY * 23, asteroidZ * 29)
				* ChunkProviderEcaaAsteroids.NOISE_OFFSET_SIZE + chunkZ;
		this.setOtherAxisFrequency(1F / (size * 2F / 2F));

		float[] sizeXArray = new float[ySize * zSize];
		float[] sizeZArray = new float[xSize * ySize];
		float[] sizeYArray = new float[xSize * zSize];

		for (int x = 0; x < xSize; x++) {
			int xx = x * zSize;
			float xxx = x + noiseOffsetX;
			for (int z = 0; z < zSize; z++) {
				sizeYArray[xx + z] = this.asteroidSkewY.getNoise(xxx, z + noiseOffsetZ);
			}
		}

		AsteroidData asteroidData = new AsteroidData(isHollow, sizeYArray, xMin, zMin, xMax, zMax, zSize, size,
				asteroidX, asteroidY, asteroidZ);
		this.largeAsteroids.add(asteroidData);
		this.largeAsteroidsLastChunkX = chunkX;
		this.largeAsteroidsLastChunkZ = chunkZ;

		if (flagDataOnly) {
			return;
		}

		for (int y = 0; y < ySize; y++) {
			int yy = y * zSize;
			float yyy = y + noiseOffsetY;
			for (int z = 0; z < zSize; z++) {
				sizeXArray[yy + z] = this.asteroidSkewX.getNoise(yyy, z + noiseOffsetZ);
			}
		}

		for (int x = 0; x < xSize; x++) {
			int xx = x * ySize;
			float xxx = x + noiseOffsetX;
			for (int y = 0; y < ySize; y++) {
				sizeZArray[xx + y] = this.asteroidSkewZ.getNoise(xxx, y + noiseOffsetY);
			}
		}

		double shellThickness = 0;
		int terrainY = 0;
		int terrainYY = 0;

		IBlockState asteroidShell = null;
		if (shell != null) {
			asteroidShell = shell.block.getStateFromMeta(shell.meta);
			shellThickness = 1.0 - shell.thickness;
		}

		IBlockState asteroidCore = core.block.getStateFromMeta(core.meta);
//		IBlockState asteroidRock0 = this.ASTEROID_STONE.getStateFromMeta(this.ASTEROID_STONE_META_0);
//		IBlockState asteroidRock1 = this.ASTEROID_STONE.getStateFromMeta(this.ASTEROID_STONE_META_1);
		IBlockState airBlock = Blocks.AIR.getDefaultState();
		IBlockState dirtBlock = this.DIRT.getStateFromMeta(this.DIRT_META);
		IBlockState grassBlock = this.GRASS.getStateFromMeta(this.GRASS_META);

		for (int x = xMax - 1; x >= xMin; x--) {
			int indexXY = (x - xMin) * ySize - yMin;
			int indexXZ = (x - xMin) * zSize - zMin;
			int distanceX = asteroidX - (x + chunkX);
			int indexBaseX = x * ChunkProviderEcaaAsteroids.CHUNK_SIZE_Y << 4;
			float xx = x + chunkX;

			for (int z = zMin; z < zMax; z++) {
				if (isHollow) {
					float sizeModY = sizeYArray[indexXZ + z];
					terrainY = this.getTerrainHeightFor(sizeModY, asteroidY, size);
					terrainYY = this.getTerrainHeightFor(sizeModY, asteroidY - 1, size);
				}

				float sizeY = size + sizeYArray[indexXZ + z];
				sizeY *= sizeY;
				int distanceZ = asteroidZ - (z + chunkZ);
				int indexBase = indexBaseX | z * ChunkProviderEcaaAsteroids.CHUNK_SIZE_Y;
				float zz = z + chunkZ;

				for (int y = yMin; y < yMax; y++) {
					float dSizeX = distanceX / (size + sizeXArray[(y - yMin) * zSize + z - zMin]);
					float dSizeZ = distanceZ / (size + sizeZArray[indexXY + y]);
					dSizeX *= dSizeX;
					dSizeZ *= dSizeZ;
					int distanceY = asteroidY - y;
					distanceY *= distanceY;
					float distance = dSizeX + distanceY / sizeY + dSizeZ;
					float distanceAbove = distance;
					distance += this.asteroidTurbulance.getNoise(xx, y, zz);

					if (isHollow && distance <= hollowSize) {
						distanceAbove += this.asteroidTurbulance.getNoise(xx, y + 1, zz);
						if (distanceAbove <= 1) {
							if ((y - 1) == terrainYY) {
								int index = indexBase | (y + 1);
								primer.setBlockState(x, y + 1, z, this.LIGHT.getStateFromMeta(this.LIGHT_META));
							}
						}
					}

					if (distance <= 1) {
						int index = indexBase | y;
						if (isHollow && distance <= hollowSize) {
							if (y == terrainY) {
								primer.setBlockState(x, y, z, grassBlock);
							} else if (y < terrainY) {
								primer.setBlockState(x, y, z, dirtBlock);
							} else {
								primer.setBlockState(x, y, z, airBlock);
							}
						} else if (distance <= core.thickness) {
							if (rand.nextBoolean()) {
								primer.setBlockState(x, y, z, asteroidCore);
							} else {
								primer.setBlockState(x, y, z, ChunkProviderEcaaAsteroids.getRandomElement(asteroidBlocks).getDefaultState());
							}
						} else if (shell != null && distance >= shellThickness) {
							primer.setBlockState(x, y, z, asteroidShell);
						} else {
							primer.setBlockState(x, y, z, ChunkProviderEcaaAsteroids.getRandomElement(asteroidBlocks).getDefaultState());
						}
					}
				}
			}
		}

		if (isHollow) {
			shellThickness = 0;
			if (shell != null) {
				shellThickness = 1.0 - shell.thickness;
			}
			for (int x = xMin; x < xMax; x++) {
				int indexXY = (x - xMin) * ySize - yMin;
				int indexXZ = (x - xMin) * zSize - zMin;
				int distanceX = asteroidX - (x + chunkX);
				distanceX *= distanceX;

				for (int z = zMin; z < zMax; z++) {
					float sizeModY = sizeYArray[indexXZ + z];
					float sizeY = size + sizeYArray[indexXZ + z];
					sizeY *= sizeY;
					int distanceZ = asteroidZ - (z + chunkZ);
					distanceZ *= distanceZ;

					for (int y = yMin; y < yMax; y++) {
						float sizeX = size + sizeXArray[(y - yMin) * zSize + z - zMin];
						float sizeZ = size + sizeZArray[indexXY + y];
						sizeX *= sizeX;
						sizeZ *= sizeZ;
						int distanceY = asteroidY - y;
						distanceY *= distanceY;
						float distance = distanceX / sizeX + distanceY / sizeY + distanceZ / sizeZ;
						distance += this.asteroidTurbulance.getNoise(x + chunkX, y, z + chunkZ);

						if (distance <= 1) {
							IBlockState state = primer.getBlockState(x, y, z);
							IBlockState stateAbove = primer.getBlockState(x, y + 1, z);
							if (Blocks.AIR == stateAbove.getBlock()
									&& (state.getBlock() == ChunkProviderEcaaAsteroids.getRandomElement(asteroidBlocks).getDefaultState() || state.getBlock() == GRASS)) {
								if (this.rand.nextInt(GLOWSTONE_CHANCE) == 0) {
									primer.setBlockState(x, y, z, this.LIGHT.getStateFromMeta(this.LIGHT_META));

								}
							}
						}
					}
				}
			}
		}
	}

	private final void setOtherAxisFrequency(float frequency) {
		this.asteroidSkewX.frequencyY = frequency;
		this.asteroidSkewX.frequencyZ = frequency;

		this.asteroidSkewY.frequencyX = frequency;
		this.asteroidSkewY.frequencyZ = frequency;

		this.asteroidSkewZ.frequencyX = frequency;
		this.asteroidSkewZ.frequencyY = frequency;
	}

	private final int clamp(int x, int min, int max) {
		if (x < min) {
			x = min;
		} else if (x > max) {
			x = max;
		}
		return x;
	}

	private final double clamp(double x, double min, double max) {
		if (x < min) {
			x = min;
		} else if (x > max) {
			x = max;
		}
		return x;
	}

	private final int getTerrainHeightFor(float yMod, int asteroidY, int asteroidSize) {
		return (int) (asteroidY - asteroidSize / 4 + yMod * 1.5F);
	}

	private final int getTerrainHeightAt(int x, int z, float[] yModArray, int xMin, int zMin, int zSize, int asteroidY,
			int asteroidSize) {
		final int index = (x - xMin) * zSize - zMin;
		if (index < yModArray.length && index >= 0) {
			final float yMod = yModArray[index];
			return this.getTerrainHeightFor(yMod, asteroidY, asteroidSize);
		}
		return 1;
	}

	@Override
	public Chunk generateChunk(int par1, int par2) {
		ChunkPrimer primer = new ChunkPrimer();

		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);

		this.generateTerrain(par1, par2, primer, false);

		if (this.world.provider instanceof WorldProviderEcaaAsteroids
				&& ((WorldProviderEcaaAsteroids) this.world.provider).checkHasAsteroids()) {
			this.dungeonGenerator.generate(this.world, par1, par2, primer);
		}

		final Chunk var4 = new Chunk(this.world, primer, par1, par2);
		final byte[] biomesArray = var4.getBiomeArray();

		final byte b = (byte) Biome.getIdForBiome(BiomeAdaptive.biomeDefault);
		for (int i = 0; i < biomesArray.length; ++i) {
			biomesArray[i] = b;
		}

		this.generateSkylightMap(var4, par1, par2);
		return var4;
	}

	private int getIndex(int x, int y, int z) {
		return x * ChunkProviderEcaaAsteroids.CHUNK_SIZE_Y * 16 | z * ChunkProviderEcaaAsteroids.CHUNK_SIZE_Y | y;
	}

	private String timeString(long time1, long time2) {
		int ms100 = (int) ((time2 - time1) / 10000);
		int msdecimal = ms100 % 100;
		String msd = ((ms100 < 10) ? "0" : "") + ms100;
		return "" + ms100 / 100 + "." + msd + "ms";
	}

	private float randFromPoint(int x, int y, int z) {
		int n = x + z * 57 + y * 571;
		n ^= n << 13;
		n = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
		return 1.0F - n / 1073741824.0F;
	}

	private float randFromPoint(int x, int z) {
		int n = x + z * 57;
		n ^= n << 13;
		n = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
		return 1.0F - n / 1073741824.0F;
	}

	private float randFromPointPos(int x, int z) {
		int n = x + z * 57;
		n ^= n << 13;
		n = n * (n * n * 15731 + 789221) + 1376312589 & 0x3fffffff;
		return 1.0F - n / 1073741824.0F;
	}

	@Override
	public void populate(int chunkX, int chunkZ) {
		int x = chunkX << 4;
		int z = chunkZ << 4;
		if (!ChunkProviderEcaaAsteroids.chunksDone.add(new BlockVec3(x, 0, z))) {
			return;
		}

		this.rand.setSeed(this.world.getSeed());
		long var7 = this.rand.nextLong() / 2L * 2L + 1L;
		long var9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(chunkX * var7 + chunkZ * var9 ^ this.world.getSeed());

		// 50:50 chance to include small blocks each chunk
		if (this.rand.nextBoolean()) {
			double density = this.asteroidDensity.getNoise(chunkX * 16, chunkZ * 16) * 0.54;
			double numOfBlocks = this.clamp(this.randFromPoint(chunkX, chunkZ), .4, 1)
					* ChunkProviderEcaaAsteroids.MAX_BLOCKS_PER_CHUNK * density
					+ ChunkProviderEcaaAsteroids.MIN_BLOCKS_PER_CHUNK;
			int y0 = this.rand.nextInt(2);
			Block block;
			int yRange = ChunkProviderEcaaAsteroids.MAX_ASTEROID_Y - ChunkProviderEcaaAsteroids.MIN_ASTEROID_Y;
			x += 4;
			z += 4;

			for (int i = 0; i < numOfBlocks; i++) {
				int y = this.rand.nextInt(yRange) + ChunkProviderEcaaAsteroids.MIN_ASTEROID_Y;

				// 50:50 chance vertically as well
				if (y0 == (y / 16) % 2) {
					int px = x + this.rand.nextInt(ChunkProviderEcaaAsteroids.CHUNK_SIZE_X);
					int pz = z + this.rand.nextInt(ChunkProviderEcaaAsteroids.CHUNK_SIZE_Z);
					
					if (asteroidBlocks.contains(AsteroidBlocks.blockBasic)) {
						block = AsteroidBlocks.blockBasic;
						int meta = 1;
	                    if (this.rand.nextInt(ILMENITE_CHANCE) == 0)
	                    {
	                    	meta = 4;

	                        if (ConfigManagerAsteroids.disableIlmeniteGen)
	                        {
	                        	
	                        	asteroidBlocks.add(AsteroidBlocks.blockBasic.getStateFromMeta(meta).getBlock());
	                            continue;
	                        }
	                    }
	                    else if (this.rand.nextInt(IRON_CHANCE) == 0)
	                    {
	                    	meta = 5;

	                        if (ConfigManagerAsteroids.disableIronGen)
	                        {
	                        	asteroidBlocks.add(AsteroidBlocks.blockBasic.getStateFromMeta(meta).getBlock());
	                            continue;
	                        }
	                    }
	                    else if (this.rand.nextInt(ALUMINUM_CHANCE) == 0)
	                    {
	                    	meta = 3;

	                        if (ConfigManagerAsteroids.disableAluminumGen)
	                        {
	                        	asteroidBlocks.add(AsteroidBlocks.blockBasic.getStateFromMeta(meta).getBlock());
	                            continue;
	                        }
	                        
	                    }
						world.setBlockState(new BlockPos(px, y, pz), block.getStateFromMeta(meta), 2);

					} else {
						block = ChunkProviderEcaaAsteroids.getRandomElement(asteroidBlocks);
						world.setBlockState(new BlockPos(px, y, pz), block.getDefaultState(), 2);
					}

					int count = 9;
					if (!(world.getBlockState(new BlockPos(px - 1, y, pz)).getBlock() instanceof BlockAir)) {
						count = 1;
					} else if (!(world.getBlockState(new BlockPos(px - 2, y, pz)).getBlock() instanceof BlockAir)) {
						count = 3;
					} else if (!(world.getBlockState(new BlockPos(px - 3, y, pz)).getBlock() instanceof BlockAir)) {
						count = 5;
					} else if (!(world.getBlockState(new BlockPos(px - 4, y, pz)).getBlock() instanceof BlockAir)) {
						count = 7;
					}
				}
			}
		}

		if (this.largeAsteroidsLastChunkX != chunkX || this.largeAsteroidsLastChunkZ != chunkZ) {
			this.generateTerrain(chunkX, chunkZ, null, true);
		}

		this.rand.setSeed(chunkX * var7 + chunkZ * var9 ^ this.world.getSeed());

		// Look for hollow asteroids to populate
		if (!this.largeAsteroids.isEmpty()) {
			for (AsteroidData asteroidIndex : new ArrayList<AsteroidData>(this.largeAsteroids)) {
				if (!asteroidIndex.isHollow) {
					continue;
				}

				float[] sizeYArray = asteroidIndex.sizeYArray;
				int xMin = asteroidIndex.xMinArray;
				int zMin = asteroidIndex.zMinArray;
				int zSize = asteroidIndex.zSizeArray;
				int asteroidY = asteroidIndex.asteroidYArray;
				int asteroidSize = asteroidIndex.asteroidSizeArray;
				boolean treesdone = false;

				if (ConfigManagerCore.challengeAsteroidPopulation
						|| rand.nextInt(ChunkProviderEcaaAsteroids.TREE_CHANCE) == 0) {
					int treeType = rand.nextInt(3);
					if (treeType == 1) {
						treeType = 0;
					}
					IBlockState log = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT,
							BlockPlanks.EnumType.OAK);
					IBlockState leaves = Blocks.LEAVES.getDefaultState()
							.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
							.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
					WorldGenTrees wg = new WorldGenTrees(false, 2, log, leaves, false);
					for (int tries = 0; tries < 5; tries++) {
						int i = rand.nextInt(16) + x + 8;
						int k = rand.nextInt(16) + z + 8;
						if (wg.generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray,
								xMin, zMin, zSize, asteroidY, asteroidSize), k))) {
							break;
						}
					}
					treesdone = true;
				}
				if (!treesdone || rand.nextInt(ChunkProviderEcaaAsteroids.TALL_GRASS_CHANCE) == 0) {
					int i = rand.nextInt(16) + x + 8;
					int k = rand.nextInt(16) + z + 8;
					new WorldGenTallGrass(GRASS_TYPE).generate(world, rand,
							new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize,
									asteroidY, asteroidSize), k));
				}
				if (rand.nextInt(ChunkProviderEcaaAsteroids.FLOWER_CHANCE) == 0) {
					int i = rand.nextInt(16) + x + 8;
					int k = rand.nextInt(16) + z + 8;
					int[] types = new int[] { 2, 4, 5, 7 };
					new WorldGenFlowers(this.FLOWER,
							EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, types[rand.nextInt(types.length)]))
									.generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z,
											sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
				}
				if (rand.nextInt(ChunkProviderEcaaAsteroids.LAVA_CHANCE) == 0) {
					int i = rand.nextInt(16) + x + 8;
					int k = rand.nextInt(16) + z + 8;
					new WorldGenLakes(this.LAVA).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x,
							k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
				}
				if (rand.nextInt(ChunkProviderEcaaAsteroids.WATER_CHANCE) == 0) {
					int i = rand.nextInt(16) + x + 8;
					int k = rand.nextInt(16) + z + 8;
					new WorldGenLakes(this.WATER).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x,
							k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
				}
			}
		}
		this.dungeonGenerator.generateStructure(this.world, this.rand, new ChunkPos(chunkX, chunkZ));
	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z) {
		this.dungeonGenerator.generate(this.world, x, z, null);
	}

	public void generateSkylightMap(Chunk chunk, int cx, int cz) {

		boolean flag = world.provider.hasSkyLight();
		for (int j = 0; j < 16; j++) {
			if (chunk.getBlockStorageArray()[j] == null) {
				chunk.getBlockStorageArray()[j] = new ExtendedBlockStorage(j << 4, flag);
			}
		}

		int i = chunk.getTopFilledSegment();
		chunk.heightMapMinimum = Integer.MAX_VALUE;

		for (int j = 0; j < 16; ++j) {
			int k = 0;

			while (k < 16) {
				chunk.precipitationHeightMap[j + (k << 4)] = -999;
				int y = i + 15;

				while (true) {
					if (y > 0) {
						if (chunk.getBlockLightOpacity(new BlockPos(j, y - 1, k)) == 0) {
							--y;
							continue;
						}

						chunk.heightMap[k << 4 | j] = y;

						if (y < chunk.heightMapMinimum) {
							chunk.heightMapMinimum = y;
						}
					}

					++k;
					break;
				}
			}
		}

		for (AsteroidData a : this.largeAsteroids) {
			int yMin = a.asteroidYArray - a.asteroidSizeArray;
			int yMax = a.asteroidYArray + a.asteroidSizeArray;
			int xMin = a.xMinArray;
			if (yMin < 0) {
				yMin = 0;
			}
			if (yMax > 255) {
				yMax = 255;
			}
			if (xMin == 0) {
				xMin = 1;
			}
			for (int x = a.xMax - 1; x >= xMin; x--) {
				for (int z = a.zMinArray; z < a.zMax; z++) {
					for (int y = yMin; y < yMax; y++) {
						if (chunk.getBlockState(x - 1, y, z).getBlock() instanceof BlockAir
								&& !(chunk.getBlockState(x, y, z).getBlock() instanceof BlockAir)) {
							int count = 2;

							if (x > 1) {
								if ((chunk.getBlockState(x - 2, y, z).getBlock() instanceof BlockAir)) {
									count += 2;
								}
							}
							if (x > 2) {
								if ((chunk.getBlockState(x - 3, y, z).getBlock() instanceof BlockAir)) {
									count += 2;
								}
								if ((chunk.getBlockState(x - 3, y + 1, z).getBlock() instanceof BlockAir)) {
									count++;
								}
								if ((chunk.getBlockState(x - 3, y + 1, z).getBlock() instanceof BlockAir)) {
									count++;
								}
								if ((z > 0) && (chunk.getBlockState(x - 3, y, z - 1).getBlock() instanceof BlockAir)) {
									count++;
								}
								if ((z < 15) && (chunk.getBlockState(x - 3, y, z + 1).getBlock() instanceof BlockAir)) {
									count++;
								}
							}
							if (/* flagXChunk || */x > 3) {
								if ((chunk.getBlockState(x - 4, y, z).getBlock() instanceof BlockAir)) {
									count += 2;
								}
								if ((chunk.getBlockState(x - 4, y + 1, z).getBlock() instanceof BlockAir)) {
									count++;
								}
								if ((chunk.getBlockState(x - 4, y + 1, z).getBlock() instanceof BlockAir)) {
									count++;
								}
								if ((z > 0) && !(chunk.getBlockState(x - 4, y, z - 1).getBlock() instanceof BlockAir)) {
									count++;
								}
								if ((z < 15)
										&& !(chunk.getBlockState(x - 4, y, z + 1).getBlock() instanceof BlockAir)) {
									count++;
								}
							}
							if (count > 12) {
								count = 12;
							}
							if (count > 12)
								count = 12;
							chunk.setBlockState(new BlockPos(x - 1, y, z),
									GCBlocks.brightAir.getStateFromMeta(13 - count));
							ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];
							if (extendedblockstorage != null) {
								extendedblockstorage.setBlockLight(x - 1, y & 15, z, count + 2);
							}
						}
					}
				}
			}
		}

		chunk.setModified(true);
	}

	public void resetBase() {
		this.dungeonGenerator.reset();
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		Biome biome = this.world.getBiome(pos);
		return biome.getSpawnableList(creatureType);
	}

	/**
	 * Whether a large asteroid is located the provided coordinates
	 *
	 * @param x0 X-Coordinate to check, in Block Coords
	 * @param z0 Z-Coordinate to check, in Block Coords
	 * @return True if large asteroid is located here, False if not
	 */
	public BlockVec3 isLargeAsteroidAt(int x0, int z0) {
		int xToCheck;
		int zToCheck;
		for (int i0 = 0; i0 <= 32; i0++) {
			for (int i1 = -i0; i1 <= i0; i1++) {
				xToCheck = (x0 >> 4) + i0;
				zToCheck = (z0 >> 4) + i1;

				if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
					return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
				}

				xToCheck = (x0 >> 4) + i0;
				zToCheck = (z0 >> 4) - i1;

				if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
					return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
				}

				xToCheck = (x0 >> 4) - i0;
				zToCheck = (z0 >> 4) + i1;

				if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
					return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
				}

				xToCheck = (x0 >> 4) - i0;
				zToCheck = (z0 >> 4) - i1;

				if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
					return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
				}
			}
		}

		return null;
	}

	private boolean isLargeAsteroidAt0(int x0, int z0) {
		for (int x = x0; x < x0 + ChunkProviderEcaaAsteroids.CHUNK_SIZE_X; x += 2) {
			for (int z = z0; z < z0 + ChunkProviderEcaaAsteroids.CHUNK_SIZE_Z; z += 2) {
				if ((Math.abs(this.randFromPoint(x, z)) < (this.asteroidDensity.getNoise(x, z) + .4)
						/ ChunkProviderEcaaAsteroids.ASTEROID_CHANCE)) {
					return true;
				}
			}
		}

		return false;
	}

	public static void reset() {
		chunksDone.clear();
	}

	private class AsteroidData {
		public boolean isHollow;
		public float[] sizeYArray;
		public int xMinArray;
		public int zMinArray;
		public int xMax;
		public int zMax;
		public int zSizeArray;
		public int asteroidSizeArray;
		public int asteroidXArray;
		public int asteroidYArray;
		public int asteroidZArray;

		public AsteroidData(boolean hollow, float[] sizeYArray2, int xMin, int zMin, int xmax, int zmax, int zSize,
				int size, int asteroidX, int asteroidY, int asteroidZ) {
			this.isHollow = hollow;
			this.sizeYArray = sizeYArray2.clone();
			this.xMinArray = xMin;
			this.zMinArray = zMin;
			this.xMax = xmax;
			this.zMax = zmax;
			this.zSizeArray = zSize;
			this.asteroidSizeArray = size;
			this.asteroidXArray = asteroidX;
			this.asteroidYArray = asteroidY;
			this.asteroidZArray = asteroidZ;
		}
	}

	private static Block getRandomElement(List<Block> list) {
		Random rand = new Random();
		return list.get(rand.nextInt(list.size()));
	}
}