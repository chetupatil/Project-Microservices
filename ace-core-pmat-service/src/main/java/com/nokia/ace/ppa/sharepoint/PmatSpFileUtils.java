package com.nokia.ace.ppa.sharepoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jgit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import com.google.common.base.Joiner;
import com.nokia.ace.connectors.common.environmentVariables.EnvironmentVariables;
import com.nokia.ace.connectors.common.environmentVariables.ReadEnvironmentVariables;
import com.nokia.ace.connectors.common.input.ConnectorInputData;
import com.nokia.ace.connectors.common.model.toolDetails.ToolResponse;
import com.nokia.ace.connectors.common.toolDetails.ToolDetails;
import com.nokia.ace.core.project.dao.maria.ProjectDao;
import com.nokia.ace.pmat.dao.PmatProjectMasterDao;
import com.nokia.ace.ppa.dao.PmatMasterDao;
import com.nokia.ace.ppa.dto.PmatMaster;
import com.nokia.ace.utils.constants.PmatConstants;
import com.nokia.ace.utils.util.CsvWriterHelper;
import com.nokia.ace.utils.util.ToolDetailImpl;

/**
 * @author Chetana
 *
 */
@Component
public class PmatSpFileUtils {

   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private PmatMasterDao pmatMasterDao;

   @Autowired
   private CsvWriterHelper csvWriterHelper;
   
   @Value("${ppa.sp.main.site}")
   private String ppaSpMainSite;
   
   @Value("${ppa.sp.sub.site}")
   private String ppaSpSubSite;
   
   @Value("${ppa.sp.pmat.saf}")
   private String ppaSpPmatSaf;
   
   @Value("${ppa.sp.saf.pmat}")
   private String ppaSpSafPmat;
   
   @Value("${ppa.ppa.file}")
   private String ppaFile;
   
   @Value("${ppa.saf.file}")
   private String safFile;
   
   @Autowired
   private ProjectDao projectDao;
   
   @Autowired
	private ToolDetailImpl toolDetailsImpl;
   @Autowired
	private ReadEnvironmentVariables readEnvironmentVariables;
   
   private static final Logger LOGGER = LoggerFactory.getLogger(PmatSpFileUtils.class);


   public void fetchPpaDumpAndDownloadFileByDefPath(List<String> cookies,String digestValue, String hostUrl) {
	   
      String apiUrl = hostUrl + "/sites/" + ppaSpMainSite + PmatConstants.SP_GET_FOLDER_FILE_BY_URL_1 + ppaSpSubSite + "/"+ppaSpPmatSaf+
            PmatConstants.SP_GET_FOLDER_FILE_BY_URL_2 + ppaFile + PmatConstants.SP_GET_FOLDER_FILE_BY_URL_3;
      try {
         HttpHeaders headers = new HttpHeaders();
         headers.add("Cookie", Joiner.on(';').join(cookies));
         headers.add("Accept", PmatConstants.SP_ACCEPT);
         headers.add("Content-Type", PmatConstants.SP_CONTENT_TYPE);
         headers.add("X-RequestDigest", digestValue);
         HttpEntity<String> entity = new HttpEntity<String>(null,headers);

         ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
         String successBody = responseEntity.getBody();
         FileWriter wr = null;
         File path = null;
         try {
            path = csvWriterHelper.createFileInResource(ppaFile);
            wr = new FileWriter(path);
            if(successBody!=null) {
               wr.write(successBody);
            }
         }catch(Exception e) {
            LOGGER.error("Exception while writing into the Prospect file:  "+e.getMessage());
         }finally {
            wr.flush();
            wr.close();
         }

          fetchPerviousSafDump(cookies,digestValue,hostUrl);

         if(path!=null) {
            Map<String,String> marketList = marketList();
            int countPmatData = pmatMasterDao.countPmatMaster();
            try(BufferedReader in = new BufferedReader(new FileReader(path))){
            String strLine = "";
            in.readLine();

            while( (strLine = in.readLine()) != null) {
               String[] x = stringValueReplace(strLine);

               if(!x[0].equalsIgnoreCase("") && isIdContainNumber(x[0])) {
                  String modified = excedeLimit(x.length,26,x);
                  int id = Integer.parseInt(excedeLimit(x.length,0,x));
                  String oppName = excedeLimit(x.length,1,x);
                  oppName = oppName.replaceAll("\\s","");
                  String market = mapMarketToStandCode(marketList,excedeLimit(x.length,5,x));
                  if(countPmatData>0) {
                     boolean isLatestDate = csvWriterHelper.checkModifiedDate(modified);
                     if(isLatestDate) {
                        insertDataIntoPmatTable(x,oppName,id,market);
                     }
                  }else {
                     insertDataIntoPmatTable(x,oppName,id,market);
                  }

               }   
            }
            } catch(Exception e) {
            	LOGGER.error("Exception occure while saving dump data into db:  "+e.getMessage());
            }
         }
         if(path!=null) {
            path.delete();
         }
         
         ToolResponse insertcustomerresponse = insertcustomerfromppa(PmatConstants.TOOL_SHAREPOINT);
         
         
         
      } catch (Exception e) {
         LOGGER.error("Exception occure while saving dump data into db:  "+e.getMessage());
      }
   }

