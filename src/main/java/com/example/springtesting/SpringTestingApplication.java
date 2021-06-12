package com.example.springtesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {
    private List<Coffee> coffees= new ArrayList<>();

    public RestApiDemoController() {
        coffees.addAll(
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
        return coffees;
    }

    @GetMapping("/{id}")
    Optional<Coffee> getCoffeeById(@PathVariable String id)
    {
        for(Coffee c: coffees){
            if(c.getId().equals(id))
            {
                return Optional.of(c);
            }
        }
        // Optional means if an empty Optional<Coffee> returned if the id requested isn’t present in our small group of coffees:
        return Optional.empty();
    }

    @PostMapping
    Coffee postCoffee(@RequestBody Coffee coffee) {
        coffees.add(coffee);
        return coffee;
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        int coffeeIndex = -1;

        for (Coffee c: coffees) {
            if (c.getId().equals(id)) {
                coffeeIndex = coffees.indexOf(c);
                coffees.set(coffeeIndex, coffee);
            }
        }

        //  Instead of returning only the updated or created Coffee object,
        //  the putCoffee method will now return a ResponseEntity containing said Coffee and
        //  the appropriate HTTP status code: 201 (Created)
        //  if the PUT coffee didn’t already exist, and 200 (OK) if it existed and was updated.
        return (coffeeIndex == -1) ?
                new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) :
                new ResponseEntity<>(coffee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteCoffee(@PathVariable String id) {
        coffees.removeIf(c -> c.getId().equals(id));
    }
}



class Coffee {
    private final String id;
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
    public String getId() {
        return id ;
    }
    public String getName() {
        return name;
    }
    public void setName (String name)
    {
        this.name = name;
    }
}
