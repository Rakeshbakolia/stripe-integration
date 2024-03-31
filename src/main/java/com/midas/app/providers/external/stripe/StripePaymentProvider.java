package com.midas.app.providers.external.stripe;

import com.midas.app.models.Account;
import com.midas.app.providers.enums.Provider;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.AccountCreateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) {
    Stripe.apiKey = configuration.getApiKey();
    AccountCreateParams params =
        AccountCreateParams.builder()
            .setType(AccountCreateParams.Type.STANDARD)
            .setCountry("US")
            .setEmail(details.getEmail())
            .build();
    try {
      com.stripe.model.Account account = com.stripe.model.Account.create(params);
      return Account.builder()
          .providerType(Provider.STRIPE)
          .providerId(account.getId())
          .email(details.getEmail())
          .firstName(details.getFirstName())
          .lastName(details.getLastName())
          .build();
    } catch (StripeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
