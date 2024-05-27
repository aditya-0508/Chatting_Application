package chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Server implements ActionListener {
	JTextField text;
	JPanel a1;
	static Box vertical=Box.createVerticalBox();//as I want msgs to come one below after the anothers
	static JFrame f=new JFrame();//made static so that no error comes in validate part ..
	static DataOutputStream dout;
	  
	Server(){
		f.setLayout(null);
		
		JPanel p1=new JPanel();
		p1.setBackground(new Color(7,84,84));
		p1.setBounds(0, 0, 450, 70);
		p1.setLayout(null);
		f.add(p1);
		
		ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("icon/3.png"));
		Image i2=i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
		ImageIcon i3=new ImageIcon(i2);
		JLabel back=new JLabel(i3);
		back.setBounds(5, 20, 25, 25);
		p1.add(back);
		
		back.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ae) {
				System.exit(0);
			}
		});
		
		ImageIcon i4=new ImageIcon(ClassLoader.getSystemResource("icon/1.png"));
		Image i5=i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		ImageIcon i6=new ImageIcon(i5);
		JLabel profile=new JLabel(i6);
		profile.setBounds(40, 10, 50, 50);
		p1.add(profile);
		
		ImageIcon i7=new ImageIcon(ClassLoader.getSystemResource("icon/video.png"));
		Image i8=i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		ImageIcon i9=new ImageIcon(i8);
		JLabel video=new JLabel(i9);
		video.setBounds(300, 20, 30, 30);
		p1.add(video);
		
		ImageIcon i10=new ImageIcon(ClassLoader.getSystemResource("icon/phone.png"));
		Image i11=i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
		ImageIcon i12=new ImageIcon(i11);
		JLabel phone=new JLabel(i12);
		phone.setBounds(360, 20, 35, 30);
		p1.add(phone);
		
		ImageIcon i13=new ImageIcon(ClassLoader.getSystemResource("icon/3icon.png"));
		Image i14=i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
		ImageIcon i15=new ImageIcon(i14);
		JLabel morevert=new JLabel(i15);
		morevert.setBounds(420, 20, 10, 25);
		p1.add(morevert);
		
		JLabel name=new JLabel("Gabbar");
		name.setBounds(110,15,100,18);
		name.setForeground(Color.WHITE);
		name.setFont(new Font("Raleway",Font.BOLD,19));
		p1.add(name);
		
		JLabel status=new JLabel("Online");
		status.setBounds(110,35,100,18);
		status.setForeground(Color.WHITE);
		status.setFont(new Font("Raleway",Font.BOLD,14));
		p1.add(status);
		
		a1=new JPanel();
		a1.setBounds(5, 74, 425, 570);
		f.add(a1);
		
		text=new JTextField();
		text.setBounds(5, 655, 310, 40);
		text.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
		f.add(text);
		
		JButton send=new JButton("Send");
		send.setBounds(320, 655, 123, 40);
		send.setBackground(new Color(7,84,84));
		send.setForeground(Color.WHITE);
		send.addActionListener(this);
		text.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
		f.add(send);
		
		f.setSize(450,700);
		f.setLocation(200,50);
		f.setUndecorated(true);
		f.getContentPane().setBackground(Color.WHITE);
		
		f.setVisible(true);
		
	}
	public void actionPerformed(ActionEvent ae) {
		try {
			String out=text.getText();
			//as out is a string can't add the out directly with the right panel 
			//then adding the the output to a panel
			JPanel p2=formatLabel(out);
		
			a1.setLayout(new BorderLayout());//will set the layout top,bottom,left and right
	
			dout.writeUTF(out);//to send msgs 
		
			JPanel right=new JPanel(new BorderLayout());//as I am the sender the msgs should be in right side
			right.add(p2,BorderLayout.LINE_END);
			vertical.add(right);
			vertical.add(Box.createVerticalStrut(12));//creates the strut of each msgs
		
			a1.add(vertical,BorderLayout.PAGE_START);
			text.setText("");//will empty that frame once all msgs have been shared
		
			f.repaint();//these three are there to repaint the frame as the frame needs to be reloaded again so that the msg can be loaded after the another
			f.invalidate();
			f.validate();
		}catch(Exception e ) {
			e.printStackTrace();
		}
	}
	public static JPanel formatLabel(String out) {
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		//adding this html so that a particular width can be maintained
		JLabel output=new JLabel("<html><p style=\"width: 150px\">"+out+"</p></html>");
		output.setFont(new Font("Tahoma",Font.PLAIN,16));
		output.setBackground(new Color(37,211,102));
		output.setOpaque(true);
		output.setBorder(new EmptyBorder(15,15,15,50));//for putting in padding
		
		panel.add(output);
		
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
		
		JLabel time=new JLabel();
		time.setText(sdf.format(cal.getTime()));
		panel.add(time);
		
		return panel;
	}
	
	public static void main(String[] args) {
		new Server();

		
		try {
			ServerSocket skt=new ServerSocket(6001);//on which port the server will run
			while(true) {//to accept in all messages
				Socket s=skt.accept();
				DataInputStream din=new DataInputStream(s.getInputStream());//to receive the messages
				dout=new DataOutputStream(s.getOutputStream());
				
				while(true) {
					String msg=din.readUTF();
					JPanel panel=formatLabel(msg);
					
					JPanel left=new JPanel(new BorderLayout());
					left.add(panel,BorderLayout.LINE_START);  
					vertical.add(left);
					f.validate();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
