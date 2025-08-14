package bot.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static String get(String url) throws Exception {
        Request req = new Request.Builder()
                .url(url)
                .header("User-Agent", "discord-bot-clima/0.1")
                .build();

        try (Response resp = CLIENT.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new IllegalStateException("http " + resp.code());
            if (resp.body() == null) throw new IllegalStateException("sem corpo");
            return resp.body().string();
        }
    }
}
