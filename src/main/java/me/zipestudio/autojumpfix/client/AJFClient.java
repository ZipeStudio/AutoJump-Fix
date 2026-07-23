package me.zipestudio.autojumpfix.client;

import me.zipestudio.autojumpfix.AJF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AJFClient {

	public static final Logger LOGGER = LoggerFactory.getLogger(AJF.MOD_NAME + "/Client");

	public static void onInitializeClient() {
		LOGGER.info("{} Client Initialized", AJF.MOD_NAME);
	}

}
