package com.jc.idea.ui;

import com.jc.idea.config.DatabaseBean;
import com.jc.idea.config.TableBean;
import com.jc.idea.db.DataSource;
import com.jc.idea.db.DataSourceConfiguration;
import com.jc.idea.db.DataSourceException;
import com.jc.idea.db.DefaultDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class TableList {

    private static Logger logger = LoggerFactory.getLogger(TableList.class);

    private int margin = 10;

    private JPanel parent;


    private TableListModel listData = new TableListModel();
    private TableBeanModel tableData ;
    private JTextField groupIdText;

    public TableList(JPanel parent) {
        this.parent = parent;


    }

    public void paint(DatabaseBean databaseBean) {


        long beginConn = System.currentTimeMillis();
        DataSourceConfiguration dataSourceConfiguration = new DataSourceConfiguration(databaseBean.getUrl(), databaseBean.getUsername(), databaseBean.getPassword());
        DataSource dataSource = new DefaultDataSource(dataSourceConfiguration);
        try (Connection connection = dataSource.getConnection()) {
            logger.debug("connect cost {}ms",System.currentTimeMillis()-beginConn);
            if (connection != null) {
                long beginShowTable = System.currentTimeMillis();
                ResultSet rs = connection.prepareStatement(
                        "show tables")
                        .executeQuery();
                List<TableBean> tableBeans = new ArrayList<>();
                while (rs.next()) {
                    TableBean tableBean = new TableBean();
                    tableBean.setTableName(rs.getString(1));
                    listData.addElement(tableBean);
                    tableBeans.add(tableBean);
                }
                rs.close();
                logger.debug("show tables cost {}ms",System.currentTimeMillis()-beginShowTable);


                long beginTableDetail = System.currentTimeMillis();
                for(TableBean bean:tableBeans){
                    String tableName = bean.getTableName();
                    DatabaseMetaData dbMetaData = connection.getMetaData();
                    ResultSet pkRSet = dbMetaData.getPrimaryKeys(null, null, tableName);
                    while (pkRSet.next()) {  //如果存在两个主键时，ResultSet时反过来读的，所以会取组合主键中的第一个
                        String pkName = pkRSet.getObject(4).toString();
                        bean.setPrimaryKey(pkName);
                    }
                    pkRSet.close();

                    rs = connection.prepareStatement(
                            "SHOW CREATE TABLE " + tableName)
                            .executeQuery();
                    while (rs.next()) {
                        String create = rs.getString(2);
                        String comment = parse(create);
                        bean.setDescription(comment);
                    }
                    rs.close();
//                    rs = connection.prepareStatement(
//                            "SELECT * FROM " + tableName + " WHERE 1=2")
//                            .executeQuery();
//                    ResultSetMetaData rsMetaData = rs.getMetaData();
                }
                tableData = new TableBeanModel(tableBeans);

                logger.debug("show more tables detail cost {}ms",System.currentTimeMillis()-beginTableDetail);

            } else {
                JOptionPane.showMessageDialog(null, "Connection fail.");
            }

        } catch (Exception dbe) {
            JOptionPane.showMessageDialog(null, "Connection fail." + dbe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long beginRemoveAll = System.currentTimeMillis();
        parent.removeAll();
        parent.repaint();
        logger.debug("panel remove all cost {}ms",System.currentTimeMillis()-beginRemoveAll);
        /**左边panel**/
        long beginPaintLeft = System.currentTimeMillis();
//        JPanel left = paintLeft();
        JPanel left = paintTable();
        parent.add(left);
        parent.repaint();
        logger.debug("paint left cost {}ms",System.currentTimeMillis()-beginPaintLeft);
        /**左边panel**/


        /**底边panel**/
        long beginPaintBottom = System.currentTimeMillis();
        JPanel bottom = paintBottom();
        parent.add(bottom);
        parent.repaint();
        logger.debug("paint bottom cost {}ms",System.currentTimeMillis()-beginPaintBottom);
        /**底边panel**/

//        parent.setPreferredSize(new Dimension(900,850));
//        parent.getParent().setPreferredSize(new Dimension(900,850));
//        parent.repaint();
//        parent.getParent().repaint();
    }

    private JPanel paintBottom() {
        int initPositionY = 504;
        int bottomPanelWith = 900;
        int bottomPanelHeight = 300;
        JPanel bottom = new JPanel();
        bottom.setLayout(null);

        int textFieldCount = 1;
        int textLabelWidth = 100;
        int textFieldHeight = 25;
        JLabel projectLabel = new JLabel("Project Info:");
        projectLabel.setBounds(margin,  margin * textFieldCount + textFieldHeight * (textFieldCount - 1), textLabelWidth, textFieldHeight);
        bottom.add(projectLabel);
        textFieldCount++;

        JLabel groupIdLabel = new JLabel("GroupId: ");
        groupIdLabel.setBounds(margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), textLabelWidth, textFieldHeight);
        groupIdText = new JTextField(20);
        groupIdText.setText("com.test");                            //TODO for Test
        groupIdText.setBounds( textLabelWidth +margin, margin * textFieldCount + textFieldHeight * (textFieldCount - 1), 400, textFieldHeight);
        bottom.add(groupIdLabel);
        bottom.add(groupIdText);
        textFieldCount++;


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

        bottom.setBounds(0, initPositionY + margin, bottomPanelWith, bottomPanelHeight);
        bottom.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        return bottom;
    }

    public String parse(String all) {
        String comment = null;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        try {
            comment = new String(comment.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("",e.getMessage());
        }
        return comment;
    }


    private JPanel paintTable() {
        int tablePanelWith = 900;
        int tablePanelHeight = 504;
        JPanel left = new JPanel();
        left.setLayout(null);

        JLabel listLabel = new JLabel("Table List");
        int listLabelHeight = 20;
        listLabel.setBounds(margin, margin, tablePanelWith - margin, listLabelHeight);
        left.add(listLabel);


        JTable table = new JTable(tableData);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setBounds(margin, 30, tablePanelWith - margin - margin, tablePanelHeight -listLabelHeight- margin - margin);


        left.add(scrollPane);
        left.setBounds(0, 0, tablePanelWith, tablePanelHeight);
        left.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        return left;
    }

    private JPanel paintLeft() {
        int leftPanelWith = 180;
        int leftPanelHeight = 504;
        JPanel left = new JPanel();
        left.setLayout(null);

        JLabel listLabel = new JLabel("Table List");
        int listLabelHeight = 20;
        listLabel.setBounds(margin, margin, leftPanelWith - margin, listLabelHeight);
        left.add(listLabel);

        JList<String> wordList = new JList<>(listData);
        JScrollPane scrollPane = new JScrollPane(wordList);
        wordList.setVisibleRowCount(4); // display 4 items
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setBounds(margin, 30, leftPanelWith - margin - margin, leftPanelHeight - listLabelHeight - margin - margin);
        wordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = e.getFirstIndex();

            }
        });


        left.add(scrollPane);
        left.setBounds(0, 0, leftPanelWith, leftPanelHeight);
        left.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        return left;
    }


    class TableListModel extends AbstractListModel<String> {

        private ArrayList<TableBean> anArrayList = new ArrayList<>();


        @Override
        public int getSize() {
            return anArrayList.size();
        }

        @Override
        public String getElementAt(int index) {
            return anArrayList.get(index).getTableName();
        }

        public void addElement(TableBean element) {
            int index = anArrayList.size();
            anArrayList.add(element);
            fireIntervalAdded(this, index, index);
        }

        public TableBean get(int index) {
            return anArrayList.get(index);
        }
    }

    class TableBeanModel extends AbstractTableModel {

//        private ArrayList<TableBean> dataList= new ArrayList<>();
        String[] columnNames = {"CheckBox","Table Name", "Primary Key", "artifactId(module)","Description"};// 定义表格列名数组

        Class[] typeArray = {  Boolean.class,Object.class,Object.class,Object.class,Object.class };

        Object[][] data;


        public TableBeanModel(List<TableBean> dataList) {
            List<Object[]> list=new ArrayList<Object[]>();
            for(TableBean bean:dataList){
                Object[] dataCell = {new Boolean(true),bean.getTableName(),bean.getPrimaryKey(),bean.getArtifactId(),bean.getDescription()};
                list.add(dataCell);
            }
            data = list.toArray(new Object[0][0]);
        }


        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return typeArray[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }
}
