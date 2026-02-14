package com.sakunoki;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FootLootMod implements ModInitializer {
	public static final String MOD_ID = "foot_loot";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Foot Loot Mod Initializing...");
	}
}
