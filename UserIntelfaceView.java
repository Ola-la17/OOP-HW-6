package org.example;

import java.io.IOException;
import java.util.Scanner;

public class UserInterfaceView {
    Controller controller = new Controller();
    public void runInterface() throws IOException {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("Выберите сервис погоды : \n" +
                    "1 - Yandex\n2 - Accuweather");
            switch (sc.nextInt()) {
                case 1:
                    System.out.println("Укажите город: ");
                    String city = sc.next();

                    System.out.println("Погода на сегодня в городе " + city);
                    YandexweatherModel.getWeather();
//                    controller.getYandexWeather(city);
                    break;
                case 2:
                    System.out.println("Укажите город: ");
                    city = sc.next();

                    System.out.println("Введите 1 для получения погоды на сегодня;\n" +
                            "Введите 5 для получения погод на пять дней;\n");

                    String command = String.valueOf(sc.nextInt());

                    if (command.equals("0")) break;

                    try {
                        controller.getWeather(command, city);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

}