package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;


    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with this mobile number: " + customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));

    }


    /**
     * @param customer -Customer Object
     * @return the new account details
     * randomAccountNumber - generate 10 digit random account number
     */
    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());

        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anynomous");
        return newAccount;
    }


    /**
     * @param mobileNumber
     * @return Account details based in mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Customer","mobileNumber",mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        return customerDto;
    }

    /**
     * @param customerDto - Customer's data that need to be updated
     * @return boolean indicating if the update of the account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated= false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto!= null){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                            ()-> new ResourceNotFoundException("Account","customerId",accountsDto.getAccountNumber().toString())
            );

            /*Here customer account details will be mapped to account entity including accountNumber
            * Mapping accountNumber is not a problem since it is only mapped if it exists in database*/
            AccountsMapper.mapToAccounts(accountsDto,accounts);
            accounts=accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    ()-> new ResourceNotFoundException("Customer","customerId",customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated=true;
        }
        return isUpdated;
    }

    /**
     * @param mobileNumber
     * @return booleab indicating if the account is deleted or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Customer","mobileNumber",mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

}
