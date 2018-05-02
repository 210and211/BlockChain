package block;


import config.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


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


    //向区块链中添加一个新区块？
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

    //区块溯源
    MedicalRecords[] trace_to_source(long patientID,long index) {
        //根据index获取一个区块，再扫描操作信息，得到后知道上一个index，再依次循环
        for (;;){
            getblock(index);
            ////

        }


    }

}
