import java.util.List;
import java.util.Map;

/**
 * Интерфейс клиента транспорта для отправки и получения сообщений при тестировании
 */
public interface BenchmarkClient extends AutoCloseable {


    /**
     * Отправка тестового сообщения
     * @param message тело сообщения
     * @param timeout таймаут отправки
     * @throws Exception
     */
    void send(BenchmarkMessage message, long timeout) throws Exception;

    /**
     * Получение тестового сообщения
     * @param timeout таймаут получения
     * @return тело сообщения
     * @throws Exception
     */
    List<BenchmarkMessage> receive(long timeout) throws Exception;
}