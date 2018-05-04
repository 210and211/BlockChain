package p2pPeer;

import org.apache.commons.io.FileUtils;
import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.api.interfaces.IH2HNode;
import org.hive2hive.core.api.interfaces.IUserManager;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import FileFragmentation_delete.FileFragmentation_delete;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import FileFragmentation_delete.*;
import config.*;
public class Peer {

    private IH2HNode node;
    private FileAgent fileAgent;
    public Peer(String directory){
        fileAgent = new FileAgent(new File(directory));
        node = H2HNode.createNode(FileConfiguration.createDefault());
        node.connect(NetworkConfiguration.createInitial());

        IUserManager userManager = node.getUserManager();
        UserCredentials everyone = new UserCredentials(Configuration.PUBLIC_ID, Configuration.PUBLIC_PASSWORD,Configuration.PUBLIC_PIN);
       
		
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
        UserCredentials everyone = new UserCredentials(Configuration.PUBLIC_ID, Configuration.PUBLIC_PASSWORD,Configuration.PUBLIC_PIN);

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
    
   

    public static void main(String args[]){
        Peer peer1 = new Peer("C:\\Users\\new\\Desktop\\tmp1");

        try{
            Peer peer2 = new Peer("127.0.0.1","C:\\Users\\new\\Desktop\\tmp2");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            File fileAtPeer1 = new File(peer1.getFileAgent().getRoot(), "test-file.txt");
            FileUtils.write(fileAtPeer1, "Hello"); // add some content
            IFileManager fileManager1 = peer1.getNode().getFileManager();
            fileManager1.createAddProcess(fileAtPeer1).execute();
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        Thread delete=new Thread(new FileFragmentation_delete_control(peer1));
    }

}
