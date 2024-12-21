public class Elevator {
    StringBuilder lifeString = new StringBuilder("#");
    int currentFloor = 1;
    String direction = "Not busy"; // - Not busy
                                   // - Going to order
                                   // - Up (уже едет с заказом)
                                   // - Down (уже едет с заказом)
    boolean[] stopList;
}
