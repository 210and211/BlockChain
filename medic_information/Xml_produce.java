package medic_information;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class Xml_produce {
	public static void BulidXml(String name,String ID,String age,String sex,String hospital,String time,String department
		,String disease,String description,String cure,String doctor){
		LocalDate today = LocalDate.now();
		
		//DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//java.util.Date date=new java.util.Date();
		String str=today.toString();
		System.out.println(str);

		
		Element root=new Element("Hospital");//创建根节点School节点
		Document document=new Document(root);//把根节点添加打文档中
		
		
		Element  elementclass=new Element("patient");//创建根节点的子节点
		elementclass.setAttribute("name",name);//给子节点添加name属性
		elementclass.setAttribute("id","002");//给子节点添加id属性
		
		Element element_age=new Element("age");//给子节点添加年龄元素
		element_age.addContent(age);
		elementclass.addContent(element_age);
		
		Element element_sex=new Element("sex");//给子节点添加性别元素
		element_sex.addContent(sex);
		elementclass.addContent(element_sex);
		
		Element element_subject_hospital=new Element("hospital");//给子节点添加医院t元素
		element_subject_hospital.addContent(hospital);
		elementclass.addContent(element_subject_hospital);
		
		Element element_subject_time=new Element("time");//给子节点添加时间元素
		element_subject_time.addContent(str);
		elementclass.addContent(element_subject_time);
		
		Element element_subject=new Element("department");//给子节点添加subject元素
		element_subject.addContent(department);
		elementclass.addContent(element_subject);
		
		Element element_subject1=new Element("disease");//给子节点添加score元素
		element_subject1.addContent(disease);
		elementclass.addContent(element_subject1);
		
		Element element_description=new Element("description");//给子节点添加card元素
		element_description.addContent(description);
		elementclass.addContent(element_description);
		
		
		
		Element element_cure=new Element("cure");//给子节点添加card元素
		element_cure.addContent(cure);
		elementclass.addContent(element_cure);
		
		Element element_doctor=new Element("doctor");//给子节点添加card元素
		element_doctor.addContent(doctor);
		elementclass.addContent(element_doctor);
		
		
		root.addContent(elementclass);//把子节点添加到根节点中。
		XMLOutputter XMLOut = new XMLOutputter(FormatXML());  //生成xml文件
		try {
			XMLOut.output(document, new FileOutputStream("test.xml"));
		} catch (FileNotFoundException e) {
			System.out.println("生成xml文件失败！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("生成xml文件失败！");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		LocalDate beginDateTime = LocalDate.parse("2017-05-01");
		  System.out.print(beginDateTime);
		  LocalDate localDate1 = LocalDate.parse("2017-09-12");
	        System.out.println(localDate1);

		//BulidXml();
		try {
			//Document doc=Xml2Doc("xml\\test.xml");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			System.out.println("生成document文件失败！");
			e.printStackTrace();
		}
	}
	  public static Format FormatXML(){  
	        //格式化生成的xml文件 
	        Format format = Format.getCompactFormat();  
	        format.setEncoding("utf-8");  
	        format.setIndent(" ");  
	        return format;  
	    }
	  public static Document Xml2Doc(String xmlFilePath) throws Exception {   
		  
		  File file = new File(xmlFilePath);   
	        return (new SAXBuilder()).build(file);   
	    } 
	  public static void doc2XML(Document doc, String filePath) throws Exception{   
	        Format format = Format.getCompactFormat();    
	        format.setEncoding("UTF-8"); //设置XML文件的字符为UTF-8   
	        format.setIndent("     ");//设置缩进    
	       
	        XMLOutputter outputter = new XMLOutputter(format);//定义输出 ,在元素后换行，每一层元素缩排四格    
	        FileWriter writer = new FileWriter(filePath);//输出流   
	        outputter.output(doc, writer);   
	        writer.close();   
	    } 
}
