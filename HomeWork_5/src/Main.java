import java.util.Random;

public class Main {
    private static final int numberOfFloors = 30;

    public static void main(String[] args) {
        ElevatorPool elevatorPool = new ElevatorPool(5, numberOfFloors);

        for (int i = 0; i < 10; i++){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Не удалось сгенерировать задачу");
                continue;
            }
            // Генерируем задачу - два числа currentFloor и targetFloor
            Random floorGenerator = new Random();
            int currentFloor = floorGenerator.nextInt(1, numberOfFloors);
            int targetFloor = floorGenerator.nextInt(1, numberOfFloors);
            while (currentFloor == targetFloor){
                targetFloor = floorGenerator.nextInt(1, numberOfFloors);
            }

            if (currentFloor > targetFloor){
                elevatorPool.callElevator(currentFloor, "Down", targetFloor);
            } else {
                elevatorPool.callElevator(currentFloor, "Up", targetFloor);
            }
        }
    }
}