

public class test {

    public static void main(String[] args) {

        test test = new test();
        System.out.println(test.recursiveMethod(10));
    }

    private int recursiveMethod(int num){
        if(num == 0){
            return num;
        }
         return recursiveMethod(num - 1) + num;
    }

}
