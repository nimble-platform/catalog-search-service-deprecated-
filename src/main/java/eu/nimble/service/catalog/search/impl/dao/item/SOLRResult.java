package eu.nimble.service.catalog.search.impl.dao.item;

import java.util.ArrayList;
import java.util.List;

public class SOLRResult {
private int totalElements;
private int totalPages;
private int pageSize;
private int currentPage;
private List<ItemType> result = new ArrayList<>();
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
public List<ItemType> getResult() {
	return result;
}
public void setResult(List<ItemType> result) {
	this.result = result;
}




}
