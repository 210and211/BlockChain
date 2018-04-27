package block;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import consensus.Configuration;

public class BlockMain extends Configuration{
	

	Block newblock;
	Block previousBlock;
	int index;

	
	BlockMain(Block previousBlock){
		this.previousBlock=previousBlock;
		if(previousBlock==null)
			previousBlock=new BlockService().getFristBlock();
		index=previousBlock.index;
	}
	BlockMain(int preindex){
		this.index=preindex;
		try {
			FileInputStream inStream = new FileInputStream(blockchainpath+index+".block");
			ObjectInputStream in = new ObjectInputStream(inStream);
			previousBlock=(Block) in.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			previousBlock =new BlockService().getFristBlock();
		}
		
	}
	Block create_block(List<String []> list){

		List<String> tempTxList = new ArrayList<String>();	//操作信息hash
		String[][] data=new String[list.size()][4];			//存储的数据
		
		
		MerkleTrees merkleTrees = new MerkleTrees(tempTxList);
	    Node root=merkleTrees.getRoot();
	    
	    newblock=new BlockService().addBlock(list.size(), root, data,previousBlock);
	    return newblock;
	}

	void save() throws Exception {
		if(index==0) {
			FileOutputStream outStream = new FileOutputStream(blockchainpath+index+".block");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(previousBlock);
		}
		FileOutputStream outStream = new FileOutputStream(blockchainpath+(index+1)+".block");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
		objectOutputStream.writeObject(newblock);
	}
	
	
	void trace_to_source(String pic_hash,int index) {
		
		
		
		
	}
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		BlockMain block=new BlockMain(null);

	}

}
