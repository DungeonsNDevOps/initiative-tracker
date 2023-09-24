package tech.jimothy.gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import tech.jimothy.db.DataShare;
import tech.jimothy.design.Effect;
import tech.jimothy.design.EffectItemType;
import tech.jimothy.design.Observable;
import tech.jimothy.design.ObservableInt;
import tech.jimothy.design.Observer;
import tech.jimothy.errors.StageNotSetForNav;
import tech.jimothy.errors.WidgetMissingChildException;
import tech.jimothy.gui.custom.EntityWidget;
import tech.jimothy.gui.custom.EffectWidget;
import tech.jimothy.gui.custom.OptionEntityWidget;
import tech.jimothy.gui.custom.SearchAndSelectWidget;
import tech.jimothy.gui.nav.Nav;
import tech.jimothy.utils.EntitySortTools;

import java.io.IOException;
import java.util.ArrayList;

public class CombatController{

    @FXML Parent root;
    @FXML VBox entitiesContainer;
    @FXML Label roundsLabel;
    @FXML Label timeLabel;
    @FXML private VBox effectsView;
    //main view of the page
    @FXML VBox mainView;
    //the amount of rounds that have taken place
    int rounds = 0;
    //time in seconds that has taken place in combat
    ObservableInt observableTime = new ObservableInt(0);
    //the amount of turns that have taken place
    int turns = 0;
    //entity with the current largest initiative
    EntityWidget topOfList;
    //entity that is being selected for the purpose of viewing effects/properties
    EntityWidget selectedEntity;
    //Indicates whether ot not the AddEffectsWidget currently exists
    boolean addEffectsWidgetExists = false;

        /**The navigation object used to naviagting around the app */
    Nav navigation = Nav.getInstance();
    

    @FXML
    protected void initialize(){

        //config VBox for entities
        entitiesContainer.setSpacing(5.0);

        //get the array of entity widgets passed stored in our datashare singleton class
        DataShare dataShare = DataShare.getInstance();
        ArrayList<Object> entityObjects = dataShare.getArray();

        ArrayList<EntityWidget> entities = new ArrayList<>();
        //convert our entityObjects back into EntityWidgets, storing them in entities ArrayList
        for(Object entityObj : entityObjects){
            if (entityObj instanceof EntityWidget){
                entities.add((EntityWidget)entityObj);
            }
        }
        //sort the entities in descending order based on their initiative
        ArrayList<EntityWidget> sortedEntities = EntitySortTools.charSortDesc(entities);

        //Add entities to their VBox container, configuring them
        for (EntityWidget entity : sortedEntities){
            //set on click to populate effects and properties view
            entity.setOnMouseClicked(event -> {selectEntity(entity);});

            //create new 'add effect' menu option for entity widget
            MenuItem addEffectsOption = new MenuItem("Add Effect");
            //set the action for the new menu option
            addEffectsOption.setOnAction(event -> {manifestAddEffectsWidget(entity);});
            ((OptionEntityWidget)entity).getOptionsMenu().getItems().add(addEffectsOption);

            entity.getChildren().add(new Label("Init: " + String.valueOf(entity.getInitiative())));
            entitiesContainer.getChildren().add(entity);
        }
        //Set the entity that is at the top of turn order
        this.topOfList = EntitySortTools.greatestInitiative(this.entitiesContainer.getChildren());

        //Set this first entity as the entity being selected on and then populate the views for that entity
        selectEntity(this.topOfList);
    }

