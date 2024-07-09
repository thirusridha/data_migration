package scrips.datamigration.report;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import scrips.datamigration.business.CbmCostCenterService;

@Service
@Slf4j
public class GenerateReportService {

	private final Logger logger = LogManager.getLogger(GenerateReportService.class);
	
	@Autowired
	DataSource dataSource;

	@Autowired
	Environment env;

	public String exportReport(String reportFormat, HttpServletResponse response, String iDate,
			String decodedreportName) throws Exception, IOException {
		String dateTime = new java.text.SimpleDateFormat("dd-MM-yy").format(new java.util.Date()).toString();
		String path = env.getProperty("report.savePath");
		//String jrxmlFilepath = env.getProperty("report.jrxmlFilepath");
		String jrxmlFilepath = "./jasperFiles/";
		logger.info("Path -> {}",path);
		//System.out.println(decodedreportName);
		//logger.info(decodedreportName);
		// String path = "C:\\Users\\RaghuwaranAkubathini\\Desktop\\"; //load file and

		if (decodedreportName.equals("MEMBER_SUMMARY_REPORT")) {
			//File file = ResourceUtils.getFile("classpath:MEMBER_SUMMARY_REPORT.jrxml");
			//JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"MEMBER_SUMMARY_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "MEMBER_SUMMARY_REPORT_" + dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
		
			if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\MEMBER_SUMMARY_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\MEMBER_SUMMARY_REPORT	.pdf");
			}

			return "report generated in path : " + path;

		}
		else if (decodedreportName.equals("ACCOUNT_SUMMARY_REPORT")) {

			//File file = ResourceUtils.getFile("classpath:ACCOUNT_SUMMARY_REPORT.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"ACCOUNT_SUMMARY_REPORT.jrxml");
			//JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "ACCOUNT_SUMMARY_REPORT_"+dateTime);
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\ACCOUNT_SUMMARY_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\ACCOUNT_SUMMARY_REPORT.pdf");
			}

