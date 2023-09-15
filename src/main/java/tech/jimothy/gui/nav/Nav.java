package tech.jimothy.gui.nav;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import tech.jimothy.db.DataShare;
import tech.jimothy.errors.StageNotSetForNav;

/**
 * Nav stores an instance of itself within a static variable(Singleton class). Within that instance are 
 * all of the methods responsible for loading all pages of the application. 
 */
public class Nav {
    /**Last visited page */
    NavMethodObj lastPage;
    /**The stage of the application*/
    private static Stage stage; 
    /**The instance of itself*/
    final static private Nav INSTANCE = new Nav(); 

    public Nav(){};

    public void goToCampaignPage() throws IOException, StageNotSetForNav{
        Parent root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/campaign-page.fxml"));
        Scene scene = new Scene(root);
        if (stage != null){
            stage.setScene(scene);
            stage.show();
        } else{
            throw new StageNotSetForNav();
        }
    }

    public void goToCreateCampaign() throws IOException, StageNotSetForNav{
        Parent root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/create-campaign-page.fxml"));
        Scene scene = new Scene(root);
        if (stage != null){
            stage.setScene(scene);
            stage.show();
        } else{
            throw new StageNotSetForNav();
        }
    }

    public void goToCreateCharacter() throws IOException, StageNotSetForNav{
        Parent root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/create-character-page.fxml"));
        Scene scene = new Scene(root);
        if (stage != null){
            stage.setScene(scene);
            stage.show();
        } else{
            throw new StageNotSetForNav();
        }
    }

    public void goToSelectedCampaignPage(int campaignID) throws IOException, StageNotSetForNav{
        DataShare dataShare = DataShare.getInstance();
        dataShare.setInt(campaignID);
        Parent root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/selected-campaign-page.fxml"));
        Scene scene = new Scene(root);
        if (stage != null){
            stage.setScene(scene);
            stage.show();
        } else{
            throw new StageNotSetForNav();
        }
    }

    public void goToCreateEffect() throws IOException, StageNotSetForNav{
        Parent root = FXMLLoader.load(getClass().getResource("/tech/jimothy/fxml/create-effect-page.fxml"));
        Scene scene = new Scene(root);
        if (stage != null){
            stage.setScene(scene);
            stage.show();
        } else{
            throw new StageNotSetForNav();
        }
    }    

    public void setLastPage(NavMethodObj method){
        this.lastPage = method;
    }

    public void goToLastPage(){
        this.lastPage.method();
    }
    
    public static void setStage(Stage stage){
        Nav.stage = stage;
    }

    public static Nav getInstance(){
        return INSTANCE;
    }
}
