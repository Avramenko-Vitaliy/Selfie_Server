package com.deadpeace.selfie.model;

import com.deadpeace.selfie.SelfieUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User implements UserDetails
{
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "authority")
    private Collection<UserRole> authorities;

    private boolean locked=false;
    private boolean enabled=true;
    private boolean credentials_expired=false;
    private boolean account_expired=false;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id=id;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public void setPassword(String password)
    {
        this.password=password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(@Email(regexp=SelfieUtil.EMAIL_PATTERN,message = "Invalid email!") String email)
    {
        this.email=email;
    }

    public void setAuthorities(Collection<UserRole> authorities)
    {
        this.authorities=authorities;
    }

    public void setLocked(boolean locked)
    {
        this.locked=locked;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled=enabled;
    }

    public void setCredentials_expired(boolean credentials_expired)
    {
        this.credentials_expired=credentials_expired;
    }

    public void setAccount_expired(boolean account_expired)
    {
        this.account_expired=account_expired;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return !account_expired;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return !credentials_expired;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof User)
        {
            User other=(User)obj;
            return Objects.equal(id,other.id);
        }
        else
            return false;
    }
}
