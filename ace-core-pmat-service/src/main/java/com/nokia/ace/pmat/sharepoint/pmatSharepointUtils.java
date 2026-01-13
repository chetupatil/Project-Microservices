package com.nokia.ace.pmat.sharepoint;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jgit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;
import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.environmentVariables.ReadEnvironmentVariables;
import com.nokia.ace.pmat.dao.PmatProjectMasterDao;
import com.nokia.ace.pmat.dto.PmatProjectMaster;
import com.nokia.ace.utils.constants.PmatConstants;
import com.nokia.ace.utils.util.CsvWriterHelper;

@Component
public class pmatSharepointUtils {
	
	   @Autowired
	   private RestTemplate restTemplate;
	   
	   @Autowired
	   private CsvWriterHelper csvWriterHelper;
	   
	   @Value("${pmat.sp.main.site}")
	   private String pmatSpMainSite;
	   
	   @Value("${pmat.sp.sub.site}")
	   private String pmatSpSubSite;
	   
	   @Value("${pmat.sp.pmat.folder}")
	   private String pmatSpPmatFolder;
	   
	   @Value("${pmat.dump.file}")
	   private String pmatFile;
	   
	   @Autowired
	   private PmatProjectMasterDao pmatProjectMasterDao;
	   
	   @Autowired
		private ReadEnvironmentVariables readEnvironmentVariables;
	   
	   private static final Logger LOGGER = LoggerFactory.getLogger(pmatSharepointUtils.class);

	   
	   public void fetchPmatDumpAndDownloadFileByDefPath(List<String> cookies,String digestValue, String hostUrl) {
		      String apiUrl = hostUrl + "/sites/" + pmatSpMainSite + "/_api/web/GetFolderByServerRelativeUrl('/sites/"+ pmatSpMainSite + "/" + pmatSpSubSite + "/" + pmatSpPmatFolder + "')/Files('" + pmatFile + "')/$value";
		      //String apiUrl = "https://nokia.sharepoint.com/sites/cns_ace_reports/_api/web/GetFolderByServerRelativeUrl('/sites/cns_ace_reports/Shared%20Documents/PMAT_DUMP')/Files('PMAT_DUMP.csv')/$value";
		      try {
		         HttpHeaders headers = new HttpHeaders();
		         headers.add("Cookie", Joiner.on(';').join(cookies));
		         headers.add("Accept", "application/json");
		         headers.add("Content-Type", "application/json");
		         headers.add("X-RequestDigest", digestValue);
		         HttpEntity<String> entity = new HttpEntity<String>(null,headers);
		         ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
		         String successBody = responseEntity.getBody();
				 FileWriter wr = null;
		         File path = null;
		         try {
		            path = csvWriterHelper.createFileInResource(pmatFile);
		            wr = new FileWriter(path);
		            if(successBody!=null) {
		               wr.write(successBody);
		            }
		         }catch(Exception e) {
		            LOGGER.error("Exception while writing into the file:  "+e.getMessage());
		         }finally {
		            wr.flush();
		            wr.close();
		         }
					
		          if(path!=null) {
		              try(BufferedReader in = new BufferedReader(new FileReader(path))){
		            	  String strLine = "";
		            	  in.readLine();
		            	  Map<String,String> marketList = marketList();
		            	  pmatProjectMasterDao.alterCharacterset();
		            	  int countPmatData = pmatProjectMasterDao.countPmatProjectMaster();
		            	  while( (strLine = in.readLine()) != null) {
		            		  String[] x = stringValueReplace(strLine);
		            		  if(x.length < 41) {continue;}
		            		  else { filterAndSaveData(x,marketList,countPmatData); }
		            	  }   
		              	} catch(Exception e) {
		              		LOGGER.error("Exception occure while saving dump data into db:  "+e.getMessage());
		              		e.printStackTrace();
		              }
		           }					 
		         if(path!=null) {
		            path.delete();
		         }	         
		      } catch (Exception e) {
		         LOGGER.error("Exception occure while saving dump data into db:  "+e.getMessage());
		      }
		   }
	   
