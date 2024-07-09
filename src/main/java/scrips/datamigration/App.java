package scrips.datamigration;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import scrips.datamigration.contloller.ReportController;
import scrips.datamigration.contloller.ScripsFileUploadController;

//import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication // (scanBasePackages = "scrips.datamigration")
@EnableConfigurationProperties
//@EnableJpaRepositories(basePackages = "scrips.datamigration")
//@EntityScan(basePackages = "scrips.datamigration")
//@ComponentScan(basePackages = {"scrips.datamigration.jpa","scrips.datamigration.contloller"})//,"scrips.datamigration.business.FileUploadService","scrips.datamigration.jpa.fileupload","scrips.datamigration.contloller","scrips.datamigration.business","scrips.datamigration.common"})
//@PropertySource("file:C:\\DataMigrationService\\config\\application.properties")
public class App {

	public static void main(String[] args) {

		ApplicationContext context = new SpringApplicationBuilder(App.class).initializers(new PropertiesInitializer())
				.build().run(args);
		try {
			if (args != null && args.length >= 1) {
				System.out.println("Argument-1: " + args[0]);
				String service = args[0];
				if (service != null && service.equalsIgnoreCase("datamigration")) {
					if (args.length > 1) {
						String reqType = args[1];
						if (reqType != null && !reqType.isEmpty())
							context.getBean(ScripsFileUploadController.class).migrateFile(reqType, null);
					} else {
						context.getBean(ScripsFileUploadController.class).migrateFile();
					}
				} else if (service != null && service.equalsIgnoreCase("report")) {
					if (args.length == 4 || args.length == 3) {
						if (args.length == 4) {
							String reqType = args[1];
							String reportName = args[2];
							String date = args[3];
							context.getBean(ReportController.class).generateReport(reqType, null, date, reportName);
						} else {
							String reqType = args[1];
							String reportName = args[2];
							String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
							context.getBean(ReportController.class).generateReport(reqType, null, " ", reportName);
						}
					} else {
						System.out.println("invalid parameters count,expecting  4 parameters");
						return;
					}
				} else if (service != null) {
					System.out.println("Invalid service name: " + service);
				} else {
					System.out.println("Expecting atlease one argument");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
