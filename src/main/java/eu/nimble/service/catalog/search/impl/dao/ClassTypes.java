package eu.nimble.service.catalog.search.impl.dao;

import java.util.List;

public class ClassTypes {

private int totalElements;
private int totalPages;
private int pageSize;
private int currentPage;
private  List<ClassType> result;
private String facets;
public int getTotalElements() {
	return totalElements;
}
public void setTotalElements(int totalElements) {
	this.totalElements = totalElements;
}
public int getTotalPages() {
	return totalPages;
}
public void setTotalPages(int totalPages) {
	this.totalPages = totalPages;
}
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}
public int getCurrentPage() {
	return currentPage;
}
public void setCurrentPage(int currentPage) {
	this.currentPage = currentPage;
}
public List<ClassType> getResult() {
	return result;
}
public void setResult(List<ClassType> result) {
	this.result = result;
}
public String getFacets() {
	return facets;
}
public void setFacets(String facets) {
	this.facets = facets;
}
@Override
public String toString() {
	return "ClassTypes [totalElements=" + totalElements + ", totalPages=" + totalPages + ", pageSize=" + pageSize
			+ ", currentPage=" + currentPage + ", result=" + result + ", facets=" + facets + "]";
}


}
