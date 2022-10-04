package com.demo.udema.repositoryDAO;

import com.demo.udema.entity.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository <ConfirmationToken, String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
