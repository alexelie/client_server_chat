import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class Client_GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private JButton connect_bt;
	private JLabel num_of_connection;
	private JTextField client_text;
	private JTextArea chat_area;
	private JScrollPane scrollPane;
	private JPanel content;
	private JPanel top_content;
	private JPanel text_content;
	private Client client;
	private String server_message;
	private String client_message;

	
	public static void main(String[] args) {
		 Client_GUI GUI=new Client_GUI();
		 GUI.start();
	}
	public Client_GUI(){
		super("client_side");	
	}
	public void start(){
		client=new Client();
		set_GUI();	
	}
	private void set_GUI(){
		
		content=new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		this.add(content);
		
		top_content=new JPanel();
		top_content.setLayout(new FlowLayout(FlowLayout.LEFT));
		connect_bt=new JButton("connect");
		num_of_connection=new JLabel("X connections established");
		top_content.add(connect_bt);
		top_content.add(num_of_connection);
		
		text_content=new JPanel();
		text_content.setLayout(new BoxLayout(text_content,BoxLayout.Y_AXIS));
		client_text=new JTextField();
		client_text.setMaximumSize(new Dimension(400,30));
		chat_area=new JTextArea();
		scrollPane=new JScrollPane(chat_area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(0,300));
		text_content.add(client_text);
		text_content.add(scrollPane);
		client_text.setEditable(false);
		chat_area.setEditable(false);
		
		content.add(top_content);
		content.add(text_content);
		
		this.setSize(600,400);
		this.setVisible(true);	
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setListeners();
		
	}
	private void setListeners(){
		//enter pressed on textField
		client_text.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent evt){
							client_text.setText("");
							client_message=(evt.getActionCommand()+"\n");
							try {
								client.get_OS().writeObject(client_message);
								client.get_OS().flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
				}
		);
		connect_bt.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					if(!client.is_connected()){
						//connect to server method
						try{
							client.connect("127.0.0.1");
							showMessage("connected to server\n");
							connect_bt.setText("disconnect");
							client_text.setEditable(true);
							//non-blocking fct. swingWorker inside.
							whileConnected();
						}catch(Exception exception){
							System.out.println("unable to connect");
						}
					}else{
						//disconnect from server method
						client.disconnect();
						connect_bt.setText("connect");
						client_text.setText("");
						chat_area.setText("");
						client_text.setEditable(false);
					}		
				}
			}
		);	
	}
	private void whileConnected(){
		//swingUtilities thread
		SwingWorker<Void,Void> worker=new SwingWorker<Void,Void>(){
			protected Void doInBackground() throws Exception {
				while(client.is_connected()){
					//looking for inputStream
					server_message=(String)client.get_IS().readObject();
					//if message received, pass work to event thread
					showMessage(server_message);
					
				}
				return null;
			}
		};
		worker.execute();
	}
	private void showMessage(final String message){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				chat_area.append(message);
			}
		});
		
		
	}
}
