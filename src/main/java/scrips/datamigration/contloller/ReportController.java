
package scrips.datamigration.contloller;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import scrips.datamigration.report.GenerateReportService;

//import io.swagger.annotation.Api;

/**
 * @author Siva Kuruva
 */

@Controller
//@RequestMapping("/rtgs")
public class ReportController {
	
	@Autowired
	GenerateReportService generateReportService;
	private final Logger logger = LogManager.getLogger(ReportController.class);
	//@GetMapping("/report/{format}")
    public String generateReport(String format,HttpServletResponse response,String iDate,String reportName) throws Exception {
    	  String decodedIdate = URLDecoder.decode(iDate, "UTF-8");
    	  String decodedreportName = URLDecoder.decode(reportName, "UTF-8");
    	 // System.out.println("decodedIdate=="+decodedIdate+"decoded report name=="+decodedreportName);
    	  logger.info("{} - {}",decodedIdate, decodedreportName);
        return generateReportService.exportReport(format, response,decodedIdate,decodedreportName);
    }

}