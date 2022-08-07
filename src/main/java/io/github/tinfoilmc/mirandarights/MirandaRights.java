package io.github.tinfoilmc.mirandarights;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class MirandaRights implements ModInitializer {
    @Override
    public void onInitialize() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> sanityCheck());
    }

    private static void sanityCheck() {
        try {
            if (MinecraftClient.getInstance().getProfileKeys().getPublicKey().isPresent()
                    || MinecraftClient.getInstance().getProfileKeys().getSigner() != null
                    || MinecraftClient.getInstance().getProfileKeys().method_45104().get().isPresent()) {
                throw new RuntimeException("Sanity check failed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
