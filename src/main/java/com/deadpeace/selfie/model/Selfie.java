package com.deadpeace.selfie.model;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
public class Selfie
{
    @Id
    @GeneratedValue
    private long id;

//    @DateTimeFormat(pattern="dd-MM-yyyy'T'HH:mm:ss'Z'")
//    @JsonFormat(pattern="dd-MM-yyyy'T'HH:mm:ss'Z'")
    private long date;

    @ManyToOne
    @JoinColumn(name = "id_user",nullable = false)
    private User creator;

    private String description;
    private String title;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date=date;
    }

    public User getCreator()
    {
        return creator;
    }

    public void setCreator(User creator)
    {
        this.creator=creator;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description=description;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title=title;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Selfie)
        {
            Selfie other=(Selfie)obj;
            return Objects.equal(id,other.id);
        }
        else
            return false;
    }
}
