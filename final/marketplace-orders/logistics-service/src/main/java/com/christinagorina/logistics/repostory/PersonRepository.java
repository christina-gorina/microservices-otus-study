package com.christinagorina.logistics.repostory;

import org.springframework.data.repository.CrudRepository;
import com.christinagorina.logistics.model.Person;

import java.util.List;


public interface PersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();
}
