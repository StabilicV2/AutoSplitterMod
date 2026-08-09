package autosplittermod.mixins;

import autosplittermod.AutoSplitterMod;
import net.minecraft.client.gui.screens.WinScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WinScreen.class)
public class MixinWinScreen
{
    @Inject(method = "init", at = @At("HEAD"))
    private void onCreditsScreen(CallbackInfo ci)
    {
        if (AutoSplitterMod.getConfig("splits:credits"))
        {
            AutoSplitterMod.LIVE_SPLIT.split();
            AutoSplitterMod.LIVE_SPLIT.pause();
            AutoSplitterMod.hasStarted = false;
        }
    }
}
