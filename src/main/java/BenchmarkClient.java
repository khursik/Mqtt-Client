import java.util.List;
import java.util.Map;

/**
 * ��������� ������� ���������� ��� �������� � ��������� ��������� ��� ������������
 */
public interface BenchmarkClient extends AutoCloseable {


    /**
     * �������� ��������� ���������
     * @param message ���� ���������
     * @param timeout ������� ��������
     * @throws Exception
     */
    void send(BenchmarkMessage message, long timeout) throws Exception;

    /**
     * ��������� ��������� ���������
     * @param timeout ������� ���������
     * @return ���� ���������
     * @throws Exception
     */
    List<BenchmarkMessage> receive(long timeout) throws Exception;
}