import javax.swing.text.Document;

public class MedicalRecords {
    private boolean createOrObtain;     //创建病历或获取病历
    private int hospitalID;     //医院ID
    private long patientID;     //患者ID
    private String section;     //就诊科室
    private Document info;      //病历内容，若为获取病历信息则此条为null

    public MedicalRecords(int hospitalID, int patientID, String section, Document info){
        createOrObtain = true;
        this.hospitalID = hospitalID;
        this.patientID = patientID;
        this.section = section;
        this.info = info;
    }
    public MedicalRecords(int hospitalID, int patientID, String section){
        createOrObtain = false;
        this.hospitalID = hospitalID;
        this.patientID = patientID;
        this.section = section;
        this.info = null;
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
    public String getSection(){
        return section;
    }

    public Document getInfo() {
        return info;
    }
}
