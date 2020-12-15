import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class Client extends JFrame implements MouseListener, Runnable{
	
	public static void main(String[] args) {
		new Client("Nguời chơi");
	}
		
	int n = 15,s = 30,m = 50;
	Vector<Point> DFdadanh = new Vector<>();
	Vector<Point> dadanh = new Vector<>();
	Socket soc;
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		for (int i = 0; i<=n; i++) {
			g.drawLine(m, m+i*s, m+n*s, m+i*s);
			g.drawLine(m+i*s, m, m+i*s, m+n*s);
		}
		g.setFont(new Font("arial", Font.BOLD, s));
		for (int i = 0; i<dadanh.size(); i++) {
			g.setColor(Color.RED);
			String st ="o";
			if (i%2 != 0) {
				g.setColor(Color.BLUE);
				st ="x";
			}

			int x = m+dadanh.get(i).x*s + s/5;
			int y = m+(dadanh.get(i).y + 1)*s - s/5;
			g.drawString(st, x, y);
		}
	}
	
	public Client(String name) {
		this.setTitle(name);
		this.setSize(m*2+n*s, m*2+n*s);
		this.setDefaultCloseOperation(3);
		this.addMouseListener(this);
		setResizable(false);
		
		try {
				soc = new Socket("localhost", 5000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		new Thread(this).start();	
		
		 this.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (x<m || x>=m+n*s) return;
		if (y<m || y>=m+n*s) return;
		int ix =(x-m)/s;
		int iy =(y-m)/s;
		try {
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			dos.writeUTF(ix+"");
			dos.writeUTF(iy+"");
		} catch (Exception e2) {
			// TODO: handle exception
		}

	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void run() {

		while (true) {
			try {
//				DataInputStream dis = new DataInputStream(soc.getInputStream());
//				int ix = Integer.parseInt(dis.readUTF());
//				int iy = Integer.parseInt(dis.readUTF());
//				dadanh.add(new Point(ix, iy));
				ObjectInputStream ois = new ObjectInputStream(soc.getInputStream()); 
				QuaTrinh qt = (QuaTrinh)ois.readObject();
				dadanh.add(new Point(qt.ix, qt.iy));
				System.out.println(qt.result);				
				repaint();
				
				if (qt.result != null) {
					JLabel jl = new JLabel(qt.result, SwingConstants.CENTER);
					jl.setFont(new Font("Serif", Font.BOLD, 20));
					dadanh.removeAllElements();
					this.add(jl);
					this.setVisible(true);
					Thread.sleep(3000);
					this.remove(jl);
					repaint();
				}

			} catch (Exception e) {
				System.out.print(e);
			}
		}
		
	}
}
