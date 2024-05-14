import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class StateLoader {

    private static Map<String, State> states = new ConcurrentHashMap<>();

    public static State get(String name) {
        return states.get(name);
    }

    public static void load() throws JsonSyntaxException, IOException {
        StatusDto statusDto = new Gson().fromJson(new String(Files.readAllBytes(Paths.get("STATUS.JSON"))),
                StatusDto.class);

        for (Entry<String, StateDto> e : statusDto.status.entrySet()) {
            String name = e.getKey();
            StateDto stateDto = e.getValue();
            String type = stateDto.type;
            String url = stateDto.url;
            List<String> parameters = stateDto.parameters;
            State state = new State(type, url, parameters);
            states.put(name, state);
        }
    }

    static class StatusDto {
        public Map<String, StateDto> status;

        class StateDto {
            public String type;
            public String url;
            public List<String> parameters;
        }
    }

    static class State {
        public String type;
        public String url;
        public List<String> parameters;

        public State(String type, String url, List<String> parameters) {
            this.type = type;
            this.url = url;
            this.parameters = parameters;
        }
    }

    public static void main(String[] args) throws IOException {
        load();
        System.out.println(states);
    }
}
