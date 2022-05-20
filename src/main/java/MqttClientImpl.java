
import com.google.protobuf.ByteString;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class MqttClientImpl implements BenchmarkClient {

    private LinkedBlockingQueue<BenchmarkMessage> messageQueue = new LinkedBlockingQueue<>();
    private final OnMessage callback = new OnMessage(messageQueue);
    private final String topic;
    private final String broker;
    private static final Logger logger = LoggerFactory.getLogger(MqttClientImpl.class.getName());
    private final MqttAsyncClient producerClient;
    private final MqttAsyncClient consumerClient;
    private final int qos = 2; //quality of service
    private final MqttConnectionOptions connOpts = new MqttConnectionOptions();


    public MqttClientImpl(Map<String, String> properties) throws MqttException {

        this.topic = properties.getOrDefault("topic", "Test");
        this.broker = properties.getOrDefault("broker", "tcp://localhost:1883");

        String producerClientId = properties.getOrDefault("producerClientId", "");
        String consumerClientId = properties.getOrDefault("consumerClientId", "");

        MemoryPersistence persistence = new MemoryPersistence();
        this.producerClient = new MqttAsyncClient(broker, producerClientId, persistence);
        this.consumerClient = new MqttAsyncClient(broker, consumerClientId, persistence);

        connOpts.setKeepAliveInterval(60);
        connOpts.setCleanStart(true);
        connOpts.setUserName(properties.get("userName"));
        connOpts.setPassword(properties.get("password").getBytes(StandardCharsets.UTF_8));
        logger.info("Connecting to broker: " + broker);

        producerClient.connect(connOpts).waitForCompletion();
    }

    @Override
    public void send(BenchmarkMessage message, long timeout) throws Exception {
        logger.debug("Origin message: {}", message);
    }

    @Override
    public List<BenchmarkMessage> receive(long timeout) throws Exception {
        createConsumerClient();
        BenchmarkMessage benchmarkMessage = messageQueue.poll(timeout, TimeUnit.MILLISECONDS);
        logger.debug("poll message: {}", benchmarkMessage);
        return benchmarkMessage == null ? Collections.emptyList() : Collections.singletonList(benchmarkMessage);
    }

    @Override
    public void close() throws Exception {
        producerClient.disconnect();
        logger.info("Producer: disconnected");
        consumerClient.disconnect();
        logger.info("Consumer: disconnected");
        producerClient.close();
        consumerClient.close();
    }

    public MqttAsyncClient createConsumerClient() throws MqttException {
        if (consumerClient.isConnected()) {
            return consumerClient;
        }
        consumerClient.connect().waitForCompletion();
        consumerClient.setCallback(callback);
        consumerClient.subscribe(topic, qos);
        return consumerClient;
    }
}