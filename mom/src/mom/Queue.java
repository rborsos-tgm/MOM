package mom;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Chat Queue Class
 * 
 * @author Robert Borsos
 * @version 04-03-2016
 *
 */
public class Queue {
	private static ConnectionFactory connectionFactory;
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private static Connection connection = null;
	private static Session session = null;
	private static Destination dest;
	private static MessageConsumer mailbox = null;

	/**
	 * Class-Method from Queue
	 * 
	 * @param url ip for connection
	 * @param user username
	 * @param topic chattopic
	 */
	public Queue(String url, String user, String topic) {
		try {
			connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// setting destination address
			dest = session.createQueue(user);
			
			// Create the consumer
			mailbox = session.createConsumer(dest);

		} catch (Exception e) {
			System.out.println("Queue connection failed");
		}
	}
	
	/**
	 * receive the messages
	 * 
	 */
	public void mailbox() {
		try {
			System.err.println("Mailbox:");
			TextMessage message = (TextMessage) mailbox.receive(1000);
			while (message != null) {
				System.out.println(message.getText());
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * sending a mail 
	 * 
	 * @param receiver message receiver
	 * @param message message text
	 */
	public void mail(String receiver, String message) {
		TextMessage msg;
		try {
			Destination dest = session.createQueue(receiver);
			// Create the producer.
			MessageProducer mail = session.createProducer(dest);
			System.err.println("Mail was sent");
			msg = session.createTextMessage(message);
			mail.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close the connection for JMSQueue
	 * 
	 */
	public void close() {
		try {
			// close connections 
			mailbox.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Queue is closed, no more inputs!");
	}
}