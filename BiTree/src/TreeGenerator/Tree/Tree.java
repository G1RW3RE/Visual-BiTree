package TreeGenerator.Tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class Tree {

    public final static int CT_PRE_WITH_EMPTY = 1;
    public final static int CT_LAYER_WITH_EMPTY = 2;

    public TreeNode root = null;

    public Tree() {

    }

    public static Tree createTree(int type, List<Elem> definition) {
        return createTree(type, definition, null);
    }

    public static Tree createTree(int type, List<Elem> definition, List<Elem> definition2) {
        Tree t = new Tree();
        TreeNode root = null;
        switch (type) {
            case CT_PRE_WITH_EMPTY:
                // 前序带空子树遍历
                if(definition != null && !definition.isEmpty()) {
                    Iterator<Elem> iterator = definition.iterator();
                    root = t.createNode(iterator);
                    t.root = root;
                    return t;
                } else {
                    return null;
                }
            case CT_LAYER_WITH_EMPTY:
                // 层序带空子树遍历
                Queue<TreeNode> queue = new LinkedList<>();
                if(definition != null && !definition.isEmpty()) {
                    int index = 0;
                    Elem elem = definition.get(index++);
                    root = new TreeNode(elem);
                    TreeNode node = null;
                    queue.offer(root);
                    while (!queue.isEmpty()) {
                        node = queue.poll();
                        if(index < definition.size()) {
                            elem = definition.get(index++);
                            if(elem.key != 0 && elem.key != -1) {
                                node.lChild = new TreeNode(elem);
                                queue.offer(node.lChild);
                            }
                        }
                        if(index < definition.size()) {
                            elem = definition.get(index++);
                            if(elem.key != 0 && elem.key != -1) {
                                node.rChild = new TreeNode(elem);
                                queue.offer(node.rChild);
                            }
                        }
                    }
                    t.root = root;
                    return t;
                } else {
                    return null;
                }
            default:
                System.out.println("Unexpected value: " + type);
                return null;
        }
    }

    /**
     * 递归创建前序遍历节点
     * */
    private TreeNode createNode(Iterator<Elem> iterator) {
        Elem elem = iterator.next();
        if(elem != null && elem.key != 0 && elem.key != -1) {
            TreeNode node = new TreeNode(elem);
            node.lChild = createNode(iterator);
            node.rChild = createNode(iterator);
            return node;
        } else {
            return null;
        }
    }

    public static final int INSERT_LEFT = 1;
    public static final int INSERT_RIGHT = 2;
    public static final int INSERT_ROOT = 3;
    public static final int INSERT_SUCCESS = 0;
    public static final int INSERT_FAIL_NODE_EXISTS = 1;
    public static final int INSERT_FAIL_INVALID_PARAM = 2;
    public int insertNode(TreeNode parent, TreeNode node, int where) {
        if(where != INSERT_ROOT && parent == null || node == null) return INSERT_FAIL_INVALID_PARAM;
        if(findNode(node) != null) return INSERT_FAIL_NODE_EXISTS;
        TreeNode oldChild;
        switch (where) {
            case INSERT_LEFT:
                oldChild = parent.lChild;
                if (oldChild != null) {
                    node.rChild = oldChild;
                }
                parent.lChild = node;
                break;
            case INSERT_RIGHT:
                oldChild = parent.rChild;
                if(oldChild != null) {
                    node.rChild = oldChild;
                }
                parent.rChild = node;
                break;
            case INSERT_ROOT:
                node.rChild = root;
                root = node;
                break;
            default:
                return INSERT_FAIL_INVALID_PARAM;
        }
        return INSERT_SUCCESS;
    }

    public void display() {
        Queue<TreeNode> queue = new LinkedList<>();
        if(!this.isEmpty()) {
            queue.offer(root);
            while(!queue.isEmpty()) {
                TreeNode node = queue.poll();
                System.out.print(node.data + "\t");
                if(node.lChild != null) queue.offer(node.lChild);
                if(node.rChild != null) queue.offer(node.rChild);
            }
        }
        System.out.println();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void modifyNodeData(TreeNode node, String data) {
        if(node != null) {
            node.data.others = data.intern();
        }
    }


    public TreeNode findParent(TreeNode node) {
        if(node == null) return null;
        return findParent(root, node);
    }

    private TreeNode findParent(TreeNode curNode, TreeNode target) {
        TreeNode t = null;
        if (curNode.lChild != null && curNode.lChild.equals(target) || curNode.rChild != null && curNode.rChild.equals(target)) {
            return curNode;
        }
        if(curNode.lChild != null) {
            t = findParent(curNode.lChild, target);
        }
        if(t == null && curNode.rChild != null) {
            t = findParent(curNode.rChild, target);
        }
        return t;
    }

    public TreeNode findNode(TreeNode node) {
        return findNode(this.root, node.data);
    }

    public TreeNode findNode(Elem elem) {
        return findNode(this.root, elem);
    }

    private TreeNode findNode(TreeNode node, Elem elem) {
        if(node == null) return null;
        TreeNode t;
        if(node.data.equals(elem)) {
            return node;
        } else {
            t = findNode(node.lChild, elem);
            if(t == null) {
                t = findNode(node.rChild, elem);
            }
            return t;
        }
    }

    public void removeNode(TreeNode node) {
        TreeNode parent = findParent(node);
        TreeNode t;
        if(parent == null) {
            // root
            if(node.lChild == null && node.rChild == null) {
                // degree = 0
                root = null;
            } else if(node.lChild != null && node.rChild == null) {
                // degree = 1, left
                root = node.lChild;
            } else if(node.lChild == null) {
                // degree = 1, right
                root = node.rChild;
            } else {
                // degree = 2
                t = node.lChild;
                while(t.rChild != null) { t = t.rChild; }
                root = node.lChild;
                t.rChild = node.rChild;
            }
        } else if(node.equals(parent.lChild)){
            if(node.lChild == null && node.rChild == null) {
                // degree = 0
                parent.lChild = null;
            } else if(node.lChild != null && node.rChild == null) {
                // degree = 1, left
                parent.lChild = node.lChild;
            } else if(node.lChild == null) {
                // degree = 1, right
                parent.lChild = node.rChild;
            } else {
                // degree = 2
                t = node.lChild;
                while(t.rChild != null) { t = t.rChild; }
                parent.lChild = node.lChild;
                t.rChild = node.rChild;
            }
        } else {
            if(node.lChild == null && node.rChild == null) {
                // degree = 0
                parent.rChild = null;
            } else if(node.lChild != null && node.rChild == null) {
                // degree = 1, left
                parent.rChild = node.lChild;
            } else if(node.lChild == null) {
                // degree = 1, right
                parent.rChild = node.rChild;
            } else {
                // degree = 2
                t = node.lChild;
                while(t.rChild != null) { t = t.rChild; }
                parent.rChild = node.lChild;
                t.rChild = node.rChild;
            }
        }
    }

    private int maxDepth;
    public int getDepth() {
        maxDepth = 0;
        if(root != null) {
            calcDepth(root, 1);
            return maxDepth;
        } else {
            return 0;
        }
    }

    private void calcDepth(TreeNode node, int depth) {
        if(depth > maxDepth) {
            maxDepth = depth;
        }
        if(node.lChild != null) {
            calcDepth(node.lChild, depth + 1);
        }
        if(node.rChild != null) {
            calcDepth(node.rChild, depth + 1);
        }
    }

    public void preOrderTraverse(Consumer<TreeNode> visitor) {
        preOrderTraverse(root, visitor);
    }

    public void preOrderTraverse(TreeNode node, Consumer<TreeNode> visitor) {
        if(node == null) return;
        visitor.accept(node);
        preOrderTraverse(node.lChild, visitor);
        preOrderTraverse(node.rChild, visitor);
    }

    public void inOrderTraverse(Consumer<TreeNode> visitor) {
        inOrderTraverse(root, visitor);
    }

    private void inOrderTraverse(TreeNode node, Consumer<TreeNode> visitor) {
        if(node == null) return;
        inOrderTraverse(node.lChild, visitor);
        visitor.accept(node);
        inOrderTraverse(node.rChild, visitor);
    }

    public void postOrderTraverse(Consumer<TreeNode> visitor) {
        postOrderTraverse(root, visitor);
    }

    private void postOrderTraverse(TreeNode node, Consumer<TreeNode> visitor) {
        if(node == null) return;
        postOrderTraverse(node.lChild, visitor);
        postOrderTraverse(node.rChild, visitor);
        visitor.accept(node);
    }

    public void levelOrderTraverse(Consumer<TreeNode> visitor) {
        if(root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode node;
        queue.offer(root);
        while(!queue.isEmpty()) {
            node = queue.poll();
            visitor.accept(node);
            if(node.lChild != null) queue.offer(node.lChild);
            if(node.rChild != null) queue.offer(node.rChild);
        }
    }

    /** 前序带空子树标准输出 */

    public void preOrderTraverseWithNull(Consumer<TreeNode> visitor) {
        preOrderTraverseWithNull(root, visitor);
    }

    public void preOrderTraverseWithNull(TreeNode node, Consumer<TreeNode> visitor) {
        if(node == null) {
            visitor.accept(null);
            return;
        }
        visitor.accept(node);
        preOrderTraverseWithNull(node.lChild, visitor);
        preOrderTraverseWithNull(node.rChild, visitor);
    }
}
