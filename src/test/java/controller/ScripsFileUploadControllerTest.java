package controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import scrips.datamigration.contloller.ScripsFileUploadController;

@RunWith(MockitoJUnitRunner.class)
public class ScripsFileUploadControllerTest {
    private final Logger logger = LogManager.getLogger(ScripsFileUploadControllerTest.class);

    @InjectMocks
    ScripsFileUploadController scripsFileUploadController;

    @Mock
    BindingResult errors;
    
    // @Before
    // public void setup(){
    //     try{

    //     } catch(Exception ex){

    //     }
    // }

    @Test
    public void testAccount(){
        logger.info("Entering -> Account Test");
        try{
            scripsFileUploadController.migrateFile("ACCOUNT_FILE_UPLOAD", errors);
        }catch(Exception ex){
            logger.info("Failed Account Migration Test");
        }
    }

    @Test
    public void testInvalidInputcase(){
        logger.info("Entering -> InvalidInputcase Test");
        try{
            Assert.assertEquals("Migrated Partially", scripsFileUploadController.migrateFile("FILE_UPLOAD", errors));
        }catch(Exception ex){
            logger.info("Failed InvalidInputcase Test");
        }
    }

}
