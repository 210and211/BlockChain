package block;


import org.hive2hive.core.api.interfaces.IFileManager;
import p2pPeer.Peer;

import java.io.*;
import java.util.*;


public class BlockService implements Serializable {

    private IFileManager fileManager;
    private File rootPath;

    public BlockService(Peer peer){
        fileManager = peer.getNode().getFileManager();
        rootPath = peer.getFileAgent().getRoot();
    }

    //获取区块链的第一个区块
    Block getFristBlock() {
        //MedicalRecords[] data = null;
        return new Block(0, "00000000000000000000000000000000", "00000000000000000000000000000000", 1, null);

    }

    //通过区块的索引获取区块，需要获取到区块的文件
    public Block getblock(long index) {
        Block block;
        if (index == 0) {
            return getFristBlock();
        } else {
            try {
                File file = new File(rootPath, String.valueOf(index) + ".block");
                Boolean netORLocal = false;
                if(!file.exists()){         //如果本地不存在，则通过网络获取
                    fileManager.createDownloadProcess(file).execute();
                    netORLocal = true;
                }
                while(!file.exists()){
                    Thread.sleep(10);   //等待文件下载完成
                }
                FileInputStream inStream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(inStream);
                block = (Block) in.readObject();
                in.close();
                inStream.close();
                if(netORLocal){
                    file.delete();
                }
            } catch (Exception e) {
                block = null;
                e.printStackTrace();
            }
            return block;
        }
    }


