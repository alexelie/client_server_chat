import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientManager implements Runnable {
	
	private Socket socket;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private String message;
	
	public ClientManager(Socket sock){
		
		socket=sock;
		try {
			setupStream();
		} catch (Exception e) {
			System.out.println("cant connect stream");
			e.printStackTrace();
		}
	}
	private void setupStream()throws Exception{
		is=new ObjectInputStream(socket.getInputStream());
		os=new ObjectOutputStream(socket.getOutputStream());
		os.flush();
		
	}
	public synchronized void send(String message){
		try {
			os.writeObject(message);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		while(socket.isConnected()){
			try {
				message=(String)is.readObject();
				//send message to clients
				for(ClientManager client : Server.client_sock_list){
					client.send("Anonymous - "+message);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				close();
			}
		}
		//ne se rend pas...
		close();
		
	}
	private void close(){
		try{
			Server.client_sock_list.remove(this);
			is.close();
			os.close();
			socket.close();		
		}catch(IOException IOException){
			IOException.printStackTrace();
			
		}		
		
	}

}
