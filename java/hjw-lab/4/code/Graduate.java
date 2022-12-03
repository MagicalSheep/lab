public class Graduate implements StudentInterface, TeacherInterface {

    private String name;
    private String sex;
    private int age;
    private double fee;
    private double pay;

    public Graduate() {
    }

    public Graduate(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public double getFee() {
        return fee;
    }

    @Override
    public void setPay(double pay) {
        this.pay = pay;
    }

    @Override
    public double getPay() {
        return pay;
    }
}
