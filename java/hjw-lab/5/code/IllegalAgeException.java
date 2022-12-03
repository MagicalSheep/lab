public class IllegalAgeException extends Exception {

    public IllegalAgeException(int age) {
        super("年龄" + age + "不符合要求，年龄范围应为[1, 150]");
    }

}
