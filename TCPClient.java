import java.net.*;  
import java.io.*;  
class TCPClient{  
public static void main(String args[]){  
Socket s=null;
try{
	int serverPort = 50000;
	s=new Socket("localhost", serverPort); 
	System.out.println("Port number: "+s.getPort());  
	BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	DataOutputStream out=new DataOutputStream(s.getOutputStream());  



	//Send HELO
	out.write(("HELO"+"\n").getBytes());  
	String data = in.readLine();
	System.out.println("Recieved: "+data);
	
	//AUTH
	String username= System.getProperty("user.name");
	out.write(("AUTH "+username+"\n").getBytes());
	data = in.readLine();
	System.out.println("Recieved: "+data);

}

catch (UnknownHostException e){
	System.out.println("Sock: "+e.getMessage());
}
catch(EOFException e){
	System.out.println("EOF: "+e.getMessage());
}
catch(IOException e){
	System.out.println("IO: "+e.getMessage());
}
finally {if (s!=null){
	try { s.close();
	}
	catch(IOException e){
		System.out.println("close: "+e.getMessage());
	}
}
 
}}}
