package autosplittermod.mixins;

import autosplittermod.AutoSplitterMod;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler
{
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"))
    private void onCameraMove(CallbackInfo ci)
    {
        if (!AutoSplitterMod.hasStarted && accumulatedDX != 0.0 ||  accumulatedDY != 0.0)
        {
            AutoSplitterMod.hasStarted = true;
            AutoSplitterMod.LIVE_SPLIT.startTimer();
        }
    }
}
