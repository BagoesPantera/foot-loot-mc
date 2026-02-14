package com.example.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public class ExampleMixin {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		// Cast this to MinecraftServer to access methods
		MinecraftServer server = (MinecraftServer) (Object) this;

		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			// Skip spectator players
			if (player.isSpectator()) continue;

			// Get the block directly below the player's feet
			BlockPos pos = player.getBlockPos().down();

			// Get the world and block state
			var world = player.getServerWorld();
			BlockState state = world.getBlockState(pos);

			// If the block is not air and not bedrock (to prevent falling into void), break it
			if (!state.isAir() && !state.isOf(Blocks.BEDROCK)) {
				// breakBlock(pos, true) drops the item. 
				// IMPORTANT: Do NOT pass the player entity here if you want drops in Creative mode!
				// Passing a Creative player suppresses drops.
				world.breakBlock(pos, true);
			}
		}
	}
}
