package com.deadpeace.selfie.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Виталий on 06.10.2015.
 */

@Entity
public class UserRole implements GrantedAuthority
{
    @Id
    @GeneratedValue
    private long id;

    private String authority;

    public UserRole()
    {

    }
    public UserRole(String authority)
    {
        this.authority=authority;
    }

    @Override
    public String getAuthority()
    {
        return authority;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof String)
            return obj.equals(this.authority);
        if(obj instanceof GrantedAuthority)
        {
            GrantedAuthority attr=(GrantedAuthority)obj;
            return this.authority.equals(attr.getAuthority());
        }
        return false;
    }

    public int hashCode()
    {
        return this.authority.hashCode();
    }

    public String toString()
    {
        return this.authority;
    }
}
