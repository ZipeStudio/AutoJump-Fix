package me.zipestudio.autojumpfix.entrypoint;

//? if fabric {

import net.fabricmc.api.ClientModInitializer;

import me.zipestudio.autojumpfix.client.AJFClient;

public class ClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		AJFClient.onInitializeClient();
	}
}

//?} elif neoforge {
/*import me.zipestudio.autojumpfix.AJFServer;
import me.zipestudio.autojumpfix.client.AJFClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = AJFServer.MOD_ID, dist = Dist.CLIENT)
public class ClientEntrypoint {

	public ClientEntrypoint(ModContainer container) {
		AJFClient.onInitializeClient();
	}

}

*///?} elif forge {

/*import me.zipestudio.autojumpfix.client.AJFClient;

public class ClientEntrypoint {

	public static void onInitializeClient() {
		AJFClient.onInitializeClient();
	}

}

*///?}
