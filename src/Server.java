import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;


public class Server {

	public static void main(String[] args) {
		new Server();
		
	}
	
	Socket s1;
	Socket s2;
	int n = 15;
	
	Vector<Point> DFdadanh = new Vector<>();
	Vector<Point> dadanh = new Vector<>();
	String DFbang[][] = new String[n][n];
	String bang[][] = new String[n][n];
	
	public Server() {
		try {
			ServerSocket server = new ServerSocket(5000);
			 s1 = server.accept();
			 new Xuly(this, s1).start();
			 s2 = server.accept();
			 new Xuly(this, s2).start();
		} catch (Exception e) {

		}
	}

}

class Xuly extends Thread {
	Server server;
	Socket s;
	public Xuly(Server server, Socket s) {
		this.server = server;
		this.s = s;
	};
	
	public void sendRessult(Socket s, int ix, int iy) {
		server.dadanh = server.DFdadanh;
		server.bang  = server.DFbang;
		String result1 = "Bạn đã thắng!";
		String result2 = "Bạn đã thua";
		
		QuaTrinh qt1 = new QuaTrinh();
		qt1.ix = ix;
		qt1.iy = iy;
		qt1.result = result1;
		
		QuaTrinh qt2 = new QuaTrinh();
		qt2.ix = ix;
		qt2.iy = iy;
		qt2.result = result2;
		
		try {
			ObjectOutputStream oos1 = new ObjectOutputStream(server.s1.getOutputStream());
			ObjectOutputStream oos2 = new ObjectOutputStream(server.s2.getOutputStream());
 
			if (s == server.s1) {
				oos1.writeObject((Object)qt1); 
				oos1.flush();
				oos2.writeObject((Object)qt2);
				oos2.flush();
			} else {
				oos2.writeObject((Object)qt1);
				oos2.flush();
				oos1.writeObject((Object)qt2);
				oos1.flush(); 
			}
		} catch (Exception e) {
			
		}
		server.dadanh.removeAllElements();
		for (int k = 0; k < server.n; k ++) {
			for (int l = 0; l < server.n; l ++) {
				server.bang[k][l] = null;
			}
		}
	}
	
	public void run() {
		loop: while (true) {
			try {
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int ix = Integer.parseInt(dis.readUTF());
				int iy = Integer.parseInt(dis.readUTF());
				
				if (server.s1 == null || server.s2 == null) continue;
				
				if (!((s == server.s1 && server.dadanh.size() % 2 == 0) ||
						(s == server.s2 && server.dadanh.size() % 2 == 1))) continue;
				
				for (Point p : server.dadanh) {
					if (ix == p.x && iy == p.y) continue loop;
				}
				server.dadanh.add(new Point(ix, iy));
				
				if (s == server.s1) {
					server.bang[ix][iy] = "O";
				} else {
					server.bang[ix][iy] = "X";
				}
				
				int i;
				int count = 0;
				
				// kiem tra hang ngang
				
				if (s == server.s1) {
					for (i=0;i<server.n;i++) {
						if (server.bang[i][iy] == "O") {
							count++;
							if (count == 4) {
								System.out.print("Nguoi 1 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							count = 0;
						}
					}
				} else {
					for (i=0;i<server.n;i++) {
						if (server.bang[i][iy] == "X") {
							count++;
							if (count == 4) {
								System.out.print("Nguoi 2 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							count = 0;
						}
					}
				}
				
				// kiem tra hang doc
				count = 0;
				if (s == server.s1) {
					for (i=0;i<server.n;i++) {
						if (server.bang[ix][i] == "O") {
							count++;
							if (count == 4) {
								System.out.print("Nguoi 1 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							count = 0;
						}
					}
				} else {
					for (i=0;i<server.n;i++) {
						if (server.bang[ix][i] == "X") {
							count++;
							if (count == 4) {
								System.out.print("Nguoi 2 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							count = 0;
						}
					}
				}
				
				int cheox;
				int cheoy;
				
				// kiem tra cheo 1
				count = 1;
				
				if (s == server.s1) {
					cheox = ix-1;
					cheoy = iy-1;
					while (cheox >= 0 && cheoy >= 0) {
						if (server.bang[cheox][cheoy] == "O") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 1 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox--;
						cheoy--;
					}
					
					cheox = ix+1;
					cheoy = iy+1;
					
					while (cheox <=server.n-1 && cheoy <= server.n -1) {
						if (server.bang[cheox][cheoy] == "O") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 1 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox++;
						cheoy++;
					}
				} else {
					cheox = ix-1;
					cheoy = iy-1;
					while (cheox >= 0 && cheoy >= 0) {
						if (server.bang[cheox][cheoy] == "X") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 2 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox--;
						cheoy--;
					}
					
					cheox = ix+1;
					cheoy = iy+1;
					
					while (cheox <= server.n-1 && cheoy <= server.n -1) {
						if (server.bang[cheox][cheoy] == "X") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 2 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox++;
						cheoy++;
					}
				}
				
				
				//kiem tra cheo 2
				count = 1;
				if (s == server.s1) {
					cheox = ix+1;
					cheoy = iy-1;
					while (cheox <= server.n -1 && cheoy >= 0) {
						if (server.bang[cheox][cheoy] == "O") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 1 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox++;
						cheoy--;
					}
					
					cheox = ix-1;
					cheoy = iy+1;
					
					while (cheox >= 0  && cheoy <= server.n -1) {
						if (server.bang[cheox][cheoy] == "O") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 1 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox--;
						cheoy++;
					}
				} else {
					cheox = ix+1;
					cheoy = iy-1;
					while (cheox <= server.n -1 && cheoy >= 0) {
						if (server.bang[cheox][cheoy] == "X") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 2 thang");
								sendRessult(s, ix, iy);
								continue loop;
							}
						} else {
							break;
						}
						cheox++;
						cheoy--;
					}
					
					cheox = ix-1;
					cheoy = iy+1;
					
					while (cheox >= 0  && cheoy <= server.n -1) {
						if (server.bang[cheox][cheoy] == "X") {
							count ++;
							if (count == 4) {
								System.out.print("Nguoi 2 thang");
								sendRessult(s, ix, iy);
							}
						} else {
							break;
						}
						cheox--;
						cheoy++;
					}
				}
				

				
				QuaTrinh qt = new QuaTrinh();
				qt.ix = ix;
				qt.iy = iy;
				qt.result = null;
				
				ObjectOutputStream oos1 = new ObjectOutputStream(server.s1.getOutputStream());
				oos1.writeObject((Object)qt);
				oos1.flush();  
				
				ObjectOutputStream oos2 = new ObjectOutputStream(server.s2.getOutputStream());
				oos2.writeObject((Object)qt);
				oos2.flush();  
				
//				DataOutputStream dos1 = new DataOutputStream(server.s1.getOutputStream());
//				DataOutputStream dos2 = new DataOutputStream(server.s2.getOutputStream());
//				dos1.writeUTF(ix+"");
//				dos1.writeUTF(iy+"");
//				dos2.writeUTF(ix+"");
//				dos2.writeUTF(iy+"");
			} catch (Exception e) {
				System.out.print(e);
			}
		}
	}
}
