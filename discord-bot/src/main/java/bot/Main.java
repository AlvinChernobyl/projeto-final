package bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) throws Exception {
        String token = System.getenv("DISCORD_BOT_TOKEN");
        if (token == null || token.isBlank()) {
            System.exit(1);
        }

        JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .addEventListeners(new MessageListener())
                .setActivity(Activity.playing("!tempo | v0.1"))
                .build()
                .awaitReady();

        System.out.println("Bot iniciado com sucesso!");
    }
}
