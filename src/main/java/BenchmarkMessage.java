import java.util.*;

public class BenchmarkMessage {

    private final Map<String, String> headers;
    private final byte[] body;

    public BenchmarkMessage() {
        this(new HashMap<>(), new byte[0]);
    }

    public BenchmarkMessage(byte[] body) {
        this(new HashMap<>(), body);
    }
    public BenchmarkMessage(Map<String, String> headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BenchmarkMessage)) return false;
        BenchmarkMessage that = (BenchmarkMessage) o;
        if (!Objects.equals(headers, that.headers) && headers != null && that.headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String v2 = that.headers.get(entry.getKey());
                if (!entry.getValue().equals(v2)) return false;
            }
        }
        return Arrays.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            result += entry.getKey().hashCode() + entry.getValue().hashCode();
        }
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BenchmarkMessage.class.getSimpleName() + "[", "]")
                .add("headers=" + headers)
                .add("body.size=" + body.length)
                .toString();
    }
}
