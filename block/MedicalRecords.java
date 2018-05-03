package block;


import javax.swing.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class MedicalRecords implements Serializable {
    private long preBlockIndex;         //患者前一条记录的区块高度（默认为0，即无前一区块）
    private boolean createOrObtain;     //创建病历或查阅病历
    private int hospitalID;     //医院ID
    private long patientID;     //患者ID
    private Date operateTime = null;   //信息产生时间
    private String section = null;     //就诊科室
    private String sign = null;        //主治医生签名
    private Document info = null;      //病历内容，若为获取病历信息则此条为null

    //构建一条创建病例的记录（如无前一条病例所在区块，则preBlockIndex=0）
    public MedicalRecords(long preBlockIndex, int hospitalID, long patientID, Date operateTime, String section, String sign, Document info){
        this.preBlockIndex = preBlockIndex;
        this.createOrObtain = true;
        this.hospitalID = hospitalID;
        this.patientID = patientID;
        this.operateTime = operateTime;
        this.section = section;
        this.sign = sign;
        this.info = info;
    }

    //构建一条查阅病例的记录（如无前一条病例所在区块，则preBlockIndex=0）
    public MedicalRecords(long preBlockIndex, int hospitalID, long patientID, Date operateTime, String section){
        this.preBlockIndex = preBlockIndex;
        this.createOrObtain = false;
        this.hospitalID = hospitalID;
        this.patientID = patientID;
        this.operateTime = operateTime;
        this.section = section;
    }

    public long getPreBlockIndex(){
        return preBlockIndex;
    }
    public boolean getCreateOrObtion(){
        return createOrObtain;
    }
    public int getHospitalID(){
        return hospitalID;
    }
    public long getPatientID(){
        return patientID;
    }
    public Date getOperateTime(){
        return operateTime;
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
}
