package eu.nimble.service.catalog.search.impl.dao;

import java.util.Collection;
import java.util.HashSet;



public class ClassType extends Named implements IClassType {
	
	private String type = TYPE_VALUE;
	
	private Collection<String> properties;

	private Collection<String> parents;
	private Collection<String> children;
	
	private Collection<String> allParents;
	private Collection<String> allChildren;

	private Integer level;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Collection<String> getProperties() {
		return properties;
	}
	public void addProperty(String property) {
		if (this.properties == null ) {
			this.properties = new HashSet<>();
		}
		this.properties.add(property);
	}
	public void setProperties(Collection<String> properties) {
		this.properties = properties;
	}
	public void addParent(String superClass) {
		if (this.parents == null ) {
			this.parents = new HashSet<>();
		}
		this.parents.add(superClass);
	}
	public Collection<String> getParents() {
		return parents;
	}
	public void setParents(Collection<String> parent) {
		this.parents = parent;
	}
	public void addChild(String childClass) {
		if (this.children == null ) {
			this.children = new HashSet<>();
		}
		this.children.add(childClass);
	}
	public Collection<String> getChildren() {
		return children;
	}
	public void setChildren(Collection<String> child) {
		this.children = child;
	}
	public void addAllParent(String superClass) {
		if (this.allParents == null ) {
			this.allParents = new HashSet<>();
		}
		this.allParents.add(superClass);
	}
	public Collection<String> getAllParents() {
		return allParents;
	}
	public void setAllParents(Collection<String> parent) {
		this.allParents = parent;
	}
	public void addAllChild(String childClass) {
		if (this.allChildren == null ) {
			this.allChildren = new HashSet<>();
		}
		this.allChildren.add(childClass);
	}
	public Collection<String> getAllChildren() {
		return allChildren;
	}
	public void setAllChildren(Collection<String> child) {
		this.allChildren = child;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

}