	   private void filterAndSaveData(String[] x, Map<String, String> marketList, int countPmatData) throws Exception {
           String modified = parseDate(excedeLimit(x.length,33,x));
           String pmatId = excedeLimit(x.length,7,x);
           String projectOwner = excedeLimit(x.length,5,x);
           String owner = null;
           if(projectOwner.contains("(")) {
           	int endIndex = projectOwner.indexOf("(");
           	 owner  = projectOwner.substring(0, endIndex).trim();
           } else owner= projectOwner;		                   
           String scrmId = excedeLimit(x.length,18,x);
           String market = mapMarketToStandCode(marketList,excedeLimit(x.length,22,x));
           if(countPmatData>0) {
              boolean isLatestDate = checkModifiedDate(modified);
              if(isLatestDate) {
           	     int check = pmatProjectMasterDao.checkIfRecordExists(pmatId, scrmId);
           	     if(check == 0) {
           	    	insertIntoPmatProjectMasterTable(x,owner,pmatId,market,true);
           	     }else {
           	    	insertIntoPmatProjectMasterTable(x,owner,pmatId,market,false);
           	     }
                 
              }
           }else {
           	insertIntoPmatProjectMasterTable(x,owner,pmatId,market,true);
           }
		
	}
		public boolean checkModifiedDate(String date) throws Exception {
			if(!StringUtils.isEmptyOrNull(date)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				long dataFromDump = parseModifiedDate(date);

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
		
		   public long parseModifiedDate(String inputDate) throws ParseException {
			   SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH);
			   SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
			   Boolean validFormat1 = isDateValid(format1,inputDate);
			   Boolean validFormat2 = isDateValid(format2,inputDate);
			   if(inputDate.length() > 1 && validFormat1) {			   
			       // Date date = format1.parse(inputDate);
			        long outputDate = outputFormat.parse(inputDate).getTime();
					return outputDate;
			   } else if(inputDate.length() > 1 && validFormat2)	{
				   //String date = format2.parse(inputDate);
			        long outputDate = outputFormat.parse(inputDate).getTime();
					return outputDate;
			   }
			   else return 0 ;
			}

	private void insertIntoPmatProjectMasterTable(String[] x, String owner, String pmatId, String market, boolean check) throws ParseException {
		   PmatProjectMaster ppm = new PmatProjectMaster();
		   ppm.setMarket(excedeLimit(x.length,0,x));
		   ppm.setMarketUnit(excedeLimit(x.length,1,x));
		   ppm.setCBT(excedeLimit(x.length,2,x));
		   ppm.setCT(excedeLimit(x.length,3,x));
		   ppm.setProjectName(excedeLimit(x.length,4,x));
		   ppm.setProjectOwner(owner);
		   ppm.setBGDM(excedeLimit(x.length,6,x));
		   ppm.setProjectIdentifier(pmatId);
		   ppm.setProjectClassification(excedeLimit(x.length,8,x));
		   ppm.setProjectStructure(excedeLimit(x.length,9,x));
		   ppm.setProjectStatus(excedeLimit(x.length,10,x));
		   ppm.setLeadBU(excedeLimit(x.length,11,x));
		   ppm.setRootID(excedeLimit(x.length,12,x));
		   ppm.setPBS(excedeLimit(x.length,13,x));
		   ppm.setCustomerName(excedeLimit(x.length,14,x));
		   ppm.setCustomerLegalID(excedeLimit(x.length,15,x));
		   ppm.setProducts(excedeLimit(x.length,16,x));
		   ppm.setLeadBG(excedeLimit(x.length,17,x));
		   ppm.setOpportunityNumberCondition(excedeLimit(x.length,18,x));
		   ppm.setOpportunityNumber(excedeLimit(x.length,19,x));
		   ppm.setPMViewName(excedeLimit(x.length,20,x));
		   ppm.setERPWBSs(excedeLimit(x.length,21,x));
		   ppm.setMarketPMAT(market);
		   ppm.setGEOL1(excedeLimit(x.length,23,x));
		   ppm.setGEOL2(excedeLimit(x.length,24,x));
		   ppm.setGEOL3(excedeLimit(x.length,25,x));
		   ppm.setGEOL4(excedeLimit(x.length,26,x));
		   ppm.setExecutionCountry(excedeLimit(x.length,27,x));
		   ppm.setSG6PlannedDate(parseDate(excedeLimit(x.length,28,x)));
		   ppm.setSG6ApprovalDate(parseDate(excedeLimit(x.length,29,x)));
		   ppm.setProjectCreated(parseDate(excedeLimit(x.length,30,x)));
		   ppm.setStartDate(parseDate(excedeLimit(x.length,31,x)));
		   ppm.setFinishDate(parseDate(excedeLimit(x.length,32,x)));
		   ppm.setModified(parseDate(excedeLimit(x.length,33,x)));
		   ppm.setLastPublished(parseDate(excedeLimit(x.length,34,x)));
		   ppm.setJIRAKey(excedeLimit(x.length,35,x));
		   ppm.setPOROAvailability(excedeLimit(x.length,36,x));
		   ppm.setPCubeFinancials(excedeLimit(x.length,37,x));
		   ppm.setPMATFinancials(excedeLimit(x.length,38,x));
		   ppm.setAIM(excedeLimit(x.length,39,x));
		   ppm.setD2C(excedeLimit(x.length,40,x));
		   ppm.setDeliveryType(excedeLimit(x.length,41,x));
		   if(check) {
			   pmatProjectMasterDao.save(ppm);
		   }else {
			   updatePmatProjectMaster(ppm);
		   }
		   
	}
	   private void updatePmatProjectMaster(PmatProjectMaster ppm) {
		   pmatProjectMasterDao.updateRow(ppm.getProjectIdentifier(),ppm.getOpportunityNumberCondition(),ppm.getAIM(),ppm.getBGDM(),ppm.getCBT(),ppm.getCT(),ppm.getCustomerLegalID(),ppm.getCustomerName(),ppm.getD2C(),ppm.getDeliveryType(),ppm.getERPWBSs(),ppm.getExecutionCountry(),ppm.getFinishDate(),ppm.getGEOL1(),ppm.getGEOL2(),ppm.getGEOL3(),ppm.getGEOL4(),ppm.getJIRAKey(),ppm.getLastPublished(),ppm.getLeadBG(),ppm.getLeadBU(),ppm.getMarket(),ppm.getMarketPMAT(),ppm.getMarketUnit(),ppm.getModified(),ppm.getOpportunityNumber(),ppm.getPBS(),ppm.getPCubeFinancials(),ppm.getPMATFinancials(),ppm.getPMViewName(),ppm.getPOROAvailability(),ppm.getProducts(),ppm.getProjectClassification(),ppm.getProjectCreated(),ppm.getProjectName(),ppm.getProjectOwner(),ppm.getProjectStatus(),ppm.getProjectStructure(),ppm.getRootID(),ppm.getSG6ApprovalDate(),ppm.getSG6PlannedDate(),ppm.getStartDate());
	}

	public String parseDate(String inputDate) throws ParseException {
		   SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH);
		   SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
		   Boolean validFormat1 = isDateValid(format1,inputDate);
		   Boolean validFormat2 = isDateValid(format2,inputDate);
		   if(inputDate.length() > 1 && validFormat1) {			   
		        Date date = format1.parse(inputDate);
		        String outputDate = outputFormat.format(date);
				return outputDate;
		   } else if(inputDate.length() > 1 && validFormat2)	{
			   Date date = format2.parse(inputDate);
		        String outputDate = outputFormat.format(date);
				return outputDate;
		   }
		   else return "";
		}
	   
