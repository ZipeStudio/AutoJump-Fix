package me.zipestudio.autojumpfix;

//? if >=1.17 {
import org.slf4j.*;
//?} else {
/*import org.apache.logging.log4j.*;
 *///?}

public class AJF {

	public static final String MOD_NAME = /*$ mod_name*/ "AutoJump Fix";
	public static final String MOD_ID = /*$ mod_id*/ "autojump_fix";

	public static Logger LOGGER = /*? if >=1.17 {*/LoggerFactory/*?} else {*//*LogManager*//*?}*/.getLogger(AJF.MOD_NAME);

	public static void onInitialize() {
		LOGGER.info("{} Initialized", MOD_NAME);
	}
}