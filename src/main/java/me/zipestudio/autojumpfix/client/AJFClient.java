package me.zipestudio.autojumpfix.client;

import me.zipestudio.autojumpfix.AJF;
//? if >=1.17 {
import org.slf4j.*;
//?} else {
/*import org.apache.logging.log4j.*;
 *///?}

public class AJFClient {

	public static final Logger LOGGER = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(AJF.MOD_NAME + "/Client");

	public static void onInitializeClient() {
		LOGGER.info("{} Client Initialized", AJF.MOD_NAME);
	}

}
