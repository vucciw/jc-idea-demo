package com.jc.idea;

import com.intellij.execution.dashboard.RunDashboardManagerImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.PanelWithActionsAndCloseButton;
import com.intellij.openapi.ui.panel.ComponentPanelBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.impl.ContentManagerImpl;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HelloAction extends AnAction {
    public HelloAction() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent event) {
//        Project project = event.getProject();
//        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
//        ContentManager contentManager = ContentFactory.SERVICE.getInstance().createContentManager(false,project);
//        PanelWithActionsAndCloseButton panel = new PanelWithActionsAndCloseButton(contentManager,"设置") {
//            @Override
//            protected JComponent createCenterPanel() {
//
//                // 添加 "Hello World" 标签
//                JLabel label = new JLabel("Hello World");
//                return label;
//            }
//
//            @Override
//            public void dispose() {
//
//            }
//        };
//        panel.show();

//        ComponentPanelBuilder componentPanelBuilder = new ComponentPanelBuilder(new JLabel("Hello World"));
//        JPanel jPanel = componentPanelBuilder.createPanel();
//        jPanel.setVisible(true);
//        DialogBuilder dialogBuilder = new DialogBuilder(project);
//        JPanel panel = new JPanel();
//        fill(panel);
//        dialogBuilder.centerPanel(panel);
//        dialogBuilder.show();


        Project project = getEventProject(event);
        VirtualFile selectedFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);


        final DialogBuilder dialogBuilder;

        final JPanel topPanel;

        final JTextArea codeArea;
        JBTextField packageText;
        JComboBox sourcePathComboBox;
        dialogBuilder = new DialogBuilder(project);
        dialogBuilder.setTitle("test");

        JPanel centerPanel = new JPanel(new GridLayout(3, 2));

        fill(centerPanel);
//        codeArea = new JTextArea("测试product");
//        codeArea.setBorder(new LineBorder(JBColor.gray));

//        centerPanel.add(new JBScrollPane(codeArea), BorderLayout.CENTER);
        centerPanel.setSize(500,500);
        dialogBuilder.setCenterPanel(centerPanel);

//        topPanel = new JPanel(new GridLayout(0, 2));
//        centerPanel.add(topPanel, BorderLayout.PAGE_START);

        dialogBuilder.removeAllActions();

        dialogBuilder.addAction(new AbstractAction("testButtion") {
            @Override
            public void actionPerformed(ActionEvent e) {

                dialogBuilder.getDialogWrapper().close(DialogWrapper.OK_EXIT_CODE);
                Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
            }
        });
        dialogBuilder.show();
    }

    private void fill(JPanel panel) {
//        panel.setLayout(null);

        JLabel userLabel = new JLabel("User:");

//        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);


        JTextField userText = new JTextField(20);
//        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
//        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
//        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
//        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
    }
}