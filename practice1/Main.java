import java.util.Locale;
import java.util.Scanner;

public class Main {
    static final int MAX_HP = 40;
    static final int HEAL_AMOUNT = 8;
    static final int WIN_XP = 20;
    static final int MIN_LEVEL = 10;
    static final int HERO_DAMAGE = 10;
    static final String VARIANT = "А — Руины";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String name = readName(scanner);
            if (name == null) return;
            String heroClass = readClass(scanner);
            if (heroClass == null) return;
            Integer level = readLevel(scanner);
            if (level == null) return;
            Boolean shield = readShield(scanner);
            if (shield == null) return;

            if (!canEnter(heroClass, level, shield)) {
                if (level < MIN_LEVEL) {
                    System.out.println("Отказ: уровень должен быть не ниже " + MIN_LEVEL);
                } else {
                    System.out.println("Отказ: нужен щит или класс маг.");
                }
                return;
            }
            System.out.println("Допущен: " + name + ", " + heroClass);
            String[] names = {"Крыса", "Скелет", "Страж"};
            int[] health = {12, 18, 24};
            int[] damage = {4, 6, 8};
            int heroHp = MAX_HP;
            int xp = 0;
            int wins = 0;
            boolean gameOver = false;
            String result = "Игра прервана";

            for (int i = 0; i < names.length && !gameOver; i++) {
                int enemyHp = health[i];
                boolean healUsed = false;
                System.out.println("Раунд " + (i + 1) + ": " + names[i]);
                while (heroHp > 0 && enemyHp > 0 && !gameOver) {
                    System.out.println("HP героя: " + heroHp + ", HP противника: " + enemyHp);
                    Integer command = readInt(scanner, "1 — атака, 2 — лечение, 0 — выход: ");
                    if (command == null) {
                        gameOver = true;
                        break;
                    }
                    boolean enemyTurn = false;
                    switch (command) {
                        case 1:
                            enemyHp = hpAfterDamage(enemyHp, HERO_DAMAGE);
                            enemyTurn = enemyHp > 0;
                            break;
                        case 2:
                            if (healUsed) {
                                System.out.println("Лечение уже использовано в этом бою.");
                            } else {
                                heroHp = hpAfterHeal(heroHp);
                                healUsed = true;
                                enemyTurn = true;
                                System.out.println("HP после лечения: " + heroHp);
                            }
                            break;
                        case 0:
                            gameOver = true;
                            break;
                        default:
                            System.out.println("Неизвестная команда.");
                    }
                    if (enemyTurn) {
                        heroHp = hpAfterDamage(heroHp, damage[i]);
                        System.out.println(names[i] + " наносит " + damage[i] + " урона.");
                    }
                    if (heroHp == 0) {
                        result = "Поражение";
                        gameOver = true;
                    }
                }
                if (enemyHp == 0 && !gameOver) {
                    wins++;
                    xp += WIN_XP;
                    System.out.println("Раунд " + (i + 1) + ": победа, HP героя " + heroHp + ", XP " + xp);
                }
            }
            if (wins == names.length) result = "Арена пройдена";
            printSummary(name, wins, xp, heroHp, result);
        }
    }

    static boolean canEnter(String heroClass, int level, boolean shield) {
        return level >= MIN_LEVEL && (shield || heroClass.equals("маг"));
    }

    static int hpAfterDamage(int hp, int damage) {
        return Math.max(0, hp - damage);
    }

    static int hpAfterHeal(int hp) {
        return Math.min(MAX_HP, hp + HEAL_AMOUNT);
    }

    static void printSummary(String name, int wins, int xp, int hp, String result) {
        System.out.println("\nИмя: " + name);
        System.out.println("Вариант: " + VARIANT);
        System.out.println("Побед: " + wins + ", XP: " + xp + ", HP: " + hp);
        System.out.println("Итог: " + result);
    }

    static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println("Ввод закончился.");
            return null;
        }
        return scanner.nextLine().strip();
    }

    static Integer readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextInt()) {
                if (!scanner.hasNextLine()) {
                    System.out.println("Ввод закончился.");
                    return null;
                }
                scanner.nextLine();
                System.out.println("Введите целое число.");
                continue;
            }
            int value = scanner.nextInt();
            if (!scanner.hasNextLine()) {
                System.out.println("Ввод закончился.");
                return null;
            }
            scanner.nextLine();
            return value;
        }
    }

    static String readName(Scanner scanner) {
        while (true) {
            String value = readLine(scanner, "Имя: ");
            if (value == null) return null;
            if (value.length() >= 1 && value.length() <= 30) return value;
            System.out.println("Имя должно содержать от 1 до 30 символов.");
        }
    }

    static String readClass(Scanner scanner) {
        while (true) {
            String value = readLine(scanner, "Класс (воин, маг, лучник): ");
            if (value == null) return null;
            value = value.toLowerCase(Locale.ROOT);
            if (value.equals("воин") || value.equals("маг") || value.equals("лучник")) return value;
            System.out.println("Неизвестный класс.");
        }
    }

    static Integer readLevel(Scanner scanner) {
        while (true) {
            Integer value = readInt(scanner, "Уровень (1–80): ");
            if (value == null) return null;
            if (value >= 1 && value <= 80) return value;
            System.out.println("Уровень должен быть от 1 до 80.");
        }
    }

    static Boolean readShield(Scanner scanner) {
        while (true) {
            String value = readLine(scanner, "Есть щит (да/нет): ");
            if (value == null) return null;
            value = value.toLowerCase(Locale.ROOT);
            if (value.equals("да")) return true;
            if (value.equals("нет")) return false;
            System.out.println("Введите да или нет.");
        }
    }
}
