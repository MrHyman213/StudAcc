package org.example.StudAcc.DTO.doc.pattern;

import java.util.HashMap;
import java.util.Map;

public class StudentListPattern {
    public final static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(1, "№ п/п");
        map.put(2, "Фамилия, Имя, Отчество");
        map.put(3, "№ личного дела");
        map.put(4, "Дата, № Приказа о зачислении");
        map.put(5, "Дата, № Приказа об отчислении");
        map.put(6, "Примечание");
    }
}
