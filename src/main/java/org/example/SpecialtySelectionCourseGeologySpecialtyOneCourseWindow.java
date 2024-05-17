package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.List;

public class SpecialtySelectionCourseGeologySpecialtyOneCourseWindow extends JFrame {

    private final JTable specialtyGeologyTable;
    private final DefaultTableModel specialtyGeologyTableModel;
    AddItem item = new AddItem();
    List<Item> items;

    public SpecialtySelectionCourseGeologySpecialtyOneCourseWindow(String title) {
        specialtyGeologyTableModel = new DefaultTableModel(new Object[]{"ID", "nameCourse"}, 0);
        specialtyGeologyTable = new JTable(specialtyGeologyTableModel);
        JScrollPane scrollPane = new JScrollPane(specialtyGeologyTable);
        add(scrollPane);
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadCourseGeologyFromDatabase();

        specialtyGeologyTable.setCellSelectionEnabled(false);
        specialtyGeologyTable.setDefaultEditor(Object.class, null);

        specialtyGeologyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // getClickCount() -  верный метод для определения двойного клика
                    int selectedRow = specialtyGeologyTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int specialtyId = (int) specialtyGeologyTableModel.getValueAt(selectedRow, 0);
                        Item selectedItem = items.get(selectedRow);
                        SecondWindow secondWindow = new SecondWindow(selectedItem);
                        secondWindow.setVisible(true);
                    }
                }
            }
        });
    }

    private void loadCourseGeologyFromDatabase() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ПервыйКурсСпециалитетГеологи WHERE idFirstCourse = ?")) {

            preparedStatement.setInt(1, 1); // 1 -  idNameIndustry для 1 курса
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameCourse = resultSet.getString("nameCourse");
                specialtyGeologyTableModel.addRow(new Object[]{id, nameCourse});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}