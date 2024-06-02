import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MahasiswaFrame {
    private DefaultTableModel tableModel;

    public MahasiswaFrame() {
        Connection conn = null;

        JFrame jFrame = new JFrame("Aplikasi Mahasiswa");
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nama");
        tableModel.addColumn("NIM");

        loadData();

        JTable table = new JTable(tableModel);
        JScrollPane pane = new JScrollPane(table);

        JButton createButton = new JButton("Create Mahasiswa");
        JButton deleteButton = new JButton("Delete Mahasiswa");
        JButton updateButton = new JButton("Update Mahasiswa");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateMahasiswaFrame(MahasiswaFrame.this);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteMahasiswa(id);
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nama = (String) tableModel.getValueAt(selectedRow, 1);
                    String nim = (String) tableModel.getValueAt(selectedRow, 2);
                    new UpdateMahasiswaFrame(id, nama, nim, MahasiswaFrame.this);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.add(createButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(pane);

        jFrame.getContentPane().add(BorderLayout.CENTER, panel);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setBounds(3, 4, 400, 400);
    }

    private void loadData() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mahasiswaku?" +
                    "user=root&password=");
            String query = "SELECT * FROM mahasiswa";
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String nama = rs.getString(2);
                String nim = rs.getString(3);

                tableModel.addRow(new Object[] { id, nama, nim });
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    private void deleteMahasiswa(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mahasiswaku?" +
                    "user=root&password=");
            String query = "DELETE FROM mahasiswa WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
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

    public void refreshTable() {
        tableModel.setRowCount(0);
        loadData();
    }
}