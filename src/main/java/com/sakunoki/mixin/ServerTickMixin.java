package com.sakunoki.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ServerTickMixin {
	private static final Map<UUID, Vec3d> LAST_POS = new HashMap<>();

	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		MinecraftServer server = (MinecraftServer) (Object) this;
		if (server.getTicks() % 5 != 0)
			return;

		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (player.isSpectator())
				continue;
			var v = player.getVelocity();
			boolean movingHoriz = Math.abs(v.x) > 0.002 || Math.abs(v.z) > 0.002;
			boolean jumping = v.y > 0.1;
			Vec3d current = player.getPos();
			Vec3d prev = LAST_POS.get(player.getUuid());
			boolean movedByPos = prev == null || current.squaredDistanceTo(prev) > 0.0001;
			if (!(movingHoriz || jumping || movedByPos)) {
				LAST_POS.put(player.getUuid(), current);
				continue;
			}

			BlockPos pos = player.getBlockPos().down();
			ServerWorld world = player.getServerWorld();
			BlockState state = world.getBlockState(pos);

			if (!state.isAir() && !state.isOf(Blocks.BEDROCK)) {
				var item = state.getBlock().asItem();
				if (item != Items.AIR) {
					ItemStack stack = new ItemStack(item);
					ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
							stack);
					entity.setToDefaultPickupDelay();
					world.spawnEntity(entity);
				}
			}
			LAST_POS.put(player.getUuid(), current);
		}
	}
}
