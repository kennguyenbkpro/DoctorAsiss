package ken.innovation.doctorassis.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ken.innovation.doctorassis.dbmodel.Medicine;
import ken.innovation.doctorassis.utils.TextUtils;

public class MedicineImportHelper {
	
	public static ArrayList<Medicine> loadMedicineFromExcel(String filePath) throws IOException{
		FileInputStream inputStream = new FileInputStream(filePath);
		
		return loadMedicineFromExcel(inputStream);
	}
	
	public static ArrayList<Medicine> loadMedicineFromExcel(File file) throws IOException{
		FileInputStream inputStream = new FileInputStream(file);
		
		return loadMedicineFromExcel(inputStream);
	}
	
	public static ArrayList<Medicine> loadMedicineFromExcel(FileInputStream inputStream) throws IOException{
		ArrayList<Medicine> listMedicines = new ArrayList<>();
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> iterator = sheet.iterator();
		int count = 0;
		while (iterator.hasNext() && count < 10000){
			count ++;
			Row row = iterator.next();
			if (row.getRowNum() == 0) continue;//Ignore header row
			Iterator<Cell> cellIterator = row.cellIterator();
			Medicine medicine = new Medicine();
			while (cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				String val = getCellValue(cell);
				int col = cell.getColumnIndex();
				switch (col) {
				case 0:
					medicine.setName(val);
					break;
				case 1:
					medicine.setIngredient(val);
					break;
				case 2:
					
					medicine.setDose(val);
					break;
				case 3:
					medicine.setUsing(val);
					break;
				case 4:
					medicine.setUnit(val);
					break;
				default:
					break;
				}
			}
			if (TextUtils.isEmpty(medicine.getName())){
				break;
			}
			listMedicines.add(medicine);
		}
		return listMedicines;
	}
	
	private static String getCellValue(Cell cell){
		int type = cell.getCellType();
		switch (type) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue() + "";
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		default:
			return "";
		}
	}
}
