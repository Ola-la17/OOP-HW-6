package Views;

import Controllers.UserController;
import Model.User;

import java.util.Scanner;

public class ViewUser {
    private final UserController userController;

    public ViewUser(UserController userController) {
        this.userController = userController;
    }

    public void run(){
        Commands com;

        while (true) {
            try {
                String command = prompt("Введите команду: ");
                com = Commands.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown command");
                continue;
            }
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE:
                    try {
                        String firstName = prompt("Имя: ");
                        String lastName = prompt("Фамилия: ");
                        String phone = prompt("Номер телефона: ");
                        userController.saveUser(new User(firstName, lastName, phone));
                        System.out.println("Successfully created!");
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    break;
                case READ:
                    String id = prompt("Идентификатор пользователя: ");
                    try {
                        User user = userController.readUser(id);
                        System.out.println("User founded:\n"+user);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case LIST:
                    userController.readUsers().forEach(user -> System.out.println(user+"\n"));
                    break;
                case UPDATE:
                    String firstName = prompt("Имя: ");
                    String lastName = prompt("Фамилия: ");
                    String phone = prompt("Номер телефона: ");
                    String userID = prompt("Идентификатор пользователя: ");
                    try{
                        userController.editUser(new User(userID, firstName, lastName, phone));
                        System.out.println("Successfully updated!");
                    }catch (IllegalStateException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case DELETE:
                    String userToDeleteID = prompt("Идентификатор пользователя: ");
                    try{
                        userController.deleteUser(userToDeleteID);
                        System.out.println("Successfully deleted!");
                    } catch (IllegalStateException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case HELP:
                    System.out.println("Commands:" +
                            "\nCREATE\tCreate new user and save" +
                            "\nREAD\tFind user by id" +
                            "\nLIST\tShow all users" +
                            "\nUPDATE\tEdit user by id" +
                            "\nDELETE\tDelete user by id" +
                            "\nEXIT\tExit program");
                    break;
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
}