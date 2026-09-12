package com.cogworks.cogcrasher.worldgen;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.registry.CogcrasherBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MalformedMazeChunkGenerator extends ChunkGenerator {

    public static final MapCodec<MalformedMazeChunkGenerator> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            BiomeSource.CODEC.fieldOf("biome_source")
                                    .forGetter(generator -> generator.biomeSource)
                    ).apply(instance, MalformedMazeChunkGenerator::new)
            );

    private static final int FLOOR_Y = 20;
    private static final int WALL_BOTTOM = 21;
    private static final int WALL_TOP = 30;

    private static final int CEILING_BOTTOM = 36;

    private static final int CELL_SIZE = 8;

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final BlockState BEDROCK = Blocks.BEDROCK.defaultBlockState();
    private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
    private static final BlockState ANIMATED_BLACKSTONE = CogcrasherBlocks.ANIMATED_BLACKSTONE.defaultBlockState();
    private static final BlockState PALE_OAK = ModBlocks.PALE_OAK_WOOD.get().defaultBlockState();
    private static final BlockState LEAVES = ModBlocks.PALE_OAK_LEAVES.get().defaultBlockState()
            .setValue(LeavesBlock.PERSISTENT, true);

    private static final ResourceLocation ANIMATED_BLACKSTONE_NOISE =
            ResourceLocation.fromNamespaceAndPath("cogcrasher", "animated_blackstone");

    public MalformedMazeChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    protected @NotNull MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(
            @NotNull Blender blender,
            @NotNull RandomState randomState,
            @NotNull StructureManager structureManager,
            ChunkAccess chunk
    ) {
        ChunkPos chunkPos = chunk.getPos();

        int startX = chunkPos.getMinBlockX();
        int startZ = chunkPos.getMinBlockZ();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        PositionalRandomFactory noiseFactory =
                randomState.getOrCreateRandomFactory(ANIMATED_BLACKSTONE_NOISE);

        NormalNoise animatedBlackstoneNoise =
                NormalNoise.create(noiseFactory.fromHashOf(ANIMATED_BLACKSTONE_NOISE.toString()), -3, 1.0D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = startX + x;
                int worldZ = startZ + z;

                if (Math.abs(worldX) > 250 || Math.abs(worldZ) > 250) {
                    for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                        pos.set(worldX, y, worldZ);
                        chunk.setBlockState(pos, BEDROCK, false);
                    }
                    continue;
                }

                Holder<Biome> biomeHolder = this.biomeSource.getNoiseBiome(
                        net.minecraft.core.QuartPos.fromBlock(worldX),
                        net.minecraft.core.QuartPos.fromBlock(FLOOR_Y),
                        net.minecraft.core.QuartPos.fromBlock(worldZ),
                        randomState.sampler()
                );

                boolean isBadlands = biomeHolder.unwrapKey()
                        .map(key -> key.location().equals(ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "blackstone_badlands")))
                        .orElse(false);

                pos.set(worldX, FLOOR_Y - 1, worldZ);
                chunk.setBlockState(pos, BEDROCK, false);

                pos.set(worldX, FLOOR_Y, worldZ);

                if (isWall(worldX, worldZ)) {
                    chunk.setBlockState(pos, isBadlands ? BLACKSTONE : PALE_OAK, false);
                } else {
                    double noise = animatedBlackstoneNoise.getValue(
                            worldX * 0.8D,
                            0.0D,
                            worldZ * 0.8D
                    );

                    chunk.setBlockState(
                            pos,
                            noise > 0.1D ? BLACKSTONE : ANIMATED_BLACKSTONE,
                            false
                    );
                }

                for (int y = WALL_BOTTOM; y <= WALL_TOP; y++) {
                    pos.set(worldX, y, worldZ);

                    if (isWall(worldX, worldZ)) {
                        chunk.setBlockState(pos, isBadlands ? BLACKSTONE : LEAVES, false);
                    } else {
                        chunk.setBlockState(pos, AIR, false);
                    }
                }

                for (int y = WALL_TOP + 1; y < CEILING_BOTTOM; y++) {
                    pos.set(worldX, y, worldZ);
                    chunk.setBlockState(pos, AIR, false);
                }

                for (int y = 62; y <= 63; y++) {
                    pos.set(worldX, y, worldZ);
                    chunk.setBlockState(pos, BEDROCK, false);
                }
            }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    private boolean isWall(int x, int z) {
        int cellX = Math.floorDiv(x, CELL_SIZE);
        int cellZ = Math.floorDiv(z, CELL_SIZE);

        int localX = Math.floorMod(x, CELL_SIZE);
        int localZ = Math.floorMod(z, CELL_SIZE);

        if (localX != 0 && localZ != 0) {
            return false;
        }

        if (localX == 0 && localZ == 0) {
            return !hasOpening(cellX, cellZ, Direction.WEST)
                    || !hasOpening(cellX, cellZ, Direction.NORTH);
        }

        if (localX == 0) {
            return !hasOpening(cellX, cellZ, Direction.WEST);
        }

        return !hasOpening(cellX, cellZ, Direction.NORTH);
    }

    @Override
    public void applyBiomeDecoration(
            @NotNull net.minecraft.world.level.WorldGenLevel level,
            @NotNull ChunkAccess chunk,
            @NotNull StructureManager structureManager
    ) {
        super.applyBiomeDecoration(level, chunk, structureManager);
    }

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion level) {
        ChunkPos chunkPos = level.getCenter();
        net.minecraft.world.level.NaturalSpawner.spawnMobsForChunkGeneration(
                level,
                level.getBiome(chunkPos.getMiddleBlockPosition(FLOOR_Y)),
                chunkPos,
                level.getRandom()
        );
    }

    private boolean hasOpening(int cellX, int cellZ, Direction direction) {
        if (direction == Direction.WEST) {
            return edgeOpen(cellX, cellZ, Direction.WEST);
        }

        if (direction == Direction.NORTH) {
            return edgeOpen(cellX, cellZ, Direction.NORTH);
        }

        return false;
    }

    private boolean edgeOpen(int x, int z, Direction direction) {
        long hash = hash(x, z, 0);

        boolean parentWest = Math.floorMod(hash, 2) == 0;

        if (parentWest && direction == Direction.WEST) {
            return true;
        }

        if (!parentWest && direction == Direction.NORTH) {
            return true;
        }

        long extraHash = hash(x, z, 1);

        return Math.floorMod(extraHash, 100) < 8;
    }

    private boolean isClosedBoundary(int cellX, int cellZ, int localX, int localZ) {
        if (localX == 0 && localZ == 0) {
            return true;
        }

        if (localX == 0) {
            return !hasOpening(cellX, cellZ, Direction.WEST);
        }

        return !hasOpening(cellX, cellZ, Direction.NORTH);
    }

    private long hash(int x, int z, int salt) {
        long value = 0x9E3779B97F4A7C15L;

        value ^= x * 0xBF58476D1CE4E5B9L;
        value ^= z * 0x94D049BB133111EBL;
        value ^= salt * 0x369DEA0F31A53F85L;

        value = (value ^ (value >>> 30)) * 0xBF58476D1CE4E5B9L;
        value = (value ^ (value >>> 27)) * 0x94D049BB133111EBL;

        return value ^ (value >>> 31);
    }

    private enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        public Direction opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case EAST -> WEST;
                case SOUTH -> NORTH;
                case WEST -> EAST;
            };
        }
    }

    @Override
    public void buildSurface(
            @NotNull WorldGenRegion level,
            @NotNull StructureManager structureManager,
            @NotNull RandomState random,
            @NotNull ChunkAccess chunk
    ) {
    }

    @Override
    public void applyCarvers(
            @NotNull WorldGenRegion level,
            long seed,
            @NotNull RandomState random,
            @NotNull BiomeManager biomeManager,
            @NotNull StructureManager structureManager,
            @NotNull ChunkAccess chunk,
            GenerationStep.@NotNull Carving step
    ) {
    }

    @Override
    public int getBaseHeight(
            int x,
            int z,
            Heightmap.@NotNull Types type,
            net.minecraft.world.level.@NotNull LevelHeightAccessor level,
            @NotNull RandomState random
    ) {
        return FLOOR_Y + 1;
    }

    @Override
    public @NotNull NoiseColumn getBaseColumn(
            int x,
            int z,
            net.minecraft.world.level.LevelHeightAccessor height,
            @NotNull RandomState random
    ) {
        BlockState[] states = new BlockState[height.getHeight()];

        if (Math.abs(x) > 250 || Math.abs(z) > 250) {
            java.util.Arrays.fill(states, BEDROCK);
            return new NoiseColumn(height.getMinBuildHeight(), states);
        }

        Holder<Biome> biomeHolder = this.biomeSource.getNoiseBiome(
                net.minecraft.core.QuartPos.fromBlock(x),
                net.minecraft.core.QuartPos.fromBlock(FLOOR_Y),
                net.minecraft.core.QuartPos.fromBlock(z),
                random.sampler()
        );

        boolean isBadlands = biomeHolder.unwrapKey()
                .map(key -> key.location().equals(ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "blackstone_badlands")))
                .orElse(false);

        for (int i = 0; i < states.length; i++) {
            int y = height.getMinBuildHeight() + i;

            if (y == FLOOR_Y - 1) {
                states[i] = BEDROCK;
            } else if (y == FLOOR_Y) {
                states[i] = isWall(x, z) ? (isBadlands ? BLACKSTONE : PALE_OAK) : BLACKSTONE;
            } else if (y >= WALL_BOTTOM && y <= WALL_TOP) {
                states[i] = isWall(x, z) ? (isBadlands ? BLACKSTONE : LEAVES) : AIR;
            } else if (y == 62 || y == 63) {
                states[i] = BEDROCK;
            } else {
                states[i] = AIR;
            }
        }

        return new NoiseColumn(height.getMinBuildHeight(), states);
    }

    @Override
    public int getGenDepth() {
        return 64;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getSeaLevel() {
        return -63;
    }

    @Override
    public void addDebugScreenInfo(
            List<String> info,
            @NotNull RandomState random,
            @NotNull BlockPos pos
    ) {
        info.add("Malformed Maze");
        info.add("Cell size: " + CELL_SIZE);
        info.add("Floor Y: " + FLOOR_Y);
    }
}