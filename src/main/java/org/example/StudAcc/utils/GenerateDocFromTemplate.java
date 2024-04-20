package org.example.StudAcc.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.example.StudAcc.model.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenerateDocFromTemplate {

    private static final String pathToDoc = "src\\main\\document\\word\\";
    private FileOutputStream fileOutputStream;
    private XWPFDocument document;
    private XWPFDocument readingDoc;
    private List<XWPFParagraph> paragraphsAfterTable;
    private final Map<String, String> keyWords;

    @Autowired
    public GenerateDocFromTemplate(@Lazy Map<String, String> keyWords) {
        this.keyWords = keyWords;
    }


    public void init(String fileName) {
        try {
            document = new XWPFDocument();
            fileOutputStream = new FileOutputStream(pathToDoc + fileName + ".docx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTemplate(String path){
        try {
            readingDoc = new XWPFDocument(OPCPackage.open(path));
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void create() {
        try {
            document.write(fileOutputStream);
            fileOutputStream.close();
            document.close();
            readingDoc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceTextInDocumentBody() {
        List<XWPFParagraph> paragraphs = readingDoc.getParagraphs();
        for(int i = 0; i < paragraphs.size(); i++) {
            try {
                if (paragraphs.get(i).getRuns().get(0).getText(0).contains("${afterTable}")) {
                    addParagraphsAfterTable(paragraphs, ++i);
                    break;
                }
            } catch (IndexOutOfBoundsException ignored){}
            XWPFParagraph mainParagraph = document.createParagraph();
            setStylesForParagraph(paragraphs.get(i), mainParagraph);
            for (XWPFRun run : paragraphs.get(i).getRuns()) {
                XWPFRun mainRun = mainParagraph.createRun();
                setStylesForRun(run, mainRun);
                String text = run.getText(0);
                for (Map.Entry<String, String> entry : keyWords.entrySet()) {
                    if (text.contains("${" + entry.getKey() + "}")) {
                        text = text.replace("${" + entry.getKey() + "}", entry.getValue());
                    }
                }
                mainRun.setText(text);
            }
        }
    }

    private void addParagraphsAfterTable(List<XWPFParagraph> paragraphs, int pos){
        paragraphsAfterTable = new ArrayList<>();
        for(int i = pos; i < paragraphs.size(); i++)
            paragraphsAfterTable.add(paragraphs.get(i));
    }

    private void setParagraphsAfterTable(){
        try {
            if (!paragraphsAfterTable.isEmpty())
                for (XWPFParagraph paragraph : paragraphsAfterTable) {
                    document.createParagraph();
                    document.setParagraph(paragraph, document.getParagraphs().size() - 1);
                }
        } catch (NullPointerException ignored){}
    }

    public void completionOfDataOnTable(List<Student> studentList) {
        XWPFTable table = null;
        XWPFTable explanation;
        List<XWPFTable> tables = readingDoc.getTables();
        for(XWPFTable t: tables) {
            if (t.getRows().size() > 1) {
                table = t;
            } else {
                explanation = t;
                XWPFTable mainExplanation = document.createTable();
                mainExplanation.removeRow(0);
                replaceTextOnExplanation(explanation, mainExplanation, keyWords);
                setStyleForTable(explanation, mainExplanation);
                setStylesForCells(explanation, mainExplanation, 0);
            }
            document.createParagraph().createRun().setText("\n");
        }
        if(table != null) {
            XWPFTable mainTable = document.createTable();
            mainTable.removeRow(0);
            mainTable.addRow(table.getRows().get(0));
            try {
                mainTable.setTableAlignment(table.getTableAlignment());
            } catch (NullPointerException ignored){}
            setStyleForTitle(table.getRow(0), mainTable.getRow(0));
            Map<Integer, String> titles = new HashMap<>();
            List<XWPFTableCell> cells = table.getRows().get(1).getTableCells();
            for (int i = 0; i < cells.size(); i++)
                titles.put(i, cells.get(i).getText());
            dataSubstitutionOnTableRow(titles, mainTable, studentList);
            setStylesForCells(table, mainTable, 1);
            setParagraphsAfterTable();
        }
    }

    private void replaceTextOnExplanation(XWPFTable table, XWPFTable mainTable, Map<String, String> keyWords){
        for(XWPFTableRow row: table.getRows()) {
            XWPFTableRow mainRow = mainTable.createRow();
            mainRow.removeCell(0);
            for (XWPFTableCell cell : row.getTableCells()) {
                XWPFTableCell mainCell = mainRow.createCell();
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    XWPFParagraph mainParagraph = mainCell.addParagraph();
                    for (XWPFRun run : paragraph.getRuns()) {
                        XWPFRun mainRun = mainParagraph.createRun();
                        String text = run.getText(0);
                        for (Map.Entry<String, String> entry : keyWords.entrySet()) {
                            if (text.contains("${" + entry.getKey() + "}")) {
                                text = text.replace("${" + entry.getKey() + "}", entry.getValue());
                            }
                        }
                        mainRun.setText(text);
                    }
                }
            }
        }
    }

    private void dataSubstitutionOnTableRow(Map<Integer, String> titles, XWPFTable mainTable, List<Student> data){
        int count = 0;
        for(Student student: data) {
            XWPFTableRow tableRow = mainTable.createRow();
            for (Map.Entry<Integer, String> entry : titles.entrySet()) {
                switch (entry.getValue()) {
                    case "${serial}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(String.valueOf(++count));
                    case "${initials}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getInitials());
                    case "${shortInitials}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getShortName());
                    case "${address}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getAddress().toString());
                    case "${orderNumber}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getOrderNumber().getName());
                    case "${caseNumber}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getCaseNumber());
                    case "${birthDate}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getBirthDate().toString());
                    case "${education}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getEducation().getName());
                    case "${snils}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getSnils());
                    case "${phone}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(student.getPhone());
                    case "${akadem}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(getStringByBool(student.isAkadem()));
                    case "${freeVisit}" ->
                            tableRow.getCell(entry.getKey()).getParagraphs().get(0).createRun().setText(getStringByBool(student.isFreeVisit()));
                }
            }
        }
    }

    private String getStringByBool(boolean value){
        if (value) return "Да";
        else return "Нет";
    }

    private void setStyleForTitle(XWPFTableRow rowTitle, XWPFTableRow mainRowTitle){
        for(int i = 0; i < rowTitle.getTableCells().size(); i++){
            setStylesForParagraph(rowTitle.getCell(i).getParagraphs().get(0), mainRowTitle.getCell(i).getParagraphs().get(0));
            try {
                setStylesForRun(rowTitle.getCell(i).getParagraphs().get(0).getRuns().get(0), mainRowTitle.getCell(i).getParagraphs().get(0).getRuns().get(0));
            }catch (IndexOutOfBoundsException ignored){}
        }
    }

    private void setStylesForCells(XWPFTable table, XWPFTable mainTable, int pos){
        List<XWPFTableCell> template = table.getRow(pos).getTableCells();
        for(int i = 1; i < mainTable.getRows().size(); i++){
            List<XWPFTableCell> cells = mainTable.getRow(i).getTableCells();
            for(int j = 0; j < cells.size(); j++){
                XWPFTableCell mainCell = cells.get(j);
                mainCell.setWidth(String.valueOf(template.get(j).getWidth()));
                mainCell.setWidthType(template.get(j).getWidthType());
                if(template.get(j).getVerticalAlignment() != null) mainCell.setVerticalAlignment(template.get(j).getVerticalAlignment());
                mainCell.setColor(template.get(j).getColor());
                setStylesForParagraph(template.get(j).getParagraphs().get(0), mainCell.getParagraphs().get(0));
                try {
                    setStylesForRun(template.get(j).getParagraphs().get(0).getRuns().get(0), mainCell.getParagraphs().get(0).getRuns().get(0));
                } catch (IndexOutOfBoundsException ignored){}
            }
        }
    }

    public void setStylesForParagraph(XWPFParagraph paragraph, XWPFParagraph mainParagraph){
        mainParagraph.setAlignment(paragraph.getAlignment());
        mainParagraph.setBorderBetween(paragraph.getBorderBetween());
        mainParagraph.setSpacingBetween(paragraph.getSpacingBetween());
        mainParagraph.setSpacingLineRule(paragraph.getSpacingLineRule());
        mainParagraph.setSpacingAfter(paragraph.getSpacingAfter());
        mainParagraph.setSpacingBefore(paragraph.getSpacingBefore());
        mainParagraph.setFirstLineIndent(paragraph.getFirstLineIndent());
        mainParagraph.setSpacingAfterLines(paragraph.getSpacingAfterLines());
        mainParagraph.setSpacingBeforeLines(paragraph.getSpacingBeforeLines());
        mainParagraph.setFontAlignment(paragraph.getFontAlignment());
        mainParagraph.setVerticalAlignment(paragraph.getVerticalAlignment());
        mainParagraph.setStyle(paragraph.getStyle());
    }

    public void setStylesForRun(XWPFRun run, XWPFRun mainRun){
        if(run.getFontSizeAsDouble() != null)
            mainRun.setFontSize(run.getFontSizeAsDouble());
        mainRun.setFontFamily(run.getFontFamily());
        mainRun.setBold(run.isBold());
        mainRun.setTextPosition(run.getTextPosition());
        mainRun.setItalic(run.isItalic());
        mainRun.setStyle(run.getStyle());
        mainRun.setCharacterSpacing(run.getCharacterSpacing());
        mainRun.setUnderline(run.getUnderline());
    }

    public void setStyleForTable(XWPFTable table, XWPFTable mainTable){
        if(table.getTableAlignment() != null)
            mainTable.setTableAlignment(table.getTableAlignment());
        else
            mainTable.setTableAlignment(TableRowAlign.LEFT);
        mainTable.setBottomBorder(table.getBottomBorderType(), table.getBottomBorderSize(), table.getBottomBorderSpace(), table.getBottomBorderColor());
        mainTable.setLeftBorder(table.getLeftBorderType(), table.getLeftBorderSize(), table.getLeftBorderSpace(), table.getLeftBorderColor());
        mainTable.setRightBorder(table.getRightBorderType(), table.getRightBorderSize(), table.getRightBorderSpace(), table.getRightBorderColor());
        mainTable.setBottomBorder(table.getBottomBorderType(), table.getBottomBorderSize(), table.getBottomBorderSpace(), table.getBottomBorderColor());
        mainTable.setTopBorder(table.getTopBorderType(), table.getTopBorderSize(), table.getTopBorderSpace(), table.getTopBorderColor());
        mainTable.setInsideHBorder(table.getInsideHBorderType(), table.getInsideHBorderSize(), table.getInsideHBorderSpace(), table.getInsideHBorderColor());
        mainTable.setInsideVBorder(table.getInsideVBorderType(), table.getInsideVBorderSize(), table.getInsideVBorderSpace(), table.getInsideVBorderColor());
    }
}
