package fr.upyourbizz.parsing.main;

import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.parsing.magento.Parser;
import fr.upyourbizz.utils.CsvFileSplitter;

/**
 * Upyourbizz site crawler!!
 */
public class App {

    // ===== Attributs statiques ==============================================

    private static Logger logger = LoggerFactory.getLogger(App.class);

    // ===== Méthodes statiques ===============================================

    /**
     * Main function
     * 
     * @param args
     */
    public static void main(String[] args) {
        logger.info("© 2013, Upyourbizz - All right reserved");
        try {
            String executionPath = System.getProperty("user.dir");
            logger.info("Executing at =>" + executionPath.replace("\\", "/"));
        }
        catch (Exception e) {
            logger.error("Exception caught =" + e.getMessage());
        }

        // create and configure beans
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Contexte contexte = context.getBean("contexte", Contexte.class);
        contexte.setFromDisk(true);
        contexte.setSaveFile(true);
        contexte.setWebSiteUrl("http://www.terredeson.com");
        contexte.setWebSiteParseResultFolder("www.terredeson.com");

        Parser parser = context.getBean("parser", Parser.class);
        try {
            parser.parseWebSite();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            CsvFileSplitter.splitProductsCsvFile("csv/products.csv", 1500);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ((ClassPathXmlApplicationContext) context).close();
        return;
    }

}
