package mom;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Chat Topic class
 * 
 * @author Robert Borsos
 * @version 04-03-2016
 *
 */
public class Topic implements MessageListener {
	private String user = null;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;

	private Session session = null;
	private Connection connection = null;
	private MessageProducer producer = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;

	/**
	 * Class-Method from Topic
	 * 
	 * @param url ip for connection
	 * @param user username
	 * @param topic chattopic
	 */
	public Topic(String url, String user, String topic) {
		this.user = user;

		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(topic);

			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);

		} catch (Exception e) {
			System.out.println("Topic connection failed");
		}
	}
	
	/**
	 * message listener
	 * 
	 */
	@Override
	public void onMessage(Message message) {
		try {
			System.out.println(((TextMessage) message).getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * message for normal text sending
	 * 
	 * @param s message
	 */
	public void send(String s) {
		TextMessage message;
		try {
			message = session.createTextMessage(user + ": " + s); // rborsos: "text"
			producer.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * close connection from JMSTopic
	 * 
	 */
	public void close() {
		try {
			// close connections
			producer.close(); 
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Topic is closed, no more inputs!");
	}
}