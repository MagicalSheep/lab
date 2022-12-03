public class Person {

    private String name;
    private int age;
    private String sex;

    public String getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) throws IllegalAgeException {
        if (age < 1 || age > 150)
            throw new IllegalAgeException(age);
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{姓名：" + name + ", 性别：" + sex + ", 年龄：" + age + "}";
    }
}
