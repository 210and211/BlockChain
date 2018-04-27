package block;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BlockService  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6389836477095993916L;

    Block getFristBlock() {
    	String[][] data= {{"frist"}};
        return new Block(0,"00000000000000000000000000000000", "00000000000000000000000000000000",1,null,data) ;
        
    }

    public Block addBlock(int num, Node root,String[][] data,Block previousBlock) {
        String fatherhash = previousBlock.gethash();
        Block block= new Block(previousBlock.index+1,fatherhash, root.hash, num, root, data);
        return block;
    }

    private boolean isValidNewBlock(Block newBlock, Block previous_Block,Block previous_two_Block) {
    	if (!previous_Block.gethash().equals(newBlock.fatherhash)) {
    		System.out.println("hash is wrong");
    		return false;
    	}
    	if(!newBlock.check_sign()) {
    		System.out.println("sign is wrong");
    		return false;
    	}
		//签名人要验证
    	return true;
    }


    private boolean isValidBlocks(List<Block> newBlocks) {
    	Block fristBlock = newBlocks.get(0);
    	if (!fristBlock.equals(getFristBlock())) {
    		return false;
    	}
    	if(!isValidNewBlock(newBlocks.get(1), fristBlock,null))
    		return false;
        
    	Block previous_two_Block=newBlocks.get(0);
    	Block previous_Block=newBlocks.get(1);
    	
    	for (int i = 2; i < newBlocks.size(); i++) {
    		if (!isValidNewBlock(newBlocks.get(i), previous_Block,previous_two_Block)) {
    			return false;
    		}
    		previous_Block = newBlocks.get(i);
    		previous_two_Block=newBlocks.get(i-1);
    		
    	}
    	return true;
    }


    boolean exist(Block block,String hash, Block previous_Block,Block previous_two_Block) {
    	if(isValidNewBlock(block,previous_Block,previous_two_Block)) {
	    	MerkleTrees merkleTrees = new MerkleTrees(block.root);
	        	if(merkleTrees.IsNodeInTree(hash))
	        		return true;
    	}else {
    		System.out.println("区块链被损坏");
    		return false;
    	}
    	return false;
    	
    }
    
}
