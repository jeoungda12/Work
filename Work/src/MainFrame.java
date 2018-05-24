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
    JList<String> list1 = new JList<>();
    JScrollPane sp = new JScrollPane();
    JScrollPane spl = new JScrollPane();
    JLabel L1 = new JLabel("File Manager");
    JPanel P1 = new JPanel(new BorderLayout());
    JPanel P2 = new JPanel(new BorderLayout());
    Vector<String> copyFile;
    String[] directoryName_List;

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
        spl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu PopMenu = new JPopupMenu();
                    if (comboBox1.getSelectedItem() == "한글") {
                        for (int i = 0; i < 4; i++) {
                            if (i == 1) PopMenu.addSeparator();
                            if (i == 1 || i == 3) continue;
                            PopMenu.add(Korea[i]);
                        }
                    } else {
                        for (int i = 0; i < 4; i++) {
                            if (i == 1) PopMenu.addSeparator();
                            if (i == 1 || i == 3) continue;
                            PopMenu.add(English[i]);
                        }
                    }
                    PopMenu.show(jt, e.getX(), e.getY());
                    PopMenu.setVisible(true);
                }
            }
        });

        jt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu PopMenu = new JPopupMenu();
                    if (comboBox1.getSelectedItem() == "한글") {
                        for (int i = 0; i < 4; i++) {
                            if (i == 1 || i == 3) PopMenu.addSeparator();
                            PopMenu.add(Korea[i]);
                        }
                    } else {
                        for (int i = 0; i < 4; i++) {
                            if (i == 1 || i == 3) PopMenu.addSeparator();
                            PopMenu.add(English[i]);
                        }
                    }
                    PopMenu.show(jt, e.getX(), e.getY());
                    PopMenu.setVisible(true);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Korea[3] || e.getSource() == English[3]) {
            int[] columns = jt.getSelectedRows();
            for (int column : columns) {
                System.out.println(column);
                System.out.println(path + File.separator + jt.getValueAt(column, 0));
            }
            for (int column : columns) {
               File delete = new File(path + File.separator + jt.getValueAt(column, 0));
               delete.delete();
           }
           for (int i = 0; i < columns.length; i++)
                model.removeRow(columns[i] - i);
            model.fireTableDataChanged();
            jt.updateUI();
        }

        if (e.getSource() ==Korea[0] || e.getSource() == English[0]) {
            File open_Directory = new File(path);
            try {
                Desktop.getDesktop().open(open_Directory);
            } catch (IOException ignored) {

            }
        }

        if (e.getSource() == Korea[1] || e.getSource() == English[1]) {
            Korea[2].setEnabled(true);
            English[2].setEnabled(true);
            copy = jt.getSelectedRows();
            copyFile = new Vector<>(jt.getRowCount());
            for (int i = 0; i < copy.length; i++) {
                copyFile.add(i, (path + "\\" + jt.getValueAt(copy[i], 0)));
            }
        }

        if (e.getSource() == Korea[2] || e.getSource() ==English[2]) {
            String tmp = path;
            int count = 0;
            for (int i = 0; i < copy.length; i++) {
                String command = "cmd /c copy \"" + copyFile.get(i) + "\"" + " \"" + tmp + "\" /y";
                try {
                    Process child = Runtime.getRuntime().exec(command);
                    InputStreamReader in = new InputStreamReader(child.getInputStream(), "MS949");
                    int c;
                    StringBuilder result;
                    result = new StringBuilder();
                    while ((c = in.read()) != -1) {
                        result.append((char) c);
                    }
                    if (result.toString().contains("0개 파일이 복사되었습니다.")) {
                        if (comboBox1.getSelectedItem() == "한글")
                            JOptionPane.showMessageDialog(null, "액세스 권한이 없습니다.", "에러", JOptionPane.ERROR_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(null, "You don't have access", "Error", JOptionPane.ERROR_MESSAGE);
                        in.close();
                        break;
                    }
                    in.close();
                    count++;
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            getJList();
            if (count == copy.length) {
                homeTextField.setText(path);
                getJList();
            }
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
}