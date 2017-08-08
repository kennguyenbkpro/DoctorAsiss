package ken.innovation.doctorassis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {
	public static final String MAIN_PATH = "F:\\workspace\\Java\\DoctorAssis";
//	public static final String MAIN_PATH = ".";
	
	public static final String DB_PATH = MAIN_PATH + "\\DoctorAssistant";
	
	public static final String CONFIG_PATH = MAIN_PATH + "\\config.json";
	
	public static final String TEMPLATE_PRES_PATH = MAIN_PATH + "\\template.xlsx";
	
	private static Config mConfig;
	
	public static Config getInstance(){
		if (mConfig == null){
			mConfig = new Config();
			mConfig.leftFooter = "Hẹn tái khám sau 7 ngày";
			mConfig.rightFooter = "Liên hệ: Số điện thoại BS";
			mConfig.printLoop = 1;
			mConfig.lastXlsxFile = MAIN_PATH;
		}
		return mConfig;
	}
	
	public void load() throws IOException, JSONException{
		File file = new File(CONFIG_PATH);
		if (!file.exists()){
			file.createNewFile();
		}
		String json = TextUtils.readFile(CONFIG_PATH);
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has("doctor")){
			doctor = jsonObject.getString("doctor");
		}
		if (jsonObject.has("xlsx")){
			lastXlsxFile = jsonObject.getString("xlsx");
		}
		if (jsonObject.has("leftFooter")){
			leftFooter = jsonObject.getString("leftFooter");
		}
		if (jsonObject.has("rightFooter")){
			rightFooter = jsonObject.getString("rightFooter");
		}
		if (jsonObject.has("printLoop")){
			printLoop = jsonObject.getInt("printLoop");
		}
	}
	
	public void save() throws FileNotFoundException, UnsupportedEncodingException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("doctor", doctor);
		jsonObject.put("printLoop", printLoop);
		jsonObject.put("leftFooter", leftFooter);
		jsonObject.put("rightFooter", rightFooter);
		jsonObject.put("xlsx", lastXlsxFile);
		TextUtils.writeToFile(CONFIG_PATH, jsonObject.toString());
	}
	
	public String doctor;
	public String leftFooter;
	public String rightFooter;
	public int printLoop;
	
	public String lastXlsxFile;
}
