import java.io.FileWriter;
import java.io.IOException;

public class FileHandling {

    public String displayNoticeDetails(AutoInfillKeys autoInfillKeys) {
        // Returns the paragraph to be displayed on the preview
        return autoInfillKeys.todayDate + "\n\nDear " + autoInfillKeys.recipName + ",\n\n" +
                "This notice was lodged to the address " + autoInfillKeys.recipAddress +
                " on the " + autoInfillKeys.setDate + " and expires on the " + autoInfillKeys.expDate + ".\n\n" +
                "Kind regards, \n\n" + autoInfillKeys.companyAddress + "\n" + autoInfillKeys.contactNumber;
    }

    public void createNewNotice(AutoInfillKeys autoInfillKeys) {
        // Creates the file after notice is generated
        try {
            FileWriter fileWriter = new FileWriter(autoInfillKeys.recipName +".txt");
            fileWriter.write(displayNoticeDetails(autoInfillKeys));
            fileWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        }
    }