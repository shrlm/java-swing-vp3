package com.example.vp3.JFrames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateMahasiswaFrame {
    private MahasiswaFrame parentFrame;
    private JPanel panel;

    public CreateMahasiswaFrame(MahasiswaFrame parentFrame) {
        this.parentFrame = parentFrame;

        JFrame newFrame = new JFrame("Create Mahasiswa");
        JLabel labelNama = new JLabel("Nama:");
        JTextField textNama = new JTextField(20);

        JLabel labelNim = new JLabel("NIM:");
        JTextField textNim = new JTextField(20);

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = textNama.getText();
                String nim = textNim.getText();

                if (!nama.isEmpty() && !nim.isEmpty()) {
                    saveMahasiswa(nama, nim);
                    parentFrame.refreshTable();
                    newFrame.dispose();
                } else {

                    JLabel errorLabel = new JLabel("All fields are required!");
                    panel.add(errorLabel);
                    newFrame.pack();
                }
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        panel.add(labelNama);
        panel.add(textNama);
        panel.add(labelNim);
        panel.add(textNim);
        panel.add(saveButton);

        newFrame.getContentPane().add(BorderLayout.CENTER, panel);
        newFrame.pack();
        newFrame.setVisible(true);
        newFrame.setBounds(3, 4, 300, 400);
    }

    private void saveMahasiswa(String nama, String nim) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mahasiswaku?" +
                    "user=root&password=");
            String query = "INSERT INTO mahasiswa (nama, nim) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, nim);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}