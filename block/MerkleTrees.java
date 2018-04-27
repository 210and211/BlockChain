package block;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class MerkleTrees  implements Serializable{
      // transaction List
//      List<String> txList;
      // Merkle Root
      Node root;
      List<Node> list=new ArrayList<Node>();
      /**
       * constructor
       * @param txList transaction List 交易List
       */
      MerkleTrees(Node root){
    	  this.root=root;
      }
      MerkleTrees(List<String> txList) {
    	  
    	  for (int i = 0; i < txList.size(); i++) {
    		  Node node=new Node(txList.get(i), null, null,null);
			list.add(node);
		}
        root = null;
        merkle_tree();
      }

      /**
       * execute merkle_tree and set root.
       */
      private void merkle_tree() {
    	  while (list.size() != 1) {
    		  list = getNewTxList(list);

    	  }
    	  this.root = list.get(0);
      }

      /**
       * return Node Hash List.
       * @param tempTxList
       * @return
       */
      private List<Node> getNewTxList(List<Node> tempTxList) {

    	  List<Node> newTxList = new ArrayList<Node>();
    	  int index = 0;
    	  while (index < tempTxList.size()) {
          // left
    		  Node left = tempTxList.get(index);
    		  index++;
          // right
    		  Node right ;
    		  if (index != tempTxList.size()) {
    			  right = tempTxList.get(index);
    		  }else 
    			  right=left;
          // sha2 hex value
    		  String md5HexValue = getMD5Value(left.hash + right.hash);
    		  Node node=new Node(md5HexValue, left, right,null);
    		  left.father=node;
    		  right.father=node;
    		  newTxList.add(node);
    		  index++;
    	  }
    	  return newTxList;
      }

      /**
       * Return hex string
       * @param str
       * @return
       */
      public String getMD5Value(String str) {
    	  return MD5.MD5_32(str);

      }

      /**
       * Get Root
       * @return
       */
      public Node getRoot() {
    	  return this.root;
      }
      boolean IsNodeInTree(String hash){
    	  Node node=new Node(hash, null, null,null);
    	  return IsNodeInTree(root,node);
      }
   
      
      private void validate(Node ndoe) {
		// TODO 自动生成的方法存根
    	  
      }
      private boolean IsNodeInTree(Node root,Node node){
           if(root == null || node ==null)
                return false;
           
           if(root.equals(node)) {
        	   return true;
           }
           if((root.left!=null&&root.right!=null)&&(getMD5Value(root.left.hash + root.right.hash).equals(root.hash))) {
	           if( IsNodeInTree(root.left,node) || IsNodeInTree(root.right,node) )
	                return true;
           }
           return false;
      }
}
    