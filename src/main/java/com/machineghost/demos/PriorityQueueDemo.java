package com.machineghost.demos;

import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

/***
 * An animal shelter, which holds only dogs and cats, operates on a strictly "first in, first out" policy.
 * People must adopt either the "oldest" animal, or can specify if they want a dog or cat, then get the 
 * oldest from that group. They cannot select specific animals.
 * @author s.stone-loveall
 *
 */
public class PriorityQueueDemo {
    
    public enum AnimalType {
        DOG,
        CAT
    }
    
    public class Animal {

        String name;
        Date arrival;
        AnimalType animalType;
        
        public Date getArrival() {
            return arrival;
        }
        
        public AnimalType getAnimalType() {
            return animalType;
        }

        /***
         * Construct an animal of type cat or dog with a name.
         * @param name
         * @param animalType
         */
        public Animal(String name, AnimalType animalType) {
            this.name = name;
            this.animalType = animalType;
            this.arrival = new Date(); // set to current date
        }
    }
    
    /***
     * Implement PriorityQueue ordering for queue of Animal using this comparator class.
     *
     */
    public class AnimalComparator implements Comparator<Animal> {

        @Override
        public int compare(Animal firstAnimal, Animal secondAnimal) {
            if (firstAnimal.getArrival().before(secondAnimal.getArrival())) {
                return 1;
            }
            else if (firstAnimal.getArrival().after(secondAnimal.getArrival())) {
                return -1;
            }
            return 0;
        }
        
    }
    
    // keep a queue of animals, ordered by next up for adoption
    PriorityQueue<Animal> adoptableAnimals = new PriorityQueue<Animal>(new AnimalComparator());
    
    // keep a list of animals that have been evaluated for next up adoptions
    PriorityQueue<Animal> polledAnimals = new PriorityQueue<Animal>(adoptableAnimals.size());
    
    /***
     * Add an adoptable animal to the shelter queue
     */
    public void addAnimal(Animal animal) {
        adoptableAnimals.add(animal);
    }
    
    /***
     * Adopt the animal (regardless of dog or cat) next up in the adoption queue
     * @return
     */
    public Animal adoptOldestAnimall() {
        return adoptableAnimals.poll();
    }
    
    /***
     * Adopt the dog next up in the adoption queue
     */
    public Animal adoptOldestDog() {
        return adoptOldestAnimalByAnimalType(AnimalType.DOG);
    }
    
    /***
     * Adopt the cat next up in the adoption queue
     */
    public Animal adoptOldestCat() {
        return adoptOldestAnimalByAnimalType(AnimalType.CAT);
    }
    
    /***
     * Return the oldest animal in the queue by requested animal type.
     * If not found for requested type, or no animals are available for adoptions, null is returned.
     */
    public Animal adoptOldestAnimalByAnimalType(AnimalType animalType) {
        // algorithm:
        //  Look at the animal next-up on the queue.
        //  If it is the requested type, return it.
        //  Otherwise, add it to the polledAnimals queue and recursively try again.
        //  Once an animal matches, add the other polledAnimals back into the adoption queue 
        //  and return the matched animal.
        
        Animal oldest = adoptableAnimals.poll();
        
        if (oldest == null || oldest.getAnimalType() == animalType) {
            // requested animal type found
            // or, if null, no animals are available
            putPolledAnimalsBackInQueue();
        }
        else {
            // requested animal type not yet found
            // add current animal to the queue then continue search
            polledAnimals.add(oldest);
            adoptOldestAnimalByAnimalType(animalType);
        }
        return oldest;
    }
    
    /***
     * Put animals polled in current search back into the queue
     */
    private void putPolledAnimalsBackInQueue() {
        adoptableAnimals.addAll(polledAnimals);
        polledAnimals.clear();
    }
    
    public static void main(String[] args) {
        // demo
        // TODO
    }
}
