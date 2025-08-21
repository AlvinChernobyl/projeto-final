package bot;

import bot.services.GeoWeatherService;
import bot.services.GeoWeatherService.Day;
import bot.services.GeoWeatherService.Place;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MessageListener extends ListenerAdapter {

    private enum State { ASK_CITY_FOR_WEATHER }
    private enum PromptKind { NOW, TOMORROW, WEEK }

    private static final long TIMEOUT_MS = 120_000L;

    private static final ConcurrentHashMap<ConvKey, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final Map<String, String> CITY_PREF = new ConcurrentHashMap<>();

    private final GeoWeatherService weather = new GeoWeatherService();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        final String userId = event.getAuthor().getId();
        final String channelId = event.getChannel().getId();
        final ConvKey key = new ConvKey(userId, channelId);

        String content = event.getMessage().getContentRaw();
        if (content == null) content = "";
        content = content.trim();
        String lower = content.toLowerCase();

        Session sess = SESSIONS.get(key);
        if (sess != null && sess.expiresAt < Instant.now().toEpochMilli()) {
            SESSIONS.remove(key); sess = null;
        }

        if (lower.equals("cancelar")) {
            SESSIONS.remove(key);
            event.getChannel().sendMessage("blz, cancelado.").queue();
            return;
        }


        if (lower.startsWith("!tempo salvar ")) {
            String city = content.substring("!tempo salvar ".length()).trim();
            if (city.isBlank()) {
                event.getChannel().sendMessage("uso: !tempo salvar <cidade>").queue();
                return;
            }
            CITY_PREF.put(userId, city);
            event.getChannel().sendMessage("ok, salvei sua cidade " + city).queue();
            System.out.println("[debug] cidade salva pra " + userId + ": " + city);
            return;
        }

        if (lower.equals("!tempo agora")) {
            String city = CITY_PREF.get(userId);
            if (city == null) {
                askCity(key, event, "vc ainda nao salvou cidade. qual cidade quer ver agora?");
            } else {
                handleNow(event, city);
            }
            return;
        }

        if (lower.equals("!tempo amanha")) {
            String city = CITY_PREF.get(userId);
            if (city == null) {
                askCity(key, event, "vc ainda nao salvou cidade. qual cidade quer ver amanha?");
            } else {
                handleTomorrow(event, city);
            }
            return;
        }

        if (lower.startsWith("!tempo")) {
            String arg = content.length() > 6 ? content.substring(6).trim() : "";
            if (arg.isEmpty()) {
                String city = CITY_PREF.get(userId);
                if (city == null) askCity(key, event, "qual cidade vc quer ver o tempo? (pode digitar 'cancelar')");
                else handleNow(event, city);
            } else {
                handleNow(event, arg);
            }
            return;
        }

        if (sess != null && sess.state == State.ASK_CITY_FOR_WEATHER) {
            if (!content.isBlank() && !content.startsWith("!")) {
                SESSIONS.remove(key);
                String city = content;
                switch (sess.promptKind) {
                    case NOW:      handleNow(event, city); break;
                    case TOMORROW: handleTomorrow(event, city); break;
                    case WEEK:     handleWeek(event, city); break;
                }
            }
        }
    }


    private void askCity(ConvKey key, MessageReceivedEvent event, String msg) {
        SESSIONS.put(key, new Session(State.ASK_CITY_FOR_WEATHER, Instant.now().toEpochMilli() + TIMEOUT_MS, kindFromMsg(msg)));
        event.getChannel().sendMessage(msg).queue();
    }

    private PromptKind kindFromMsg(String msg) {
        String s = msg.toLowerCase();
        if (s.contains("amanha")) return PromptKind.TOMORROW;
        if (s.contains("semana")) return PromptKind.WEEK;
        return PromptKind.NOW;
    }

    private void handleNow(MessageReceivedEvent event, String city) {
        try {
            Place p = weather.geocodeOne(city);
            GeoWeatherService.Now now = weather.weatherNow(p);
            String out =
                    "**clima agora — " + now.nome + "**\n" +
                            "```\n" +
                            "temperatura : " + String.format("%.1f", now.tempC) + " °c\n" +
                            "vento       : " + Math.round(now.windKmh) + " km/h\n" +
                            "hora local  : " + now.time + "\n" +
                            "```\n" +
                            "_fonte: open-meteo_  ·  dicas: `!tempo salvar <cidade>`  |  `!tempo amanha`  |  `!tempo semana`";
            event.getChannel().sendMessage(out).queue();
            System.out.println("[debug] tempo agora: " + p.nome);
        } catch (Exception e) {
        }

    }

    private void handleTomorrow(MessageReceivedEvent event, String city) {
        try {
            Place p = weather.geocodeOne(city);
            Day d = weather.weatherTomorrow(p);
            String out =
                    "**amanhã — " + p.nome + "**\n" +
                            "```\n" +
                            "mínima : " + String.format("%.1f", d.tMin) + " °c\n" +
                            "máxima : " + String.format("%.1f", d.tMax) + " °c\n" +
                            "```\n" +
                            "_dica: `!tempo` (agora)  |  `!tempo semana`_";
            event.getChannel().sendMessage(out).queue();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleWeek(MessageReceivedEvent event, String city) {
        try {
            Place p = weather.geocodeOne(city);
            Day[] days = weather.weatherWeek(p);
            StringBuilder sb = new StringBuilder("**semana — ").append(p.nome).append("**\n```\n");
            sb.append("dia         min   max\n");
            sb.append("---------------------\n");
            for (Day d : days) {
                sb.append(String.format("%-11s %5.1f %5.1f\n", d.date, d.tMin, d.tMax));
            }
            sb.append("```\n_fonte: open-meteo_");
            event.getChannel().sendMessage(sb.toString()).queue();

            System.out.println("[debug] tempo semana: " + p.nome);
        } catch (Exception e) {
            event.getChannel().sendMessage("nao achei essa cidade. tenta escrever diferente (tipo: cidade, estado)").queue();
            System.out.println("[debug] erro semana: " + e.getMessage());
        }
    }

    private String fmt(double v){ return String.format("%.1f", v); }


    private static final class Session {
        final State state; final long expiresAt; final PromptKind promptKind;
        Session(State s, long t, PromptKind pk){ this.state=s; this.expiresAt=t; this.promptKind=pk; }
    }

    private static final class ConvKey {
        final String userId, channelId;
        ConvKey(String u, String c){ this.userId=u; this.channelId=c; }
        @Override public boolean equals(Object o){ if(this==o)return true; if(!(o instanceof ConvKey))return false;
            ConvKey k=(ConvKey)o; return Objects.equals(userId,k.userId)&&Objects.equals(channelId,k.channelId); }
        @Override public int hashCode(){ return Objects.hash(userId,channelId); }
    }
}
