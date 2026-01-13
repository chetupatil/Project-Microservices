package com.nokia.ace.utils.util;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.nokia.ace.ppa.dto.PmatWorkPackageDTO;
import com.nokia.ace.utils.constants.PmatConstants;

/**
 * @author Chetana
 *
 */
@Component
public class CsvWriterHelper {


	private static final Logger LOGGER = LoggerFactory.getLogger(CsvWriterHelper.class);

	public void writeDumpHeaderIntoCSV(FileWriter csvWriter) {
		try {
			csvWriter.append(PmatConstants.CSV_HEADER_PROJECT_TYPE);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_SAF_UNIQUE_ID);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_PPA_ID);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_WORKPACKAGE_NAME);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_SAF_PROJECT_ID);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_SERVICE_OFFERING);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_SERVICE_SUB_TYPE);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_AGGREGATE_EFFORTS);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_OPPORTUNITY_NUMBER);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_GIC);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_LAST_MODIFIED);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_WP_START_DATE);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_WP_END_DATE);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_BUSSINESS_LINE);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_BUSSINESS_UNIT);
			csvWriter.append(";");
			csvWriter.append(PmatConstants.CSV_HEADER_IS_DELETED);
			csvWriter.append(";");
			csvWriter.append("\n");
		}catch(Exception e) {
			LOGGER.error("Exception occure while writing dump header into csv: "+e.getMessage());
		}
	}


	public void writeDumpDataIntoCsv(FileWriter csvWriter, List<PmatWorkPackageDTO> pWpList) {	
		try {
			for(PmatWorkPackageDTO pWp: pWpList) {
				csvWriter.append(pWp.getProspectType());
				csvWriter.append(";");
				csvWriter.append(pWp.getSafUniqueId());
				csvWriter.append(";");
				csvWriter.append(pWp.getPpaId());
				csvWriter.append(";");
				csvWriter.append(pWp.getWorkpackageName());
				csvWriter.append(";");
				csvWriter.append(pWp.getSafProjectId());
				csvWriter.append(";");
				csvWriter.append(pWp.getServiceOffering());
				csvWriter.append(";");
				csvWriter.append(pWp.getServiceSubType());
				csvWriter.append(";");
				csvWriter.append(pWp.getAggregateEfforts());
				csvWriter.append(";");
				csvWriter.append(pWp.getOpportunityNumber());
				csvWriter.append(";");
				csvWriter.append(pWp.getGic());
				csvWriter.append(";");
				csvWriter.append(pWp.getLastModifiedDate());
				csvWriter.append(";");
				csvWriter.append(pWp.getWpStartDate());
				csvWriter.append(";");
				csvWriter.append(pWp.getWpEndDate());
				csvWriter.append(";");
				csvWriter.append(pWp.getBussinessLine());
				csvWriter.append(";");
				csvWriter.append(pWp.getBussinessUnit());
				csvWriter.append(";");
				csvWriter.append(pWp.getIsDeleted());
				csvWriter.append("\n");
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while writing dump data into csv: "+e.getMessage());
		}
	}


	public String wpStartDate(Set<Long> longList) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat(PmatConstants.dateFormateSlash);
		long wpStartLong = longList.stream().mapToLong(v -> v).min().getAsLong();
		return sdf1.format(wpStartLong);
	}

	public String wpEndDate(Set<Long> longList) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat(PmatConstants.dateFormateSlash);
		long wpEndLong = longList.stream().mapToLong(v -> v).max().getAsLong();
		return sdf1.format(wpEndLong);
	}

	public String lastModified(Set<Long> longList) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat(PmatConstants.dateFormateSlash);
		long lastModifedLong = longList.stream().mapToLong(v -> v).max().getAsLong();
		return sdf1.format(lastModifedLong);
	}

	public String convertHoursToDays(String hours) {
		try {
			int a = Integer.parseInt(hours);
			a =  a/(60*60*8);
			return String.valueOf(a);
		}catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
		return "";
	}

	private String estimationCalcul(Set<Integer> aggregateEfforsList) {
		Integer affCount = 0;
		if(!aggregateEfforsList.isEmpty()) {
			affCount = aggregateEfforsList.stream().mapToInt(Integer::intValue).sum();
		}
		return String.valueOf(affCount);
	}

	public File createFileInResource(String fileName) {
		File file = null;
		try {
			Path resourceDirectory = Paths.get("src","main","resources","ppa_file");
			String absolutePath = resourceDirectory.toFile().getAbsolutePath();
			File fileDir = new File(absolutePath);
			if(!fileDir.isDirectory()) {
				fileDir.mkdir();
			}
			file = new File(absolutePath +File.separator+fileName);
			if (!file.isFile()) {
				file.createNewFile();
				return file;
			} 
		}catch(Exception e) {
			LOGGER.error("Exception occure while creating file in resource : "+e.getMessage());
		}
		return file;

	}

	public File fetchFileFromResource(String fileName) {
		File file = null;
		try {
			Path resourceDirectory = Paths.get("src","main","resources","ppa_file");
			String absolutePath = resourceDirectory.toFile().getAbsolutePath();
			file = new File(absolutePath + File.separator +fileName);
			if(file.isFile()) {
				return file;
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while fetching file from resource folder : "+e.getMessage());
		}
		return null;
	}

	public void deleteFileInResource(File path) {
		try {
			if(path.isFile()) {
				FileUtils.forceDelete(path);
				LOGGER.error("File deleted successfully");
			} else {
				LOGGER.error("File doesn't exist");
			}
		}catch(Exception e) {
			LOGGER.error("Exception occure while deleting file in resource : "+e.getMessage());
		}


	}
	public List<String> fetchPlProspectScope(String plProspectScope){
		List<String> blList = new ArrayList<String>();
		String[] plProScopList = plProspectScope.split(",");

		Map<String,String> mapPl = mapPlProspectScope();
		List<String> bList = getBl();
		for(String plProScope: plProScopList) {
			bList.stream().forEach(i ->{
				if(plProScope.contains(i)) {
					blList.add(mapPl.get(i));
				}
			});			
		}
		return blList;
	}

	private Map<String,String> mapPlProspectScope(){
		Map<String,String> mapPlProspectScope = new HashMap<String,String>();	
		mapPlProspectScope.put(PmatConstants.SCOPE_ANALYTICS, "Analytics");
		mapPlProspectScope.put(PmatConstants.SCOPE_DIGITAL_OPERATION, "DO");
		mapPlProspectScope.put(PmatConstants.SCOPE_MEDIATION, "Mediation");
		mapPlProspectScope.put(PmatConstants.SCOPE_CHARGING,"Charging");
		mapPlProspectScope.put(PmatConstants.SCOPE_DEVICE_MANAGMENT,"DM");
		mapPlProspectScope.put(PmatConstants.SCOPE_SECURITY,"Security");
		//		mapPlProspectScope.put(PmatConstants.SCOPE_CC_DDI,"");
		//		mapPlProspectScope.put(PmatConstants.SCOPE_S_S,"");
		return mapPlProspectScope;
	}

	private List<String> getBl(){
		List<String> blList = new ArrayList<String>();
		blList.add("Analytics");
		blList.add("Digital Operations");
		blList.add("Mediation");
		blList.add("Charging");
		blList.add("Device Management");
		blList.add("Security");
		return blList;

	}
	
	public boolean checkModifiedDate(String date) throws Exception {
		if(!StringUtils.isEmptyOrNull(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate1);
			long dataFromDump = sdf.parse(date).getTime();

			Date dateYesterday = new Date(new Date().getTime() - PmatConstants.MILLIS_IN_A_DAY);
			String dateYesterdayS = sdf.format(dateYesterday);
			long yesterday = sdf.parse(dateYesterdayS).getTime();

			Date dateToday = new Date(new Date().getTime());
			String dateTodayS = sdf.format(dateToday);
			long today = sdf.parse(dateTodayS).getTime();

			if(yesterday<dataFromDump && today>dataFromDump) {
				return true;
			}
		}
		return false;
	}
	
	public Map<String,String> diffServiceEndDate(String safStartDate, String safEndDate, String serviceStartDate) throws Exception {
		Map<String,String> dateMap = new HashMap<String,String>();
		
		SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate1);
		SimpleDateFormat safJiraUpdate = new SimpleDateFormat(PmatConstants.JiraDateFormate);
		
		Date startDateP = safJiraUpdate.parse(safStartDate);
		Date endDateP = safJiraUpdate.parse(safEndDate);
		Date servicestartP = sdf.parse(serviceStartDate);
		
		long diff_in_time = endDateP.getTime()-startDateP.getTime();
		long days = (diff_in_time/ (1000 * 60 * 60 * 24))% 365;
		
		Instant sI = servicestartP.toInstant();
		
		Instant serviceEndDateI = sI.plus(days, ChronoUnit.DAYS);
		Date finalEndDate = Date.from(serviceEndDateI);
		
		String serviceStartDatef = safJiraUpdate.format(servicestartP);
		String serviceEndDate = safJiraUpdate.format(finalEndDate);
		
		dateMap.put("startDate", serviceStartDatef);
		dateMap.put("endDate", serviceEndDate);
		
		
		return dateMap;
	}



}
