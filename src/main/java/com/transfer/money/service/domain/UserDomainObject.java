package com.transfer.money.service.domain;

import com.transfer.money.service.ServiceResponse;

import java.sql.Date;

public interface UserDomainObject extends ServiceResponse {

    public long getId();

    public String getUserName();

    public String getFirstName();

    public String getLastName();

    public String getEmailAddress();

    public String getAddress();

    public Date getDob();

    public void setId(long id);

    public void setUserName(String userName);

    public void setFirstName(String firstName);

    public void setLastName(String lastName);

    public void setEmailAddress(String emailAddress);

    public void setAddress(String address);

    public void setDob(Date dob);
}
