package block;


import config.Configuration;

import java.io.*;
import java.util.*;


public class BlockService implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6389836477095993916L;

    //获取区块链的第一个区块
    Block getFristBlock() {
        //MedicalRecords[] data = null;
        return new Block(0, "00000000000000000000000000000000", "00000000000000000000000000000000", 1, null);

    }

    //通过区块的索引获取区块，需要获取到区块的文件
    //若存储时采用分片则需要修改
    public Block getblock(long index) {
        Block block;
        if (index == 0) {
            return getFristBlock();
        } else {
            try {
                FileInputStream inStream = new FileInputStream(Configuration.BLOCKCHAINPATH + index + ".block");
                ObjectInputStream in = new ObjectInputStream(inStream);
                block = (Block) in.readObject();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                block = null;    //网络获取

            }
            return block;
        }
    }


    //将区块写入文件
    void save(Block block) {
        try {
            FileOutputStream outStream = new FileOutputStream(Configuration.BLOCKCHAINPATH + block.index + ".block");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(block);
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
            System.out.println("hash is wrong");
            return false;
        }
        if (!newBlock.check_sign()) {
            System.out.println("sign is wrong");
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
    *   根据index获取一个区块，再扫描操作信息
    *   得到符合条件的记录数组及其前一区块号数组
    *   遍历前一区块号数组，找出其中符合条件的记录，依次循环
    *   在寻找过程中向已找过区块记录数组添加，防止重复查找
    */
    MedicalRecords[] traceToSource(long index, long patientID) {

        HashSet<MedicalRecords> data = new HashSet<MedicalRecords>();   //保存找到的记录
        Iterator<MedicalRecords> dataIt = data.iterator();
        HashSet<Long> indexArr = new HashSet<Long>();       //保存当前需要查找的区块号
        Iterator<Long> indexArrIt = indexArr.iterator();
        HashSet<Long> foundIndex = new HashSet<Long>();    //保存已找过的区块号

        long blockIndex;
        indexArr.add(index);

        while (indexArr.size() != 0) {
            blockIndex = indexArrIt.next();

            if (foundIndex.add(blockIndex)) {    //若该区块未被找过
                MedicalRecords[] tmpData = getblock(blockIndex).data;       //提取信息
                for (int i = 0; i < tmpData.length; i++) {                  //遍历信息
                    if (tmpData[i].getPatientID() == patientID) {           //匹配信息
                        data.add(tmpData[i]);                               //成功匹配的放入信息集中
                        if(tmpData[i].getPreBlockIndex() != 0){             //前一区块高度为0表示无前一区块
                            indexArr.add(tmpData[i].getPreBlockIndex());    //将前一区块放入待查找区块
                        }
                    }
                }
            }
            indexArr.remove(blockIndex);
        }
        return (MedicalRecords[]) data.toArray();
    }

    MedicalRecords[] traceToSource(long index, long patientID, String section) {

        HashSet<MedicalRecords> data = new HashSet<MedicalRecords>();   //保存找到的记录
        Iterator<MedicalRecords> dataIt = data.iterator();
        HashSet<Long> indexArr = new HashSet<Long>();       //保存当前需要查找的区块号
        Iterator<Long> indexArrIt = indexArr.iterator();
        HashSet<Long> foundIndex = new HashSet<Long>();    //保存已找过的区块号

        long blockIndex;
        indexArr.add(index);

        while (indexArr.size() != 0) {
            blockIndex = indexArrIt.next();

            if (foundIndex.add(blockIndex)) {    //若该区块未被找过
                MedicalRecords[] tmpData = getblock(blockIndex).data;       //提取信息
                for (int i = 0; i < tmpData.length; i++) {                  //遍历信息
                    if (tmpData[i].getPatientID() == patientID &&
                            tmpData[i].getSection() == section) {           //匹配信息
                        data.add(tmpData[i]);                               //成功匹配的放入信息集中
                        if(tmpData[i].getPreBlockIndex() != 0){             //前一区块高度为0表示无前一区块
                            indexArr.add(tmpData[i].getPreBlockIndex());    //将前一区块放入待查找区块
                        }
                    }
                }
            }
            indexArr.remove(blockIndex);
        }
        return (MedicalRecords[]) data.toArray();
    }

    MedicalRecords[] traceToSource(long index, long patientID, Date startDate, Date endDate) {

        HashSet<MedicalRecords> data = new HashSet<MedicalRecords>();   //保存找到的记录
        Iterator<MedicalRecords> dataIt = data.iterator();
        HashSet<Long> indexArr = new HashSet<Long>();       //保存当前需要查找的区块号
        Iterator<Long> indexArrIt = indexArr.iterator();
        HashSet<Long> foundIndex = new HashSet<Long>();    //保存已找过的区块号

        long blockIndex;
        indexArr.add(index);

        while (indexArr.size() != 0) {
            blockIndex = indexArrIt.next();

            if (foundIndex.add(blockIndex)) {    //若该区块未被找过
                MedicalRecords[] tmpData = getblock(blockIndex).data;       //提取信息
                for (int i = 0; i < tmpData.length; i++) {                  //遍历信息
                    if (tmpData[i].getPatientID() == patientID &&
                            tmpData[i].getOperateTime().before(endDate) &&
                            startDate.before(tmpData[i].getOperateTime())) {           //匹配信息
                        data.add(tmpData[i]);                               //成功匹配的放入信息集中
                        if(tmpData[i].getPreBlockIndex() != 0){             //前一区块高度为0表示无前一区块
                            indexArr.add(tmpData[i].getPreBlockIndex());    //将前一区块放入待查找区块
                        }
                    }
                }
            }
            indexArr.remove(blockIndex);
        }
        return (MedicalRecords[]) data.toArray();
    }

}
