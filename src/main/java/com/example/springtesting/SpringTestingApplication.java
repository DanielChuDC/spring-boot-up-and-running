package com.example.springtesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SpringTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestingApplication.class, args);
    }

}

interface CoffeeRepository extends CrudRepository<Coffee, String> {}

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {

    private final CoffeeRepository coffeeRepository;

    public RestApiDemoController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeRepository.saveAll(
                List.of(
                        new Coffee("Café Cereza"),
                        new Coffee("Café Ganador"),
                        new Coffee("Café Lareño"),
                        new Coffee("Café Três Pontas")
                )
        );
    }

    // Get endpoint coffees
    // Marshalling and unmarshalling of objects to JSON automatically
//    @RequestMapping(value = "/coffees", method = RequestMethod.GET)
//    Iterable<Coffee> getCoffees(){
//        return coffees;
//    }

    // This method works the same for the @RequestMapping
    @GetMapping
    Iterable<Coffee> getCoffees() {
        return coffeeRepository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Coffee> getCoffeeById(@PathVariable String id)
    {
        return coffeeRepository.findById(id);
    }

    @PostMapping
    Coffee postCoffee(@RequestBody Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        return (!coffeeRepository.existsById(id))
                ? new ResponseEntity<>(coffeeRepository.save(coffee),
                HttpStatus.CREATED)
                : new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteCoffee(@PathVariable String id) {
        coffeeRepository.deleteById(id);
    }
}


@Entity
class Coffee {
    //With Coffee now defined as a valid JPA entity able to be stored and retrieved, it’s time to make the connection to the database
    @Id
    private  String id;
    private String name;

    public Coffee (String id, String name)
    {
        this.id = id;
        this.name = name;
    }
    public Coffee(String name){
        // THIS LINE called the constructor above
        this(UUID.randomUUID().toString(), name);
        //?

    }

    public Coffee() {

    }

    public String getId() {
        return id ;
    }
    public String getName() {
        return name;
    }
    public void setId(String id) { this.id = id;};
    public void setName (String name)
    {
        this.name = name;
    }
}
