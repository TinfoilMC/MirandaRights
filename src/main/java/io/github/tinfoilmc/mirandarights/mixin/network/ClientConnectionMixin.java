package io.github.tinfoilmc.mirandarights.mixin.network;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.message.ArgumentSignatureDataMap;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @ModifyVariable(at = @At("HEAD"), index = 1, method = "sendInternal", argsOnly = true)
    private Packet<?> onSendInternal(Packet<?> packet) {
        if (packet instanceof LoginHelloC2SPacket hello) {
            return new LoginHelloC2SPacket(hello.name(), Optional.empty(), hello.profileId());
        } else if (packet instanceof LoginKeyC2SPacket key) {
            // Key response is in another mixin
            // Sanity check
            if (((AccessorLoginKeyC2SPacket) key).getNonce().right().isPresent()) {
                throw new IllegalStateException("Trying to send a key response with signature");
            }
        } else if (packet instanceof ChatMessageC2SPacket chatPacket) {
            return new ChatMessageC2SPacket(chatPacket.chatMessage(), chatPacket.timestamp(), chatPacket.salt(),
                    MessageSignatureData.EMPTY, false, chatPacket.acknowledgment());
        } else if (packet instanceof CommandExecutionC2SPacket command) {
            var args = new ArgumentSignatureDataMap(
                    command.argumentSignatures().entries().stream()
                            .map(it -> new ArgumentSignatureDataMap.Entry(it.name(), MessageSignatureData.EMPTY))
                            .collect(Collectors.toList()));
            return new CommandExecutionC2SPacket(command.command(), command.timestamp(), command.salt(),
                    args, false, command.acknowledgment());
        }
        return packet;
    }
}
