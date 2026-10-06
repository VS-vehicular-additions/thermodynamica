package com.Tribulla.thermodynamica.forge;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.ThermodynamicaCommand;
import com.Tribulla.thermodynamica.forge.network.ForgeNetworkHandler;
import com.Tribulla.thermodynamica.platform.ForgePlatformHelper;
import com.Tribulla.thermodynamica.resource.ThermalPropertyResourceLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Thermodynamica.MODID)
public class ForgeThermodynamica {

    public ForgeThermodynamica() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgePlatformHelper.BLOCKS.register(modBus);
        ForgePlatformHelper.ITEMS.register(modBus);
        ForgePlatformHelper.BLOCK_ENTITIES.register(modBus);

        Thermodynamica.init();
        ForgeNetworkHandler.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        Thermodynamica.getInstance().onServerAboutToStart(event.getServer());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        Thermodynamica.getInstance().onServerStarting(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        Thermodynamica.getInstance().onServerStopping(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        Thermodynamica.getInstance().onServerStopped(event.getServer());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ThermodynamicaCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ThermalPropertyResourceLoader(
                Thermodynamica.getInstance().getConfigManager().getThermalPropertiesRegistry()));
    }
}
