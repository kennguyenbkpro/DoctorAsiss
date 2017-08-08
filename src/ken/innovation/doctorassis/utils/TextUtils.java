package ken.innovation.doctorassis.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.NumberFormatter;

public class TextUtils {
	public static boolean isEmpty(String s){
		return s == null || s.length() == 0;
	}
	
	public static String convertSex(int sex){
		switch (sex) {
		case 0:
			return "Nam";
		case 1:
			return "Nữ";
		default:
			return "Khác";
		}
	}
	
	public static NumberFormatter getIntFormater(){
		NumberFormat format = NumberFormat.getIntegerInstance();
		NumberFormatter numberFormatter = new NumberFormatter(format);
		numberFormatter.setValueClass(Integer.class); //optional, ensures you will always get a int value
		numberFormatter.setAllowsInvalid(false); //this is the key!!
		return numberFormatter;
	}
	
	private static SimpleDateFormat dateFormat;
	public static String getDateStringFrom(long timeInMillis){
		Date date = new Date(timeInMillis);
		if (dateFormat == null){
			dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		}
		return dateFormat.format(date);
	}
	
	private static SimpleDateFormat displayDateFormat;
	public static String getPrintableDateStringFrom(long timeInMillis){
		Date date = new Date(timeInMillis);
		if (displayDateFormat == null){
			displayDateFormat = new SimpleDateFormat("'Ngày' dd 'tháng' MM 'năm' yyyy");
		}
		return displayDateFormat.format(date);
	}
	
	public static String readFile(String path) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}
	
	public static void writeToFile(String path, String content) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter out = new PrintWriter(path, StandardCharsets.UTF_8.name());
		out.println(content);
		out.close();
	}
}

