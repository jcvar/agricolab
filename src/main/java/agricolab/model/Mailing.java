package agricolab.model;

public class Mailing {
    private String city;
    private String department;
    private String address;
    private String details;
    private String neighbourhood;

    public Mailing() {
    }

    public Mailing(String city, String department, String address, String details, String neighbourhood) {
        this.city = city;
        this.department = department;
        this.address = address;
        this.details = details;
        this.neighbourhood = neighbourhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        neighbourhood = neighbourhood;
    }
}