    private void manifestAddEffectsWidget(EntityWidget assocEntity){

        if(!this.addEffectsWidgetExists){
            this.addEffectsWidgetExists = true;

            SearchAndSelectWidget addEffectsWidget = new SearchAndSelectWidget(new EffectItemType());
            //set addEffectsWidget height
            addEffectsWidget.setMinHeight(.5*this.mainView.getHeight());
            try{
                addEffectsWidget.setOnButtonPress(event -> {
                    for(Object effectObj : addEffectsWidget.getSelections()){
                         if(effectObj instanceof Effect){
                            Effect effect = (Effect)effectObj;

                            //register the effect object as an observer of the combat time
                            observableTime.registerObserver(effect);
                            
                            //add an effect widget to the effects view IF this char is currently the one selected on
                            //AND the associated entity widget does not currently have an instance of the effect already(compare effect id)
                            if(!assocEntity.hasEffect(effect.getID())){
                                if (assocEntity.equals(this.selectedEntity)){
                                    EffectWidget effectWidget = new EffectWidget(effect, assocEntity);


                                    this.effectsView.getChildren().add(effectWidget);
                                }
                                //add effect to the entity which is associated with this addEffectsWidget
                                assocEntity.addEffect(effect);
                            }
                         }
                    }
                    this.mainView.getChildren().remove(addEffectsWidget);
                    this.addEffectsWidgetExists = false;
                });
            } catch (WidgetMissingChildException e){
                e.printStackTrace();
            }
            this.mainView.getChildren().add(addEffectsWidget);
        }
    }

    public void cycle(ActionEvent event){
        ObservableList<Node> entities = entitiesContainer.getChildren();
        Node first = entities.get(0);

        //entity with greatest initiative will be the marker for each new round
        this.topOfList = EntitySortTools.greatestInitiative(entities);

        entities.remove(0);
        entities.add(first);

        
        selectEntity((EntityWidget)entities.get(0));

        if (entities.indexOf(topOfList) == 0){
            this.rounds += 1;

            String[] temp = roundsLabel.getText().split(" ");

            roundsLabel.setText(temp[0] + " " + String.valueOf(this.rounds));

            addTime();

        }

    }

    private void addTime(){
        this.observableTime.setInt(this.observableTime.getInt() + 6);
        
        String[] temp = this.timeLabel.getText().split(" ");

        if (this.observableTime.getInt() < 60){
            this.timeLabel.setText(temp[0] + " " + String.valueOf(this.observableTime.getInt()) + " s");
        } else{
            this.timeLabel.setText(temp[0] + " " + String.valueOf(this.observableTime.getInt()/60) + 
            " m " + String.valueOf(this.observableTime.getInt() % 60) + " s");
        }   
    }

    private void populateEffectsView(){

        //Ensure the removal of the EffectWidget from old population from the list of observers for each effect
        for(Node child : this.effectsView.getChildren()){
            Effect effect = ((EffectWidget)child).grabEffect();
            effect.removeObserver((Observer)child);
        }
        //remove previous population
        this.effectsView.getChildren().removeAll(this.effectsView.getChildren());

        ArrayList<Effect> charEffects = this.selectedEntity.getEffects();

        for (Effect effect : charEffects){
            EffectWidget effectWidget = new EffectWidget(effect, this.selectedEntity);

            this.effectsView.getChildren().add(effectWidget);

        }
    }

    private void populatePropertiesView(){
        ;
    }


    public void endCombat() throws IOException, StageNotSetForNav{
        navigation.goToSelectedCampaignPage(DataShare.getInstance().getInt());
    }

    /**
     * Selects the specified entity widget. Upon selection, the entity widget's border is highlighted
     * and the effects and property views are populated with information about that entity widget
     * @param entityWidget the entity widget to be selected
     */
    private void selectEntity(EntityWidget entityWidget){
        //if a entity has been selected in the past, reset its style.
        if(this.selectedEntity != null){
            //? Find a way to not append the CSS?
            this.selectedEntity.setStyle(entityWidget.getStyle() + "; -fx-border-width: 1px; -fx-border-color: Black;");
        }
        //highlight the border of the entity and set it as the selected entity
        entityWidget.setStyle(entityWidget.getStyle() + "; -fx-border-width: 3px; -fx-border-color: Blue;");
        this.selectedEntity = entityWidget;

        //populate the views
        populateEffectsView();
        populatePropertiesView();
    }

}
 