package consensus;

import FileFragmentation_delete.FileFragmentation_delete_control;
import block.Block;
import block.BlockService;
import block.MedicalRecords;
import config.Configuration;
import ip_net.My_ip;
import p2pPeer.Peer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.util.*;

public class Main {
    Configuration config = new Configuration();
    static long count = 0;
    void consensus(BlockService bs,Peer peer,Server server,String name,String my_ip,ArrayList<String> ip_list) {


        MedicalRecords[] records=new MedicalRecords[server.medicalRecords_list.size()];
        server.medicalRecords_list.toArray(records);
        server.medicalRecords_list.clear();
        server.medicalRecords_list = new ArrayList<MedicalRecords>();

        String[] s = new String[records.length]; // 设置发送信息
        for (int i = 0; i < s.length; i++) {
            s[i] = MD5Util.md5(records[i].toByteArray());
        }
        Byzantine bzt = new Byzantine();
        Byzantine_socket_info b_s_i = new Byzantine_socket_info();
        b_s_i.set_information(s);
        b_s_i.name = name;

        HashSet<ArrayList<String>> set = server.set;
        ArrayList<Byzantine_socket_info> bsi = server.bsi;
        bsi.add(b_s_i);

        int[] cicle = server.cicle;
//		server.set_byzantine_hash(set, bsi);

        ArrayList<String> ip_list_new = (ArrayList<String>) ip_list.clone();
        ip_list_new.remove(my_ip);
        bzt.set(name, ip_list_new, set, bsi, cicle);


        bzt.cicle(); // 发送
        while (true) {
            if (cicle[cicle.length - 1] >= config.getBYZANTINE_PEER_COUNT() - 1)
                break;
        }
        for(int i = 0; i < server.cicle.length; i++){
            server.cicle[i] = 0;
        }
        server.set_byzantine_block(bzt.result);

        MedicalRecords[] records_result = new MedicalRecords[bzt.result.size()];



        for (int i = 0, j = 0; i < s.length && j < records_result.length; i++) {
            if (bzt.result.contains(s[i])) {
                records_result[j] = records[i];			//设置共识后自己的数据
                j++;
            }
        }

        Block block=null;
        System.out.println(ip_list.get(0));
        if(!ip_list.get(0).equals(my_ip)) {
            SendMedicalRecords_thread sendMedicalRecords = new SendMedicalRecords_thread(ip_list.get(0), config.getPORT(), records_result);
            sendMedicalRecords.start();

            while(server.block==null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
            block=server.block;
            server.block=null;
            MedicalRecords[] data=block.data;
            if(data.length!=bzt.result.size()) {
                System.out.println("错误");
            }
            for(int i=0;i<data.length;i++) {
                if(!bzt.result.contains(MD5Util.md5(data[i].toByteArray()))) {
                    System.out.println("错误");
                }
            }
            block.sign(name);
            //检查block
            //对block签名
            int next_ip=ip_list.indexOf(my_ip)+1;
            if(next_ip<ip_list.size()) {
                SendBlock_Thread sendblock=new SendBlock_Thread(ip_list.get(next_ip),config.getPORT(),block);
                sendblock.start();
                System.out.println("next:"+next_ip);
            }else {
                bs.save(block);
                //config.getBLOCKCHAIN_HIGH()++;
            }
        }else {
            System.out.println("create block");
            /**
            for(int i=0;i<records_result.length;i++) {
                server.block_list.add(records_result[i]);
            }
            /**/
            System.out.println("server.block_list.size():\t"+server.block_list.size());
            System.out.println("bzt.result.size():\t"+bzt.result.size());
            Iterator it=server.block_list.iterator();
            while(it.hasNext()) {
                MedicalRecords mr=(MedicalRecords) it.next();
                System.out.println(MD5Util.md5(mr.toByteArray()));
            }


            while (server.block_list.size() != bzt.result.size()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
            HashSet result_block=server.block_list;
            server.block_list=new HashSet<>();
            System.out.println(result_block.size());

            List dataList=new ArrayList<>(result_block);
            //long index=config.getBLOCKCHAIN_HIGH();									/**上一个区块高度****/
            block=bs.createBlock(dataList, bs.getblock(count));
            System.out.println("count:  "+count);

            block.sign(name);			//区块签名
            SendBlock_Thread sendblock=new SendBlock_Thread(ip_list.get(1),config.getPORT(),block);
            sendblock.start();
            System.out.println("成功"+ip_list.get(1));


        }

        if(ip_list.indexOf(my_ip)==ip_list.size()-1) {
            if(block!=null) {		//验证区块
                System.out.println("成功");
            }
        }
        server.hash_list.clear();
        server.hash_list=null;
        server.set.clear();
        server.set=new HashSet<>();
        server.bsi.clear();
        server.bsi=new ArrayList<>();
        count++;
    }

    public long getBlockChainHigh(){
        File blockChainDir = new File(config.getBLOCKCHAIN_SAVE_PATH());
        String[] fileSet = blockChainDir.list();
        if(fileSet.length==0)
        	return 0;
        long[] numSet = new long[fileSet.length];

        for(int i = 0; i < fileSet.length; i++){
            numSet[i] = Long.parseLong(fileSet[i].split("\\.")[0]);
        }
        Arrays.sort(numSet);

        return numSet[numSet.length - 1];
    }




    public static void main(String[] args) throws IOException {
        // TODO 自动生成的方法存根
        //System.setOut(new PrintStream(new FileOutputStream("log.txt")));
        Configuration config = new Configuration();
        Main main = new Main();

        Peer peer;
        if(config.getP2P_NODE_IP().equals("")){
            peer = new Peer(config.getBLOCKCHAIN_SAVE_PATH());
        }else {
            peer = new Peer(config.getP2P_NODE_IP(),config.getBLOCKCHAIN_SAVE_PATH());
        }

        BlockService bs = new BlockService(peer);

        Server server = new Server(peer); // 开启服务端
        server.start();
        String name = config.getSERVER_NAME();



        String my_ip="";
        try {
            my_ip=My_ip.getLocalHostLANAddress();
            System.out.println("my_ip:"+my_ip);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

//        Thread delete = new Thread(new FileFragmentation_delete_control(peer,server.block_Fragmentationdelete_us));
//        delete.start();
/**/
        count = main.getBlockChainHigh();
        long t1=System.currentTimeMillis();
        while(true) {
            ArrayList<String> list = new ArrayList<String>();
            for(int i=0;i<config.getIP_LIST().size();i++) {
                list.add(config.getIP_LIST().get(i));
            }
            System.out.println("count"+count);
            ArrayList<String> ip_list=new POS().get_node(list,
                    config.getBYZANTINE_PEER_COUNT(),
                    bs.getblock(count-1).gethash());
            for (int o=0;o<ip_list.size();o++){
                System.out.println(ip_list.get(o));
            }
            try {
                Thread.sleep(t1-System.currentTimeMillis()+config.getCREATE_BLOCK_TIME());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            t1=System.currentTimeMillis();
            if(ip_list.contains(my_ip)) {
                new Main().consensus(bs,peer,server,name,my_ip,ip_list);
            }

        }
/**/

    }

}