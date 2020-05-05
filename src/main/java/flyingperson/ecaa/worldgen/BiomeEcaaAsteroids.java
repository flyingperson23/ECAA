package flyingperson.ecaa.worldgen;

import java.util.LinkedList;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


/**
 * This class is from Galacticraft Planets and modified to allow 
 * customization to mod spawns to make sure no issues arise from config changes.
 * 
 * 
 * all credit for the original class go to the Galacticraft developement team
 * 
 * Modified by: ROMVoid95
 * Created: 5/4/2020
 *
 */
public class BiomeEcaaAsteroids extends BiomeGenBaseGC {
	public static final Biome asteroid = new BiomeEcaaAsteroids(new BiomeProperties("Asteroids").setRainfall(0.0F));

	private BiomeEcaaAsteroids(BiomeProperties properties) {
		super(properties, true);
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.resetMonsterListByMode(ConfigManagerCore.challengeMobDropsAndSpawning);
	}

	@Override
	public void registerTypes(Biome b) {
		// Currently unused for Asteroids due to adaptive biomes system
		BiomeDictionary.addTypes(b, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD,
				BiomeDictionary.Type.SPOOKY);
	}

	public void resetMonsterListByMode(boolean challengeMode) {
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 500, 1, 2));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 700, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 400, 1, 1));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedCreeper.class, 500, 1, 1));
		if (challengeMode)
			this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedEnderman.class, 250, 1, 1));
	}

	@Override
	public void initialiseMobLists(LinkedList<SpawnListEntry> mobInfo) {
	}

	@Override
	public float getSpawningChance() {
		return 0.01F;
	}
}
