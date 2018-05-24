import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.datatransfer.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MainFrame implements ActionListener, ClipboardOwner {
	JTable jt = new JTable();
	JList<String> list1 = new JList<>();
    JScrollPane sp = new JScrollPane();
    JScrollPane spl = new JScrollPane();
    JLabel L1 = new JLabel("File Manager");
    JPanel P1 = new JPanel(new BorderLayout());
    JPanel P2 = new JPanel(new BorderLayout());
    Vector<String> copyFile;
    String[] directoryName_List;
    String[] asd = {"한글", "English"};
    File name;
    File getBack;
    String path = "C:\\";
    JPanel pa = new JPanel(new BorderLayout());
    JTextField homeTextField = new JTextField("C:\\");
    String[][] data;
    JMenuItem[] Korea = new JMenuItem[6];
    JMenuItem[] English = new JMenuItem[6];
    DefaultTableModel model;
    int[] copy;
    String[] Kr = {"이름", "크기", "수정한 날짜"};
    String[] En = {"Name", "Size", "Modified"};
    JComboBox comboBox1 = new JComboBox(asd);

    public MainFrame() {
    	
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        English[0] = new JMenuItem("Open file");
        English[1] = new JMenuItem("Copy");
        English[2] = new JMenuItem("Paste");
        English[3] = new JMenuItem("Delete");
        Korea[0] = new JMenuItem("열어 보기");
        Korea[1] = new JMenuItem("복사");
        Korea[2] = new JMenuItem("붙여넣기");
        Korea[3] = new JMenuItem("삭제");
   
        getJList();

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String clicked;
                    getBack = new File(path, "..");
                    clicked = list1.getSelectedValue();
                    if (clicked.equals("..")) {
                        try {
                            path = getBack.getCanonicalPath();
                        } catch (Exception ignored) {

                        }
                        homeTextField.setText(path);
                        getJList();
                    } else {
                        path = name.getPath() + File.separator + clicked;
                        if (path.contains("C:\\\\"))
                            path = name.getPath() + clicked;
                        homeTextField.setText(path);
                        getJList();
                    }
                } catch (NullPointerException aee) {
                
                }
            }
        });
        
        for (int i = 0; i < 4; i++) {
            English[i].addActionListener(this);
            Korea[i].addActionListener(this);
        }
        pa.add(homeTextField, BorderLayout.NORTH);
        pa.add(P1, BorderLayout.SOUTH);
        pa.add(P2, BorderLayout.CENTER);
        sp.setViewportView(list1);;
        spl.setViewportView(jt);
        P1.add(L1,BorderLayout.WEST);
        P1.add(comboBox1,BorderLayout.EAST);
        P2.add(spl, BorderLayout.CENTER);
        P2.add(sp,BorderLayout.WEST);

       
        f.setSize(750, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(pa);
        f.setVisible(true);
        
        comboBox1.addActionListener(e -> setTable());
    }

    @Override
    public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
        
    	
    }
    private void setTable() {
        if (comboBox1.getSelectedItem() == "한글") {
            model = new DefaultTableModel(data, Kr);
            jt.setModel(model);
            L1.setText("파일 매니져");
        }
        if (comboBox1.getSelectedItem() == "English") {
            model = new DefaultTableModel(data, En);
            jt.setModel(model);
            L1.setText("File Manager");
        }
    }

   

    private void getJList() {

        File[] directory_list;
        File[] file_list;
        name = new File(path);

        directory_list = name.listFiles(File::isDirectory);
        file_list = name.listFiles(File::isFile);

      
        directoryName_List = new String[0];
        if (directory_list != null) {
            directoryName_List = new String[directory_list.length + 1];
            for (int i = -1; i < directory_list.length; i++) {
                String back = "..";
                if (i == -1) directoryName_List[0] = back;
                else {
                    if (directory_list[i].getName().contains("$") ||
                            directory_list[i].getName().contains("Recovery") ||
                            directory_list[i].getName().contains("System") ||
                            directory_list[i].getName().contains("Temp") ||
                            directory_list[i].getName().contains("PerfLogs") ||
                            directory_list[i].getName().contains("Documents and Settings") ||
                            !directory_list[i].canRead()) continue;

                    directoryName_List[i + 1] = directory_list[i].getName();
                }
            }
        }
        list1.setListData(directoryName_List);
        if (list1.getVisibleRowCount() != 0)
            data = new String[0][3];
        if (file_list != null) {
            {
                data = new String[file_list.length][3];
                for (int i = 0; i < file_list.length; i++) {
                    data[i][0] = file_list[i].getName();
                    String file_size;
                    long size = file_list[i].length();
                    if (size < 1024) {
                        file_size = String.format("%d B", size);
                    } else if (size < 1024 * 1024) {
                        file_size = String.format("%.2f KB", size / 1024.0);
                    } else if (size < 1024 * 1024 * 1024) {
                        file_size = String.format("%.2f MB", size / 1048576.0);
                    } else {
                        file_size = String.format("%.2f GB", size / 1073741824.0);
                    }
                    data[i][1] = file_size; 
                    Date dt = new Date(file_list[i].lastModified());
                    SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy HH:mm:ss");
                    String date = formatter.format(dt);
                    data[i][2] = String.valueOf(date);
                }
            }

            setTable();
        } else {
            if (comboBox1.getSelectedItem() == "한글")
                JOptionPane.showMessageDialog(null, " ", "에러", JOptionPane.ERROR_MESSAGE);
            else JOptionPane.showMessageDialog(null, " ", "Error", JOptionPane.ERROR_MESSAGE);
            getBack = new File(path, "..");
            try {
                path = getBack.getCanonicalPath();
            } catch (Exception ignored) {

            }
            homeTextField.setText(path);
            getJList();

        }
    }


    public static void main(String[] args) {
        new MainFrame();
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}