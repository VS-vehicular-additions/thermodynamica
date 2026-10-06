package com.Tribulla.thermodynamica;

import com.Tribulla.thermodynamica.api.HeatAPI;
import com.Tribulla.thermodynamica.api.impl.HeatAPIImpl;
import com.Tribulla.thermodynamica.api.targeting.HeatTargetingInternal;
import com.Tribulla.thermodynamica.config.HeatConfigManager;
import com.Tribulla.thermodynamica.debug.DebugRegistry;
import com.Tribulla.thermodynamica.simulation.HeatSavedData;
import com.Tribulla.thermodynamica.simulation.HeatSimulationManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Thermodynamica {
    public static final String MODID = "thermodynamica";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static Thermodynamica instance;

    private final HeatConfigManager configManager;
    private final HeatAPIImpl heatApi;
    private HeatSimulationManager simulationManager;

    public Thermodynamica() {
        instance = this;
        this.configManager = new HeatConfigManager();
        this.configManager.loadAll();
        this.heatApi = new HeatAPIImpl(configManager);
        HeatAPI.setInstance(heatApi);
        DebugRegistry.init();
        LOGGER.info("Thermodynamica heat library initialized");
    }

    public static void init() {
        if (instance == null) {
            new Thermodynamica();
        }
    }

    public void onServerAboutToStart(MinecraftServer server) {
        simulationManager = new HeatSimulationManager(server, configManager);
        simulationManager.start();
        heatApi.setSimulationManager(simulationManager);

        HeatTargetingInternal.setSourceProvider((level, minCelsius) ->
                simulationManager.getActiveHeatSources(level.dimension().location(), minCelsius));

        LOGGER.info("Thermodynamica heat simulation engine started");
    }

    public void onServerStarting(MinecraftServer server) {
        if (simulationManager != null && simulationManager.getSavedData() == null) {
            ServerLevel overworld = server.getLevel(Level.OVERWORLD);
            if (overworld != null) {
                HeatSavedData data = overworld.getDataStorage().computeIfAbsent(
                        tag -> HeatSavedData.load(tag, simulationManager),
                        () -> new HeatSavedData(simulationManager),
                        "thermodynamica_heat");
                simulationManager.setSavedData(data);
                LOGGER.info("Thermodynamica saved data loaded (fallback path)");
            }
        }
    }

    public void onServerStopping(MinecraftServer server) {
        if (simulationManager != null) {
            HeatSavedData data = simulationManager.getSavedData();
            if (data != null) {
                data.setDirty();
            }
            simulationManager.stopProcessing();
        }
        LOGGER.info("Thermodynamica heat simulation engine stopping");
    }

    public void onServerStopped(MinecraftServer server) {
        if (simulationManager != null) {
            simulationManager.stop();
            simulationManager = null;
        }
        LOGGER.info("Thermodynamica heat simulation engine stopped");
    }

    public static Thermodynamica getInstance() {
        return instance;
    }

    public HeatConfigManager getConfigManager() {
        return configManager;
    }

    public HeatSimulationManager getSimulationManager() {
        return simulationManager;
    }
}
