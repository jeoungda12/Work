import java.awt.*;
import java.util.List;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.TIMEOUT;

public class PortScanner {

	public static void main(String[] args) throws InterruptedException, ExecutionException{
		// TODO Auto-generated method stub

		final ExecutorService es = Executors.newFixedThreadPool(20);
		final String ip = "127.0.0.1";
		final int timeout  = 200;
		final List<Future<ScanResult>> futures = new ArrayList<>();
		for(int port = 1; port<=1024;port++) {
			futures.add(portlsOpen(es, ip, port, timeout));
		}
		int openPorts = 0;
		String openPortNumber = "";
		try {
			es.awaitTermination(200L, TimeUnit.MILLISECONDS);
		for (final Future<ScanResult> f : futures) {
			if(f.get().isOpen()) {
				openPorts++;
				if(openPortNumber=="")
					openPortNumber+=f.get().getPort();
				else
					openPortNumber +=","+ f.get().getPort();
			}
		}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static Future<ScanResult> portlsOpen(final ExecutorService es, final String ip, final int port,final int timeout){
		return es.submit(new Callable<ScanResult>() {
			public ScanResult call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip,port), timeout);
					socket.close();
					return new ScanResult(port, true);
				} catch (Exception ex) {
					return new ScanResult(port,false);
				}
			}
		});
	}
}
