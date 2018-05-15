package block;


import javax.swing.text.Document;

import consensus.MD5Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class MedicalRecords implements Serializable {
    private long preBlockIndex;         //患者前一条记录的区块高度（默认为0，即无前一区块）
    private boolean createOrObtain;     //创建病历或查阅病历，创建true为查阅为false
    private Date operateTime;           //信息产生时间
    private int opHospitalID;       //产生此操作记录的医院ID，若为创建操作则与hospitalID相同，否则为发起查询医院的ID
    private int infoID;             //通过patientID和infoID唯一确定患者的病历，每位患者的病历从1开始计数
    /*病历信息*/
    private int hospitalID;             //医院ID
    private long patientID;             //患者ID
    private String section;             //就诊科室
    private String sign = null;         //主治医生签名(若为获取病历信息则此条为null)
    private Document info = null;       //病历内容(若为获取病历信息则此条为null)

    //构建一条创建病例的记录（如无前一条病例所在区块，则preBlockIndex=0）
    public MedicalRecords(long preBlockIndex, Date operateTime, int infoID, int hospitalID, long patientID, String section, String sign, Document info){
        this.preBlockIndex = preBlockIndex;
        this.createOrObtain = true;
        this.operateTime = operateTime;
        this.opHospitalID = hospitalID;
        this.infoID = infoID;
        this.hospitalID = hospitalID;
        this.patientID = patientID;
        this.section = section;
        this.sign = sign;
        this.info = info;
    }

    //构建一条查阅病例的记录（如无前一条病例所在区块，则preBlockIndex=0）
    public MedicalRecords(long preBlockIndex, Date operateTime,int opHospitalID, int infoID, int hospitalID, long patientID, String section){
        this.preBlockIndex = preBlockIndex;
        this.createOrObtain = false;
        this.operateTime = operateTime;
        this.opHospitalID = opHospitalID;
        this.infoID = infoID;
        this.hospitalID = hospitalID;
        this.patientID = patientID;
        this.section = section;
    }

    public long getPreBlockIndex(){
        return preBlockIndex;
    }
    public boolean getCreateOrObtion(){
        return createOrObtain;
    }
    public Date getOperateTime(){
        return operateTime;
    }

    public int getOpHospitalID() {
        return opHospitalID;
    }

    public int getInfoID() {
        return infoID;
    }

    public int getHospitalID(){
        return hospitalID;
    }
    public long getPatientID(){
        return patientID;
    }

    public String getSection(){
        return section;
    }
    public String getSign(){
        return sign;
    }
    public Document getInfo() {
        return info;
    }

    public byte[] toByteArray () {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    @Override
    public int hashCode() 
    {
        return new Long(patientID).hashCode(); 
    } 
    @Override
    public boolean equals(Object o) {
    	MedicalRecords o1=(MedicalRecords) o;
    	String s=MD5Util.md5(o1.toByteArray());
    	return MD5Util.md5(toByteArray()).equals(s);
    	
    }
    
    
}
