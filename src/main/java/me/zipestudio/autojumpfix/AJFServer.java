package me.zipestudio.autojumpfix;

import net.fabricmc.api.ModInitializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AJFServer implements ModInitializer {

    public static final String MOD_NAME = /*$ mod_name*/ "AutoJumpFix";
    public static final String MOD_ID = /*$ mod_id*/ "autojumpfix";
    public static final String YACL_DEPEND_VERSION = /*$ yacl*/ "3.6.6+1.21.3-fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static MutableText text(String path, Object... args) {
        return Text.translatable(String.format("%s.%s", MOD_ID, path), args);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("{} Initialized", MOD_NAME);
    }

}