	private Boolean isDateValid(SimpleDateFormat format, String inputDate) {
		Boolean valid = false;
		try {
			   format.parse(inputDate);
			   valid = true;
		   }
		catch (ParseException e) {
            valid = false;
        }
		return valid;
	}
	public Map<String,String> marketList() {
		Map<String,String> marketList = new HashMap<String,String>();     
		marketList.put(PmatConstants.PMAT_MARKET_EUR, PmatConstants.EUR);
		marketList.put(PmatConstants.PMAT_MARKET_NAM, PmatConstants.NAM);
		marketList.put(PmatConstants.PMAT_MARKET_APJ, PmatConstants.APJ);
		marketList.put(PmatConstants.PMAT_MARKET_GCHN, PmatConstants.GCHN);
		marketList.put(PmatConstants.PMAT_MARKET_MEA, PmatConstants.MEA);
		marketList.put(PmatConstants.PMAT_MARKET_LAT, PmatConstants.LAT);
		marketList.put(PmatConstants.PMAT_MARKET_IND, PmatConstants.IND);
		return marketList;
	}
		   
	public String mapMarketToStandCode(Map<String,String> marketList, String market) {
		if(!market.isEmpty()&& marketList.containsKey(market)) {
			return marketList.get(market);
		}
		return "";
	}
		   
	private String[] stringValueReplace(String strLine) {
	      return strLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	}

	public EnvironmentVariables readEnvVariables() {
		EnvironmentVariables envVariables = new EnvironmentVariables();
		envVariables = readEnvironmentVariables.readEnvVariables(envVariables);
		return envVariables;
	}
   private String excedeLimit(int length, int index,String[] indexValue) {
	      if(length<=index) {
	         return " "; 
	      }
	      if(StringUtils.isEmptyOrNull(indexValue[index])) {
	         return " ";
	      }
	      return indexValue[index];
	   }
}
