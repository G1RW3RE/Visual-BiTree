package TreeGenerator.Tree;

// 树节点
public class TreeNode {
    public TreeNode lChild;
    public TreeNode rChild;
    public Elem data;

    public TreeNode(Elem elem) {
        if (elem != null && elem.key != 0) {
            this.data = new Elem(elem);
        } else {
            this.data = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TreeNode) {
            TreeNode t = (TreeNode) obj;
            return this.data.equals(t.data);
        }
        return false;
    }
}
