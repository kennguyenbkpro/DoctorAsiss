package ken.innovation.doctorassis.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFHeaderFooter;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ken.innovation.doctorassis.appmodel.FullPrescription;
import ken.innovation.doctorassis.dbmodel.MediPres;
import ken.innovation.doctorassis.dbmodel.Patient;
import ken.innovation.doctorassis.dbmodel.Prescription;
import ken.innovation.doctorassis.utils.Config;
import ken.innovation.doctorassis.utils.TextUtils;

public class PrescriptionExportHelper {
	public static void main(String[] args)throws Exception 
	{
		Patient patient = (Patient) Patient.searchPatientByName(DBConnection.getInstance().createStatement(), "Nguyễn Trọng Cầu").get(0);
		FullPrescription fullPrescription = Prescription.getByPatientID(DBConnection.getInstance().createStatement(), patient.getId()).get(0);
		readTemplate("F:\\workspace\\Java\\DoctorAssis\\template.xlsx", fullPrescription, Config.MAIN_PATH + "\\Pres\\createdocument.xlsx");
		
	}
	
	public static File readTemplate(String templatePath, FullPrescription prescription, String outPath) throws IOException{
		FileInputStream inputStream = new FileInputStream(templatePath);
		
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		
		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		XSSFHeaderFooter footer = (XSSFHeaderFooter) sheet.getFooter();
		footer.setRight(footer.getRight().replace("#s12", Config.getInstance().rightFooter));
		footer.setLeft(footer.getLeft().replace("#s11", Config.getInstance().leftFooter));
		
		Iterator<Row> iterator = sheet.iterator();
		while (iterator.hasNext()){
			Row row = iterator.next();
			Iterator<Cell> cellIte = row.cellIterator();
			while (cellIte.hasNext()){
				Cell cell = cellIte.next();
				if (cell.toString().contains("#s01")){
					cell.setCellValue(prescription.getPatient().getName());
				} else if (cell.toString().contains("#s02")){
					cell.setCellValue(prescription.getPatient().getYearOfBirth());
				} else if (cell.toString().contains("#s03")){
					cell.setCellValue(prescription.getPatient().getInsuranceNumber());
				} else if (cell.toString().contains("#s04")){
					cell.setCellValue(prescription.getPatient().getPhoneNumber());
				} else if (cell.toString().contains("#s05")){
					cell.setCellValue(prescription.getPatient().getAddress());
				} else if (cell.toString().contains("#s06")){
					cell.setCellValue(prescription.getDisease().getNameWithCode());
				} else if (cell.toString().contains("#s07")){
					cell.setCellValue(prescription.getDisease().getAdvice());
				} else if (cell.toString().contains("#s08")){
					cell.setCellValue(TextUtils.getPrintableDateStringFrom(prescription.getPrescription().getTime()));
				} else if (cell.toString().contains("#s09")){
					cell.setCellValue(prescription.getPrescription().getDoctor());
				} else if (cell.toString().contains("#s10")){
					cell.setCellValue(" ");
					ArrayList<MediPres> listMediPres = prescription.getListMedicine();
					int rowNum = cell.getRow().getRowNum();
					if (listMediPres.size() > 1){
						sheet.shiftRows(rowNum + 3, sheet.getLastRowNum(), (listMediPres.size() - 1) * 2);
					}
					Row row1 = sheet.getRow(rowNum + 1);
					Row row2 = sheet.getRow(rowNum + 2);
					setData(row1, row2, listMediPres.get(0), 1);
					for (int i = 1; i < listMediPres.size(); i ++){
						row1 = copyRow(workbook, sheet, rowNum + 1, rowNum + 2 * i + 1);
						row2 = copyRow(workbook, sheet, rowNum + 2, rowNum + 2 * i + 2);
						setData(row1, row2, listMediPres.get(i), i + 1);
					}
				} else if (cell.toString().contains("#s11")){
					cell.setCellValue(Config.getInstance().rightFooter);
				} else if (cell.toString().contains("#s12")){
					cell.setCellValue(Config.getInstance().leftFooter);
				} 
			}
		}
		
		File outFile = new File(outPath);
		if (!outFile.exists()){
			outFile.getParentFile().mkdirs();
		}
		FileOutputStream out = new FileOutputStream(outFile);
		workbook.write(out);
		out.close();
		
		
		return outFile;
	}
	
	private static void setData(Row row1, Row row2, MediPres mediPres, int order){
		row1.getCell(0).setCellValue(order + ". ");
		row1.getCell(1).setCellValue(mediPres.getMedicine() + " x " + mediPres.getAmount() + " " + mediPres.getUnit());
		row2.getCell(1).setCellValue(mediPres.getDose());
	}
	
	private static Row copyRow(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        Row newRow = worksheet.getRow(destinationRowNum);
        Row sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1, true, false);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

//         If there are are any merged regions in the source row, copy to new row
        for (int i = worksheet.getNumMergedRegions() - 1; i >= 0; i --) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                        )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
        return newRow;
    }
}
