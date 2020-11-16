package TreeGenerator.Tree;

public class Elem {
    public int key;
    public String others;

    public Elem(int key, String others) {
        this.key = key;
        this.others = others.intern();
    }

    public Elem(Elem e) {
        this.key = e.key;
        this.others = e.others.intern();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Elem) {
            Elem e = ((Elem) obj);
            return this.key == e.key;
        }
        return false;
    }

    @Override
    public String toString() {
        return key + ", " + others;
    }
}
