package com.demo.udema.repositoryDAO;

import com.demo.udema.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository <VerificationToken, String> {

    VerificationToken findVerificationTokenBy(String verificationToken);
}
