package face;

import java.io.File;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLParser {
	Element child;
	XMLParser(String path){
		try {
			SAXReader reader = new SAXReader();
			File file = new File(path);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			child =childElements.get(0);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("解析错误");
		}
	}
	public String get_name() {
		return child.attribute("name").getText();
	}
	public String get_id() {
		return child.attribute("id").getText();
	}
	public String get_age() {
		return child.elementText("age");
	}
	public String get_sex() {
		return child.elementText("sex");
	}
	public String get_hospital() {
		return child.elementText("hospital");
	}
	public String get_time() {
		return child.elementText("time");
	}
	public String get_department() {
		return child.elementText("department");
	}
	public String get_disease() {
		return child.elementText("disease");
	}
	public String get_description() {
		return child.elementText("description");
	}
	public String get_cure() {
		return child.elementText("cure");
	}
	public String get_doctor() {
		return child.elementText("doctor");
	}
	
	
	public static void main(String[] args) throws Exception {
		XMLParser l=new XMLParser("test.xml");
		System.out.println(l.get_age());
		System.out.println(l.get_cure());
		System.out.println(l.get_department());
		System.out.println(l.get_description());
		System.out.println(l.get_disease());
		System.out.println(l.get_doctor());
		System.out.println(l.get_hospital());
		System.out.println(l.get_id());
		System.out.println(l.get_name());
		System.out.println(l.get_sex());
		System.out.println(l.get_time());
	}
}