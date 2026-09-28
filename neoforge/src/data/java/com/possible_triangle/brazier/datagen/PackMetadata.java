package com.possible_triangle.brazier.datagen;

import com.possible_triangle.brazier.BrazierConstants;
import java.util.Optional;
import net.minecraft.DetectedVersion;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

public final class PackMetadata extends PackMetadataGenerator {

    public PackMetadata(PackOutput output) {
        super(output);
        add(PackMetadataSection.TYPE, new PackMetadataSection(
                Component.literal(BrazierConstants.MOD_ID + " resources"),
                DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
                Optional.empty()
        ));
    }

}