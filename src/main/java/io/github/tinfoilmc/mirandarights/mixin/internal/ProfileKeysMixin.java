package io.github.tinfoilmc.mirandarights.mixin.internal;

import com.mojang.authlib.minecraft.UserApiService;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.network.encryption.PlayerKeyPair;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.encryption.Signer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Class which disables ProfileKeys function, preventing some mods to leak the key.
 * It's probably not so secure, because you can get the key from Mojang API with the access token
 */
@Mixin(ProfileKeys.class)
public class ProfileKeysMixin {
    @Inject(method = "loadKeyPairFromFile", at = @At("HEAD"), cancellable = true)
    private void dontLoadFile(CallbackInfoReturnable<Optional<PlayerKeyPair>> cir){
        cir.setReturnValue(Optional.empty());
    }

    @Inject(method = "getKeyPair", at = @At("HEAD"), cancellable = true)
    private void dontReturnKey(Optional<PlayerKeyPair> optional, CallbackInfoReturnable<CompletableFuture<Optional<?>>> cir) {
        cir.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
    }

    @Inject(method = "fetchKeyPair", at = @At("HEAD"))
    private void dontFetch(UserApiService userApiService, CallbackInfoReturnable<PlayerKeyPair> cir) throws IOException {
        throw new IOException("Signing is disabled");
    }

    @Inject(method = "saveKeyPairToFile", at = @At("HEAD"), cancellable = true)
    private void dontSaveFile(PlayerKeyPair keyPair, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "getPublicKey", at = @At("HEAD"), cancellable = true)
    private void noPublicKey(CallbackInfoReturnable<Optional<PlayerPublicKey>> cir) {
        cir.setReturnValue(Optional.empty());
    }

    @Inject(method = "getSigner", at = @At("HEAD"), cancellable = true)
    private void noSigner(CallbackInfoReturnable<Signer> cir){
        cir.setReturnValue(null);
    }

    @Inject(method = "method_45104", at = @At("HEAD"), cancellable = true)
    private void noKeyData(CallbackInfoReturnable<CompletableFuture<Optional<PlayerPublicKey.PublicKeyData>>> cir) {
        cir.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
    }
}
