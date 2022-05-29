module com.insuranceagency {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jetbrains.annotations;


    opens com.insuranceagency to javafx.fxml;
    exports com.insuranceagency;
    exports com.insuranceagency.controllerPolicyholder;
    opens com.insuranceagency.controllerPolicyholder to javafx.fxml;
    exports com.insuranceagency.controllerEmployee;
    opens com.insuranceagency.controllerEmployee to javafx.fxml;
    exports com.insuranceagency.controllerPersonAllowedToDrive;
    opens com.insuranceagency.controllerPersonAllowedToDrive to javafx.fxml;
    exports com.insuranceagency.controllerReport;
    opens com.insuranceagency.controllerReport to javafx.fxml;
    exports com.insuranceagency.controllerCar;
    opens com.insuranceagency.controllerCar to javafx.fxml;
}