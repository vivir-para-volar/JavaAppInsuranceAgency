package com.insuranceagency.database;

import com.insuranceagency.model.Employee;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBEmployee {

    /**
     * Получение всего списка сотрудников
     * @return Список сотрудников
     */
    public static ArrayList<Employee> allEmployees() throws Exception {
        var resultList = new ArrayList<Employee>();

        String query = "SELECT * FROM employees ORDER BY fullName";

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");

                String birthdayTemp = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(birthdayTemp, formatter);

                String telephone = resultSet.getString("telephone");
                String passport = resultSet.getString("passport");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");

                var employee = new Employee(id, fullName, birthday, telephone, passport, login, password);
                resultList.add(employee);
            }

            return resultList;

        } catch (Exception exp) {
            throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Добавление сотрудника в БД
     * @param employee Сотрудник
     */
    public static void addEmployee(@NotNull Employee employee) throws Exception {
        if (employee == null) throw new Exception("Сотрудник не выбран");

        String query1 = String.format("SELECT id FROM employees WHERE telephone = '%s'", employee.getTelephone());
        String query2 = String.format("SELECT id FROM employees WHERE passport = '%s'", employee.getPassport());
        String query3 = String.format("SELECT ID FROM employees WHERE login = '%s'", employee.getLogin());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String query = String.format("INSERT INTO employees (fullName, birthday, telephone, passport, login, password) VALUES('%s', '%s', '%s', '%s', '%s', '%s')",
                employee.getFullName(),
                employee.getBirthday().format(formatter),
                employee.getTelephone(),
                employee.getPassport(),
                employee.getLogin(),
                Database.getHash(employee.getPassword()));

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный телефон уже используется");
            }

            resultSet = statement.executeQuery(query2);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный паспорт уже используется");
            }

            resultSet = statement.executeQuery(query3);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный логин уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Изменение сотрудника в БД
     * @param employee Сотрудник
     */
    public static void changeEmployee(@NotNull Employee employee, boolean changePassword) throws Exception {
        if (employee == null) throw new Exception("Сотрудник не выбран");

        String query1 = String.format("SELECT id FROM employees WHERE telephone = '%s' AND id <> %d",
                employee.getTelephone(),
                employee.getId());
        String query2 = String.format("SELECT id FROM employees WHERE passport = '%s' AND id <> %d",
                employee.getPassport(),
                employee.getId());
        String query3 = String.format("SELECT id FROM employees WHERE login = '%s' AND id <> %d",
                employee.getLogin(),
                employee.getId());

        String password;
        if (changePassword) password = Database.getHash(employee.getPassword());
        else password = employee.getPassword();
        String query = String.format("UPDATE employees SET fullName = '%s', telephone = '%s', passport = '%s', login = '%s', password = '%s' WHERE id = %d",
                employee.getFullName(),
                employee.getTelephone(),
                employee.getPassport(),
                employee.getLogin(),
                password,
                employee.getId());

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный телефон уже используется");
            }

            resultSet = statement.executeQuery(query2);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный паспорт уже используется");
            }

            resultSet = statement.executeQuery(query3);
            countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Данный логин уже используется");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Удаление сотрудника из БД
     * @param id Id сотрудника
     */
    public static void deleteEmployee(int id) throws Exception {
        String query1 = String.format("SELECT id FROM policies WHERE employeeId = %d", id);

        String query = String.format("DELETE FROM employees WHERE id = %d", id);

        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query1);
            int countRow = 0;
            while (resultSet.next()) countRow++;
            if (countRow != 0) {
                flag = true;
                throw new Exception("Вы не можете удалить данного сотрудника, так как на него оформлен полис");
            }

            statement.executeUpdate(query);
        } catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }

    /**
     * Поиск сотрудника по номеру телефона или паспорту
     * @param telephoneOrPassport Номер телефона или паспорт
     * @return Найденный сотрудник
     */
    public static Employee searchEmployeeTelephoneOrPassport(@NotNull String telephoneOrPassport) throws Exception{
        String query = String.format("SELECT * FROM employees WHERE telephone = '%s' OR passport = '%s'", telephoneOrPassport, telephoneOrPassport);
        return searchEmployee(query);
    }

    /**
     * Поиск сотрудника по Id
     * @param id Id сотрудника
     * @return Найденный сотрудник
     */
    public static Employee searchEmployeeID(int id) throws Exception {
        String query = String.format("SELECT * FROM employees WHERE id = %d", id);
        return searchEmployee(query);
    }

    /**
     * Поиск сотрудника по определённому запросу
     * @param query Запрос
     * @return Найденный сотрудник
     */
    private static Employee searchEmployee(@NotNull String query) throws Exception{
        if (query == null) throw new Exception("Запрос не выбран");

        boolean flag = false;
        try (Connection connection = DriverManager.getConnection(Database.DB_URL, Database.LOGIN, Database.PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int countRow = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (resultSet.next()) {
                countRow++;

                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");

                String birthdayTemp = resultSet.getString("birthday");
                LocalDate birthday = LocalDate.parse(birthdayTemp, formatter);

                String telephone = resultSet.getString("telephone");
                String passport = resultSet.getString("passport");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");

                var employee = new Employee(id, fullName, birthday, telephone, passport, login, password);
                return employee;
            }

            if (countRow == 0) {
                flag = true;
                throw new Exception("Данный сотрудник не существует");
            }
            return null;

        }catch (Exception exp) {
            if (flag) throw exp;
            else throw new Exception("Ошибка в работе БД");
        }
    }
}
