package com.ao.importCsvToH2.person;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/all/count")
    public int getNumberOfAllPersons() {
        return personService.getNumberOfAllPersons();
    }

    @GetMapping("/all/sort")
    public List<Person> getAllPersonsSortByAgeDesc() {
        return personService.findAllByAgeDesc();
    }

    // http://localhost:8080/person/all/sortPaged?pageSize=5&pageNo=1
    @GetMapping("/all/sortPaged")
    public ResponseEntity<List<Person>> getAllPersonsSortByAgeDescPaged(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSize) {
        List<Person> list = personService.findAllByAgeDescPaged(pageNo, pageSize);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/all/oldest")
    public Person getOldestWithPhoneNo() {
        return personService.getOldestWithPhoneNo();
    }

    @GetMapping("/findById/{id}")
    public Person getPerson(@PathVariable("id") Long id) {
        return personService.getPersonById(id);
    }

    @GetMapping("/findByLastName/{lastName}")
    public Set<Person> getPersonByLastName(@PathVariable("lastName") String lastName) {
        return personService.findByLastName(lastName);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable("id") Long id) {
        personService.delete(id);
    }

    @RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
    public void deleteAllPersons() {
        personService.deleteAll();
    }

    @PostMapping("/save")
    public Long savePerson(@RequestBody Person person) {
        personService.save(person);
        return person.getId();
    }

    @GetMapping("/savecsv")
    public List<Person> saveFromCsv() throws IOException {
        personService.saveFromCsv();
        return personService.getAllPersons();
    }
}
