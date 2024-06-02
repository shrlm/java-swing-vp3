import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateMahasiswaFrame {
    private MahasiswaFrame parentFrame;
    private JFrame newFrame;
    private JPanel panel;
    private JLabel errorLabel; 

    public UpdateMahasiswaFrame(int id, String nama, String nim, MahasiswaFrame parentFrame) {
        this.parentFrame = parentFrame;

        newFrame = new JFrame("Update Mahasiswa");
        JLabel labelId = new JLabel("ID:");
        JTextField textId = new JTextField(20);
        textId.setText(String.valueOf(id));
        textId.setEditable(false);

        JLabel labelNama = new JLabel("Nama:");
        JTextField textNama = new JTextField(20);
        textNama.setText(nama);

        JLabel labelNim = new JLabel("NIM:");
        JTextField textNim = new JTextField(20);
        textNim.setText(nim);

        JButton saveButton = new JButton("Save");

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        panel.add(labelId);
        panel.add(textId);
        panel.add(labelNama);
        panel.add(textNama);
        panel.add(labelNim);
        panel.add(textNim);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newNama = textNama.getText();
                String newNim = textNim.getText();

                if (!newNama.isEmpty() && !newNim.isEmpty()) {
                    updateMahasiswa(id, newNama, newNim);
                    parentFrame.refreshTable();
                    newFrame.dispose();
                } else {

                    if (errorLabel != null) {
                        panel.remove(errorLabel);
                    }

                    errorLabel = new JLabel("All fields are required!");
                    panel.add(errorLabel);
                    newFrame.pack();
                }
            }
        });

        newFrame.getContentPane().add(BorderLayout.CENTER, panel);
        newFrame.pack();
        newFrame.setVisible(true);
        newFrame.setBounds(3, 4, 300, 400);
    }

    private void updateMahasiswa(int id, String nama, String nim) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mahasiswaku?" +
                    "user=root&password=");
            String query = "UPDATE mahasiswa SET nama = ?, nim = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, nama);
            preparedStatement.setString(2, nim);
            preparedStatement.setInt(3, id);

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