			return "report generated in path : " + path;

		}
		else if (decodedreportName.equals("CONTROL_TOTAL_REPORT")) {

//			File file = ResourceUtils.getFile("classpath:CONTROL_TOTAL_REPORT.jrxml");
//			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CONTROL_TOTAL_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "CONTROL_TOTAL_REPORT_" + dateTime);
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());

			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\CONTROL_TOTAL_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\CONTROL_TOTAL_REPORT.pdf");
			}

			return "report generated in path : " + path;
		}

		else if (decodedreportName.equals("MEMBER_DETAIL_REPORT")) {

//			File file = ResourceUtils.getFile("classpath:MEMBER_DETAIL_REPORT.jrxml");
//			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"MEMBER_DETAIL_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "MEMBER_DETAIL_REPORT_" + dateTime);
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\MEMBER_DETAIL_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\MEMBER_DETAIL_REPORT.pdf");
			}

			return "report generated in path : " + path;
		}

		else if (decodedreportName.equals("SSS_CONTROL_TOTAL_REPORT")) {

//			File file = ResourceUtils.getFile("classpath:SSS_CONTROL_TOTAL_REPORT.jrxml");
//			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_CONTROL_TOTAL_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "SSS_CONTROL_REPORT" + dateTime);
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\SSS_CONTROL_TOTAL_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\SSS_CONTROL_TOTAL_REPORT.pdf");
			}

			return "report generated in path : " + path;
		} else if (decodedreportName.equals("SSS_SUMMARY_REPORT")) {

//			File file = ResourceUtils.getFile("classpath:SSS_SUMMARY_REPORT.jrxml");
//			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_SUMMARY_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "SSS_SUMMARY_REPORT" + dateTime);
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\SSS_SUMMARY_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\SSS_SUMMARY_REPORT.pdf");
			}

			return "report generated in path : " + path;
		} 
		
		 else if (decodedreportName.equals("ACCOUNT_DETAIL_REPORT")) {

//				File file = ResourceUtils.getFile("classpath:ACCOUNT_DETAIL_REPORT.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"ACCOUNT_DETAIL_REPORT.jrxml");
//				JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "ACCOUNT_DETAIL_REPORT_"+ dateTime);
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\ACCOUNT_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "ACCOUNT_DETAIL_REPORT.pdf");
				}

				return "report generated in path : " + path;
			}
		 else if (decodedreportName.equals("ACCOUNT_POSITION_DETAIL_REPORT")) {
				//File file = ResourceUtils.getFile("classpath:SSS_SECURITIES_CODE_DETAIL_REPORT.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"ACCOUNT_POSITION_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "ACCOUNT_POSITION_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);	
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\ACCOUNT_POSITION_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "ACCOUNT_POSITION_DETAIL_REPORT.pdf");
				}

				return "report generated in path : " + path;
			}
		 else if (decodedreportName.equals("CBM_GL_ACCOUNT_DETAIL_REPORT")) {
				//File file = ResourceUtils.getFile("classpath:SSS_SECURITIES_CODE_DETAIL_REPORT.jrxml");
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CBM_GL_ACCOUNT_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "CBM_GL_ACCOUNT_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);	
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\CBM_GL_ACCOUNT_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "CBM_GL_ACCOUNT_DETAIL_REPORT.pdf");
				}

				return "report generated in path : " + path;
			}
		
		 else if (decodedreportName.equals("CBM_DEPOSIT_RATE_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CBM_DEPOSIT_RATE_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "CBM_DEPOSIT_RATE_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\CBM_DEPOSIT_RATE_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "CBM_DEPOSIT_RATE_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 

			else if (decodedreportName.equals("CBM_LIABILITIES_BASE_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CBM_LIABILITIES_BASE_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "CBM_LIABILITIES_BASE_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\CBM_LIABILITIES_BASE_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "CBM_LIABILITIES_BASE_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
			
			
			else if (decodedreportName.equals("CBM_LIABILITIES_BASE_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CBM_LIABILITIES_BASE_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "CBM_LIABILITIES_BASE_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\CBM_LIABILITIES_BASE_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "CBM_LIABILITIES_BASE_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
			else if (decodedreportName.equals("CBM_COST_CENTRE_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CBM_COST_CENTRE_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "CBM_COST_CENTRE_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\CBM_COST_CENTRE_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "CBM_COST_CENTRE_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
			else if (decodedreportName.equals("CBM_FLOATING_RATE_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"CBM_FLOATING_RATE_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "CBM_FLOATING_RATE_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\CBM_FLOATING_RATE_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "CBM_FLOATING_RATE_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
			
			else if (decodedreportName.equals("SSS_ALLOTMENT_DETAIL_REPORT")) {
	            try {
	            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_ALLOTMENT_DETAIL_REPORT.jrxml");
	            Map<String, Object> parameters = new HashMap<>();
	            parameters.put("iDate", iDate);
	            parameters.put("reportID", "SSS_ALLOTMENT_DETAIL_REPORT");
	        	//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
	                    dataSource.getConnection());
	            if (reportFormat.equalsIgnoreCase("html")) {
	                JasperExportManager.exportReportToHtmlFile(jasperPrint,
	                        path + "\\SSS_ALLOTMENT_DETAIL_REPORT.html");
	            }
	            if (reportFormat.equalsIgnoreCase("pdf")) {
	                JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_ALLOTMENT_DETAIL_REPORT.pdf");
	            }
	            }catch(Exception e) {
	                e.printStackTrace();
	                }
	            return "report generated in path : " + path;
	        }else if (decodedreportName.equals("SSS_SECURITIES_CODE_STATISTICS_DETAIL_REPORT")) {
	            try {
	            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_SECURITIES_CODE_STATISTICS_DETAIL_REPORT.jrxml");
	            Map<String, Object> parameters = new HashMap<>();
	            parameters.put("iDate", iDate);
	            parameters.put("reportID", "SSS_SECURITIES_CODE_STATISTICS");
	        	//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
	                    dataSource.getConnection());
	            if (reportFormat.equalsIgnoreCase("html")) {
	                JasperExportManager.exportReportToHtmlFile(jasperPrint,
	                        path + "\\SSS_SECURITIES_CODE_STATISTICS_DETAIL_REPORT.html");
	            }
	            if (reportFormat.equalsIgnoreCase("pdf")) {
	                JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_SECURITIES_CODE_STATISTICS_DETAIL_REPORT.pdf");
	            }
	            }catch(Exception e) {
	                e.printStackTrace();
	                }
	            return "report generated in path : " + path;
	        }
		
		
		else if (decodedreportName.equals("SSS_SECURITIES_CODE_DETAIL_REPORT")) {
			//File file = ResourceUtils.getFile("classpath:SSS_SECURITIES_CODE_DETAIL_REPORT.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_SECURITIES_CODE_DETAIL_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "SSS_SECURITIES_CODE_DETAIL_REPORT");
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);	
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint,
						path + "\\SSS_SECURITIES_CODE_DETAIL_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_SECURITIES_CODE_DETAIL_REPORT.pdf");
			}

			return "report generated in path : " + path;
		}
			else if (decodedreportName.equals("SSS_FLOATING_RATE_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_FLOATING_RATE_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "SSS_FLOATING_RATE_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\SSS_FLOATING_RATE_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_FLOATING_RATE_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
		
			else if (decodedreportName.equals("SSS_SECURITIES_POSITION_STATS_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_SECURITIES_POSITION_STATS_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "SECURITIES_POSITION_STATS");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\SSS_SECURITIES_POSITION_STATS_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_SECURITIES_POSITION_STATS_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
		
			else if (decodedreportName.equals("SSS_SECURITIES_PRICE_DETAIL_REPORT")) {
				try {
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"SSS_SECURITIES_PRICE_DETAIL_REPORT.jrxml");
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("iDate", iDate);
				parameters.put("reportID", "SECURITIES_PRICE_DETAIL_REPORT");
				//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						dataSource.getConnection());
				if (reportFormat.equalsIgnoreCase("html")) {
					JasperExportManager.exportReportToHtmlFile(jasperPrint,
							path + "\\SSS_SECURITIES_PRICE_DETAIL_REPORT.html");
				}
				if (reportFormat.equalsIgnoreCase("pdf")) {
					JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_SECURITIES_PRICE_DETAIL_REPORT.pdf");
				}
				}catch(Exception e) {
					e.printStackTrace();
					}
				return "report generated in path : " + path;
			} 
		
		
		else if (decodedreportName.equals("REPO_CLOSING_TRANSACTION_REPORT")) {
			try {
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilepath+"REPO_CLOSING_TRANSACTION_REPORT.jrxml");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "REPO_CLOSING_TRANSACTION");
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint,
						path + "\\REPO_CLOSING_TRANSACTION_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "REPO_CLOSING_TRANSACTION_REPORT.pdf");
			}
			}catch(Exception e) {
				e.printStackTrace();
				}
			return "report generated in path : " + path;
		} 
		
		else if (decodedreportName.equals("SSS_COUPON_SCHEDULES_REPORT")) {

			File file = ResourceUtils.getFile("classpath:SSS_COUPON_SCHEDULES_REPORT.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "SSS_COUPON_SCHEDULES_DETAIL_REPORT");
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\SSS_COUPON_SCHEDULES_REPORT.html");
			}
			if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "SSS_COUPON_SCHEDULES_REPORT.pdf");
			}
			return "report generated in path : " + path;
		} 
		
		
        else if (decodedreportName.equals("COUPON")) {
			System.out.println("hii");
			File file = ResourceUtils.getFile("classpath:SSS_COUPON_SCHEDULES_REPORT.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("iDate", iDate);
			parameters.put("reportID", "SSS_COUPON_SCHEDULES_DETAIL_REPORT");
			//	logger.info("{} - {}", parameters.get("iDate"), dateTime);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					dataSource.getConnection());
			if (reportFormat.equalsIgnoreCase("html")) {
				JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\SSS_COUPON_SCHEDULES_REPORT.html");
			} else if (reportFormat.equalsIgnoreCase("pdf")) {
				JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\SSS_COUPON_SCHEDULES_REPORT.pdf");
			}

			return "report generated in path : " + path;
		}
		

		return null;

	}

}
