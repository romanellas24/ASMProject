package joliebank.romanellas.layer.model;

public class Payment {
    private String token;
    //In un'ambiente di produzione non dovrebbe essere salvato in chiaro
    private String pan;
    //In un'ambiente di produzione non dovrebbe esistere - Non salverei il CVV
    private Integer cvv;
    private String firstName;
    private String lastName;
    private Integer expireMonth;
    private Integer expireYear;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    public Integer getCvv() { return cvv; }
    public void setCvv(Integer cvv) { this.cvv = cvv; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getExpireMonth() { return expireMonth; }
    public void setExpireMonth(Integer expireMonth) { this.expireMonth = expireMonth; }

    public Integer getExpireYear() { return expireYear; }
    public void setExpireYear(Integer expireYear) { this.expireYear = expireYear; }
}

