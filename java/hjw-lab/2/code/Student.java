public class Student {
    private int sNo;
    private String sName;
    private int sSex;
    private int sAge;
    private double sJava;

    public Student() {
    }

    public Student(int sNo, String sName, int sSex, int sAge, double sJava) {
        this.sNo = sNo;
        this.sName = sName;
        this.sSex = sSex;
        this.sAge = sAge;
        this.sJava = sJava;
    }

    public int getsNo() {
        return sNo;
    }

    public String getsName() {
        return sName;
    }

    public int getsSex() {
        return sSex;
    }

    public int getsAge() {
        return sAge;
    }

    public double getsJava() {
        return sJava;
    }

    public void setsAge(int sAge) {
        this.sAge = sAge;
    }

    public void setsJava(double sJava) {
        this.sJava = sJava;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public void setsSex(int sSex) {
        this.sSex = sSex;
    }

    @Override
    public String toString() {
        return "{" + sNo + ", " + sName + ", " + sAge + ", " + ((sSex == 0) ? "男" : "女") + ", " + sJava + "}";
    }
}

