package com.vimworldedit;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VimWorldEdit implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("vimworldedit");

	@Override
	public void onInitialize() {
		LOGGER.info("VimWorldEdit loaded!");
	}
}