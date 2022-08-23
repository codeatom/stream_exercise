package se.lexicon.vxo.service;

import se.lexicon.vxo.data.JsonReader;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;
import se.lexicon.vxo.model.PersonDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class PeopleImpl implements People {

    private static final PeopleImpl INSTANCE;

    static {
        INSTANCE = new PeopleImpl();
    }

    static PeopleImpl getInstance() {
        return INSTANCE;
    }

    private List<Person> people;

    private PeopleImpl() {
        people = JsonReader.getInstance().read();
    }

    @Override
    public List<Person> getPeople() {
        return people;
    }

    public Optional<Person> findById(int id) {
        return people.stream().filter(p -> p.getPersonId() == id).findFirst();
    }

    public List<Person> findByFirstName(String firstName) {
        return people.stream().filter(p -> p.getFirstName().equalsIgnoreCase(firstName)).collect(Collectors.toList());
    }

    public List<Person> findByLastName(String lastName) {
        return people.stream().filter(p -> p.getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());
    }

    public List<Person> findByFirstOrLastName(String name) {
        return people.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(name) || p.getLastName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<Person> findByGender(Gender gender) {
        return people.stream().filter(p -> p.getGender().equals(gender)).collect(Collectors.toList());
    }

    public Set<Person> findByBirthDate(LocalDate date) {
        return people.stream().filter(person -> person.getDateOfBirth().equals(date)).collect(Collectors.toSet());
    }

    public Set<Person> findAllHavingBirthDate() {
        return people.stream().filter(person -> person.getDateOfBirth() != null).collect(Collectors.toSet());
    }

    public Set<LocalDate> getAllBirthDate() {
        return people.stream()
                .map(Person::getDateOfBirth)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Optional<String> birthDateToString(int id) {
      //  LocalDate dateOfBirth = findById(id).map(Person::getDateOfBirth).orElse(LocalDate.now());
      //
      //  String birthDate = dateOfBirth.getDayOfWeek()
      //          + " " + dateOfBirth.getDayOfMonth()
      //          + " " + dateOfBirth.getMonth()
      //          + " " + dateOfBirth.getYear();
      //
      //  return Optional.of(birthDate);

        return people.stream()
                .filter(person -> person.getPersonId() == id)
                .map(Person::getDateOfBirth)
                .map(date -> date.getDayOfWeek() + " " + date.getDayOfMonth() + " " + date.getMonth() + " " + date.getYear())
                .findFirst();
    }

    public int getAge(Person person){
       return Period.between(person.getDateOfBirth(), LocalDate.now()).getYears();
    }

    public OptionalDouble getAgeAverage() {
        return people.stream().mapToInt(this::getAge).average();
    }

    public boolean isPalindrome(String str){
      //  StringBuilder reversedChar = new StringBuilder();
      //
      //  for (int i = str.length() - 1; i >= 0; i--){
      //      reversedChar.append(str.charAt(i));
      //  }
      //
      //  return reversedChar.toString().equalsIgnoreCase(str);

        return str.equalsIgnoreCase(new StringBuilder(str).reverse().toString());
    }

    public Set<String> getUniquePalindrome(){
        return people
                .stream().map(Person::getFirstName).collect(Collectors.toList())
                .stream().filter(this::isPalindrome).collect(Collectors.toSet());
    }

    public Map<String, List<Person>> getLastNameMap(){
      //  Set<String> lastNames = people.stream().map(Person::getLastName).collect(Collectors.toSet());
      //
      //  Map<String, List<Person>> personMap = new HashMap<>();
      //
      //  for (String str : lastNames){
      //      List<Person> personList = people.stream()
      //              .filter(p -> p.getLastName().equalsIgnoreCase(str))
      //              .collect(Collectors.toList());
      //
      //      personMap.put(str, personList);
      //  }
      //
      //  return personMap;

        return people.stream()
                .collect(Collectors.groupingBy(Person::getLastName));
    }

    public List<PersonDto> constructPersonDto(LocalDate date) {
      //  List<Person> personList = people.stream().filter(p -> p.getDateOfBirth().isBefore(date)).collect(Collectors.toList());
      //
      //  List<PersonDto> dtoList = new ArrayList<>();
      //
      //  for (Person p : personList) {
      //      PersonDto personDto = new PersonDto(p.getPersonId(), p.getFirstName() + " " + p.getLastName());
      //      dtoList.add(personDto);
      //  }
      //
      //  return dtoList;

        return people.stream()
                .filter(p -> p.getDateOfBirth().isBefore(date))
                .map(person -> new PersonDto(person.getPersonId(), person.getFirstName()+ " " + person.getLastName()))
                .collect(Collectors.toList());
    }

}


































