package tech.xavi.realista.configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ScannerConfiguration {

    private static String urlToScan;

    public static synchronized String getUrlToScan(){
        if (urlToScan == null){
            try {
                String propertiesFile = "/realista.properties";
                Properties properties = new Properties();
                InputStream is = ScannerConfiguration.class.getResourceAsStream(propertiesFile);
                properties.load(is);
                if (is != null) is.close();
                urlToScan = properties.getProperty("tech.xavi.realista.url-yanencontre");
            } catch (IOException ioException) {
                log.error("Error closing InputStream");
                ioException.getStackTrace();
            }
        }
        return urlToScan;
    }

}
