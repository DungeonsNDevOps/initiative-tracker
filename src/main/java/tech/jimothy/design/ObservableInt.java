package tech.jimothy.design;

import java.util.ArrayList;
import java.util.Collection;
/**
 * Observable wrapper for an integer
 */
public class ObservableInt implements Observable{

    ArrayList<Observer> observers = new ArrayList<>();
    

    private int integer;
    
    public ObservableInt(int integer){
        this.integer = integer;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);

        //register the instance of this Observable within the observer as well
        o.registerObservable(this);
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
    public void removeObservers(Collection<?> observers) {
        ArrayList<Observer> observersClone = new ArrayList<>();
        for (Observer observer : this.observers){
            observersClone.add(observer);
        }
        observersClone.removeAll(observers);
        this.observers = observersClone;
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers){
            o.update();
        }
    }

    public void setInt(int integer){
        this.integer = integer;

        //notify all of the observers of the change
        notifyObservers();
    }

    public int getInt(){
        return this.integer;
    }




}
