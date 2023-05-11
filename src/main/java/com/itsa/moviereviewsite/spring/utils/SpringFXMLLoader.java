package com.itsa.moviereviewsite.spring.utils;

import com.itsa.moviereviewsite.fxml.controllers.HomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class SpringFXMLLoader {
    private final ApplicationContext applicationContext;

    /**
     * Constructor
     *
     * @param applicationContext
     */
    public SpringFXMLLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * loads FXML through the Spring Environment to
     * enable using Repositories in FXMLControllers
     *
     * @param url
     * @return
     * @throws IOException
     */
    public Parent load(URL url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Object controller = fxmlLoader.getController();
        if (controller instanceof HomeController) {
            ((HomeController) controller).shutdownExecutorService();
        }
        return fxmlLoader.load();
    }
}
