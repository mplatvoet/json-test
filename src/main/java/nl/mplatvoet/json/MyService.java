package nl.mplatvoet.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MyService {
    private final ObjectMapper mapper = new ObjectMapper();

    public String createJson() {
        StringWriter writer = new StringWriter();
        try {
            mapper.writer().writeValue(writer, new SampleObject());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    public static class SampleObject {
        private int a = 3;
        private int b = 2;
        private int c = 1;

        private List<String> ds = listOf("e", "f", "g");

        private List<String> listOf(String... ss) {
            ArrayList<String> r = new ArrayList<>();
            for (String s : ss) {
                r.add(s);
            }
            return r;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getC() {
            return c;
        }

        public List<String> getDs() {
            return ds;
        }
    }

}
