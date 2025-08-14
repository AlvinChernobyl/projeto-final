package bot.services;

import bot.http.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GeoWeatherService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public static class Place {
        public final String nome; public final double lat, lon;
        public Place(String nome, double lat, double lon){ this.nome=nome; this.lat=lat; this.lon=lon; }
    }
    public static class Now {
        public final String nome, time; public final double tempC, windKmh;
        public Now(String nome, String time, double tempC, double windKmh){
            this.nome=nome; this.time=time; this.tempC=tempC; this.windKmh=windKmh;
        }
    }
    public static class Day {
        public final String date; public final double tMin, tMax;
        public Day(String date, double tMin, double tMax){ this.date=date; this.tMin=tMin; this.tMax=tMax; }
    }

    public Place geocodeOne(String city) throws Exception {
        String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name="+enc(city)+"&language=pt&count=1";
        System.out.println("[debug] geocode: " + geoUrl);
        JsonNode g = MAPPER.readTree(HttpClient.get(geoUrl));
        if (g.get("results") == null || g.get("results").isEmpty())
            throw new IllegalArgumentException("cidade nao encontrada");
        JsonNode r = g.get("results").get(0);
        String nome = r.get("name").asText();
        if (r.hasNonNull("admin1")) nome += ", " + r.get("admin1").asText();
        if (r.hasNonNull("country")) nome += " - " + r.get("country").asText();
        return new Place(nome, r.get("latitude").asDouble(), r.get("longitude").asDouble());
    }

    public Now weatherNow(Place p) throws Exception {
        String url = "https://api.open-meteo.com/v1/forecast?latitude="+p.lat+"&longitude="+p.lon+"&current_weather=true&timezone=auto";
        System.out.println("[debug] weather now: " + url);
        JsonNode w = MAPPER.readTree(HttpClient.get(url));
        JsonNode cur = w.get("current_weather");
        return new Now(p.nome, cur.get("time").asText(), cur.get("temperature").asDouble(), cur.get("windspeed").asDouble());
    }

    public Day weatherTomorrow(Place p) throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String url = "https://api.open-meteo.com/v1/forecast?latitude="+p.lat+"&longitude="+p.lon+
                "&daily=temperature_2m_max,temperature_2m_min&timezone=auto&start_date="+DAY_FMT.format(tomorrow)+
                "&end_date="+DAY_FMT.format(tomorrow);
        System.out.println("[debug] weather tomorrow: " + url);
        JsonNode root = MAPPER.readTree(HttpClient.get(url)).get("daily");
        double tmin = root.get("temperature_2m_min").get(0).asDouble();
        double tmax = root.get("temperature_2m_max").get(0).asDouble();
        return new Day(tomorrow.toString(), tmin, tmax);
    }

    public Day[] weatherWeek(Place p) throws Exception {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(6);
        String url = "https://api.open-meteo.com/v1/forecast?latitude="+p.lat+"&longitude="+p.lon+
                "&daily=temperature_2m_max,temperature_2m_min&timezone=auto&start_date="+DAY_FMT.format(start)+
                "&end_date="+DAY_FMT.format(end)+"&timezone=auto";
        System.out.println("[debug] weather week: " + url);
        JsonNode d = MAPPER.readTree(HttpClient.get(url)).get("daily");
        int n = d.get("time").size();
        Day[] days = new Day[n];
        for (int i=0;i<n;i++){
            String date = d.get("time").get(i).asText();
            double tmin = d.get("temperature_2m_min").get(i).asDouble();
            double tmax = d.get("temperature_2m_max").get(i).asDouble();
            days[i] = new Day(date, tmin, tmax);
        }
        return days;
    }

    private String enc(String s){ return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8); }
}
