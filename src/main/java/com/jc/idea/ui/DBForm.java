package com.jc.idea.ui;



import com.jc.idea.config.DatabaseBean;
import com.jc.idea.db.DataSource;
import com.jc.idea.db.DataSourceConfiguration;
import com.jc.idea.db.DataSourceException;
import com.jc.idea.db.DefaultDataSource;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;

public class DBForm extends JPanel {

    private int margin = 10;


    private DatabaseBeanListModel listData = new DatabaseBeanListModel();

    private JTextField dbNameText;
    private JTextField hostNameText;
    private JTextField userNameText;
    private JPasswordField passwordField;
    private JTextField schemaText;


    private TableList tableList;

    public TableList getTableList() {
        return tableList;
    }

    public void setTableList(TableList tableList) {
        this.tableList = tableList;
    }

    public DBForm() {
        this.setLayout(null);

        /**左边panel**/
        JPanel left = combineLeft();
        this.add(left);
        /**左边panel**/


        /**右边panel**/
        JPanel right = combineRight();
        this.add(right);
        /**右边panel**/


        /**底边panel**/
        JPanel bottom = combineBottom();
        this.add(bottom);
        /**底边panel**/

    }

    /**
     * {
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {
        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建 JFrame 实例
        JFrame frame = new JFrame("Database Info Manager");
        // Setting the width and height of frame
        frame.setSize(900, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 创建面板，这个类似于 HTML 的 div 标签
         * 我们可以创建多个面板并在 JFrame 中指定位置
         * 面板中我们可以添加文本字段，按钮及其他组件。
         */
        JPanel panel = new DBForm();


        // 添加面板
        frame.add(panel);
        /*
         * 调用用户定义的方法并添加组件到面板
         */
//        placeComponents(panel);

        // 设置界面可见
        frame.setVisible(true);
    }

    public JPanel getThis(){
        return this;
    }

    private JPanel combineBottom() {
        int bottomPanelWith = 860;
        int bottomPanelHeight = 50;
        JPanel bottom = new JPanel();
        bottom.setLayout(null);

        JButton projectInfo = new JButton("Project Info");
//        button.setMinimumSize(new Dimension(30,20));
        projectInfo.setBounds(margin, margin, 90, 30);
        projectInfo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 创建 JFrame 实例
                JFrame frame = new JFrame("Project Info Manager");
                // Setting the width and height of frame
                frame.setSize(900, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                JPanel panel = new ProjectInfoPanel(project);
//                frame.add(panel);
//                frame.setVisible(true);
            }
        });
        bottom.add(projectInfo);


        JButton next = new JButton("next");
//        button.setMinimumSize(new Dimension(30,20));
        next.setBounds(650-180-margin-90-margin, margin, 90, 30);
        next.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseBean databaseBean = getDatabaseBean();
                if(databaseBean.isInvalid()){
                    JOptionPane.showMessageDialog(null, databaseBean.getNullField()+" not null.","Error",JOptionPane.ERROR_MESSAGE);
                    return ;
                }
                JPanel parent = getThis();
                TableList t = new TableList(parent);
                setTableList(t);
                t.paint(databaseBean);
            }
        });
        bottom.add(next);


        JButton button = new JButton("add");
