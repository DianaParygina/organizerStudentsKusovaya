package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialtySelectionSystemISTWindow extends JFrame  {

    private final JTable specialtyISTTable;
    private final DefaultTableModel specialtyISTTableModel;


    public SpecialtySelectionSystemISTWindow(String title) {

        specialtyISTTableModel = new DefaultTableModel(new Object[]{"ID", "nameProgram"}, 0);
        specialtyISTTable = new JTable(specialtyISTTableModel);
            JScrollPane scrollPane = new JScrollPane(specialtyISTTable);
            add(scrollPane);
            setTitle(title);
            //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);
            setVisible(true);

            // Загрузка специальностей из БД
            loadCourseGeologyFromDatabase();

        specialtyISTTable.setCellSelectionEnabled(false);
        specialtyISTTable.setDefaultEditor(Object.class, null);

        specialtyISTTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // getClickCount() -  верный метод для определения двойного клика
                        int selectedRow = specialtyISTTable.getSelectedRow();
                        if (selectedRow != -1) {
                            int specialtyId = (int) specialtyISTTable.getValueAt(selectedRow, 0);
                            if (specialtyId == 1) {
                                new SpecialtySelectionCourseGeologyWindow("Специалитет").setVisible(true);
                            } else if (specialtyId == 2) {
                                //new SpecialtySelectionCourseISTWindow("Бакалавриат").setVisible(true);
                            }
                        }
                    }
                }
            });
        }

        private void loadCourseGeologyFromDatabase () {
            try (Connection connection = DBConnector.connection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameProgram FROM EducationalProgram WHERE idNameIndustry = ?")) { // Добавляем idNameIndustry

                // Замените 1 на фактическое значение idNameIndustry для геологии
                preparedStatement.setInt(1, 1); // 1 -  idNameIndustry для геологии
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nameProgram = resultSet.getString("nameProgram");
                    specialtyISTTableModel.addRow(new Object[]{id, nameProgram});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }

}
