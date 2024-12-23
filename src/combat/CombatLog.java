package combat;

import java.util.ArrayList;
import java.util.List;

public class CombatLog {
    private final List<String> entries;

    public CombatLog() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(String entry) {
        entries.add(entry);
    }

    public void display() {
        entries.forEach(System.out::println);
    }
}