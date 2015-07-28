

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.irdresearch.smstarseel.global.SmsTarseelGlobal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SocketService extends Service 
{
	private static ServerSocket server;
	private static Socket client;
	private String TAG = "TAGSERverSock";

	// socket stuff

	//private HashMap<String, Boolean> hdmiApps;

	@Override
	public void onCreate() {
		super.onCreate();
		//hdmiApps = parseFeaturedApps();
		try {
			createSocketServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class Globals {
		public static boolean connected;
	}

	// socket server needs to do the following things.
	// accept connections until one is established.
	// restart if a connection is lost.
	private void createSocketServer() throws IOException {
		/*if (!Globals.connected) {
			Thread t = new Thread(new ServerThread());
			t.start();

			Log.d(TAG , "INITIALIZING SOCKET");
		} else {
			Log.d(TAG, "ALREADY CONNECTED");
		}*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null/*mBinder*/;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// an alarm invokes this every second
		//checkRunningApp();
	}

	public static synchronized Socket getSocketConnection() throws IOException
	{
		if(server == null || server.isClosed())
		{
			server = new ServerSocket(38300);
			server.setSoTimeout(1000*60*1);
		}
		
		if(client == null || client.isClosed()){
			client = server.accept();
		}
/*		// add a shutdown hook to close the socket if system crashes or exists unexpectedly
		Thread closeSocketsOnShutdown = new Thread() {
			public void run() {
				try {
					client.shutdownInput();
				} catch (IOException e) {
				e.printStackTrace();
				}
				try {
					client.shutdownOutput();
				} catch (IOException e) {
				e.printStackTrace();
				}
				try {
					client.close();
				} catch (IOException e) {
				e.printStackTrace();
				}
				try {
					server.close();
				} catch (IOException e) {
				e.printStackTrace();
				}
			}
		};
	
		Runtime.getRuntime().addShutdownHook(closeSocketsOnShutdown);*/	
		return client;
	}
	public class ServerThread implements Runnable {

		public void run() {
		    
			try {
				Log.d(TAG, "waiting for connection");

				while (true) {
					// listen for incoming clients
					client = server.accept();

					try {
						BufferedReader in = new BufferedReader(
								new InputStreamReader(client.getInputStream()));
						PrintWriter outpw = new PrintWriter(
								client.getOutputStream(), true);
						
						String receivedinputmsg = null;
						String actualinputmessage = null;
						boolean readMore = true;
						while (readMore) {
							receivedinputmsg = in.readLine();
							System.out.println("receivedinputmsg: " + receivedinputmsg);
							
							if(receivedinputmsg == null || receivedinputmsg.endsWith(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG)){
								readMore = false;
							}
							
							if(receivedinputmsg != null){
								actualinputmessage = receivedinputmsg.replaceAll(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG, "\r\n");
							}
							
							System.out.println("actualinputmessage: " + actualinputmessage);
						}

						outpw.write("server read:"+actualinputmessage);
						outpw.write(SmsTarseelGlobal.SOCKET_MESSAGE_END_FLAG);
						outpw.flush();

						// break;
					} catch (Exception e) {
						Log.d(TAG, "lost client");
						e.printStackTrace();
					}
				}

			}
			// }
			catch (Exception e) {
				Log.d(TAG, "error, disconnected");
				e.printStackTrace();
			}
		}
	}

}
