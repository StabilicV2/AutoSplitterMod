package autosplittermod.mixins;

import autosplittermod.AutoSplitterMod;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void onHandleKeybinds(CallbackInfo ci)
    {
        if (!AutoSplitterMod.hasStarted)
        {
            Minecraft mc = (Minecraft)(Object)this;
            if (mc.options.keyAttack.isDown() ||
                mc.options.keyUse.isDown() ||
                mc.options.keyUp.isDown() ||
                mc.options.keyLeft.isDown() ||
                mc.options.keyRight.isDown() ||
                mc.options.keyJump.isDown() ||
                mc.options.keyShift.isDown() ||
                mc.options.keySprint.isDown() ||
                mc.options.keyDrop.isDown() ||
                mc.options.keyInventory.isDown() ||
                mc.options.keySwapOffhand.isDown() ||
                mc.options.keyPickItem.isDown())
            {
                AutoSplitterMod.hasStarted = true;
                AutoSplitterMod.LIVE_SPLIT.startTimer();
            }
        }
    }
}
