package test.pubsub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test4PubSub {

	public static final String CHANNEL_NAME = "commonChannel";

	private static Logger logger = LoggerFactory.getLogger(Test4PubSub.class);

	public static void main(String[] args) throws Exception {
		final JedisDao dao = new JedisDao();
		
		//1.监听
		 new Thread(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                    logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
	                    dao.listener(CHANNEL_NAME);//先监听
	                    logger.info("Subscription ended.");
	                } catch (Exception e) {
	                    logger.error("Subscribing failed.", e);
	                }
	            }
	        }).start();
		
		 
		 //发送消息
		logger.info("Type your message (quit for terminate)");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String line = reader.readLine();

                if (!"quit".equals(line)) {
                	dao.publish( CHANNEL_NAME,line);
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            logger.error("IO failure while reading input, e");
        }
		


		logger.info("Subscription ended.");

		// subscriber.unsubscribe();
	}
}
