import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Mainclass implements ActionListener,KeyListener,WindowListener{
    JFrame frame;
    JScrollPane scoll;
    JTextArea area;
    Thread runthread;
    Thread consolethread;
    JPanel northpanel;
    JPanel panel;
    JLabel label;
    JButton button;
    static int flag=0;
    JButton button2;
    consoledialog dia;
    static JButton button4[];
    String str3;
    JButton button3;
    JPanel southpanel;
	public  void mainclass() {
		// TODO Auto-generated constructor stub
		frame=new JFrame("java编辑器");
		frame.addWindowListener(this);
	    dia=new consoledialog();
		button4=new JButton[100];
		frame.setLayout(new BorderLayout());
		northpanel=new JPanel();
		label=new JLabel();
		label.setText("小型java编译器");
		northpanel.add(label);
		area=new JTextArea();
		area.addKeyListener(this);
		scoll=new JScrollPane(area);
		button=new JButton("编译");
		button.addActionListener(this);
		button2=new JButton("运行");
		button2.addActionListener(this);
		button3=new JButton("新建文件");
		button3.addActionListener(this);
		northpanel.add(button);
		northpanel.add(button2);
		northpanel.add(button3);
		frame.add(BorderLayout.NORTH,northpanel);
		frame.add(BorderLayout.CENTER,scoll);
		frame.setBounds(200,0,500,500);
		frame.setVisible(true); 
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==button3)
		{
			String str=JOptionPane.showInputDialog(null, "请输入java文件名", "文件建立窗口", JOptionPane.PLAIN_MESSAGE);
			System.out.println(str+"shfslf");
			if(str==null)
			{
				str="s";
			}
			if(str.equals("")||str.equals("s"))
			{
				if(str.equals("s"))
				{
					JOptionPane.showMessageDialog(null, "运行java程序需要先建立源文件");
				}
				else
				JOptionPane.showMessageDialog(null, "请输入文件名或者文件名输入错误");
			}
			else
			{
				area.setText("public class"+" "+str+"\n"+"{"+"\n"+"}");
				String str2=str+".txt";
				str3=str2;
				File file=new File(str2);
				if(!file.exists())
				{
				   try {
					file.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
				else
				{
				    for(int i=0;i<flag;i++)
				    {
				    	if(button4[i].getText().equals(str2.substring(0,str2.indexOf("."))+".java"))
				    	{
				    		northpanel.remove(button4[i]);
				    		northpanel.validate();
				    	}
				    }
					file.delete();
					try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				button4[flag]=new JButton(str2.substring(0,str2.indexOf("."))+".java");
				button4[flag].addActionListener(this);
				//JLabel label=new JLabel(str2.substring(0,str2.indexOf("."))+".java");
				northpanel.add(button4[flag]);
				flag++;
				frame.validate();
			}
		}
		if(e.getSource()==button)
		{
			consolethread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 dia.consolearea.setText("");
					 File file=new File(str3);
					 BufferedWriter bos=null;
					 try {
						bos=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
						String str4=area.getText();
						String[] temp=str4.split("[\\r\\n]");
						System.out.println(temp[0]);
						for (int i = 0; i < temp.length; i++) {
							bos.write(temp[i]);
							bos.write("\r\n");
							}
						bos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}
					  file.renameTo(new File(str3.substring(0,str3.indexOf("."))+".java"));
					  str3=str3.substring(0,str3.indexOf("."))+".java";
					  Runtime ce=Runtime.getRuntime();
					  try {
						Process process=ce.exec("javac"+" "+str3.substring(0,str3.indexOf("."))+".java");
						InputStream in=process.getErrorStream();
						BufferedInputStream bin=new BufferedInputStream(in);
						int n;
						boolean bn=true;
						byte error[]=new byte[100];
						while((n=in.read(error,0,100))!=-1)
						{
							String s=null;
							s=new String(error, 0, n);
							dia.consolearea.append(s);
							if(s!=null)
								bn=false;
						}
						if(bn)
						{
							dia.consolearea.append("编译正确");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
				}
			}) ;
		    
			  consolethread.start();
			 
		}
		if(e.getSource()==button2)
		{
			runthread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dia.consolearea.setText("");
					Runtime ce=Runtime.getRuntime();
					try {
						Process process=ce.exec("java"+" "+str3.substring(0,str3.indexOf(".")));
						InputStream in=process.getInputStream();
						BufferedInputStream bin=new BufferedInputStream(in);
						int n;
						boolean bn=true;
						byte mess[]=new byte[100];
						while((n=bin.read(mess,0,100))!=-1)
						{
							String s=null;
							s=new String(mess, 0, n);
							dia.consolearea.append(s);
							if(s!=null)
							{
								bn=false;
							}
							if(bn)
							{
								dia.consolearea.append("无输入");
							}
						}
						System.out.println("运行成功");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}
					dia.validate();
				}
			});
			runthread.start();
		}
		for(int i=0;i<flag;i++)
		{
			if(e.getSource()==button4[i])
			{
				str3=button4[i].getText();
				File file=new File(button4[i].getText());
				file.renameTo(new File(button4[i].getText().substring(0, button4[i].getText().indexOf('.'))+".txt"));
				File file3=new File(button4[i].getText().substring(0, button4[i].getText().indexOf('.'))+".txt");
				try {
					BufferedReader buff=new BufferedReader(new FileReader(file3));
					String n;
					String s="";
					while((n=buff.readLine())!=null)
					{
						s=s+n+"\n";
					}
					area.setText(s);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_OPEN_BRACKET&&e.isShiftDown())
		{
			/*System.out.println("sfssf"+area.getCaretPosition());
			area.append("\n\n"+"}");
			area.setCaretPosition(area.getCaretPosition()-2);
			System.out.println("sfs"+area.getCaretPosition());*/
		}
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		 int i = JOptionPane.showConfirmDialog(null, "确定关闭编辑器", "系统提示",
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				        if(i == JOptionPane.YES_OPTION)
				        {
				        	System.out.println(Mainclass.flag);
				        	for(int j=0;j<Mainclass.flag;j++)
				        	{
				        		File file3=new File(Mainclass.button4[j].getText().substring(0, Mainclass.button4[j].getText().indexOf('.'))+".txt");
				        		if(file3.exists())
				        		{
				        			file3.exists();
				        		}
				        		File file=new File(Mainclass.button4[j].getText());
				        		if(file.exists())
				        		{
				        			file.delete();
				        		}
				        		File file2=new File(Mainclass.button4[j].getText().substring(0, Mainclass.button4[j].getText().indexOf('.'))+".class");
				        		if(file2.exists())
				        		{
				        			file2.delete();
				        		}
				        	}
				        	System.exit(0);
				        }
				        	
				        else
				        	return;
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}  
