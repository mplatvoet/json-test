package nl.mplatvoet.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonUtil {
    private static final Pattern DOT_PATTERN = Pattern.compile("[.]");
    private static ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
        //no instances
    }

    public static JsonNode toJsonNode(String json) {
        if (json == null) return null;

        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse content to Json", e);
        }
    }

    public static JsonNode toJsonNode(Class<?> base, String resource) {
        if (base == null) {
            throw new IllegalArgumentException("base class cannot be null");
        }
        String path = toBasePath(base) + "/" + resource;
        try (InputStream stream = base.getClass().getResourceAsStream(path)) {
            return mapper.readTree(stream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse [" + path + "] to Json", e);
        }
    }

    private static String toBasePath(Class<?> base) {
        base.getName();
        Matcher matcher = DOT_PATTERN.matcher(base.getName());
        return "/" + matcher.replaceAll("/");

    }


}
