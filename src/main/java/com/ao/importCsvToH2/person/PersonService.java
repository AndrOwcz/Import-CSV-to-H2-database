package com.ao.importCsvToH2.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final ImportCsv importCsv;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public PersonService(PersonRepository personRepository, ImportCsv importCsv) {
        this.personRepository = personRepository;
        this.importCsv = importCsv;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    public int getNumberOfAllPersons() {
        return personRepository.findAll().size();
    }

    public List<Person> findAllByAgeDesc() {
        return personRepository.findAllByAgeDesc();
    }

    public List<Person> findAllByAgeDescPaged(Integer pageNo, Integer pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);

        Page<Person> pagedResult = personRepository.findAllByAgeDescPaged(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public Person getOldestWithPhoneNo() {
        return personRepository.findOldestByPhoneNoNotNull();
    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

    public Set<Person> findByLastName(String lastName) {
        return personRepository.findByLastName(lastName);
    }

    public void saveFromCsv() throws IOException {
        List<String[]> importedRecords = importCsv.importCsvFromFile();
        log.info("Imported csv");
        for (String[] importedRecord : importedRecords) {
            try {
                if (importedRecord.length != 1) {
                    Person person = new Person();
                    person.setFirstName(importedRecord[0]);
                    person.setLastName(importedRecord[1]);

                    int[] yearMonthDay = getYearMonthDay(importedRecord[2]);
                    LocalDate dateOfBirth = LocalDate.of(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2]);
                    person.setAge(Period.between(dateOfBirth, LocalDate.now()).getYears());

                    if (importedRecord[3].length() != 0) {
                        person.setPhoneNo(importedRecord[3]);
                    }
                    try {
                        personRepository.save(person);
                        log.info("saved data " + person);
                    } catch (Exception e) {
                        log.warn("incorrect data in phone number column " + person);
                        person.setPhoneNo(null);
                        personRepository.save(person);
                        log.info("saved data with phone number set to null for " + person);
                    }
                }
            } catch (Exception e) {
                log.error("Data import interrupted " + e);
            }
        }
        log.info("csv import completed");
    }

    private int[] getYearMonthDay(String date) {
        String[] elementsAsString = date.split("\\.");
        return Arrays.stream(elementsAsString).mapToInt(Integer::parseInt).toArray();
    }
}