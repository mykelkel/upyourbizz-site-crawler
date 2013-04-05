package fr.upyourbizz.parsing.main;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.parsing.magento.Parser;

/**
 * Upyourbizz site crawler!!
 */
public class App {

    /**
     * Main function
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            String executionPath = System.getProperty("user.dir");
            System.out.println("Executing at =>" + executionPath.replace("\\", "/"));
        }
        catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
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

        ((ClassPathXmlApplicationContext) context).close();
        return;
    }

}
