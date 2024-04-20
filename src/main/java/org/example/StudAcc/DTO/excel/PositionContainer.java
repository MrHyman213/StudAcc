package org.example.StudAcc.DTO.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionContainer {

    private final List<String> columns = List.of(
            "Фамилия", "Имя", "Отчество", "Дата рождения", "Пол",
            "СНИЛС", "Группа", "№ приказа", "Дата приказа",
            "Дата окончания предыдущего обучения", "Образование", "Адрес места жительства"
    );

    private Map<Integer, String> positions;

    public List<String> getColumns(){
        return columns;
    }

    public void addPosition(int position, String column) {
        positions.put(position, column);
    }

    public String getColumn(int position) {
        return positions.get(position);
    }

    public PositionContainer() {
        positions = new HashMap<>();
    }
}
