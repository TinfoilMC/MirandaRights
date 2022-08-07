package io.github.tinfoilmc.mirandarights.mixin.network;

import com.mojang.datafixers.util.Either;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LoginKeyC2SPacket.class)
public interface AccessorLoginKeyC2SPacket {
    @Accessor("nonce")
    Either<byte[], NetworkEncryptionUtils.SignatureData> getNonce();
}
