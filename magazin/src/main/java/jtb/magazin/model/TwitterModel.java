package jtb.magazin.model;

public class TwitterModel {
private String id;
private String title;
private String autor;
private String datePublish;
private String url;
private String imageUrl;
private String retweetedBy;

public String getId() {
	return id;
}
public void setId(String twitterID) {
	this.id = twitterID;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDatePublish() {
	return datePublish;
}
public void setDatePublish(String datePublish) {
	this.datePublish = datePublish;
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
public String getAutor() {
	return autor;
}
public void setAutor(String autor) {
	this.autor = autor;
}
public String getRetweetedBy() {
	return retweetedBy;
}
public void setRetweetedBy(String retweetedBy) {
	this.retweetedBy = retweetedBy;
}



}
