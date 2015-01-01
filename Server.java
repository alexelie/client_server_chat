import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private ServerSocket server_socket;
	private Boolean connected;
	public static ArrayList<ClientManager> client_sock_list;
	
	public static void main(String[] args){
		Server server=new Server();
		System.out.println("server started");
		server.runServer();
	}
	public Server(){
		client_sock_list=new ArrayList<ClientManager>();
	}
	private void runServer(){
		//new serverSocket to listen to any port
		try {
			server_socket=new ServerSocket(6789);
			connected=true;
			//listen for connections. blocking cmd.
			while(connected){
				Socket client_socket=server_socket.accept();
				System.out.println("new client connected");
				System.out.println(client_socket.getLocalSocketAddress());
				ClientManager client = new ClientManager(client_socket);
				//put nw client in arrayList
				client_sock_list.add(client);
				//start new thread to manage client every new connection				
				new Thread(client).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//close all connections and stop server
			
		}

	}
	
}
