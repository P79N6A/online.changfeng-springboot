package com.xuecheng.framework.domain.user.pojo;

import java.util.Date;

/**
 * Created by sang on 2018/7/8.
 */
public class Book {
    protected String name;

    private String author;
//    @JsonIgnore
    protected Float price;
//    @JsonFo r mat (pattern =”yyyy- MM- dd” )

//    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publicationDate;
    //省略getter/setter

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
