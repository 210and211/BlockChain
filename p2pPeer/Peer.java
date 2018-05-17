package p2pPeer;

import config.Configuration;
import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IH2HNode;
import org.hive2hive.core.api.interfaces.IUserManager;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Peer {
    Configuration config = new Configuration();
    private IH2HNode node;
    private FileAgent fileAgent;
    public Peer(String directory){
        fileAgent = new FileAgent(new File(directory));
        node = H2HNode.createNode(FileConfiguration.createDefault());
        node.connect(NetworkConfiguration.createInitial());

        IUserManager userManager = node.getUserManager();
        UserCredentials everyone = new UserCredentials(config.getPUBLIC_ID(), config.getPUBLIC_PASSWORD(),config.getPUBLIC_PIN());


        try{
            IProcessComponent<Void> register = userManager.createRegisterProcess(everyone);
            register.execute();

            IProcessComponent<Void> login = userManager.createLoginProcess(everyone, fileAgent);
            login.execute();

            node.getFileManager().subscribeFileEvents(new FileEventListener(node.getFileManager()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Peer(String ipAddr, String directory){

        fileAgent = new FileAgent(new File(directory));
        node = H2HNode.createNode(FileConfiguration.createDefault());
        try{
            NetworkConfiguration nodeConf = NetworkConfiguration.create(InetAddress.getByName(ipAddr));
            node.connect(nodeConf);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }

        IUserManager userManager = node.getUserManager();
        UserCredentials everyone = new UserCredentials(config.getPUBLIC_ID(), config.getPUBLIC_PASSWORD(),config.getPUBLIC_PIN());

        try {
            IProcessComponent<Void> login = userManager.createLoginProcess(everyone, fileAgent);
            login.execute();
            if(userManager.isLoggedIn()){//
                System.out.println("login success!");//
            }//

            node.getFileManager().subscribeFileEvents(new FileEventListener(node.getFileManager()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public IH2HNode getNode(){
        return node;
    }

    public FileAgent getFileAgent() {
        return fileAgent;
    }

/**

    public static void main(String args[]){
        Peer peer = new Peer("tmp");

        //Peer peer1 = new Peer("127.0.0.1","tmp1");

        try{
            File fileAtPeer = new File(peer.getFileAgent().getRoot(), "Test-file.txt");
            FileUtils.write(fileAtPeer, "Hello"); // add some content
            IFileManager fileManager = peer.getNode().getFileManager();
            fileManager.createAddProcess(fileAtPeer).execute();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
 /**/
}