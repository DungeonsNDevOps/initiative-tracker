package tech.jimothy.design;

import java.util.Collection;

public interface Observable {
    void registerObserver(Observer o);
    void notifyObservers();
    void removeObserver(Observer o);
    void removeObservers(Collection<?> collection);
}
