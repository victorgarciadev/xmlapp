package presentacio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Classe que defineix l'objecte Aplicació. Permet configurar la finestra de
 * l'aplicació, carregant la interfície principal i també com mostrar-la.
 *
 * @author Txell Llanas - Implementació
 */
public class App extends Application {

    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {
        
        try {

            // Carregar vista principal
            scene = new Scene(loadFXML("importar"));

            // Fulla d'estils CSS
            scene.getStylesheets().add("/css/styles.css");

            // Propietats de la finestra
            stage.setScene(scene);
            stage.setTitle("Dades demogràfiques de catalans a l'estranger");
            stage.setWidth(820);
            stage.setHeight(620);
            stage.setMinWidth(620);
            stage.setMinHeight(420);
            stage.show();
            
        } catch (IOException ex) {
            System.out.println("No s'ha pogut carregat la interfície d'usuari.");
            System.out.println(ex.toString());
        }
    }

    /**
     * Mètode per carregar una vista *.fxml dins un Stage definit com a
     * principal (Root).
     *
     * @param fxml Layout *.fxml a visualitzar dins l'Stage
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Mètode per carregar una vista *.fxml dins un Stage.
     *
     * @param fxml Ruta al layout *.fxml     *
     * @return Parent que conté el layout *.fxml a visualitzar dins l'Stage
     * @throws IOException Excepció a mostrar en cas que no es trobi el Layout
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Executa l'aplicació (MAIN).
     *
     * @param args args
     */
    public static void main(String[] args) {
        launch();
    }
}