   public File fetchPerviousSafDump(List<String> cookies,String digestValue, String hostUrl) {
      String apiUrl = hostUrl + "/sites/"+ ppaSpMainSite + PmatConstants.SP_GET_FOLDER_FILE_BY_URL_YESTERDAY_1 +ppaSpSubSite +"/" + ppaSpSafPmat + PmatConstants.SP_GET_FOLDER_FILE_BY_URL_YESTERDAY_2 + safFile +
            PmatConstants.SP_GET_FOLDER_FILE_BY_URL_YESTERDAY_3;
      
      File path = null;
      try {
         HttpHeaders headers = new HttpHeaders();
         headers.add("Cookie", Joiner.on(';').join(cookies));
         headers.add("Accept", PmatConstants.SP_ACCEPT);
         headers.add("Content-Type", PmatConstants.SP_CONTENT_TYPE);
         headers.add("X-RequestDigest", digestValue);
         HttpEntity<String> entity = new HttpEntity<String>(null,headers);

         ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
         String successBody = responseEntity.getBody();

         FileWriter wr = null;
         try {
            path = csvWriterHelper.createFileInResource("SAF_DUMP_Pervious.csv");
            wr = new FileWriter(path);
            if(successBody!=null) {
               wr.write(successBody);
            }
         }catch(Exception e) {
            LOGGER.error("Exception while writing into the Prospect file:  "+e.getMessage());
         }finally {
            wr.flush();
            wr.close();
         }
      }catch(Exception e) {
         LOGGER.error("Exception occure while fetching pervious saf dump data:  "+e.getMessage());
      }
      return path;
   }

   public void dumpFileIntoSpCSV(List<String> cookies,String digestValue, File file, String spHost) {
//    String url =spHost + PmatConstants.SP_UPLOAD_FOLDER_FILE_URL + "Add(url='" + file.getName() + "',overwrite=true)";
//    
      String url =spHost + "/sites/" +ppaSpMainSite+ PmatConstants.SP_UPLOAD_FOLDER_FILE_URL_1 + "/sites/"+ ppaSpMainSite +"/" + ppaSpSubSite +"/" + ppaSpSafPmat + PmatConstants.SP_UPLOAD_FOLDER_FILE_URL_2  + "Add(url='" + file.getName() + "',overwrite=true)";
      try {
         HttpClient client = HttpClientBuilder.create().build();
         HttpPost post = new HttpPost(url);
         post.setHeader("Authorization", "Bearer "+digestValue);
         post.setHeader("Cookie", Joiner.on(';').join(cookies));
         post.setHeader("accept", PmatConstants.SP_ACCEPT);
         post.setEntity(new FileEntity(file));
         HttpResponse response = client.execute(post);
         if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()|| response.getStatusLine().getStatusCode() == HttpStatus.ACCEPTED.value()){
            LOGGER.info("Success");
         }
      }catch(Exception e) {
         LOGGER.error("Exception while uploading the file into sharepoint site: "+e.getMessage());
      }finally {
         if(file!=null) {
            file.delete();
         }
      }
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

   public Map<String,String> marketList() {
      Map<String,String> marketList = new HashMap<String,String>();     
      marketList.put(PmatConstants.MARKET_EUR, PmatConstants.EUR);
      marketList.put(PmatConstants.MARKET_NAM, PmatConstants.NAM);
      marketList.put(PmatConstants.MARKET_APJ, PmatConstants.APJ);
      marketList.put(PmatConstants.MARKET_GCHN, PmatConstants.GCHN);
      marketList.put(PmatConstants.MARKET_MEA, PmatConstants.MEA);
      marketList.put(PmatConstants.MARKET_LAT, PmatConstants.LAT);
      return marketList;
   }
   
   public String mapMarketToStandCode(Map<String,String> marketList, String market) {
      if(!market.isEmpty()&& marketList.containsKey(market)) {
         return marketList.get(market);
      }
      return "";
   }
   
