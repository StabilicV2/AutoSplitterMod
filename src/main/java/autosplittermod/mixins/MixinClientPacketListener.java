package autosplittermod.mixins;

import autosplittermod.AutoSplitterMod;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener
{
    @Inject(method = "handleLogin", at = @At("HEAD"))
    private void onWorldLoad(ClientboundLoginPacket packet, CallbackInfo ci)
    {
        AutoSplitterMod.hasStarted = false;
        AutoSplitterMod.LIVE_SPLIT.start();
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onWorldUnload(CallbackInfo ci)
    {
        AutoSplitterMod.LIVE_SPLIT.stop();
    }
}
