package tech.jimothy.design;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

 public class EntityProperty {
     private StringProperty propertyName;
     public void setPropertyName(String value) { propertyNameProperty().set(value); }
     public String getPropertyName() { return propertyNameProperty().get(); }
     public StringProperty propertyNameProperty() { 
         if (propertyName == null) propertyName = new SimpleStringProperty(this, "propertyName");
         return propertyName; 
     }
 
     private StringProperty value;
     public void setValue(String value) { valueProperty().set(value); }
     public String getValue() { return valueProperty().get(); }
     public StringProperty valueProperty() { 
         if (value == null) value = new SimpleStringProperty(this, "value");
         return value; 
     } 
 }