package com.jc.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBTextField;
import com.jc.idea.ui.DBForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CodeGenAction extends AnAction {
    public CodeGenAction() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent event) {
//

        Project project = getEventProject(event);
        VirtualFile selectedFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);


        final DialogBuilder dialogBuilder;

        final JPanel topPanel;

        final JTextArea codeArea;
        JBTextField packageText;
        JComboBox sourcePathComboBox;
        dialogBuilder = new DialogBuilder(project);
        dialogBuilder.setTitle("Project Info");


//        centerPanel.setSize(500,500);
//        dialogBuilder.setCenterPanel(centerPanel);
        dialogBuilder.setCenterPanel(new DBForm());


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


    class ProjectInfoPanel extends JPanel {

        public ProjectInfoPanel(Project project) {
            super(new GridLayout(11, 2));
            if (project != null) {
                JLabel nameLabe = new JLabel("Name:");
                this.add(nameLabe);
                JLabel name = new JLabel(project.getName());
                this.add(name);
                JLabel labe = new JLabel("BaseDir:");
                this.add(labe);
                labe = new JLabel(project.getBaseDir().toString());
                this.add(labe);
                labe = new JLabel("BasePath:");
                this.add(labe);
                labe = new JLabel(project.getBasePath());
                labe = new JLabel(project.getBasePath());
                this.add(labe);
                labe = new JLabel("ProjectFile:");
                this.add(labe);
                labe = new JLabel(project.getProjectFile().toString());
                this.add(labe);

                labe = new JLabel("ProjectFilePath:");
                this.add(labe);
                labe = new JLabel(project.getProjectFilePath());
                this.add(labe);
                labe = new JLabel("PresentableUrl:");
                this.add(labe);
                labe = new JLabel(project.getPresentableUrl());
                this.add(labe);
                labe = new JLabel("WorkspaceFile:");
                this.add(labe);
                labe = new JLabel(project.getWorkspaceFile().toString());
                this.add(labe);
                labe = new JLabel("isOpen:");
                this.add(labe);
                labe = new JLabel(String.valueOf(project.isOpen()));
                this.add(labe);
                labe = new JLabel("isInitialized:");
                this.add(labe);
                labe = new JLabel(String.valueOf(project.isInitialized()));
                this.add(labe);
                labe = new JLabel("isDefault:");
                this.add(labe);
                labe = new JLabel(String.valueOf(project.isDefault()));
                this.add(labe);
            }
        }
    }
}