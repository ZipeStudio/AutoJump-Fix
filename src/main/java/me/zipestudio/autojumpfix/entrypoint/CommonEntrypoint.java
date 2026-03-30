package me.zipestudio.autojumpfix.entrypoint;

//? if fabric {

import me.zipestudio.autojumpfix.AJFServer;

import net.fabricmc.api.ModInitializer;

public class CommonEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		AJFServer.onInitialize();
	}
}

//?} elif neoforge {
/*import me.zipestudio.autojumpfix.AJFServer;
import net.neoforged.fml.common.Mod;

@Mod(AJFServer.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		AJFServer.onInitialize();
	}

}

*///?} elif forge {
/*import me.zipestudio.autojumpfix.AJFServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(AJFServer.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		AJFServer.onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientEntrypoint::onInitializeClient);
	}

}

*///?}

