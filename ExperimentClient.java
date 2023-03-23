import java.net.*;  
import java.io.*;  
class ExperimentClient{  
public static void main(String args[]){  

	//Create a socket
	Socket s=null;


	try{
	
		//Connect to ds-server
		int serverPort = 50000;
		s=new Socket("localhost", serverPort); 
		System.out.println("Port number: "+s.getPort());  
		
		//Intialise input and output streams associated with socket
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		DataOutputStream out=new DataOutputStream(s.getOutputStream());  



		//HANDSHAKE
		//Send HELO
		out.write(("HELO\n").getBytes()); 
	
		//Recieve OK 
		String serverMessage = in.readLine();
		System.out.println("Recieved: "+serverMessage);
	
		//Send AUTH
		String username= System.getProperty("user.name");
		out.write(("AUTH"+username+"\n").getBytes());
	
		//Recieve OK
		serverMessage = in.readLine();
		System.out.println("Recieved: "+serverMessage);

		
		
		//While last message is not NONE
		while(serverMessage!=("NONE\n")){
			out.write(("REDY\n").getBytes());
			String jobState = in.readLine();
			System.out.println("Job is: "+jobState);
			String [] jobInfo = jobState.split(" ", 4);
			String jobIDTemp = jobInfo[2];
			int jobID = Integer.valueOf(jobIDTemp);
			
			//Sent GETS message
			out.write(("GETS All\n").getBytes()); //May need to add parameters
				
			//Recieve DATA nRecs recSize
			serverMessage = in.readLine();
			System.out.println("GETS is: "+serverMessage);
			//extract nRecs
			String [] arrOfMess = serverMessage.split(" ", 3);
			System.out.println("nRecs is:"+arrOfMess[1]);
			//create int
			int nRecs = Integer.valueOf(arrOfMess[1]);
				
			//Send OK
			out.write(("OK\n").getBytes());
				
				
			//Recieve each record of servers
			String [] servers = new String[nRecs]; 
			serverMessage=in.readLine();


			int biggestCore = 0;
			int serverIDInt=0;
			String serverType ="";
			String serverID="";
			
			for(int i=0;i<nRecs;i++){
				servers[i]=in.readLine();	
			
				String [] serverInfo = servers[i].split(" ", 7);
			
				String serverCores = serverInfo[4];
				int cores = Integer.valueOf(serverCores);
			
				if(cores>biggestCore){
					biggestCore=cores;
					System.out.println("Largest no. of Cores: " + cores);
					serverType = serverInfo[0];
					System.out.println("Server Type: "+serverType);
					serverID= serverInfo[1];
					System.out.println("Server ID: "+serverID);
					serverIDInt = Integer.valueOf(serverID);
				}
			
			
			System.out.println("looping");	
			}
			System.out.println("loop ended");
				
			//Send OK
			out.write(("OK\n").getBytes());
			System.out.println("sending OK");
			
			
			//Recieve .
			serverMessage = in.readLine();
			System.out.println("Server says: "+serverMessage);
		

			System.out.println("Scheduling job:SCHD "+jobID+" "+serverType+" "+serverIDInt+"/n");
			out.write(("SCHD "+jobID+" "+serverType+" "+serverIDInt+"/n").getBytes());
				
			

		
		}
	
	
	
	
		//Send QUIT
		out.write(("QUIT\n").getBytes());
		
		//Recieve QUIT
	 	serverMessage = in.readLine();
	 	System.out.println("Server is now "+serverMessage+"ing");
	 	
	 	//Close the socket
	 	s.close();


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
