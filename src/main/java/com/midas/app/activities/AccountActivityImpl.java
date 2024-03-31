package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountActivityImpl implements AccountActivity {

  private final AccountRepository accountRepository;

  private final PaymentProvider provider;

  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    return provider.createAccount(
        CreateAccount.builder()
            .email(account.getEmail())
            .lastName(account.getLastName())
            .firstName(account.getFirstName())
            .build());
  }
}