//        button.setMinimumSize(new Dimension(30,20));
        button.setBounds(650-180-margin, margin, 90, 30);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseBean databaseBean = getDatabaseBean();
                    if (listData.containDbName(databaseBean.getDatabaseName())) {
                        JOptionPane.showMessageDialog(null, "database name exists..","Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    listData.addElement(databaseBean);
                }catch (Exception el){
                    JOptionPane.showMessageDialog(null, el.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        bottom.add(button);


        JButton testConn = new JButton("Test Connection");
//        button.setMinimumSize(new Dimension(30,20));
        testConn.setBounds(650-90, margin, 180, 30);
        testConn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DatabaseBean databaseBean = getDatabaseBean();
                if(databaseBean.isInvalid()){
                    JOptionPane.showMessageDialog(null, databaseBean.getNullField()+" not null.","Error",JOptionPane.ERROR_MESSAGE);
                    return ;
                }

                String url = "jdbc:mysql://"+databaseBean.getHostname()+":"+databaseBean.getPort();
                String schema = databaseBean.getSchema();
                if(StringUtils.isNoneBlank(schema)){
                    url= url + "/"+schema;
                }
                String username = databaseBean.getUsername();
                String password = new String(databaseBean.getPassword());

                DataSourceConfiguration dataSourceConfiguration = new DataSourceConfiguration(url,username,password);

                DataSource dataSource = new DefaultDataSource(dataSourceConfiguration);
                try(Connection connection = dataSource.getConnection()){
                    if(connection!=null){
                        JOptionPane.showMessageDialog(null, "Connection success.","Success",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Connection fail.");
                    }

                }catch (Exception dbe){
                    JOptionPane.showMessageDialog(null, "Connection fail."+dbe.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    throw new DataSourceException(dbe);
                }

            }
        });
        bottom.add(testConn);

        JButton cancel = new JButton("cancel");
//        button.setMinimumSize(new Dimension(30,20));
        cancel.setBounds(650 + margin + 90, margin, 90, 30);
        cancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        bottom.add(cancel);

        bottom.setBounds(0, 504 + margin, bottomPanelWith, bottomPanelHeight);
        bottom.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        return bottom;
    }

    @Nullable
    private DatabaseBean getDatabaseBean() {
        String dbName = dbNameText.getText();
        String hostName = hostNameText.getText();
        String username = userNameText.getText();
        String password = new String(passwordField.getPassword());
        String schema = schemaText.getText();
        DatabaseBean databaseBean = new DatabaseBean();
        databaseBean.setDatabaseName(dbName);
        databaseBean.setHostname(hostName);
        databaseBean.setUsername(username);
        databaseBean.setPassword(password);
        databaseBean.setPort(3306);
        databaseBean.setSchema(schema);
        return databaseBean;
    }

    private JPanel combineRight() {
        int rightPanelWith = 670;
        int rightPanelHeight = 504;
        int textFieldHeight = 25;
        JPanel right = new JPanel();
        right.setLayout(null);
        right.setSize(rightPanelWith, rightPanelHeight);
        // 创建 DataBase Name输入框
        int textFieldCount = 1;
        JLabel dbNameLabel = new JLabel("DataBase Name:");
        dbNameLabel.setBounds(margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 100, textFieldHeight);
        dbNameText = new JTextField(20);
        dbNameText.setText("test");                            //TODO for Test
        dbNameText.setBounds(150 + margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 400, textFieldHeight);
        right.add(dbNameLabel);
        right.add(dbNameText);
        textFieldCount++;


        // 创建 Hostname Name输入框
        JLabel hostNameLabel = new JLabel("Hostname:");
        hostNameLabel.setBounds(margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 100, textFieldHeight);
        hostNameText = new JTextField(20);
        hostNameText.setText("192.168.31.190");                            //TODO for Test
        hostNameText.setBounds(150 + margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 400, textFieldHeight);
        right.add(hostNameLabel);
        right.add(hostNameText);
        textFieldCount++;

        // 创建 Username Name输入框
        JLabel userNameLabel = new JLabel("Username:");
        userNameLabel.setBounds(margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 100, textFieldHeight);
        userNameText = new JTextField(20);
        userNameText.setText("test");                            //TODO for Test
        userNameText.setBounds(150 + margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 400, textFieldHeight);
        right.add(userNameLabel);
        right.add(userNameText);
        textFieldCount++;


        // 创建 Password Name输入框
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 100, textFieldHeight);
        passwordField = new JPasswordField(20);
        passwordField.setText("test");                            //TODO for Test
        passwordField.setBounds(150 + margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 400, textFieldHeight);
        right.add(passwordLabel);
        right.add(passwordField);
        textFieldCount++;


        // 创建 Schema Name输入框
        JLabel schemaLabel = new JLabel("Schema:");
        schemaLabel.setBounds(margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 100, textFieldHeight);
        schemaText = new JTextField(20);
        schemaText.setText("test");                            //TODO for Test
        schemaText.setBounds(150 + margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 400, textFieldHeight);
        right.add(schemaLabel);
        right.add(schemaText);
        textFieldCount++;


        right.setBounds(190, 0, rightPanelWith, rightPanelHeight);
        right.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        return right;
    }

    private JPanel combineLeft() {
        int leftPanelWith = 180;
        int leftPanelHeight = 504;
        JPanel left = new JPanel();
        left.setLayout(null);

        JLabel listLabel = new JLabel("DataBase List");
        int listLabelHeight = 20;
        listLabel.setBounds(margin, margin, leftPanelWith - margin, listLabelHeight);
        left.add(listLabel);

        JList<String> wordList = new JList<>(listData);
        JScrollPane scrollPane = new JScrollPane(wordList);
        wordList.setVisibleRowCount(4); // display 4 items
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordList.setBounds(margin, 30, leftPanelWith - margin - margin, leftPanelHeight - listLabelHeight - margin - margin);
        wordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = e.getFirstIndex();
                DatabaseBean databaseBean = listData.get(index);
                if (databaseBean != null) {
                    dbNameText.setText(databaseBean.getDatabaseName());
                    hostNameText.setText(databaseBean.getHostname());
                    userNameText.setText(databaseBean.getUsername());
                    passwordField.setText(databaseBean.getPassword());
                    schemaText.setText(databaseBean.getPassword());
                }
            }
        });


        left.add(wordList);
        left.setBounds(0, 0, leftPanelWith, leftPanelHeight);
        left.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        return left;
    }


    public static void main(String[] args) {
        // 显示应用 GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


    class DatabaseBeanListModel extends  AbstractListModel<String>{

        private ArrayList<DatabaseBean> anArrayList = new ArrayList<>();



        @Override
        public int getSize() {
            return anArrayList.size();
        }

        @Override
        public String getElementAt(int index) {
            return anArrayList.get(index).getDatabaseName();
        }

        public void addElement(DatabaseBean element) {
            if(element.isInvalid()){
                throw new RuntimeException(element.getNullField()+" not null");
            }
            int index = anArrayList.size();
            anArrayList.add(element);
            fireIntervalAdded(this, index, index);
        }

        public boolean containDbName(String dbName) {
            for(DatabaseBean b:anArrayList){
                if(b.getDatabaseName().equals(dbName)){
                    return true;
                }
            }
            return false;
        }

        public DatabaseBean get(int index) {
            return anArrayList.get(index);
        }
    }

}
