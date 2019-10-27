package com.ao.importCsvToH2.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long>, PagingAndSortingRepository<Person, Long> {

    @Query("SELECT p from Person p ORDER BY p.age DESC")
    List<Person> findAllByAgeDesc();

    @Query(value = "SELECT * from person WHERE phone_no IS NOT NULL ORDER BY age DESC LIMIT 1", nativeQuery = true)
    Person findOldestByPhoneNoNotNull();

    Set<Person> findByLastName(String lastName);

    @Query("SELECT p from Person p ORDER BY p.age DESC")
    Page<Person> findAllByAgeDescPaged(Pageable paging);


}
