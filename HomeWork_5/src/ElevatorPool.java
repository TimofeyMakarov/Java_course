import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ElevatorPool {
    final Elevator[] elevators;
    int numberOfFloors;
    int numberOfElevators;
    Executor orderExecutor = Executors.newSingleThreadExecutor();
    StringBuilder orderList = new StringBuilder();

    private static class Order{
        int currentFloor;
        int targetFloor;
        String moveDirection;

        Order(int currentFloor, int targetFloor, String moveDirection){
            this.currentFloor = currentFloor;
            this.targetFloor = targetFloor;
            this.moveDirection = moveDirection;
        }
    }

    ElevatorPool(int numberOfElevators, int numberOfFloors){
        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        elevators = new Elevator[numberOfElevators];
        for (int i = 0; i < numberOfElevators; i++){
            elevators[i] = new Elevator();
            elevators[i].stopList = new boolean[numberOfFloors];
            for (int j = 0; j < numberOfFloors; j++){
                elevators[i].stopList[j] = false; // Устанавливаем изначально, что ни одна кнопка не вызвана
            }
        }
    }

    public void callElevator(int floor, String moveDirection, int target){
        orderExecutor.execute(new orderHandler(new Order(floor, target, moveDirection)));
        String order = "Заказ: " + floor + " -> " + target + "\n";
        orderList.append(order);
    }

    private class orderHandler implements Runnable{
        private final Order currentOrder;

        orderHandler(Order currentOrder){
            this.currentOrder = currentOrder;
        }

        @Override
        public void run() {
            int minimumDistance = Integer.MAX_VALUE;
            Elevator suitableElevator = null;
            int numberOfElevator = 0;
            synchronized (elevators) {
                // Выполняем цикл пока не найдём лифт
                while (true) {
                    // Проходимся по массиву лифтов и ищем подходящий, чтоб ничего с массивом не происходило - захватываем монитор
                    for (int i = 0; i < numberOfElevators; i++) {
                        synchronized (elevators[i]) {
                            // Если направления движения не противоположны и лифт самый близкий - запоминаем
                            if (elevators[i].direction.equals(currentOrder.moveDirection) || elevators[i].direction.equals("Not busy")) {
                                if (elevators[i].direction.equals("Up") && (currentOrder.currentFloor < elevators[i].currentFloor))
                                    continue;
                                if (elevators[i].direction.equals("Down") && (currentOrder.currentFloor > elevators[i].currentFloor))
                                    continue;
                                int currentDistance = Math.abs(currentOrder.currentFloor - elevators[i].currentFloor);
                                if (currentDistance < minimumDistance) {
                                    // Если лифт в движении - он подходит, если только не на нашем этаже, иначе есть опасность, что он проскочит
                                    if ((!elevators[i].direction.equals("Not busy")) && (currentDistance == 0))
                                        continue;
                                    suitableElevator = elevators[i];
                                    minimumDistance = currentDistance;
                                    numberOfElevator = i;
                                }
                            }
                        }
                    }
                    if (suitableElevator != null) break;

                    try {
                        elevators.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Ошибка при поиске подходящего лифта");
                    }
                }

                synchronized (elevators[numberOfElevator]) {
                    if (!suitableElevator.direction.equals("Not busy")) {
                        suitableElevator.stopList[currentOrder.currentFloor - 1] = true;
                        suitableElevator.stopList[currentOrder.targetFloor - 1] = true;
                    } else {
                        // Если лифт до этого стоял - запускаем его в движение
                        suitableElevator.stopList[currentOrder.targetFloor - 1] = true;
                        suitableElevator.direction = "Going to order";
                        ElevatorMove elevatorMove = new ElevatorMove(suitableElevator, numberOfElevator + 1, currentOrder.currentFloor, currentOrder.moveDirection);
                        Thread elevator = new Thread(elevatorMove);
                        elevator.start();
                    }
                }
            }
        }
    }

    private class ElevatorMove implements Runnable{
        Elevator elevator;
        int numberOfElevator;
        int orderFloor;
        String orderDirection;

        ElevatorMove(Elevator elevator, int numberOfElevator, int orderFloor, String orderDirection){
            this.elevator = elevator;
            this.numberOfElevator = numberOfElevator;
            this.orderFloor = orderFloor;
            this.orderDirection = orderDirection;
        }

        @Override
        public void run() {
            // Едем до текущего заказа
            movingToOrder();

            elevator.direction = orderDirection;

            if (orderDirection.equals("Down")){
                while (thereAreOrderOnMovingDirection()){
                    moveDown();
                    checkFloor();
                }
            }

            if (orderDirection.equals("Up")){
                while (thereAreOrderOnMovingDirection()){
                    moveUp();
                    checkFloor();
                }
            }

            synchronized (elevators) {
                elevator.direction = "Not busy";
                elevators.notifyAll();
            }
        }

        private void movingToOrder(){
            if (elevator.currentFloor == orderFloor) {
                openDoor();
                return;
            }
            while (elevator.currentFloor < orderFloor){
                moveUp();
            }
            while (elevator.currentFloor > orderFloor){
                moveDown();
            }
            openDoor();
        }

        private void checkFloor(){
            if (elevator.stopList[elevator.currentFloor - 1]){
                openDoor();
                elevator.stopList[elevator.currentFloor - 1] = false;
            }
        }

        private boolean thereAreOrderOnMovingDirection(){
            if (elevator.direction.equals("Up")) {
                for (int i = elevator.currentFloor - 1; i < numberOfFloors; i++) {
                    if (elevator.stopList[i]) {
                        return true;
                    }
                }
            } else {
                for (int i = elevator.currentFloor - 1; i >= 0; i--) {
                    if (elevator.stopList[i]) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void openDoor(){
            elevator.lifeString.deleteCharAt(elevator.currentFloor - 1);
            elevator.lifeString.append('O');
            printCurrentSituation();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("\t\tЛифт " + numberOfElevator + ": не удалось остановиться на" + elevator.currentFloor + " этаже");
            }
            elevator.lifeString.deleteCharAt(elevator.currentFloor - 1);
            elevator.lifeString.append('#');
            printCurrentSituation();
        }

        private void moveDown(){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            elevator.currentFloor -= 1;
            elevator.lifeString.deleteCharAt(elevator.currentFloor);
            printCurrentSituation();
        }

        private void moveUp(){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
            elevator.currentFloor += 1;
            elevator.lifeString.append("#");
            printCurrentSituation();
        }

        public static void clearScreen() {
            for (int i = 0; i < 20; i++){
                System.out.println();
            }
        }

        private synchronized void printCurrentSituation(){
            synchronized (elevators) {
                clearScreen();
                for (int i = 0; i < numberOfElevators; i++) {
                    System.out.print(elevators[i].currentFloor + "\t");
                    System.out.print(elevators[i].lifeString);
                    System.out.println();
                }
                System.out.println();
                System.out.print(orderList);
            }
        }
    }
}
