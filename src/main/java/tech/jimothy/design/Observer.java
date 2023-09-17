package tech.jimothy.design;


public interface Observer {
    void update();
    void registerObservable(Observable o);
}
