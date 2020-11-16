package TreeGenerator;

import TreeGenerator.Tree.Elem;
import TreeGenerator.Tree.Tree;
import TreeGenerator.Tree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TreePanel extends JPanel {

    private static final int Y_OFF = 20;
    private static final int X_OFF = 100;
    public Tree tree;

    public TreePanel() {
        super();
        tree = new Tree();
    }

    private static class ClickableTreeNode extends TreeNode {

        public Rectangle clickBound;

        public ClickableTreeNode(Elem elem) {
            super(elem);
        }

    }

    public boolean isEmptyTree() {
        return tree.root == null;
    }

    public void createRoot(Elem elem) {
        tree.root = new ClickableTreeNode(elem);
        updateTree();
    }

    public void modifyNodeData(TreeNode node, String data) {
        tree.modifyNodeData(node, data);
        updateTree();
    }

    public int attachToNode(TreeNode parent, Elem elem, int where) {
        int result = tree.insertNode(parent, new ClickableTreeNode(elem), where);
        updateTree();
        return result;
    }

    public void deleteNode(TreeNode node) {
        tree.removeNode(node);
        updateTree();
    }

    public void clearTree() {
        tree.root = null;
        updateTree();
    }

    public StringBuffer stringBuffer;
    private void visit(TreeNode node) {
        if(node == null) return;
        stringBuffer.append(node.data.key + "  ");
    }

    private void visitNull(TreeNode node) {
        if(node == null) {
            stringBuffer.append("0 null  ");
        } else {
            stringBuffer.append(node.data.key + " " + node.data.others + "  ");
        }
    }

    public void drawTraverseResult(Graphics2D g, int x, int y) {
        g.setFont(FONT_TRAVERSE);

        stringBuffer = new StringBuffer("前序： ");
        tree.preOrderTraverse(this::visit);
        g.drawString(stringBuffer.toString(), x, y);

        stringBuffer = new StringBuffer("中序： ");
        tree.inOrderTraverse(this::visit);
        g.drawString(stringBuffer.toString(), x, y + 40);

        stringBuffer = new StringBuffer("后序： ");
        tree.postOrderTraverse(this::visit);
        g.drawString(stringBuffer.toString(), x, y + 80);

        stringBuffer = new StringBuffer("层序： ");
        tree.levelOrderTraverse(this::visit);
        g.drawString(stringBuffer.toString(), x, y + 120);

        stringBuffer = new StringBuffer("前序带空子树： ");
        tree.preOrderTraverseWithNull(this::visitNull);
        g.drawString(stringBuffer.toString() + "-1 null", x, y + 160);
    }

    int radiusX, radiusY, distance;
    int[] xs = new int[1024];
    int[] ys = new int[1024];
    private final static BasicStroke STOKE_LINE = new BasicStroke(2.0f);

    @Override
    public void update(Graphics g) {
        super.update(g);
        updateTree();
    }

    @Override
    public void repaint() {
        super.repaint();
        updateTree();
    }

    private final static Font FONT_NODE = new Font("等线", Font.BOLD, 16);
    private final static Font FONT_TRAVERSE = new Font("等线", Font.BOLD, 18);

    /** 在屏幕上显示树 */
    public void updateTree() {
        Graphics2D g = (Graphics2D) this.getGraphics();
        if(g != null) g.clearRect(0, 0, getWidth(), getHeight());
        if (tree == null) return;
        int depth = tree.getDepth();
        int H = getHeight() - Y_OFF - 200, W = getWidth() - X_OFF - 200;
        if(depth > 0) {
            radiusY = 2 * H / (depth * 3 + 1);
            radiusY = Math.min(radiusY, 60);
            radiusX = radiusY * 3 / 2;
            distance = radiusY / 2;
            int index = 0, lp = 1;
            int xpos, ypos;
            for(int i = 0; i < depth; i++) {
                for(int j = 0; j < lp; j++) {
                    xpos = (int) (radiusX * j + ((float)W - (lp * radiusX)) / (lp + 1) * (j + 1));
                    xpos = xpos + 100;
                    ypos = radiusY * i + distance * (i + 1);
                    xs[index + 1] = xpos + X_OFF;
                    ys[index + 1] = ypos + Y_OFF;
                    index++;
                }
                lp = lp * 2;
            }
            g.setStroke(STOKE_LINE);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(FONT_NODE);
            drawNodes(g, (ClickableTreeNode) tree.root, 1);
//            g.drawRect(0 + X_OFF, 0 + Y_OFF, W, H - Y_OFF);
            drawTraverseResult(g, X_OFF, H + 20);
        }
    }

    /* index 从 1 开始好算一点 */
    private void drawNodes(Graphics2D g, ClickableTreeNode node, int index) {
        int xpos = xs[index];
        int ypos = ys[index];
        if(node != null) {
            g.setColor(Color.ORANGE);
            if(node.lChild != null) {
                g.drawLine(xs[index], ys[index], xs[index * 2], ys[index * 2]);
            }
            if(node.rChild != null) {
                g.drawLine(xs[index], ys[index], xs[index * 2 + 1], ys[index * 2 + 1]);
            }
            node.clickBound = new Rectangle(xpos - radiusX / 2, ypos - radiusY / 2, radiusX, radiusY);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(xpos - radiusX / 2, ypos - radiusY / 2, radiusX, radiusY, 5, 5);
            g.setColor(Color.DARK_GRAY);
            g.drawRoundRect(xpos - radiusX / 2, ypos - radiusY / 2, radiusX, radiusY, 5, 5);
            g.setColor(Color.BLACK);
            String data = node.data.toString();
            Rectangle2D rect = g.getFontMetrics().getStringBounds(data, g);
            g.drawString(data, xpos - ((int) rect.getWidth()) / 2, ypos - ((int) rect.getHeight()) / 2);
            if(node.lChild != null) {
                drawNodes(g, (ClickableTreeNode) node.lChild, index * 2);
            }
            if(node.rChild != null) {
                drawNodes(g, (ClickableTreeNode) node.rChild, index * 2 + 1);
            }
        }
    }

    public TreeNode getHighlightingNode(int x, int y) {
        if(highlightingNode != null && ((ClickableTreeNode) highlightingNode).clickBound.contains(x, y)) {
            return highlightingNode;
        } else {
            return null;
        }
    }

    private TreeNode highlightingNode = null;

    public void highlightNode(int x, int y) {
        ClickableTreeNode node = ((ClickableTreeNode) getEnteredNode(x, y));
        updateTree();
        if(node != null) {
            Graphics2D g = (Graphics2D) this.getGraphics();
            g.setStroke(STOKE_LINE);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(FONT_NODE);
            g.setColor(Color.WHITE);
            g.fillRoundRect(node.clickBound.x, node.clickBound.y, radiusX, radiusY, 5, 5);
            g.setColor(Color.green.darker());
            g.drawRoundRect(node.clickBound.x, node.clickBound.y, radiusX, radiusY, 5, 5);
            g.setColor(Color.BLACK);
            g.setColor(Color.BLACK);
            String data = node.data.toString();
            Rectangle2D rect = g.getFontMetrics().getStringBounds(data, g);
            g.drawString(data,
                    node.clickBound.x + (radiusX - (int) rect.getWidth()) / 2,
                    node.clickBound.y + (radiusY - (int) rect.getHeight()) / 2);
        }
        highlightingNode = node;
    }

    public TreeNode getEnteredNode(int x, int y) {
        return getEnteredNode(tree.root, x, y);
    }

    private TreeNode getEnteredNode(TreeNode node, int x, int y) {
        if(node == null) {
            return null;
        } else {
            ClickableTreeNode cNode = ((ClickableTreeNode) node);
            if (!cNode.clickBound.contains(x, y)) {
                node = getEnteredNode(cNode.lChild, x, y);
                if (node == null) {
                    node = getEnteredNode(cNode.rChild, x, y);
                }
            }
            return node;
        }
    }
}
