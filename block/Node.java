package block;

import java.io.Serializable;

public class Node  implements Serializable{
	String hash;
	Node left;
	Node right;
	Node father;
	Node(String hash,Node left,Node right,Node faher){
		this.hash=hash;
		this.left=left;
		this.right=right;
		this.father=father;
		
	}
	boolean equals(Node node) {
		if(hash.equals(node.hash))
			return true;
		else
			return false;
	}
}
