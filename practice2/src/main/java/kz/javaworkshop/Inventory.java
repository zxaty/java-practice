package kz.javaworkshop;

import java.util.Arrays;
import java.util.Locale;

public class Inventory {
    private final Item[] items;
    private int size;
    private final int maxWeightGrams;

    public Inventory(int capacity, int maxWeightGrams) {
        if (capacity < 1 || capacity > 20)
            throw new IllegalArgumentException("Ёмкость должна быть от 1 до 20");
        if (maxWeightGrams < 1 || maxWeightGrams > 100000)
            throw new IllegalArgumentException("Лимит массы должен быть от 1 до 100000");
        items = new Item[capacity];
        this.maxWeightGrams = maxWeightGrams;
    }

    public boolean add(Item item) {
        if (item == null || size == items.length
                || totalWeightGrams() + item.getWeightGrams() > maxWeightGrams) return false;
        items[size++] = item;
        return true;
    }

    public Item remove(int index) {
        if (index < 0 || index >= size) return null;
        Item removed = items[index];
        for (int i = index; i < size - 1; i++) items[i] = items[i + 1];
        items[--size] = null;
        return removed;
    }

    public Item get(int index) {
        return index < 0 || index >= size ? null : items[index];
    }

    public int size() { return size; }
    public int capacity() { return items.length; }
    public int getMaxWeightGrams() { return maxWeightGrams; }

    public int totalWeightGrams() {
        int sum = 0;
        for (int i = 0; i < size; i++) sum += items[i].getWeightGrams();
        return sum;
    }

    public long totalValue() {
        long sum = 0;
        for (int i = 0; i < size; i++) sum += items[i].getValue();
        return sum;
    }

    public Item[] snapshot() { return Arrays.copyOf(items, size); }

    public Item[] findByName(String query) {
        if (query == null) throw new IllegalArgumentException("Запрос не должен быть null");
        String text = query.strip().toLowerCase(Locale.ROOT);
        Item[] found = new Item[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (items[i].getName().toLowerCase(Locale.ROOT).contains(text))
                found[count++] = items[i];
        }
        return Arrays.copyOf(found, count);
    }
}
