package com.itsa.moviereviewsite;

import com.itsa.moviereviewsite.spring.utils.SpringFXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.itsa.moviereviewsite")
@EntityScan("com.itsa.moviereviewsite")
public class MovieReviewSiteApplication extends Application {

    private static Scene scene;
    private static SpringFXMLLoader springFXMLLoader;
    private ConfigurableApplicationContext applicationContext;

    /**
     * Starts the application
     *
     * @throws IOException
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads the FXML file
     *
     * @param fxml
     * @return
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        return springFXMLLoader.load(MovieReviewSiteApplication.class.getResource("/templates/" + fxml + ".fxml"));
    }

    /**
     * Main method to launch the Spring Application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the application
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(MovieReviewSiteApplication.class);
        springFXMLLoader = applicationContext.getBean(SpringFXMLLoader.class);
    }

    /**
     * Stops the application
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }

    /**
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = springFXMLLoader.load(getClass().getResource("/templates/splash.fxml"));
        scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/templates/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
