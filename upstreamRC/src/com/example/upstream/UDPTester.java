package com.example.upstream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upstream.MainActivity;

public class UDPTester {
	private ImageView imgView; 
	private byte pkgCount = 0;
	private long pkgTime;
	private InetAddress opp; 
	private boolean found = false;
	private MainActivity main;
	
/*	private Handler txtHandle;
	private Handler imgHandle;
	private Bundle picbun;
	private Bundle txtbun;
	
//	private Message txtmsg;
//	private Message picmsg;
	*/
	
	private static final long serialVersionUID = -4045857603693960687L;
	public static final int CALC_CNT = 10;
	public static final String A_IMGPARA = "A_IMGPARA";
	
	public UDPTester(MainActivity mainActivity) {
		
		//txtHandle=txt;
		//imgHandle=imgHandler;

	
		this.main=mainActivity;
		
		
		
		
		new Thread(new Runnable() { 
            public void run(){
            	int server_port = 9931;
        		byte[] message	= new byte[128000];
        		DatagramPacket p = new DatagramPacket(message, message.length);
        		DatagramSocket s = null;
        		
        		// TODO: Erst ��ber Broadcast Server suchen
        		
				try {
					s = new DatagramSocket(server_port);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
            	// Erstmal das Auto suchen (Broadcast)
				try {
					opp = InetAddress.getByName("255.255.255.255");
					//opp = InetAddress.getByName("141.59.148.145");
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				byte[] handshake	= { (byte) 240, 'H', 'E', 'L', 'L', 'O', 'U', 'P', 'S', 'T', 'R', 'E', 'A', 'M'};
				byte[] handsh_ack	= { (byte) 255, 'W', 'E', 'L', 'C', 'O', 'M', 'E', 'U', 'P', 'S', 'T', 'R', 'E', 'A', 'M'};
				
				for (;;) {
					sendUDPPacket(handshake, 14);
					try {
						// Timeout fehlt noch
        				s.receive(p);
        			} catch (Exception ex) {
        				System.out.println("UDP-Port nicht verf��gbar.");
        				System.exit(0);
        			}
					
					// geht nicht mit .equals..
					if (p.getData()[0]==(byte)255) {
						opp = p.getAddress();
						System.out.println("Gegenseite gefunden");
						found = true;
						break;
					} else {
						System.out.println("Kein g��ltiges Ack-Packet: ");
						for (int i=0; i<handsh_ack.length; i++)
							System.out.print((char)handsh_ack[i] + "|");
						System.out.println(" ");
						for (int i=0; i<p.getLength(); i++)
							System.out.print((char)p.getData()[i] + "|");
						System.out.println(" ");
						// Paket ausgeben
						System.exit(0);
					}
				}
        		
				//BufferedImage image = null;
				
				int imgCnt	= 0;
				int imgSize	= 0;
				int imgRate	= 0;
				long fpsStart	= 0;
				long fpsEnde	= 0;
				
				for (;;) {
					// Paket empfangen
        			try {
        				s.receive(p);
        			} catch (Exception ex) {
        				System.out.println("UDP-Port nicht verf��gbar.");
        				System.exit(0);
        			}
        			System.out.print("case "+p.getData()[0]+": ");
        			
        			// Paket verarbeiten
        			switch (p.getData()[0]) {
        			case 1:
    					long timeDiff = System.nanoTime() - pkgTime;
    					//connLabel.setText("Latenz: "+ Math.round(timeDiff/1e4)/1e2 +"ms");
    					System.out.println("Received new Ack");
    				break;
        			case 3:
        				//Bitmap bild = (BitmapFactory.decodeByteArray(p.getData(), 1, p.getLength()-1));
        				//ima.zeichne(bild);
        				//imgView.setImageBitmap);
						//imgView.setImage(image.getScaledInstance((int)(image.getWidth()*1), (int)(image.getHeight()*1), 0));


						imgRate += p.getLength();
						if (imgCnt == CALC_CNT) {
							fpsEnde = System.nanoTime();
							if (fpsStart != 0) {
								//txtbun.putString("fps",(Math.round(imgCnt*100/((fpsEnde-fpsStart)/1e9))/100d + "FPS"));
								//txtmsg.setData(txtbun);
								//txtHandle.sendMessage(txtmsg);
							}
							//sizeLabel.setText(imgSize/imgCnt  +" Bytes, "+Math.round(imgRate/(((double)fpsEnde-fpsStart)/1e9d)/1024)+" kb/s");
							fpsStart = System.nanoTime();
							imgSize = 0;
							imgCnt = 0;
							imgRate = 0;
						}
						imgCnt++;
						imgSize += p.getLength();
  				
						System.out.println("Received new Image ("+ (p.getLength()-1) + " Bytes)");
	        			break;
        			}
        			
        			if (message[0]==127)
        				break;
        		}
        		s.close();

            }
		}).start();
		
		new Thread(new Runnable() { 
            public void run(){
            	// Erstmal ��ber Broadcast nach Server suchen
            	
            	for (;;) {
            		if (found) {
            			break;
            		} else {
            			try {
            				Thread.sleep(200);
            			} catch (InterruptedException e) {
						// 	TODO Auto-generated catch block
            				e.printStackTrace();
            			}
            		}
            	}
            	
            	for (;;) {
            		byte[] ctrlData = { 0,
            				pkgCount,
            				main.getLenkung(),
            				main.getGeschwindigkeit() };
            		sendUDPPacket(ctrlData, 4);
            		pkgTime = System.nanoTime();
            		System.out.println("Lenkung: "+ctrlData[2]+".....Geschwindigkeit: "+ ctrlData[3]);
            		            		
            		try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		// Paketz��hler inkrementieren, landet bei overflow wieder bei 0 
            		pkgCount++;
            	}
            }
		}).start();
	}

	private void sendUDPPacket(byte[] msg, int length) {
		int server_port = 9930;
		DatagramSocket s = null;
		try {
			s = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetAddress local = null;
		local = opp;//InetAddress.getByName("127.0.0.1");//192.168.178.5");
		DatagramPacket p = new DatagramPacket(msg, length,local,server_port);
		try {
			s.send(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 * @throws SocketException 
	 */
	/*public static void main(String[] args) {
		new UDPTester();
	}*/



	public void sendImgParam(byte aufloesung, byte fps, byte quali, byte farbe) {
		//
		byte[] imgCtrlData = { 2,
				aufloesung,
				fps,
				quali,
				farbe};
		System.out.println("FPS: "+imgCtrlData[2]);
		//System.out.println("Resol.:" + );
		sendUDPPacket(imgCtrlData, 5);
	}
	
}
