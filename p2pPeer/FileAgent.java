package p2pPeer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.hive2hive.core.file.IFileAgent;


public class FileAgent implements IFileAgent {

    private final File root;

    public FileAgent() {
        root = new File(FileUtils.getTempDirectory(), UUID.randomUUID().toString());
        root.mkdirs();
    }
    public FileAgent(File rootDir){
        rootDir = rootDir.getAbsoluteFile();
        if(rootDir.exists()){
            if(!rootDir.isDirectory()){
                rootDir.mkdir();
            }
        }else {
            rootDir.mkdir();
        }
        root = rootDir;
    }

    @Override
    public File getRoot() {
        return root;
    }

    @Override
    public void writeCache(String key, byte[] data) throws IOException {
        // do nothing as examples don't depend on performance
    }

    @Override
    public byte[] readCache(String key) throws IOException {
        // do nothing as examples don't depend on performance
        return null;
    }

}