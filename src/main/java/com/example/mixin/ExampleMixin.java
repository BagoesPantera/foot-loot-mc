package com.example.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
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
public class ExampleMixin {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		MinecraftServer server = (MinecraftServer) (Object) this;

		// Throttle drops to every 5 ticks (0.25s) to prevent lag
		if (server.getTicks() % 5 != 0) return;

		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			// Skip spectator players
			if (player.isSpectator()) continue;

			// Get the block directly below the player's feet
			BlockPos pos = player.getBlockPos().down();
			ServerWorld world = player.getServerWorld();
			BlockState state = world.getBlockState(pos);

			// If the block is not air and not bedrock
			if (!state.isAir() && !state.isOf(Blocks.BEDROCK)) {
				// Duplicate mode: Drop item without breaking block
				// Get standard drops for this block
				List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, null);
				
				for (ItemStack stack : drops) {
					// Spawn item 1 block above so it appears on top of the block
					// Using stack.copy() to ensure safety
					ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack.copy());
					itemEntity.setToDefaultPickupDelay(); // Add pickup delay so it doesn't instantly fill inventory
					world.spawnEntity(itemEntity);
				}
			}
		}
	}
}
