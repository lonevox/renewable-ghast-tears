package com.lonevox.renewableghasttears.mixin;

import com.lonevox.renewableghasttears.GhastEntityAccessor;
import com.lonevox.renewableghasttears.RenewableGhastTearsMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity implements GhastEntityAccessor {

	private static final TrackedData<Boolean> CRYING = DataTracker.registerData(GhastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private int ghastTearTime = random.nextInt(500) + 500;

	protected GhastEntityMixin(EntityType<? extends FlyingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean isCrying() {
		return this.dataTracker.get(CRYING);
	}

	@Override
	public void setCrying(boolean crying) {
		this.dataTracker.set(CRYING, crying);
		RenewableGhastTearsMod.LOGGER.info("SET TO " + crying);
	}

	@Override
	public void tryCryGhastTear() {
		if (isCrying()) {
			if (--ghastTearTime <= 0) {
				dropItem(Items.GHAST_TEAR);
				ghastTearTime = random.nextInt(500) + 500;
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "initDataTracker")
	public void renewableGhastTears_InitDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(CRYING, false);
	}
}
