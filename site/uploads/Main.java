import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class Main {
    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        String str = args[0];
        try {
            Class c = Class.forName(str);
            Result result = junit.run(c);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
}
