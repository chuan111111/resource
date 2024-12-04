package lab4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CityAnalysis {

    public static class City {
        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population) {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public int getPopulation() {
            return population;
        }

        @Override
        public String toString() {
            return "City{name='" + name + "', state='" + state + "', population=" + population + '}';
        }
    }

    public static Stream<City> readCities(String filename) throws IOException {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException {
        Stream<City> cities = readCities("cities.txt");

        // Q1: Count how many cities there are for each state
        Map<String, Long> cityCountPerState = cities.collect(
                Collectors.groupingBy(City::getState, Collectors.counting())
        );
        System.out.println("Q1: # of cities per state:");
        cityCountPerState.forEach((state, count) ->
                System.out.printf("%s: %d%n", state, count)
        );

        cities = readCities("cities.txt");
        // Q2: Count the total population for each state
        Map<String, Integer> statePopulation = cities.collect(
                Collectors.groupingBy(City::getState, Collectors.summingInt(City::getPopulation))
        );
        System.out.println("\nQ2: population per state:");
        statePopulation.forEach((state, population) ->
                System.out.printf("%s: %d%n", state, population)
        );

        cities = readCities("cities.txt");
        // Q3: For each state, get the city with the longest name
        Map<String, String> longestCityNameByState = cities.collect(
                Collectors.groupingBy(
                        City::getState,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(c -> c.getName().length())),
                                city -> city.map(City::getName).orElse("")
                        )
                )
        );
        System.out.println("\nQ3: longest city name per state:");
        longestCityNameByState.forEach((state, cityName) ->
                System.out.printf("%s: %s%n", state, cityName)
        );

        cities = readCities("cities.txt");
        // Q4: For each state, get the set of cities with >500,000 population
        Map<String, Set<City>> largeCitiesByState = cities
                .filter(city -> city.getPopulation() > 500000)
                .collect(Collectors.groupingBy(
                        City::getState, Collectors.toSet()
                ));
        System.out.println("\nQ4: cities with >500,000 population for each state:");
        largeCitiesByState.forEach((state, citySet) -> {
            System.out.printf("%s: %s%n", state, citySet.stream()
                    .map(City::toString)
                    .collect(Collectors.joining(", "))
            );
        });
    }
}
