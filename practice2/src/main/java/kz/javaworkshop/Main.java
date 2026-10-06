package kz.javaworkshop;

import java.util.Scanner;

public class Main {
    static String readLine(Scanner scanner, String prompt) {
        System.out.println(prompt);
        if (!scanner.hasNextLine()) throw new IllegalStateException("Ввод завершён.");
        return scanner.nextLine();
    }

    static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                if (scanner.hasNextLine()) scanner.nextLine();
                return number;
            }
            if (!scanner.hasNextLine()) throw new IllegalStateException("Ввод завершён.");
            scanner.nextLine();
            System.out.println("Введите целое число.");
        }
    }

    static void showItems(Inventory inventory) {
        if (inventory.size() == 0) System.out.println("Инвентарь пуст.");
        for (int i = 0; i < inventory.size(); i++)
            System.out.println((i + 1) + ". " + inventory.get(i));
    }

    static void summary(Inventory inventory) {
        System.out.println("Ячейки: " + inventory.size() + "/" + inventory.capacity()
                + "; масса: " + inventory.totalWeightGrams() + "/" + inventory.getMaxWeightGrams()
                + " г; стоимость: " + inventory.totalValue());
    }

    static void addItem(Scanner scanner, Inventory inventory) {
        String name = readLine(scanner, "Имя предмета:");
        String type = readLine(scanner, "Тип: weapon, armor или potion:");
        int grams = readInt(scanner, "Масса в граммах:");
        int value = readInt(scanner, "Стоимость в монетах:");
        try {
            Item item = new Item(name, type, grams, value);
            System.out.println(inventory.add(item) ? "Добавлено" : "Нет места или массы");
        } catch (IllegalArgumentException ex) {
            System.out.println("Данные предмета: " + ex.getMessage());
        }
    }

    static void removeOrUse(Scanner scanner, Inventory inventory, boolean use) {
        showItems(inventory);
        int number = readInt(scanner, "Номер предмета:");
        if (number < 1 || number > inventory.size()) {
            System.out.println("Неверный номер.");
            return;
        }
        int index = number - 1;
        Item item = inventory.get(index);
        if (use && !item.getType().equals("potion")) {
            System.out.println("Этот предмет не зелье.");
            return;
        }
        inventory.remove(index);
        System.out.println(use ? "Зелье использовано" : "Удалено: " + item.getName());
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory(4, 4000);
        inventory.add(new Item("Лук", "weapon", 1500, 100));
        inventory.add(new Item("Куртка", "armor", 2000, 80));
        inventory.add(new Item("Зелье", "potion", 500, 20));
        System.out.println("Вариант Б — Разведчик");
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                int command = readInt(scanner,
                        "\n1 — добавить, 2 — предметы, 3 — удалить, 4 — использовать зелье,"
                        + "\n5 — сводка, 6 — поиск, 0 — выход:");
                switch (command) {
                    case 0: running = false; break;
                    case 1: addItem(scanner, inventory); break;
                    case 2: showItems(inventory); break;
                    case 3: removeOrUse(scanner, inventory, false); break;
                    case 4: removeOrUse(scanner, inventory, true); break;
                    case 5: summary(inventory); break;
                    case 6:
                        Item[] found = inventory.findByName(readLine(scanner, "Подстрока имени:"));
                        if (found.length == 0) System.out.println("Ничего не найдено");
                        for (Item item : found) System.out.println(item);
                        break;
                    default: System.out.println("Неизвестная команда.");
                }
            }
        } catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
