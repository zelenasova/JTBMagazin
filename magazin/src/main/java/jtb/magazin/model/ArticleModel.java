package jtb.magazin.model;

public class ArticleModel {
private String id;
private String title;
private String content;
private String perex;
private String datePublish;
private String url;
private String imageUrl;
private String categoryID;
private String locked;
private String source;
private String file;
private String keywords;
private String zipUrl;
private String magazinID;
private String magnusArticleId;
private String lastChange;
private int pos;

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {

        return price;
    }

    private String price;

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getDatePublish() {
	return datePublish;
}
public void setDatePublish(String newstring) {
	this.datePublish = newstring;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getPerex() {
	return perex;
}
public void setPerex(String perex) {
	this.perex = perex;
}
public String getCategoryID() {
	return categoryID;
}
public void setCategoryID(String categoryID) {
	this.categoryID = categoryID;
}

public String getLocked() {
	return locked;
}
public void setLocked(String locked) {
	this.locked = locked;
}
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getFile() {
	return file;
}
public void setFile(String file) {
	this.file = file;
}
public String getKeywords() {
	return keywords;
}
public void setKeywords(String keywords) {
	this.keywords = keywords;
}
public String getZipUrl() {
	return zipUrl;
}
public void setZipUrl(String zipUrl) {
	this.zipUrl = zipUrl;
}
public String getMagazinID() {
	return magazinID;
}
public void setMagazinID(String magazinID) {
	this.magazinID = magazinID;
}
public String getMagnusArticleId() {
	return magnusArticleId;
}
public void setMagnusArticleId(String magnusArticleId) {
	this.magnusArticleId = magnusArticleId;
}
public String getLastChange() {
	return lastChange;
}
public void setLastChange(String lastChange) {
	this.lastChange = lastChange;
}
public int getPos() {
	return pos;
}
public void setPos(int pos) {
	this.pos = pos;
}



}
