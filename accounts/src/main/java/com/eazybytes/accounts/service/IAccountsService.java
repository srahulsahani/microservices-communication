package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDto;

public interface IAccountsService {


    /**
     * @param customerDto - CustomerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     * @param mobileNumber
     * @return Customer account details
     */
    CustomerDto fetchAccount(String mobileNumber);


    /**
     * @param customerDto
     * @return boolean indicating if the update of the account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);


    /**
     * @param mobileNumber
     * @return booleab indicating if the account is deleted or not
     */
    boolean deleteAccount(String mobileNumber);


}
