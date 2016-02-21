package com.example.project1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/*
 * Service which runs in the background to receive the notification from 
 * pi and send broadcast message 
 * 
 * @author : Suresh Babu Jothilingam
 * @author : Nitish Krishna Ganesan
 * @author : Sarvdeep
 */
public class RoomStatusService extends Service{
	private static boolean isRunFlag = false;
	private UpdateThread th;

	static boolean isServiceRunning(){
		return isRunFlag;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		th = new UpdateThread(); 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		isRunFlag = true;
		th.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunFlag = false;
		th.interrupt();
		th = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//class thread which is started to receive the notification from server
	class UpdateThread extends Thread{

		JSONRPC2Response process(JSONRPC2Request arg0){
			Intent in = new Intent("com.example.project1.tempUpdate");

			in.putExtra("temperature", arg0.getNamedParams().get("temperature").toString());
			in.putExtra("ambient",arg0.getNamedParams().get("ambient").toString());
			in.putExtra("blind",arg0.getNamedParams().get("blind").toString());
			in.putExtra("time",arg0.getNamedParams().get("time").toString());
			sendBroadcast(in);

			return new JSONRPC2Response("ok", arg0.getID());

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String response = "";
			super.run();
			try {
				ServerSocket serverSocket = new ServerSocket(8090);
				while(isRunFlag){

					Socket socket = serverSocket.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

					String line;
					line = in.readLine();
					//System.out.println(line);
					StringBuilder raw = new StringBuilder();
					raw.append("" + line);
					boolean isPost = line.startsWith("POST");
					int contentLength = 0;
					while (!(line = in.readLine()).equals("")) {
						//System.out.println(line);
						raw.append('\n' + line);
						if (isPost) {
							final String contentHeader = "Content-Length: ";
							if (line.startsWith(contentHeader)) {
								contentLength = Integer.parseInt(line.substring(contentHeader.length()));
							}
						}
					}
					StringBuilder body = new StringBuilder();
					if (isPost) {
						int c = 0;
						for (int i = 0; i < contentLength; i++) {
							c = in.read();
							body.append((char) c);
						}
					}

					//System.out.println(body.toString());
					JSONRPC2Request request = JSONRPC2Request.parse(body.toString());
					JSONRPC2Response resp = process(request);

					//Log.d("Notification", in.readLine());
					out.write("HTTP/1.1 200 OK\r\n");
					out.write("Content-Type: application/json\r\n");
					out.write("\r\n");
					out.write(resp.toJSONString());
					// do not in.close();
					out.flush();
					out.close();
					socket.close();


				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
