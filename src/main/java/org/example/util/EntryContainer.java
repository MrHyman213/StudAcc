package org.example.util;

import org.example.DTO.entries.Entry;
import org.example.DTO.student.ShortStudent;
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
        lists.put("lists", List.of(" - Группы и специальности", " - Области",
                " - Номера приказов", " - Преподаватели", " - Дисциплины",
                " - Предыдущее образование", " - Организация", " - Пользователи"));
        lists.put("views", List.of("groupAndSpecial", "region", "orderNumber",
                "teacher", "discipline", "education", "organization", "user"));
    }
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ModelMapper getModelMapper(){
        return modelMapper;
    }

    public static String getViewByListName(String name){
        return lists.get("views").get(getIndexByName(lists.get("lists"), name));
    }

    public static void addList(String key, List<String> list){
        if (lists.get(key) == null || !lists.get(key).isEmpty())
            lists.put(key, list);
        else
            lists.replace(key, list);
    }

    public static int getIndexByName(List<String> list, String name){
        for (int i = 0; i < list.size(); i++){
            if(Objects.equals(name, list.get(i)))
                return i;
        }
        return 0;
    }

    public static List<String> getList(String key){
        return lists.get(key);
    }

    public static void put(String key, Map<String, Integer> value){
        if(container.get(key) == null || !container.get(key).isEmpty())
            container.put(key, value);
        else container.replace(key, value);
    }

    public static int getIdByName(String cell, String name){
        return container.get(cell).get(name);
    }

}
