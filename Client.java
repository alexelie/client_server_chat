import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client{
	//socket,stream
	private Socket socket;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private String host;
	private Boolean connected;
	
	public Client(){
		connected=false;
	}
	public void connect(String host)throws Exception{
		this.host=host;
		this.connected=true;
		//connect to server
		socket=new Socket(InetAddress.getByName(this.host),6789);
		//setup stream
		os=new ObjectOutputStream(socket.getOutputStream());
		os.flush();
		is=new ObjectInputStream(socket.getInputStream());
	}
	public void disconnect(){
		try{
			is.close();
			os.close();
			socket.close();
			connected=false;
		}catch(IOException IOException){
			IOException.printStackTrace();
		};
	}
	public boolean is_connected(){
		if (connected){
			return true;
		}
		return false;
		
	}
	public Socket get_sock(){
		return this.socket;
	}
	public ObjectOutputStream get_OS(){
		return this.os;
	}
	public ObjectInputStream get_IS(){
		return this.is;
	}

}
