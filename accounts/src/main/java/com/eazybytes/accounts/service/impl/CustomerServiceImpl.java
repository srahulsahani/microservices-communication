package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.dto.*;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.ICustomerService;
import com.eazybytes.accounts.service.client.CardsFeignClient;
import com.eazybytes.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;
    /**
     * @param mobileNumber -Input Mobile Number
     * @return Customer Details based on given mobileNumber
     */
    @Override
    public CustomerDetailsDTO fetchCustomerDetails(String mobileNumber,String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Customer","mobileNumber",mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Account","customerId",customer.getCustomerId().toString()));
        CustomerDetailsDTO customerDetailsDTO = CustomerMapper.mapToCustomerDetailsDto(customer,new CustomerDetailsDTO());
        customerDetailsDTO.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId,mobileNumber);
        customerDetailsDTO.setLoansDto(loansDtoResponseEntity.getBody());
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId,mobileNumber);
        customerDetailsDTO.setCardsDto(cardsDtoResponseEntity.getBody());
        return customerDetailsDTO;
    }
}
