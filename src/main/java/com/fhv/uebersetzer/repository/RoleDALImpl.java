package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.Role;
import com.fhv.uebersetzer.model.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDALImpl implements RoleDAL {

    private final MongoTemplate mongoTemplate;

   @Autowired
   RoleDALImpl(MongoTemplate mongoTemplate){
       this.mongoTemplate = mongoTemplate;
   }

    @Override
    public Optional<Role> findByName(RoleName roleName) {
       Query query = new Query();
       query.addCriteria(Criteria.where("name").is(roleName));

        return Optional.ofNullable(mongoTemplate.findOne(query, Role.class));
    }
}
