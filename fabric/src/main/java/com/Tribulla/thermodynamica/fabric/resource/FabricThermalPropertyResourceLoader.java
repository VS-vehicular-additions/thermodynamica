package com.Tribulla.thermodynamica.fabric.resource;

import com.Tribulla.thermodynamica.Thermodynamica;
import com.Tribulla.thermodynamica.config.ThermalPropertiesRegistry;
import com.Tribulla.thermodynamica.resource.ThermalPropertyResourceLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricThermalPropertyResourceLoader extends ThermalPropertyResourceLoader implements IdentifiableResourceReloadListener {

    private static final ResourceLocation ID = new ResourceLocation(Thermodynamica.MODID, "thermal_properties");

    public FabricThermalPropertyResourceLoader(ThermalPropertiesRegistry registry) {
        super(registry);
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }
}
