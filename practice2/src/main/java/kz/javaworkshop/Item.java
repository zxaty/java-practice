package kz.javaworkshop;

public class Item {
    private final String name;
    private final String type;
    private final int weightGrams;
    private final int value;

    public Item(String name, String type, int weightGrams, int value) {
        if (name == null || name.strip().length() < 1 || name.strip().length() > 40)
            throw new IllegalArgumentException("Имя должно содержать 1–40 символов");
        if (!"weapon".equals(type) && !"armor".equals(type) && !"potion".equals(type))
            throw new IllegalArgumentException("Тип: weapon, armor или potion");
        if (weightGrams < 1 || weightGrams > 100000)
            throw new IllegalArgumentException("Масса должна быть от 1 до 100000 г");
        if (value < 0 || value > 1000000)
            throw new IllegalArgumentException("Стоимость должна быть от 0 до 1000000");
        this.name = name.strip();
        this.type = type;
        this.weightGrams = weightGrams;
        this.value = value;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getWeightGrams() { return weightGrams; }
    public int getValue() { return value; }

    @Override
    public String toString() {
        return name + " [" + type + "], " + weightGrams + " г, " + value + " монет";
    }
}
