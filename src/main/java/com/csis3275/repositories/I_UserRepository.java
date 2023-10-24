package com.csis3275.repositories;

import com.csis3275.models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface I_UserRepository extends MongoRepository<UserModel, String>{
}
