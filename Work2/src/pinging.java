import java.awt.List;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class pinging extends Thread {
	
	private Object[] msg;
	private String ip;
	
	public pinging(String ip) {
		this.ip = ip;
		msg = new Object[4];
	}
	
	@Override
	public void run() {
		BufferedReader br = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("ping -a " + ip);
			msg[0] = ip;
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			
			ExecutorService es = Executors.newFixedThreadPool(20);
			int timeout = 20;
			List<Future<ScanResult>> futures = new ArrayList<>();
			int openPorts = 0;
			String openPortNumber = "";
			for(int port = 1; port <= 1024; port++) {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip,port), timeout);
					socket.close();
					if(openPortNumber=="")
						openPortNumber+=port;
					else
						openPortNumber+=", "+port;
			} catch(Exception ex) {
			}
				
			while ((line = br.readLine()) != null) {
				if (line.indexOf("[") >= 0)
				{
					msg[3] = line.substring(5, line.indexOf("[") - 1);
				}
				if (line.indexOf("ms") >= 0) {
					msg[1] = line.substring(line.indexOf("ms") - 1, line.indexOf("ms") + 2);
					msg[2] = line.substring(line.indexOf("TTL=") + 4, line.indexOf("TTL=") + 7);
					break;
				}
			} }
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Object[] getMsg() {
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	public static void main(String[] args) {
		pinging[] pg = new pinging[254];
		//add
		String fixedIP = "192.168.1.";
		for(int i=0; i<=253; i++) {
			pg[i] = new pinging(fixedIP + (i+1));
			pg[i].start();
		}
		for(int i=0; i<=253;i++) {
			Object[] msg = pg[i].getMsg();
			if(msg == null) {
				System.out.println("die");
			}
			else {
				System.out.println(msg[0]+","+msg[2]+","+msg[3]);
			}
		}
	}

}
public static class ScanResult {
	private int port;
	
	private boolean isOpen;
	
	public ScanResult(int port, boolean isOpen) {
		super();
		this.port = port;
		this.isOpen = isOpen;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
		
	}
	public boolean isOpen() {
		return isOpen;
	}
public void setOpen(boolean isOpen) {
	this.isOpen = isOpen;
}
}


