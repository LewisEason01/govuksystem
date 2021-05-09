public class AutoInfillKeys extends ManualInfillKeys {
    public String contactNumber;
    public String companyAddress;
    public String todayDate;

    public AutoInfillKeys(String contactNumber, String companyAddress, String todayDate, String recipName,
                          String recipAddress, String expDate, String setDate) {
        this.contactNumber = contactNumber;
        this.companyAddress = companyAddress;
        this.todayDate = todayDate;
        this.recipName = recipName;
        this.recipAddress = recipAddress;
        this.expDate = expDate;
        this.setDate = setDate;
    }
}
