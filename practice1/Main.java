import java.util.Locale;
import java.util.Scanner;

public class Main {
    static final int MAX_HP = 40;
    static final int HEAL = 8;
    static final int REWARD = 20;

    static boolean canEnter(String heroClass, int level, boolean shield) {
        return level >= 10 && (shield || heroClass.equals("маг"));
    }

    static int afterDamage(int hp, int damage) {
        return Math.max(0, hp - damage);
    }

    static int afterHealing(int hp) {
        return Math.min(MAX_HP, hp + HEAL);
    }

    static void printResult(String name, int wins, int xp, int hp, String result) {
        System.out.println("\nИмя: " + name + "; вариант Б — Пещера");
        System.out.println("Побед: " + wins + "; XP: " + xp + "; HP: " + hp);
        System.out.println("Итог: " + result);
    }

    static String readLine(Scanner scanner, String prompt) {
        System.out.println(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().strip() : null;
    }

    static Integer readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                if (scanner.hasNextLine()) scanner.nextLine();
                return value;
            }
            if (!scanner.hasNextLine()) return null;
            scanner.nextLine();
            System.out.println("Введите целое число.");
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String name;
            while (true) {
                name = readLine(scanner, "Имя героя (1–30 символов):");
                if (name == null) { System.out.println("Ввод завершён. Регистрация прервана."); return; }
                if (name.length() >= 1 && name.length() <= 30) break;
                System.out.println("Неверная длина имени.");
            }
            String heroClass;
            while (true) {
                heroClass = readLine(scanner, "Класс: воин, маг или лучник:");
                if (heroClass == null) { System.out.println("Ввод завершён. Регистрация прервана."); return; }
                heroClass = heroClass.toLowerCase(Locale.ROOT);
                if (heroClass.equals("воин") || heroClass.equals("маг") || heroClass.equals("лучник")) break;
                System.out.println("Неизвестный класс.");
            }
            Integer level;
            while (true) {
                level = readInt(scanner, "Уровень (1–80):");
                if (level == null) { System.out.println("Ввод завершён. Регистрация прервана."); return; }
                if (level >= 1 && level <= 80) break;
                System.out.println("Уровень должен быть от 1 до 80.");
            }
            boolean shield;
            while (true) {
                String answer = readLine(scanner, "Есть щит? да/нет:");
                if (answer == null) { System.out.println("Ввод завершён. Регистрация прервана."); return; }
                answer = answer.toLowerCase(Locale.ROOT);
                if (answer.equals("да") || answer.equals("нет")) {
                    shield = answer.equals("да");
                    break;
                }
                System.out.println("Ответьте да или нет.");
            }
            if (!canEnter(heroClass, level, shield)) {
                System.out.println(level < 10 ? "Отказ: уровень ниже 10." : "Отказ: нужен щит или класс маг.");
                return;
            }
            System.out.println("Допущен: " + name + ", " + heroClass);
            String[] enemies = {"Паук", "Гоблин", "Огр"};
            int[] enemyHealth = {14, 20, 26};
            int[] enemyDamage = {5, 7, 9};
            int strength = 11;
            int hp = MAX_HP, xp = 0, wins = 0;
            boolean interrupted = false;
            for (int i = 0; i < enemies.length && hp > 0 && !interrupted; i++) {
                int enemyHp = enemyHealth[i];
                boolean healed = false;
                System.out.println("\nПротивник: " + enemies[i]);
                while (enemyHp > 0 && hp > 0 && !interrupted) {
                    System.out.println("HP героя: " + hp + "; HP противника: " + enemyHp);
                    Integer command = readInt(scanner, "1 — атака, 2 — лечение, 0 — выход:");
                    if (command == null) {
                        System.out.println("Ввод завершён.");
                        interrupted = true;
                        break;
                    }
                    switch (command) {
                        case 0:
                            interrupted = true;
                            break;
                        case 1:
                            enemyHp = afterDamage(enemyHp, strength);
                            if (enemyHp > 0) hp = afterDamage(hp, enemyDamage[i]);
                            break;
                        case 2:
                            if (healed) {
                                System.out.println("Лечение в этом бою уже использовано.");
                            } else {
                                hp = afterHealing(hp);
                                healed = true;
                                hp = afterDamage(hp, enemyDamage[i]);
                            }
                            break;
                        default:
                            System.out.println("Неизвестная команда.");
                    }
                }
                if (enemyHp == 0) {
                    wins++;
                    xp += REWARD;
                    System.out.println("Раунд " + (i + 1) + ": победа, HP героя " + hp + ", XP " + xp);
                }
            }
            String result = interrupted ? "Игра прервана" : hp == 0 ? "Поражение" : "Арена пройдена";
            printResult(name, wins, xp, hp, result);
        }
    }
}
