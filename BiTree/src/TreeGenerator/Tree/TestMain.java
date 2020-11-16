package TreeGenerator.Tree;

public class TestMain {

    public static final String PATH = "src/TestInput";

    private static Tree tree;

    public static void main(String[] args) {
        Tree tree = Tree.createTree(Tree.CT_PRE_WITH_EMPTY, TxtParser.parseElem(PATH));
        tree.display();
        System.out.println(tree.getDepth());
    }

}
