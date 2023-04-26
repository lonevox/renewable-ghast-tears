package com.lonevox.renewableghasttears.mixin;

import com.lonevox.renewableghasttears.GhastEntityCrying;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(GhastEntityRenderer.class)
public abstract class GhastEntityRendererCryingTextureMixin {
	@Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/entity/mob/GhastEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	protected void renewableGhastTears_CryingCondition(GhastEntity ghastEntity, CallbackInfoReturnable<Identifier> cir) {
		Optional<Identifier> ghastCryingTexture = GhastEntityCrying.getGhastCryingTexture(ghastEntity);
		ghastCryingTexture.ifPresent(cir::setReturnValue);
	}
}
