package ken.innovation.doctorassis.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ken.innovation.doctorassis.dbmodel.PatientResource;
import ken.innovation.doctorassis.utils.TextUtils;

public class PatientResourceImportHelper {
	
	public static ArrayList<PatientResource> loadPatientResourceFromExcel(String filePath) throws IOException{
		FileInputStream inputStream = new FileInputStream(filePath);
		
		return loadPatientResourceFromExcel(inputStream);
	}
	
	public static ArrayList<PatientResource> loadPatientResourceFromExcel(File file) throws IOException{
		FileInputStream inputStream = new FileInputStream(file);
		
		return loadPatientResourceFromExcel(inputStream);
	}
	
	public static ArrayList<PatientResource> loadPatientResourceFromExcel(FileInputStream inputStream) throws IOException{
		ArrayList<PatientResource> listPatientResources = new ArrayList<>();
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> iterator = sheet.iterator();
		int count = 0;
		while (iterator.hasNext() && count < 10000){
			count ++;
			Row row = iterator.next();
			if (row.getRowNum() == 0) continue;//Ignore header row
			Iterator<Cell> cellIterator = row.cellIterator();
			PatientResource patientResource = new PatientResource();
			while (cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				String val = getCellValue(cell);
				int col = cell.getColumnIndex();
				switch (col) {
				case 0:
					patientResource.setName(val);
					break;
				case 1:
					patientResource.setAddress(val);
					break;
				case 2:
					try {
						patientResource.setYearOfBirth(Float.valueOf(val).intValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 3:
					patientResource.setPhoneNumber(val);
					break;
				case 4:
					try {
						patientResource.setGender(Float.valueOf(val).intValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 5:
					patientResource.setInsuranceNumber(val);
					break;
				default:
					break;
				}
			}
			if (TextUtils.isEmpty(patientResource.getName())){
				break;
			}
			listPatientResources.add(patientResource);
		}
		return listPatientResources;
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
