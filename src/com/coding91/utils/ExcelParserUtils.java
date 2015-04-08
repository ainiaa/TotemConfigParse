package com.coding91.utils;

import com.coding91.ui.NoticeMessageJFrame;
import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.CellType;
import jxl.FormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.formula.FormulaException;
import jxl.read.biff.BiffException;

/**
 *
 * @author Administrator
 */
public class ExcelParserUtils {

    public static String[][] parseXls(String filePath) throws IOException, BiffException {
        //通过Workbook的静态方法getWorkbook选取Excel文件
        WorkbookSettings workbookSettings = getWorkbookSettings();
        Workbook workbook = Workbook.getWorkbook(new File(filePath), workbookSettings);
        int sheetTotal = workbook.getNumberOfSheets();
        Sheet sheet;
        int rows, cols;
        Cell cells[][];
        String[][] finalContents = {};
        String[][] tmpContents;
        //通过Workbook的getSheet方法选择第一个工作簿（从0开始）
        for (int sheetNum = 0; sheetNum < sheetTotal; sheetNum++) {
            sheet = workbook.getSheet(sheetNum);
            rows = sheet.getRows();
            cols = sheet.getColumns();
            cells = new Cell[cols][rows];
            tmpContents = new String[rows][cols];
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    cells[i][j] = sheet.getCell(i, j);
                    if (cells[i][j].getType() == CellType.DATE) {//对日期数据进行特殊处理，如果不处理的话 默认24h制会变成12小时制
                        tmpContents[j][i] = DateTimeUtils.formatTime(cells[i][j]);
                    } else {
                        String tmpContent = cells[i][j].getContents();
                        tmpContent = tmpContent.replace("\\'", "\'");//防止单引号已经被转义了 如果没有这一步骤的话 被转义的单引号就会出现问题
                        tmpContent = tmpContent.replace("\'", "\\'");//将所有的单引号进行转义
                        tmpContents[j][i] = tmpContent;
                    }
                }
            }
            if (finalContents.length > 0) {
                finalContents = ArrayUtils.arrayUnite(finalContents, tmpContents);//合并到最终的数组中
            } else {
                finalContents = tmpContents;
            }
        }

        workbook.close();
        return finalContents;
    }

    public static String[][] parseXls(String filePath, int sheetNum, boolean escapeSlash) throws IOException, BiffException {
        String[][] finalContents = parseXls(filePath, sheetNum, true, escapeSlash);
        return finalContents;
    }

    public static WorkbookSettings getWorkbookSettings() {
        return getWorkbookSettings("ISO-8859-1");
    }

    public static WorkbookSettings getWorkbookSettings(String encoding) {
        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setEncoding(encoding); //关键代码，解决乱码
        return workbookSettings;
    }

    public static String[][] parseXls(String filePath, int sheetNum, boolean reverse, boolean escapeSlash) throws IOException, BiffException {
        //通过Workbook的静态方法getWorkbook选取Excel文件
        WorkbookSettings workbookSettings = getWorkbookSettings();
        Workbook workbook = Workbook.getWorkbook(new File(filePath), workbookSettings);
        //通过Workbook的getSheet方法选择第一个工作簿（从0开始）
        Sheet sheet = workbook.getSheet(sheetNum);
        int rows = sheet.getRows();
        int cols = sheet.getColumns();
        String[][] finalContents;
        if (reverse) {
            finalContents = new String[rows][cols];
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    finalContents[j][i] = getSingleCellContent(sheet, i, j, escapeSlash);
                }
            }
        } else {
            finalContents = new String[cols][rows];
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    finalContents[i][j] = getSingleCellContent(sheet, i, j, escapeSlash);
                }
            }
        }

        workbook.close();
        return finalContents;
    }

    public static String getSingleCellContent(Sheet sheet, int i, int j, boolean escapeSlash) {
        Cell currentCell = sheet.getCell(i, j);
        String finalCellContent;
        CellType cellType = currentCell.getType();
        if (cellType == CellType.DATE) {//对日期数据进行特殊处理，如果不处理的话 默认24h制会变成12小时制
            finalCellContent = DateTimeUtils.formatTime(currentCell);
        } else if (cellType == CellType.NUMBER_FORMULA || cellType == CellType.BOOLEAN_FORMULA || cellType == CellType.DATE_FORMULA || cellType == CellType.FORMULA_ERROR || cellType == CellType.STRING_FORMULA) {//todo 这个估计还是有问题。。
            FormulaCell currentFormulaCell = (FormulaCell) currentCell;
            String cellContent = currentFormulaCell.getContents();
            if (!StringUtils.isNumeric(cellContent) && escapeSlash) {
                cellContent = cellContent.replace("\\'", "\'");//防止单引号已经被转义了 如果没有这一步骤的话 被转义的单引号就会出现问题
                cellContent = cellContent.replace("\'", "\\'");
            }
            finalCellContent = cellContent;
            try {
                NoticeMessageJFrame.noticeMessage(i + " line " + j + "column  is formula:" + currentFormulaCell.getFormula());
            } catch (FormulaException ex) {
                NoticeMessageJFrame.noticeMessage(ex.getClass() + ":" + ex.getMessage());
            }

        } else {
            String tmpContent = currentCell.getContents();
            if (!StringUtils.isNumeric(tmpContent) && escapeSlash) {
                tmpContent = tmpContent.replace("\\'", "\'");//防止单引号已经被转义了 如果没有这一步骤的话 被转义的单引号就会出现问题
                tmpContent = tmpContent.replace("\'", "\\'");
            }
            finalCellContent = tmpContent;
        }
        return finalCellContent;
    }

    public static String getSheetNameBySheetIndex(String filePath, int sheetIndex) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(filePath));
        String[] sheetNames = workbook.getSheetNames();
        String sheetName = sheetNames[sheetIndex];
        return sheetName;
    }

    public static int getSheetIndexBySheetName(String filePath, String sheetName) throws IOException, BiffException {
        int sheetIndex = 0;
        Workbook workbook = Workbook.getWorkbook(new File(filePath));
        String[] sheetNames = workbook.getSheetNames();
        for (int index = 0; index < sheetNames.length; index++) {
            String currentSheetName = sheetNames[index];
            if (sheetName.equals(currentSheetName)) {
                sheetIndex = index;
                break;
            }
        }
        return sheetIndex;
    }

    public static int getSheetNumber(String filePath) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(filePath));
        int sheetNumber = workbook.getNumberOfSheets();
        return sheetNumber;
    }
}
