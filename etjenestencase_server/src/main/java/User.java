public class User {
    private int seq;
    private String fName;
    private String lName;
    private int age;
    private String street;
    private String city;
    private String state;
    private Float lat;
    private Float lng;
    private Float ccNumber;

    public User(int seq, String fName, String lName, int age, String street, String city, String state, Float lat, Float lng, Float ccNumber) {
        this.seq = seq;
        this.fName = fName;
        this.lName = lName;
        this.age = age;
        this.street = street;
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.ccNumber = ccNumber;
    }

    public int getSeq() {
        return seq;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public int getAge() {
        return age;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Float getLat() {
        return lat;
    }

    public Float getLng() {
        return lng;
    }

    public Float getCcNumber() {
        return ccNumber;
    }

}
