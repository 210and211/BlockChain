package medic_information;

import java.io.File;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class Xml2Doc {
	public static Document Xml2Doc(String xmlFilePath) throws Exception {   
        File file = new File(xmlFilePath);   
        return (new SAXBuilder()).build(file);   
    } 
}
