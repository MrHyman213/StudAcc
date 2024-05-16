package org.example.util;

import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EntryContainer {

    private static final Map<String, Map<String, Integer>> container = new HashMap<>();
    private static final Map<String, List<String>> lists = new HashMap<>();
    static {
        lists.put("sex", List.of("Мужской", "Женский"));
        lists.put("titles", List.of(" - Группы и специальности", " - Области",
                " - Дисциплины", " - Номера приказов", " - Преподаватели",
                " - Предыдущее образование", " - Организация", " - Пользователи"));
        lists.put("lists", List.of("spec", "region", "Spec", "order",
                "employee", "education", "organization", "user"));
        lists.put("subsidiaryLists", List.of("group", "district", "discipline"));
        lists.put("subsidiaryTitles", List.of("Группы", "Районы", "Дисциплины"));
    }
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ModelMapper getModelMapper(){
        return modelMapper;
    }

    public static String getListByListTitle(String title) {
        try {
            return lists.get("lists").get(getIndexByName(lists.get("titles"), title));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public static void addList(String key, List<String> list){
        if (lists.get(key) == null || !lists.get(key).isEmpty())
            lists.put(key, list);
        else
            lists.replace(key, list);
    }

    public static Integer getIndexByName(List<String> list, String name){
        for (int i = 0; i < list.size(); i++){
            if(Objects.equals(name, list.get(i)))
                return i;
        }
        return null;
    }

    public static List<String> getList(String key){
        return lists.get(key);
    }

    public static String getSubItem(String mainItem) {
        try {
            return lists.get("subsidiaryLists").get(getIndexByName(lists.get("lists"), mainItem));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public static String getSubTitle(String subName) {
        try {
            return lists.get("subsidiaryTitles").get(getIndexByName(lists.get("subsidiaryLists"), subName));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public static void put(String key, Map<String, Integer> value){
        if(container.get(key) == null || !container.get(key).isEmpty())
            container.put(key, value);
        else container.replace(key, value);
    }

    public static Map<String, Integer> get(String key){
        return container.get(key);
    }

    public static int getIdByName(String cell, String name){
        return container.get(cell).get(name);
    }

}