   private void insertDataIntoPmatTable(String[] x,String oppName,int id,String market) {
      try {
         if(oppName!=null) {
            PmatMaster pmatMaster = new PmatMaster();
            pmatMaster.setId(id);
            pmatMaster.setOpportunityNumber(oppName);
            pmatMaster.setOpportunityName(excedeLimit(x.length,2,x));
            pmatMaster.setBGDMName(excedeLimit(x.length,3,x));
            pmatMaster.setDescription(excedeLimit(x.length,4,x));
            pmatMaster.setMarket(market);
            pmatMaster.setMarketUnit(excedeLimit(x.length,6,x));
            pmatMaster.setCBT(excedeLimit(x.length,7,x));
            pmatMaster.setCT(excedeLimit(x.length,8,x));
            pmatMaster.setCountry(excedeLimit(x.length,9,x));
            pmatMaster.setCustomerName(excedeLimit(x.length,10,x));
            pmatMaster.setIncludedBUs(excedeLimit(x.length,11,x));
            pmatMaster.setPSEstimationGroup(excedeLimit(x.length,12,x));
            pmatMaster.setRisk(excedeLimit(x.length,13,x));
            pmatMaster.setStatus(excedeLimit(x.length,14,x));
            pmatMaster.setServiceStartDate(excedeLimit(x.length,15,x));
            //pmatMaster.setServiceEndDate(excedeLimit(x.length,16,x));
            pmatMaster.setServiceEndDate("");
            pmatMaster.setComments(excedeLimit(x.length,16,x));
            pmatMaster.setEnoviaProductRelease(excedeLimit(x.length,17,x));
            pmatMaster.setPLsProspectScope(excedeLimit(x.length,18,x));
            pmatMaster.setNgSEET(excedeLimit(x.length,19,x));
            pmatMaster.setNgSEETURL(excedeLimit(x.length,20,x));
            pmatMaster.setXSU(excedeLimit(x.length,21,x));
            pmatMaster.setXSUURL(excedeLimit(x.length,22,x));
            pmatMaster.setCreatedBy(excedeLimit(x.length,23,x));
            pmatMaster.setModifiedBy(excedeLimit(x.length,24,x));
            pmatMaster.setCreated(excedeLimit(x.length,25,x));
            pmatMaster.setModified(excedeLimit(x.length,26,x));
            pmatMaster.setIsDeleted(excedeLimit(x.length,27,x));
            
            pmatMaster.setIsProspectCreated(checkProspectCreatedByScrm(pmatMaster.getOpportunityNumber(),Integer.toString(pmatMaster.getId())));

            pmatMasterDao.insertPmatMaster(pmatMaster.getId(), pmatMaster.getOpportunityNumber(), pmatMaster.getOpportunityName(),
                  pmatMaster.getBGDMName(), pmatMaster.getDescription(), 
                  pmatMaster.getMarket(), pmatMaster.getMarketUnit(), pmatMaster.getCBT(), 
                  pmatMaster.getCT(), 
                  pmatMaster.getCountry(),
                  pmatMaster.getCustomerName(),
                  pmatMaster.getIncludedBUs(), pmatMaster.getPSEstimationGroup(),
                  pmatMaster.getRisk(), pmatMaster.getStatus(),
                  pmatMaster.getServiceStartDate(), 
                  pmatMaster.getServiceEndDate(),
                  pmatMaster.getComments(), 
                  pmatMaster.getEnoviaProductRelease(), pmatMaster.getPLsProspectScope(),
                  pmatMaster.getNgSEET(), pmatMaster.getNgSEETURL(),
                  pmatMaster.getXSU(), pmatMaster.getXSUURL(), pmatMaster.getCreatedBy(), pmatMaster.getModifiedBy(), 
                  pmatMaster.getCreated(), pmatMaster.getModified(), pmatMaster.getIsDeleted(),
                  pmatMaster.getIsProspectCreated());
         }
      }catch(Exception e) {
         LOGGER.error("Exception : "+e.getMessage());
      }
   }
   
   private String[] stringValueReplace(String strLine) {
      strLine = strLine.replaceAll(".com;", ".com");
      strLine = strLine.replace("(BU);", "(BU) ");
      strLine = strLine.replace("\"","");
      return strLine.split(";");
   }
   
   private boolean isIdContainNumber(String idStr) {
      if (!StringUtils.isEmptyOrNull(idStr) && idStr.matches("[0-9]+") && idStr.length() >= 1) {
         return true;
      }
      return false;
   }
   
   private String checkProspectCreatedByScrm(String sCrmId,String ppaId) {
      int scrmIds = projectDao.fetchsCrmIdAndTypeAndPpaId(sCrmId, PmatConstants.PROSPECT, ppaId);
      if(scrmIds>=1) {
         return "True";
      }
      return "False";
   }
// private boolean removeDuplicatesCrmId(String opportunityNumber) {
//    List<Object[]> oppList = pmatMasterDao.findByOpportunityNumber();
//
//    if(!CollectionUtils.isEmpty(oppList)) {
//       return oppList.stream().anyMatch(s -> s[1].toString().equalsIgnoreCase(opportunityNumber));
//    }
//    return false;
// }

   private ToolResponse insertcustomerfromppa(String toolName) {
		ConnectorInputData jiraInput = new ConnectorInputData();
		jiraInput.setProjectName(PmatConstants.PROJECT_NAME);
		jiraInput.setServerIp(PmatConstants.SERVER_IP);
		EnvironmentVariables envVariables = readEnvVariables();
		ToolResponse toolResponse = toolDetailsImpl.insertCustomerData(toolName, jiraInput,envVariables);
		return toolResponse;
	}
   
   public EnvironmentVariables readEnvVariables() {
		EnvironmentVariables envVariables = new EnvironmentVariables();
		envVariables = readEnvironmentVariables.readEnvVariables(envVariables);
		return envVariables;
	}


}

 