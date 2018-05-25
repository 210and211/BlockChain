package patientCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class PatientCard implements Serializable{
    /**
     * 生成病历卡文件
     * 增加preBlockIndex
     * infoID++
     * 添加key
     */
    private long patientID;                     //患者ID
    private String name;                        //患者姓名
    private Map<Integer, String> key;           //患者在各个医院的密钥
    private int infoID;                         //患者下一次要生成的病历的序号
    private ArrayList<Long> preBlockIndex;      //患者最近一个病历所在区块号

    private PatientCard(long patientID, String name, Map<Integer, String> key){
        this.patientID = patientID;
        this.name = name;
        this.key = key;
        infoID = 1;         //一张新病历卡病历从1开始计数
        preBlockIndex = null;
    }

    public void save(String filePath){
        File file = new File(filePath);
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(this);

        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PatientCard getPatientCard(String filePath){
        File file = new File(filePath);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (PatientCard) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getPatientID() {
        return patientID;
    }

    public int getInfoID() {
        return infoID;
    }

    public String getName() {
        return name;
    }

    public long getPreBlockIndex(){
        return preBlockIndex.indexOf(preBlockIndex.size() - 1);
    }

    public long getPreBlockIndex(long index){
        return preBlockIndex.indexOf(index);
    }

    public ArrayList<Long> getAllPreBlockIndex(){
        return preBlockIndex;
    }

    public void addPreBlockIndex(long preBlockIndex){
        this.preBlockIndex.add(preBlockIndex);
    }

    public void addInfoID(){
        infoID++;
    }

    public void addKey(int hospitalID, String key){
        this.key.put(hospitalID,key);
    }
}