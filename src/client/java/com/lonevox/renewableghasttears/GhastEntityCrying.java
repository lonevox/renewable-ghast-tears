package com.lonevox.renewableghasttears;

import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class GhastEntityCrying {
    private static final Identifier GHAST_CRYING_TEXTURE = RenewableGhastTearsMod.id("textures/entity/ghast_crying.png");
    private static final Identifier GHAST_ANGRY_CRYING_TEXTURE = RenewableGhastTearsMod.id("textures/entity/ghast_crying_shooting.png");

    /**
     * Return the crying texture if the ghast is crying.
     *
     * @param ghastEntity The ghast entity being rendered.
     * @return An Optional potentially containing an Identifier for a ghast crying texture.
     */
    public static Optional<Identifier> getGhastCryingTexture(GhastEntity ghastEntity) {
        if (ghastEntity instanceof GhastEntityCryingAccessor ghastEntityCryingAccessor) {
            if (ghastEntityCryingAccessor.isCrying()) {
                return Optional.of(ghastEntity.isShooting() ? GHAST_ANGRY_CRYING_TEXTURE : GHAST_CRYING_TEXTURE);
            }
        }
        return Optional.empty();
    }
}
