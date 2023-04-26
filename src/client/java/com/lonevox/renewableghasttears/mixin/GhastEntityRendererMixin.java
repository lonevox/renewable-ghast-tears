package com.lonevox.renewableghasttears.mixin;

import com.lonevox.renewableghasttears.GhastEntityCryingAccessor;
import com.lonevox.renewableghasttears.RenewableGhastTearsMod;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(GhastEntityRenderer.class)
public abstract class GhastEntityRendererMixin {
	private static final Identifier GHAST_CRYING_TEXTURE = RenewableGhastTearsMod.id("textures/entity/ghast_crying.png");
	private static final Identifier GHAST_ANGRY_CRYING_TEXTURE = RenewableGhastTearsMod.id("textures/entity/ghast_crying_shooting.png");

	/**
	 * Return the crying texture if the ghast is crying.
	 *
	 * @param ghastEntity The ghast entity being rendered.
	 * @return An Optional potentially containing an Identifier for a ghast crying texture.
	 */
	private static Optional<Identifier> getGhastCryingTexture(GhastEntity ghastEntity) {
		if (ghastEntity instanceof GhastEntityCryingAccessor ghastEntityCryingAccessor) {
			if (ghastEntityCryingAccessor.isCrying()) {
				return Optional.of(ghastEntity.isShooting() ? GHAST_ANGRY_CRYING_TEXTURE : GHAST_CRYING_TEXTURE);
			}
		}
		return Optional.empty();
	}

	@Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/entity/mob/GhastEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	protected void renewableGhastTears_CryingCondition(GhastEntity ghastEntity, CallbackInfoReturnable<Identifier> cir) {
		Optional<Identifier> ghastCryingTexture = getGhastCryingTexture(ghastEntity);
		ghastCryingTexture.ifPresent(cir::setReturnValue);
	}
}
