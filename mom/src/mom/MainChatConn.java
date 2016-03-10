package mom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class with the Main-Method an task allocation
 * Also the Run Config Inputs are taken from here, and put in Queue and Topic
 * 
 * @author Robert Borsos
 * @version 04-03-2016
 *
 */
public class MainChatConn {

	/*
	 * Main-Method
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		// check if the arguments are given
		if (args.length != 3) {
			System.out.println("Arguments missing! \n Please insert in the Run Configuration the following Information \n  <ipMessageBroker> <username> <chatroom>");
		} else {
			System.out.println("Chatting is activated");
			
			// information input to the other classes
			Queue queue = new Queue("tcp://" + args[0] + ":61616", args[1], args[2]);
			Topic topic = new Topic("tcp://" + args[0] + ":61616", args[1], args[2]);

			try {
				// Reader for the Systemout text
				BufferedReader systemout = new java.io.BufferedReader(new InputStreamReader(System.in));

				// choose an action -> "\exit", "\mail", "\mailbox"
				while (true) {
					String temp;

					temp = systemout.readLine();
					String[] s = temp.split("\\s+");
					String[] ms = null;

					if (s[0].equalsIgnoreCase("/exit")) { // if match than close functions
						topic.close(); // call method "close" from Topic.java
						queue.close(); // call method "close" from Queue.java
						System.exit(0); // exit with 0 -> no problems
					} else if (s[0].equalsIgnoreCase("/mailbox")) { // if match than open mailbox
						queue.mailbox(); // open mailbox
					} else if (s[0].equalsIgnoreCase("/mail")) { // if match than open mails
						if (temp.matches("/\\S+\\s+\\S+\\s+\\S+.+")) {
							ms = temp.split("/\\S+\\s+\\S+\\s+");
							queue.mail(s[1], ms[1]);
						} else {
							System.err.println("Wrong Usage! Please insert in the Command Line follwoing syntax: /mail <destination_username> <message>");
						}					
					} else {
						topic.send(temp.toString()); // if there is no "function" like /mail, /mailbox and /exit chosen, so 
													//the text is going to be sent as a normal text
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}