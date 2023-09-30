package tech.jimothy.design;

import java.util.ArrayList;
import java.util.Collection;

import tech.jimothy.gui.custom.EffectWidget;

/**
 * Class represents an in-game effect. It stores all of the pertinent data of an effect. 
 */

public class Effect implements Observer, Observable{
    /**Description for effect*/
    private String desc;
    /**Name of effect */
    private String name;
    /**The unique ID of the effect */
    private int id;
    /**The amount of time in seconds that the effect lasts */
    int duration;
    /**The amount of time that has passed for this particular effect */
    int timePassed;

    /**The combat time object that this effect object is observing */
    Observable observableTime;

    /**The observers of this effect. Typically, it will only have two: 1 CharacterWidget, 1 EffectWidget */
    ArrayList<Observer> observers = new ArrayList<>();


    public Effect(){
        this.desc = null;
        this.name = null;
        this.id = 0;
        this.duration = 0;
        this.timePassed = 0;
    }

    public Effect(String desc, String name, int id, int duration){
        this.desc = desc;
        this.name = name;
        this.id = id;
        this.duration = duration;
        this.timePassed = 0;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getID(){
        return this.id;
    }

    public int getDuration(){
        return this.duration;
    }

    public int getTimePassed(){
        return this.timePassed;
    }

    public boolean isFinished(){
        return (this.timePassed >= this.duration);
    }
    
    @Override
    public String toString(){
        return this.name;
    }

//*-------------------Observer Methods---------------------------------------------


    /**Updates this effect's timePassed and checks to see if the effect has expired*/
    @Override
    public void update(){
        this.timePassed += 6;
        // System.out.println("\nEffect Name: \t" + this.getName() + 
        // "\nHash Code \t" + this.hashCode() +
        // "\nHow much time I have: \t" + this.getDuration() +
        // "\nHow much time has transpired: \t" + this.getTimePassed());

        //sift through the observers -- if the observer is the effectwidget, update it's displayed time left
        for (Observer o : this.observers){
            if(o instanceof EffectWidget){
                ((EffectWidget)o).updateDisplayedTimeLeft();
            }
        }

        //if this effect has expired, let the observers know
        if(this.isFinished()){
            notifyObservers();

            //remove this from the list of observers found within observableTime as it no longer needs to be notified
            this.observableTime.removeObserver(this);
            this.observableTime = null;
        }
    }

    @Override
    public void registerObservable(Observable o) {
        this.observableTime = o;
    }

//*--------------------------------------------------------------------------------

//*-------------------Observable Methods---------------------------------------------

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers){
            //Set this effect as being ready for removal within the observer
            o.registerObservable(this);
            o.update();
        }
    }

    @Override
    public void removeObserver(Observer o) {
        ArrayList<Observer> observersClone = new ArrayList<>();
        for (Observer observer : observers){
            if(!observer.equals(o)){
                observersClone.add(observer);
            }
        }
        this.observers = observersClone;
    }

    @Override
    public void removeObservers(Collection<?> collection) {
        ArrayList<Observer> observersClone = new ArrayList<>();
        for (Observer observer : this.observers){
            observersClone.add(observer);
        }
        observersClone.removeAll(observers);
        this.observers = observersClone;
    }

//*--------------------------------------------------------------------------------

    public Observable getObservableTime(){
        return this.observableTime;
    }
}
