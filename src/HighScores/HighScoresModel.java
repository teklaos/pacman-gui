package HighScores;

import javax.swing.*;
import java.util.*;

public class HighScoresModel extends AbstractListModel<String> {
    private final List<String> items;

    public HighScoresModel(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        Collections.reverse(entryList);

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            list.add(entry.getKey() + " --- " + entry.getValue());
        }
        items = list;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public String getElementAt(int index) {
        return items.get(index);
    }
}