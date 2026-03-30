package me.zipestudio.autojumpfix;

import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AJFServer {

	public static final String MOD_NAME = /*$ mod_name*/ "AutoJump Fix";
	public static final String MOD_ID = /*$ mod_id*/ "autojump_fix";

	public static Logger LOGGER = LoggerFactory.getLogger(AJFServer.MOD_NAME);

	public static void onInitialize() {
		LOGGER.info("{} Initialized", MOD_NAME);
	}
}