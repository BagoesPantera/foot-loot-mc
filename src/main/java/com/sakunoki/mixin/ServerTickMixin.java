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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ServerTickMixin {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		MinecraftServer server = (MinecraftServer) (Object) this;
		if (server.getTicks() % 5 != 0)
			return;

		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			if (player.isSpectator())
				continue;

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
		}
	}
}
