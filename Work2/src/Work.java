import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.Choice;
import java.awt.Color;

public class Work extends JFrame{
	public Work() {
		//menu begin
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		JMenu scanMenu = new JMenu("Scan");
		JMenu gotoMenu = new JMenu("Go to");
		JMenu commandsMenu = new JMenu("Commands");
		JMenu favoritesMenu = new JMenu("Favorites");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu helpMenu = new JMenu("Help");

		menubar.add(scanMenu);
		menubar.add(gotoMenu);
		menubar.add(commandsMenu);
		menubar.add(favoritesMenu);
		menubar.add(toolsMenu);
		menubar.add(helpMenu);

		JMenuItem loadFromFilesAction = new JMenuItem("Load from file...");
		JMenuItem ExportAllAction = new JMenuItem("Export all...");
		JMenuItem ExportSelectionAction = new JMenuItem("Export selection...");
		JMenuItem QuitAction = new JMenuItem("Quit");

		scanMenu.add(loadFromFilesAction);
		scanMenu.add(ExportAllAction);
		scanMenu.add(ExportSelectionAction);
		scanMenu.addSeparator();
		scanMenu.add(QuitAction);

		JMenuItem NextAliveHostAction = new JMenuItem("Next alive host");
		JMenuItem NextOpenPortAction = new JMenuItem("Next open port");
		JMenuItem NextDeadHostAction = new JMenuItem("Next dead host");
		JMenuItem PreviousAliveHostFindAction = new JMenuItem("Previous alive host");
		JMenuItem PreviousOpenHostAction = new JMenuItem("Previous open host");
		JMenuItem PreviousDeadHostAction = new JMenuItem("Previous dead host");
		JMenuItem FindAction = new JMenuItem("Find...");

		gotoMenu.add(NextAliveHostAction);
		gotoMenu.add(NextOpenPortAction);
		gotoMenu.add(NextDeadHostAction);
		gotoMenu.addSeparator();
		gotoMenu.add(PreviousAliveHostFindAction);
		gotoMenu.add(PreviousOpenHostAction);
		gotoMenu.add(PreviousDeadHostAction);
		gotoMenu.addSeparator();
		gotoMenu.add(FindAction);

		JMenuItem ShowAction = new JMenuItem("Show");
		JMenuItem RescanIPAction = new JMenuItem("Rescan IP(s)");
		JMenuItem DeleteIPAction = new JMenuItem("Delete IP(s)");
		JMenuItem CopyIPAction = new JMenuItem("Copy Ip");
		JMenuItem CopyDetailsAction = new JMenuItem("Copy details");
		JMenuItem OpenAction = new JMenuItem("Open");

		commandsMenu.add(ShowAction);
		commandsMenu.addSeparator();
		commandsMenu.add(RescanIPAction);
		commandsMenu.add(DeleteIPAction);
		commandsMenu.addSeparator();
		commandsMenu.add(CopyIPAction);
		commandsMenu.add(CopyDetailsAction);
		commandsMenu.addSeparator();
		commandsMenu.add(OpenAction);

		JMenuItem AddCurrentAction = new JMenuItem("Add current...");
		JMenuItem ManageFaoritesAction = new JMenuItem("Manage facorites...");

		favoritesMenu.add(AddCurrentAction);
		favoritesMenu.add(ManageFaoritesAction);
		favoritesMenu.addSeparator();

		JMenuItem PreferencesAction = new JMenuItem("Preferences...");
		JMenuItem FetchersAction = new JMenuItem("Fetchers...");
		JMenuItem SelectionAction = new JMenuItem("Selecion");
		JMenuItem ScanStatisticsAction = new JMenuItem("Scan statistics");

		toolsMenu.add(PreferencesAction);
		toolsMenu.add(FetchersAction);
		toolsMenu.addSeparator();
		toolsMenu.add(SelectionAction);
		toolsMenu.add(ScanStatisticsAction);

		JMenuItem GettingStartedAction = new JMenuItem("Getting Started");
		JMenuItem OfficialWebsiteAction = new JMenuItem("Official Website");
		JMenuItem FAQAction = new JMenuItem("FAQ");
		JMenuItem ReportAnIssueAction = new JMenuItem("report an issue");
		JMenuItem PluginsAction = new JMenuItem("Plugins");
		JMenuItem CommandLineUsageAction = new JMenuItem("Command-line usage");
		JMenuItem CheckForNewerVersionAction = new JMenuItem("Check for newer version...");
		JMenuItem AboutAction = new JMenuItem("About");

		helpMenu.add(GettingStartedAction);
		helpMenu.addSeparator();
		helpMenu.add(OfficialWebsiteAction);
		helpMenu.add(FAQAction);
		helpMenu.add(ReportAnIssueAction);
		helpMenu.add(PluginsAction);
		helpMenu.addSeparator();
		helpMenu.add(CommandLineUsageAction);
		helpMenu.addSeparator();
		helpMenu.add(CheckForNewerVersionAction);
		helpMenu.addSeparator();
		helpMenu.add(AboutAction);

		QuitAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

		//menu end

		//status bar begin
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		JLabel readyLable = new JLabel("Ready");
		readyLable.setPreferredSize(new Dimension(200,17));
		readyLable.setBorder(new BevelBorder(BevelBorder.LOWERED));
		JLabel displayLable = new JLabel("Display:All");
		displayLable.setPreferredSize(new Dimension(200,17));
		displayLable.setBorder(new BevelBorder(BevelBorder.LOWERED));
		JLabel threadLabel = new JLabel("Thread:0");
		threadLabel.setPreferredSize(new Dimension(200,17));
		threadLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.add(readyLable);
		statusPanel.add(displayLable);
		statusPanel.add(threadLabel);

		//status bar end

		//table begin
		String[] titles = new String[] {
				"IP","Ping","TTl","Hostname","Ports[4+]"	
		};
		Object[][] stats = initTalbe();
		JTable jTable = new JTable(stats,titles);
		JScrollPane sp = new JScrollPane(jTable);
		getContentPane().add(sp,BorderLayout.CENTER);

		//table end

		//toolbar begin
		String myIP = null;
		String myHostname = null;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			myIP = ia.getHostAddress();
			myHostname = ia.getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		Font myFont=new Font("Serif",Font.BOLD,16);
		JToolBar toolbar1=new JToolBar();
		toolbar1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JToolBar toolbar2=new JToolBar();
		toolbar2.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel IPrangeLable=new JLabel("IP Range: ");
		JTextField tfRangeStart=new JTextField(10);
		JLabel ragneEndLabel=new JLabel("to: ");
		JTextField tfRangend=new JTextField(10);

		IPrangeLable.setFont(myFont);
		IPrangeLable.setPreferredSize(new Dimension(90, 30));
		ragneEndLabel.setFont(myFont);
		ragneEndLabel.setPreferredSize(new Dimension(90, 30));

		toolbar1.add(IPrangeLable);
		toolbar1.add(tfRangeStart);

		JLabel lblTo = new JLabel("to");
		toolbar1.add(lblTo);
		toolbar1.add(tfRangend);
		toolbar1.add(tfRangend);

		JLabel hostNameabel = new JLabel("Hostanme:");
		JTextField tfHostname=new JTextField(10);
		JButton upButton=new JButton("IP");
		JComboBox optionComboBox=new JComboBox();
		optionComboBox.addItem("/24");
		optionComboBox.addItem("/26");

		hostNameabel.setFont(myFont);
		tfHostname.setPreferredSize(new Dimension(90,30));
		upButton.setPreferredSize(new Dimension(50,30));
		optionComboBox.setPreferredSize(new Dimension(90,30));

		toolbar2.add(hostNameabel);
		toolbar2.add(tfHostname);
		toolbar2.add(upButton);
		toolbar2.add(optionComboBox);

		JPanel pane=new JPanel(new BorderLayout());
		pane.add(toolbar1, BorderLayout.NORTH);

		JComboBox IPRangeCB = new JComboBox();
		IPRangeCB.setModel(new DefaultComboBoxModel(new String[] {"IP Range"}));
		IPRangeCB.setToolTipText("           ");
		toolbar1.add(IPRangeCB);
		pane.add(toolbar2, BorderLayout.SOUTH);

		JButton startButton = new JButton("▶Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		toolbar2.add(startButton);

		getContentPane().add(pane, BorderLayout.NORTH);
		String fixedIP=myIP.substring(0, myIP.lastIndexOf(".")+1);
		tfRangeStart.setText(fixedIP+"1");
		tfRangend.setText(fixedIP+"254");
		tfHostname.setText(myHostname);
		//toolabr end
		setSize(700,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startButton.setText("■Stop");
				pinging[] pg = new pinging[254];
				//add
				for(int i=0; i<=253; i++) {
					pg[i] = new pinging(fixedIP + (i+1));
					pg[i].start();
				}
				for(int i=0; i<=253;i++) {
					Object[] msg = pg[i].getMsg();
					stats[i][0] = msg[0];
					if(msg[1] != null) {
						stats[i][1] = msg[1];
					} else {
						stats[i][1] = "[n/s]";
					}
					if(msg[2] != null) {
						stats[i][2] = msg[2];
					} else {
						stats[i][2] = "[n/s]";
					}
					if(msg[2] != null) {
						stats[i][3] = msg[3];
					} else {
						stats[i][3] = "[n/s]";
					}
					//msg[1] != null || msg[2] != null || msg[3] != null
					//portscan.
					//scan value == null -> stats[i][4] = [n/s]
					//scan value != null -> assgin valuet stats[i][4]
					jTable.repaint();
				}
			}
		});

	}
		public Object[][] initTalbe() {
			Object[][] result = new Object[255][5];
			return result;
		}

		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Work op = new Work();

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