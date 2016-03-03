package logic.database;

import java.util.Objects;

public class Book {

    private int id = 0;
    private String name = "";
    private String author = "";
    private String publishingHouse = "";
    private String description = "";
    private String picturePath = "";
    private String bookPath = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    @Override
    public String toString() {
        return getName() + "\n"
                + getAuthor() + "\n"
                + getPublishingHouse() + "\n"
                + getDescription() + "\n"
                + getPicturePath() + "\n"
                + getBookPath();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.author);
        hash = 37 * hash + Objects.hashCode(this.publishingHouse);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + Objects.hashCode(this.picturePath);
        hash = 37 * hash + Objects.hashCode(this.bookPath);
        return hash;
    }
}
