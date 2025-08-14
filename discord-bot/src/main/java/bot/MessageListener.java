package bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MessageListener extends ListenerAdapter {

    private enum State { CONFIRM_HELLO, CONFIRM_TRY, AWAIT_LOCATION }

    private static final long TIMEOUT_MS = 120_000L;
    private static final ConcurrentHashMap<ConvKey, Session> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        final String userId = event.getAuthor().getId();
        final String channelId = event.getChannel().getId();
        final ConvKey key = new ConvKey(userId, channelId);

        String content = event.getMessage().getContentRaw();
        content = content.trim();
        String lower = content.toLowerCase();

        Session sess = SESSIONS.get(key);
        if (sess != null && sess.expiresAt < Instant.now().toEpochMilli()) {
            SESSIONS.remove(key);
            sess = null;
        }

        if (lower.equals("!hello")) {
            setState(key, State.CONFIRM_HELLO);
            String where = event.isFromGuild() ? ("#" + event.getChannel().getName()) : "DM";
            event.getChannel().sendMessage(
                    "Você me chamou? \uD83D\uDE42 ( " + where + ")\n" +
                            "Responda **sim** para continuar ou **não** para encerrar.\n"
            ).queue();
            return;
        }

        if (lower.equals("cancelar")) {
            SESSIONS.remove(key);
            event.getChannel().sendMessage("Cancelado. Se quiser de novo, mande **!hello**.").queue();
            return;
        }

        if (sess == null) return;

        switch (sess.state) {
            case CONFIRM_HELLO: {
                if (isNo(lower)) {
                    SESSIONS.remove(key);
                    event.getChannel().sendMessage("Suave, tô por aqui. Até logo!").queue();
                    return;
                }
                if (isYes(lower)) {
                    setState(key, State.CONFIRM_TRY);
                    event.getChannel().sendMessage(
                            "Top! Vamos de desafio: **Eu consigo adivinhar onde você mora com uma pergunta**. Quer tentar?"
                    ).queue();
                    return;
                }

                event.getChannel().sendMessage("Responda **sim** ou **não**").queue();
                return;
            }
            case CONFIRM_TRY: {
                if (isNo(lower)) {
                    SESSIONS.remove(key);
                    event.getChannel().sendMessage("Ate logo").queue();
                    return;
                }
                if (isYes(lower)) {
                    setState(key, State.AWAIT_LOCATION);
                    event.getChannel().sendMessage("**Onde você mora?**").queue();
                    return;
                }
                event.getChannel().sendMessage("Manda **sim** para jogar  ou **não** para sair.").queue();
                return;
            }
            case AWAIT_LOCATION: {
                if (!content.isBlank() && !content.startsWith("!")) {
                    SESSIONS.remove(key);
                    event.getChannel().sendMessage("Nossa! Você mora em **'" + content + "'**. Foi fácil, viu?").queue();
                    event.getChannel().sendMessage("Quer brincar de novo? Digite **!hello**.").queue();
                }
            }
        }
    }

    private static boolean isYes(String lower) {
        return lower.equals("sim") || lower.equals("s");
    }

    private static boolean isNo(String lower) {
        return lower.equals("não") || lower.equals("nao") || lower.equals("n");
    }

    private void setState(ConvKey key, State state) {
        SESSIONS.put(key, new Session(state, Instant.now().toEpochMilli() + TIMEOUT_MS));
    }

    private static final class Session {
        final State state;
        final long expiresAt;
        Session(State state, long expiresAt) { this.state = state; this.expiresAt = expiresAt; }
    }

    private static final class ConvKey {
        final String userId;
        final String channelId;
        ConvKey(String userId, String channelId) { this.userId = userId; this.channelId = channelId; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ConvKey)) return false;
            ConvKey k = (ConvKey) o;
            return Objects.equals(userId, k.userId) && Objects.equals(channelId, k.channelId);
        }
        @Override public int hashCode() { return Objects.hash(userId, channelId); }
    }
}
