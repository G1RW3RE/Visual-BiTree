package TreeGenerator;

import TreeGenerator.Tree.Elem;
import TreeGenerator.Tree.Tree;
import TreeGenerator.Tree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TREE GENERATOR BY GLORIA
 * @author ggloria
 * */
public class TreeGen extends JFrame implements MouseListener, ActionListener, MouseMotionListener {

    private PopupMenu popupMenu;
    private MenuItem miCreate;
    private MenuItem miClear;
    private MenuItem miAddRoot;
    private MenuItem miRefresh;
    private PopupMenu popupMenuNode;
    private MenuItem miAddLeft, miAddRight;
    private MenuItem miDelete;
    private MenuItem miChange;
    private TreePanel treePanel;

    public TreeGen() {
        super("二叉树测试");
        setSize(1080 + 300, 720 + 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        popupMenu = new PopupMenu();
        miCreate = new MenuItem();
        miCreate.setLabel("New tree");
        miCreate.addActionListener(this);
        miClear = new MenuItem();
        miClear.setLabel("Clear tree");
        miClear.addActionListener(this);
        miAddRoot = new MenuItem();
        miAddRoot.setLabel("Add root node");
        miAddRoot.addActionListener(this);
        miRefresh = new MenuItem();
        miRefresh.setLabel("Refresh");
        miRefresh.addActionListener(this);
        popupMenu.add(miCreate);
        popupMenu.add(miClear);
        popupMenu.add(miAddRoot);
        popupMenu.add(miRefresh);
        add(popupMenu);

        popupMenuNode = new PopupMenu();
        miAddLeft = new MenuItem();
        miAddLeft.setLabel("Add left child");
        miAddLeft.addActionListener(this);
        miAddRight = new MenuItem();
        miAddRight.setLabel("Add right child");
        miAddRight.addActionListener(this);
        miDelete = new MenuItem();
        miDelete.setLabel("Delete this node");
        miDelete.addActionListener(this);
        miChange = new MenuItem();
        miChange.setLabel("Modify node");
        miChange.addActionListener(this);
        popupMenuNode.add(miAddLeft);
        popupMenuNode.add(miAddRight);
        popupMenuNode.add(miChange);
        popupMenuNode.add(miDelete);
        add(popupMenuNode);

        treePanel = new TreePanel();
        treePanel.addMouseListener(this);
        setContentPane(treePanel);
        treePanel.addMouseMotionListener(this);

    }

    public static void main(String[] args) {
        new TreeGen();
    }

    private TreeNode clickingNode;

    @Override
    public void mouseClicked(MouseEvent event) {
       if(event.getButton() == MouseEvent.BUTTON3) {
           TreeNode node = treePanel.getEnteredNode(event.getX(), event.getY());
           if(node == null) {
               popupMenu.show(this, event.getX() + 20, event.getY() + 30);
           } else {
               clickingNode = treePanel.getHighlightingNode(event.getX(), event.getY());
               if(clickingNode == null) clickingNode = node;
               popupMenuNode.show(this, event.getX() + 20, event.getY() + 30);
           }
       }
       if(event.getButton() == MouseEvent.BUTTON1) {
           treePanel.highlightNode(event.getX(), event.getY());
       }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // 外部菜单
        /* 刷新 */
        if(event.getSource() == miRefresh) {
            treePanel.updateTree();
        }
        /* 清空树 */
        if(event.getSource() == miClear) {
            int op;
            if(!treePanel.isEmptyTree()) {
                op = JOptionPane.showConfirmDialog(this, "确认要清空整棵树吗？", "清空", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            } else {
                op = JOptionPane.OK_OPTION;
            }
            if(op == JOptionPane.OK_OPTION) {
                treePanel.clearTree();
            }
        }
        /* 重建 */
        if(event.getSource() == miCreate) {
            int op;
            if(!treePanel.isEmptyTree()) {
                op = JOptionPane.showConfirmDialog(this, "确认要清除并重建吗？", "新建", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            } else {
                op = JOptionPane.OK_OPTION;
            }
            if (op == JOptionPane.OK_OPTION) {
                String str = JOptionPane.showInputDialog(this, "请输入key，others数据，空格隔开：", "创建树", JOptionPane.PLAIN_MESSAGE);
                if (str != null && str.trim().contains(" ")) {
                    str = str.trim();
                    System.out.println("Creating new tree.");
                    String[] ss = str.split(" ");
                    int key;
                    try {
                        key = Integer.parseInt(ss[0]);
                    } catch (NumberFormatException ignored) {
                        key = 0;
                    }
                    String others = ss[1].intern();
                    if (key > 0) {
                        Elem elem = new Elem(key, others);
                        treePanel.createRoot(elem);
                    }
                }
            }
        }
        /* 添加根节点 */
        if(event.getSource() == miAddRoot) {
            insertNodeToTree("添加根节点", Tree.INSERT_ROOT);
        }
        // 节点菜单
        /* 添加左节点 */
        if(event.getSource() == miAddLeft) {
            insertNodeToTree("添加节点", Tree.INSERT_LEFT);
        }
        /* 添加右节点 */
        if(event.getSource() == miAddRight) {
            insertNodeToTree("添加节点", Tree.INSERT_RIGHT);
        }
        /* 修改数据 */
        if(event.getSource() == miChange) {
            String str = JOptionPane.showInputDialog(this, "请输入替换的数据：", "修改", JOptionPane.PLAIN_MESSAGE);
            if(str != null) {
                str = str.trim().intern();
                treePanel.modifyNodeData(clickingNode, str);
            }
        }
        /* 删除节点 */
        if(event.getSource() == miDelete) {
            treePanel.deleteNode(clickingNode);
        }
    }

    private void insertNodeToTree(String title, int where) {
        String str = JOptionPane.showInputDialog(this, "请输入key，others数据，空格隔开：", title, JOptionPane.PLAIN_MESSAGE);
        if (str != null && str.trim().contains(" ")) {
            str = str.trim();
            String[] ss = str.split(" ");
            int key;
            try {
                key = Integer.parseInt(ss[0]);
            } catch (NumberFormatException ex) {
                key = 0;
            }
            String others = ss[1];
            if (key > 0) {
                Elem elem = new Elem(key, others);
                int result = treePanel.attachToNode(clickingNode, elem, where);
                // 插入节点重复
                if(result == Tree.INSERT_FAIL_NODE_EXISTS) {
                    JOptionPane.showMessageDialog(this, "插入的节点与已有节点重复！", "无法插入", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

}
