package queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            IntStream.range(0, 10)
                    .parallel()
                    .forEach(i -> {
                        try {
                            String m = message + "_" + i;
                            channel.basicPublish("", QUEUE_NAME, null, m.getBytes(StandardCharsets.UTF_8));
                            System.out.println(" [x] Sent '" + m + "'");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