    //将区块写入文件
    void save(Block block) {
        try {
            File file = new File(rootPath, String.valueOf(block.index) + ".block");
            FileOutputStream outStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(block);

            fileManager.createAddProcess(file).execute();   //向p2p网络广播
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //生成一个新区块
    public Block createBlock(List<MedicalRecords> dataList, Block previousBlock) {
        MedicalRecords[] data = new MedicalRecords[dataList.size()];
        dataList.toArray(data);

        MerkleTrees merkleTrees = new MerkleTrees(data);

        Node root = merkleTrees.getRoot();
        String fatherhash = previousBlock.gethash();
        Block block = new Block(previousBlock.index + 1, fatherhash, root.hash, data.length, data);
        return block;
    }

    //验证某个区块是否合法
    private boolean isValidNewBlock(Block newBlock, Block previous_Block, Block previous_two_Block) {
        if (!previous_Block.gethash().equals(newBlock.fatherhash)) {
            //System.out.println("hash is wrong");
            return false;
        }
        if (!newBlock.check_sign()) {
            //System.out.println("sign is wrong");
            return false;
        }
        //签名人要验证
        return true;
    }

    //验证某些区块是否合法
    private boolean isValidBlocks(List<Block> newBlocks) {
        Block fristBlock = newBlocks.get(0);
        if (!fristBlock.equals(getFristBlock())) {
            return false;
        }
        if (!isValidNewBlock(newBlocks.get(1), fristBlock, null))
            return false;

        Block previous_two_Block = newBlocks.get(0);
        Block previous_Block = newBlocks.get(1);

        for (int i = 2; i < newBlocks.size(); i++) {
            if (!isValidNewBlock(newBlocks.get(i), previous_Block, previous_two_Block)) {
                return false;
            }
            previous_Block = newBlocks.get(i);
            previous_two_Block = newBlocks.get(i - 1);

        }
        return true;
    }

    //区块溯源（由给出的条件，在区块中找出所有符合条件的记录）
    /**
     *  ①将index放入待查询区块集合
     *  ②从带查询区块集合中取出区块高度
     *  ③判断该区块是否在已查询过区块集合中，并放入已查询过区块集合
     *      若不在该集合中，获取该区块，遍历数据，筛选符合条件的，放入记录集合中
     *  ④从待查询集合中删除该块号
     *  ⑤回到①，直到待查询集合为空
     */
    public MedicalRecords[] traceToSource(long index, long patientID) {

        HashSet<MedicalRecords> data = new HashSet<MedicalRecords>();   //保存找到的记录
        HashSet<Long> indexArr = new HashSet<Long>();       //保存当前需要查找的区块号
        Iterator<Long> indexArrIt;
        HashSet<Long> foundIndex = new HashSet<Long>();    //保存已找过的区块号

        long blockIndex;
        MedicalRecords[] tmpData;

        indexArr.add(index);
        indexArrIt = indexArr.iterator();
        while (indexArr.size() != 0) {
            blockIndex = indexArrIt.next();

            if (foundIndex.add(blockIndex)) {    //若该区块未被找过
                tmpData = getblock(blockIndex).data;       //提取信息
                int patientIndex = binarySearch(tmpData,patientID);             //二分查找，找出第一个符合的信息索引

                if(patientIndex != -1){
                    for(int i = patientIndex; i < tmpData.length; i++){         //遍历信息
                        if (tmpData[i].getPatientID() == patientID) {           //匹配信息
                            data.add(tmpData[i]);                               //成功匹配的放入信息集中
                            if(tmpData[i].getPreBlockIndex() != 0){             //前一区块高度为0表示无前一区块
                                indexArr.add(tmpData[i].getPreBlockIndex());    //将前一区块放入待查找区块
                            }
                        }
                    }
                }
            }
            indexArr.remove(blockIndex);
            indexArrIt = indexArr.iterator();
        }
        return data.toArray(new MedicalRecords[]{});
    }

    //二分查找
    public int binarySearch(MedicalRecords[] data, long patientID){
        int low = 0;
        int high = data.length - 1;
        int middle = 0;

        if(patientID < data[low].getPatientID() || patientID >data[high].getPatientID() || low > high){
            return -1;
        }

        while (low < high){
            middle = (low + high)/2;
            if(data[middle].getPatientID() > patientID){
                high = middle - 1;
            }else if(data[middle].getPatientID() < patientID){
                low = middle + 1;
            }else {
                while (data[middle - 1].getPatientID() == patientID){
                    middle--;
                }
                return middle;
            }
        }
        return -1;
    }

    public MedicalRecords[] traceToSource(long index, long patientID, String sction){
        return null;
    }

    public MedicalRecords[] traceToSource(long index, long patientID, Date startTime, Date endTime){
        return null;
    }

    public MedicalRecords[] traceToSource(long index, long patientID, Date startTime, Date endTime, String sction){
        return null;
    }

    public static void main(String[] args){
        Peer peer = new Peer("C:/Users/new/Desktop/tmp");
        Peer peer1 = new Peer("localhost","C:/Users/new/Desktop/tmp1");
        BlockService blockService = new BlockService(peer);

        ArrayList<MedicalRecords> medicalRecordsArrayList = new ArrayList<MedicalRecords>();
        MedicalRecords[] medicalRecords = new MedicalRecords[10];
        medicalRecords[0] =
                new MedicalRecords(0, 1, 1, new Date(), "内科", null, null);
        medicalRecords[1] =
                new MedicalRecords(0, 2, 2, new Date(), "内科", null, null);
        medicalRecords[2] =
                new MedicalRecords(0, 3, 3, new Date(), "内科", null, null);
        medicalRecords[3] =
                new MedicalRecords(0, 4, 4, new Date(), "内科", null, null);
        medicalRecords[4] =
                new MedicalRecords(0, 5, 5, new Date(), "内科", null, null);
        medicalRecords[5] =
                new MedicalRecords(0, 6, 5, new Date(), "内科", null, null);
        medicalRecords[6] =
                new MedicalRecords(0, 7, 7, new Date(), "内科", null, null);
        medicalRecords[7] =
                new MedicalRecords(0, 8, 8, new Date(), "内科", null, null);
        medicalRecords[8] =
                new MedicalRecords(0, 9, 9, new Date(), "内科", null, null);
        medicalRecords[9] =
                new MedicalRecords(0, 10, 10, new Date(), "内科", null, null);

        for(int i = 0; i < medicalRecords.length; i++){
            medicalRecordsArrayList.add(medicalRecords[i]);
        }

        Block block = blockService.createBlock(medicalRecordsArrayList,blockService.getFristBlock());
        blockService.save(block);

        BlockService blockService1 = new BlockService(peer1);

        //Block block1 = blockService1.getblock(1);

        MedicalRecords[] test = blockService1.traceToSource(1,5);

        for(int i = 0; i < test.length; i++){
            System.out.printf("hospitalID:%d\tpatientID:%d\n", test[i].getHospitalID(),test[i].getPatientID());
        }
    }
}
