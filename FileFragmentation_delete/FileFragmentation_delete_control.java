package FileFragmentation_delete;

import config.*;

import p2pPeer.*;

import java.io.File;
import java.util.Arrays;

public class FileFragmentation_delete_control extends Thread {
    Peer peer;
    int[] block_us;
    Configuration config = new Configuration();

    public FileFragmentation_delete_control(Peer peer, int[] block_us) {
        this.peer = peer;
        this.block_us = block_us;
    }

    public long getBlockChainHigh(){
        File blockChainDir = peer.getFileAgent().getRoot();
        String[] fileSet = blockChainDir.list();
        long[] numSet = new long[fileSet.length];

        for(int i = 0; i < fileSet.length; i++){
            numSet[i] = Long.parseLong(fileSet[i].split("\\.")[0]);
        }
        Arrays.sort(numSet);

        return numSet[numSet.length - 1];
    }

    public void run() {//循环一轮删除节点

        while (true) {
            try {
                Thread.sleep(5000);//12个小时启动一次
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }

            for (long count = getBlockChainHigh(); count > (getBlockChainHigh() - config.getBLOCKCHAIN_RANGE()) && count > 0; count--) {
                Thread DeletetThread = new Thread(new FileFragmentation_delete(count, config.getPORT(), config.getIP_LIST(), block_us, peer));
                System.out.println("kaishishanchu" + count + "号区块");
                DeletetThread.start();
            }
        }

    }
}
