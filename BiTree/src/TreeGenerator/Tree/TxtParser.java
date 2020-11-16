package TreeGenerator.Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TxtParser {

    public static final String PATH = "src/TestInput";

    // 将文本输入转换为 Elem 数组
    public static List<Elem> parseElem(String path) {
        List<Elem> list = new ArrayList<>();
        int key;
        String others;
        Elem elem;
        try (Scanner scanner = new Scanner(new File(path))) {
            while(scanner.hasNext()) {
                key = scanner.nextInt();
                if(scanner.hasNext()) {
                    others = scanner.next();
                    elem = new Elem(key, others);
                    list.add(elem);
                }
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // unit test
    public static void main(String[] args) {
        List<Elem> list = parseElem(PATH);
        if(list != null) {
            list.forEach(System.out::println);
        }
    }
}
