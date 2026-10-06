package com.Tribulla.thermodynamica.simulation;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.api.HeatAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class SimulationEventHandler {

    public static void onServerTick(MinecraftServer server) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null)
            return;
        if (instance.getSimulationManager() != null) {
            instance.getSimulationManager().tick();
        }
    }

    public static void onChunkLoad(ServerLevel level, LevelChunk chunk) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null || instance.getSimulationManager() == null)
            return;
        instance.getSimulationManager().onChunkLoad(level, chunk);
    }

    public static void onChunkUnload(ServerLevel level, LevelChunk chunk) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null || instance.getSimulationManager() == null)
            return;
        instance.getSimulationManager().onChunkUnload(level, chunk);
    }

    public static void onBlockPlace(ServerLevel level, BlockPos pos, BlockState state) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null || instance.getSimulationManager() == null)
            return;

        instance.getSimulationManager().onBlockChanged(level, pos);
        checkAndRegisterSource(level, pos, state, instance);
    }

    public static void onBlockBreak(ServerLevel level, BlockPos pos) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null || instance.getSimulationManager() == null)
            return;

        instance.getSimulationManager().onBlockChanged(level, pos);
        instance.getSimulationManager().markInactive(level, pos);
    }

    public static void onNeighborNotify(ServerLevel level, BlockPos pos, BlockState state) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null || instance.getSimulationManager() == null)
            return;

        checkAndRegisterSource(level, pos, state, instance);
    }

    public static void onBlockStateChanged(ServerLevel level, BlockPos pos, BlockState oldState, BlockState newState) {
        Thermodynamica instance = Thermodynamica.getInstance();
        if (instance == null || instance.getSimulationManager() == null)
            return;

        instance.getSimulationManager().onBlockChanged(level, pos);
        if (newState.isAir()) {
            instance.getSimulationManager().markInactive(level, pos);
        } else {
            checkAndRegisterSource(level, pos, newState, instance);
        }
    }

    public static void checkAndRegisterSource(ServerLevel level, BlockPos pos,
            BlockState state, Thermodynamica instance) {
        // Skip fluid blocks entirely — lava pools underground are the #1 source of
        // simulation lag and fluid heat is not important for gameplay.
        if (!state.getFluidState().isEmpty())
            return;

        HeatSimulationManager sim = instance.getSimulationManager();
        if (sim == null)
            return;

        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (blockId == null)
            return;

        double ambientTemp = instance.getConfigManager().getSettings().getAmbientTemperature();
        double blockTemp = HeatAPI.get().getBaseCelsiusForState(blockId, state);

        if (Math.abs(blockTemp - ambientTemp) > instance.getConfigManager().getSettings().getDeltaThreshold()) {
            sim.markActive(level, pos);
        } else {
            sim.markInactive(level, pos);
        }
    }

    public static void onLevelLoad(ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            Thermodynamica instance = Thermodynamica.getInstance();
            if (instance == null)
                return;
            if (instance.getSimulationManager() != null) {
                HeatSavedData data = level.getDataStorage().computeIfAbsent(
                        (tag) -> HeatSavedData.load(tag, instance.getSimulationManager()),
                        () -> new HeatSavedData(instance.getSimulationManager()),
                        "thermodynamica_heat");
                instance.getSimulationManager().setSavedData(data);
            }
        }
    }

    public static void onLevelSave(ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            Thermodynamica instance = Thermodynamica.getInstance();
            if (instance == null)
                return;
            if (instance.getSimulationManager() != null) {
                HeatSavedData data = instance.getSimulationManager().getSavedData();
                if (data != null) {
                    data.setDirty();
                }
            }
        }
    }
}
