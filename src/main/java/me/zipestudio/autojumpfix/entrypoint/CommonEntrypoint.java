package me.zipestudio.autojumpfix.entrypoint;

//? if fabric {

import me.zipestudio.autojumpfix.AJF;

import net.fabricmc.api.ModInitializer;

public class CommonEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		AJF.onInitialize();
	}
}

//?} elif neoforge {
/*import me.zipestudio.autojumpfix.AJF;
import net.neoforged.fml.common.Mod;

@Mod(AJF.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		AJF.onInitialize();
	}

}

*///?} elif forge {
/*import me.zipestudio.autojumpfix.AJF;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(AJF.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		AJF.onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientEntrypoint::onInitializeClient);
	}

}

*///?}

