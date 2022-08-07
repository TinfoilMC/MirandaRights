package io.github.tinfoilmc.mirandarights.mixin.network;

import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.network.encryption.Signer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLoginNetworkHandler.class)
public class ClientLoginNetworkHandlerMixin {
    @Redirect(method = "onHello", at = @At(value = "INVOKE", ordinal = 0,
            target = "Lnet/minecraft/client/util/ProfileKeys;getSigner()Lnet/minecraft/network/encryption/Signer;"))
    private Signer removeHelloSignature(ProfileKeys instance) {
        return null;
    }
}
