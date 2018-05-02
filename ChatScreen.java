/**
 * This program is a rudimentary demonstration of Swing GUI programming.
 * Note, the default layout manager for JFrames is the border layout. This
 * enables us to position containers using the coordinates South and Center.
 *
 * Usage:
 *	java ChatScreen
 *
 * When the user enters text in the textfield, it is displayed backwards
 * in the display area.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.*;
import javax.swing.border.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChatScreen extends JFrame implements ActionListener, KeyListener
{

	public static final int DEFAULT_PORT = 8029;
	private static final Executor exec = Executors.newCachedThreadPool();
	private JButton sendButton;
	private JButton exitButton;
	private JTextField sendText;
	private JTextArea displayArea;
	private Socket server = null;
	private Vector<String> clients = new Vector<String>();
	private String user = null;
	private static String ipNum = null;
	private PrintWriter printThis = null;
	private JList checkList;

	public ChatScreen() {
		/**
		 * a panel used for placing components
		 */
		JPanel p = new JPanel();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Enter Message Here ...");
		p.setBorder(titled);

		/**
		 * set up all the components
		 */
		sendText = new JTextField(30);
		sendButton = new JButton("Send");
		exitButton = new JButton("Exit");

		/**
		 * register the listeners for the different button clicks
		 */
		sendText.addKeyListener(this);
		sendButton.addActionListener(this);
		exitButton.addActionListener(this);

		/**
		 * add the components to the panel
		 */
		p.add(sendText);
		p.add(sendButton);
		p.add(exitButton);

		/**
		 * add the panel to the "south" end of the container
		 */
		getContentPane().add(p,"South");

		/**
		 * add the text area for displaying output. Associate
		 * a scrollbar with this text area. Note we add the scrollpane
		 * to the container, not the text area
		 */
		displayArea = new JTextArea(15,40);
		displayArea.setEditable(false);
		displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

		JScrollPane scrollPane = new JScrollPane(displayArea);
		getContentPane().add(scrollPane,"Center");

		/**
		 * set the title and size of the frame
		 */
		setTitle("GUI Demo");
		pack();

		setVisible(true);
		sendText.requestFocus();

		/** anonymous inner class to handle window closing events */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		} );

	}

	/**
	 * These methods responds to keystroke events and fulfills
	 * the contract of the KeyListener interface.
	 */	
	public void sendToUser() {
		String mess = sendText.getText().trim();
		JSONObject sendMessage = new JSONObject();
		sendMessage.put("type", "chatroom-send");
		sendMessage.put("from", user);
		sendMessage.put("message", mess);
		ArrayList<String> selection = new ArrayList(checkList.getSelectedValuesList());
		if(!selection.isEmpty())
		{
			String[] selectionArray = selection.toArray(new String[selection.size()]);
			JSONArray receiveThis = new JSONArray();
			for(int i = 0; i < selectionArray.length; i ++){
				receiveThis.add(selectionArray[i]);
			}
			sendMessage.put("to", receiveThis);
		}
		else
		{
			sendMessage.put("to", new JSONArray());
		}
		sendMessage.put("message-length", mess.length());
		printThis.println(sendMessage.toString());

		sendText.setText("");
		sendText.requestFocus();
	}
	
	/**
	 * This gets the text the user entered and outputs it
	 * in the display area.
	 */
	public void displayText() {
		String message = sendText.getText().trim();
		StringBuffer buffer = new StringBuffer(message.length());

		// now reverse it
		for (int i = message.length()-1; i >= 0; i--)
			buffer.append(message.charAt(i));

		displayArea.append(buffer.toString() + "\n");

		sendText.setText("");
		sendText.requestFocus();
	}


	/**
	 * This method responds to action events .... i.e. button clicks
	 * and fulfills the contract of the ActionListener interface.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == sendButton)
			displayText();
		else if (source == exitButton)
			System.exit(0);
	}

	
	/**
	 * This is invoked when the user presses
	 * the ENTER key.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			displayText();
	}

	/** Not implemented */
	public void keyReleased(KeyEvent e) { }

	/** Not implemented */
	public void keyTyped(KeyEvent e) {  }

	public void setUpServer(JPanel userInput)
	{
		ipNum = JOptionPane.showInputDialog(userInput, "Enter Server IP:", null);
		
		try {
			server = new Socket(ipNum, DEFAULT_PORT);
			printThis = new PrintWriter(server.getOutputStream(), true);
			System.out.println("User Has Connected.");
			user = JOptionPane.showInputDialog(userInput, "Enter Your Desired Username:", null);
			JSONObject json = new JSONObject();
			json.put("type", "chatroom-begin");
			json.put("username", user);
			json.put("len", user.length());
			printThis.println(json.toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static void main(String[] args) {
		JFrame win = new ChatScreen();
	}
}
