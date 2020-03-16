package de.biba.triple.store.access.dmo;

public class Value {
 String value ;
 String uUID;
 
 public Value(){
	 
 }
 
 public Value(String value){
	 this.value = value;
 }
 
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getuUID() {
	return uUID;
}
public void setuUID(String uUID) {
	this.uUID = uUID;
}
@Override
public String toString() {
	return value;
}
 
}
