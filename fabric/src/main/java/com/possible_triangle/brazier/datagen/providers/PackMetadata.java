package com.possible_triangle.brazier.datagen.providers;

import com.possible_triangle.brazier.BrazierConstants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.DetectedVersion;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

public final class PackMetadata extends PackMetadataGenerator {

    public PackMetadata(FabricDataOutput output) {
        super(output);
        add(PackMetadataSection.TYPE, new PackMetadataSection(
                Component.literal(BrazierConstants.MOD_ID + " resources"),
                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES)
        ));
    }

}