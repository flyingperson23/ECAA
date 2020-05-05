package flyingperson.ecaa.worldgen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import net.minecraft.world.biome.Biome;

public class BiomeProviderAsteroids extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeEcaaAsteroids.asteroid;
    }
}
