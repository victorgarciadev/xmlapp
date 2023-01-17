module com.mycompany.m06uf1practd {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.m06uf1practd to javafx.fxml;
    exports com.mycompany.m06uf1practd;
}
