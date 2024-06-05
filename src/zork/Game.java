package zork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Game {

  /* HashMaps for rooms, items, gods. */
  public static HashMap<String, Room> roomMap = new HashMap<String, Room>();
  public static HashMap<String, Item> itemMap = new HashMap<String, Item>();
  public static HashMap<String, God> godMap = new HashMap<String, God>();

  private Parser parser;
  private Room currentRoom;

  /**
   * Create the game and initialise its internal map.
   */
  public Game() {
    try {
      initRooms("src\\zork\\data\\rooms.json");
      initItems("src\\zork\\data\\items.json");
      initGods("src\\zork\\data\\gods.json");
      currentRoom = roomMap.get("1.1");
    } catch (Exception e) {
      e.printStackTrace();
    }
    parser = new Parser();
  }

  /* Sets up all gods within the game, configuring them from gods.json. 
   * Assigns gods to designated rooms and adds each one to the god hashmap.
   */
  private void initGods(String fileName) throws Exception {
    /* Read gods.json */
    Path path = Path.of(fileName);
    String jsonString = Files.readString(path);
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);
    JSONArray jsonItems = (JSONArray) json.get("gods");

    /* Loop through every god in gods.json */
    for (Object godObj : jsonItems) {
      /* Store the values of each god in variables */
      String godName = (String) ((JSONObject) godObj).get("name");
      String godId = (String) ((JSONObject) godObj).get("id");
      String description = (String) ((JSONObject) godObj).get("description");
      int godWeight = Integer.parseInt((String) ((JSONObject) godObj).get("weight"));
      boolean isOpenable = Boolean.parseBoolean((String) ((JSONObject) godObj).get("isOpenable"));
      String loc_id = (String) ((JSONObject) godObj).get("roomId");
      boolean isKillable = Boolean.parseBoolean((String) ((JSONObject) godObj).get("isKillable"));
      String bribeItem = (String) ((JSONObject) godObj).get("bribeItem");

      /* Create a god object with the values from gods.json. */
      God god = new God(godName, isKillable, description, godWeight, bribeItem);
      roomMap.get(loc_id).addItem(god); // Add the current god object to the designated room. 
      godMap.put(godId, god); // Add the current god object to the god hashmap.
    }
  }


  /* Sets up all items within the game, configuring them from items.json. Assigns items
   * to designated rooms or inside other items and adds each one to the item hashmap.
   */
  private void initItems(String fileName) throws Exception {
    /* Read items.json */
    Path path = Path.of(fileName);
    String jsonString = Files.readString(path);
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);
    JSONArray jsonItems = (JSONArray) json.get("items");

    /* Loop through every item in items.json */
    for (Object itemObj : jsonItems) {
      /* Store the values of each item in variables */
      String itemName = (String) ((JSONObject) itemObj).get("name");
      String itemId = (String) ((JSONObject) itemObj).get("id");
      String description = (String) ((JSONObject) itemObj).get("description");
      int itemWeight = Integer.parseInt((String) ((JSONObject) itemObj).get("weight"));
      boolean isOpenable = Boolean.parseBoolean((String) ((JSONObject) itemObj).get("isOpenable"));
      String keyId = (String) ((JSONObject) itemObj).get("keyId");
      String loc_id = (String) ((JSONObject) itemObj).get("roomId");
      boolean isKey = Boolean.parseBoolean((String) ((JSONObject) itemObj).get("isKey"));

      /* Create a Item reference to assign a Key or Item object. */
      Item item;
      if (isKey)
      {
        item = new Key(keyId, itemName, itemWeight, description);
      }
      else
      {
        item = new Item(itemWeight, itemName, isOpenable, description);
      }

      /* If the item has a designated room, add it to the room. Else it belongs inside another item, add it inside that item. */
      if (loc_id != null){
        roomMap.get(loc_id).addItem(item);
      }else{
        loc_id = (String) ((JSONObject) itemObj).get("itemId");
        itemMap.get(loc_id).addItem(item);
      }

      /* Add the current Item object to the item hashmap. */
      itemMap.put(itemId, item);
    }
  }


  /* Sets up all rooms within the game, configuring them from rooms.json
   * Adds each room to the room hashmap.
   */
  private void initRooms(String fileName) throws Exception {
    /* Read rooms.json */
    Path path = Path.of(fileName);
    String jsonString = Files.readString(path);
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);
    JSONArray jsonRooms = (JSONArray) json.get("rooms");

    /* Loop through every room in rooms.json */
    for (Object roomObj : jsonRooms) {
      /* Store the values of each room in variables */
      Room room = new Room(); // Create a room object to hold the values.
      String roomName = (String) ((JSONObject) roomObj).get("name");
      String roomId = (String) ((JSONObject) roomObj).get("id");
      String roomDescription = (String) ((JSONObject) roomObj).get("description");
      room.setDescription(roomDescription);
      room.setRoomName(roomName);

      /* Read the exits of the current room from rooms.json */
      JSONArray jsonExits = (JSONArray) ((JSONObject) roomObj).get("exits");
      ArrayList<Exit> exits = new ArrayList<Exit>();
      /* Loop through the exits of the current room. */
      for (Object exitObj : jsonExits) {
        /* Store the values of each exit in variables */
        String direction = (String) ((JSONObject) exitObj).get("direction");
        String adjacentRoom = (String) ((JSONObject) exitObj).get("adjacentRoom");
        String keyId = (String) ((JSONObject) exitObj).get("keyId");
        Boolean isLocked = (Boolean) ((JSONObject) exitObj).get("isLocked");
        Boolean isOpen = (Boolean) ((JSONObject) exitObj).get("isOpen");

        /* Create a Exit object with the values for the current exit. */
        Exit exit = new Exit(direction, adjacentRoom, isLocked, keyId, isOpen);
        exits.add(exit);
      }
      room.setExits(exits);  // Set the exits of the room.
      roomMap.put(roomId, room);  // Add the current Room object to the room hashmap.
    }
  }

  /**
   * Main play routine. Loops until end of play.
   */
  public void play() {
    printWelcome();

    boolean finished = false;
    while (!finished) {
      Command command;
      try {
        command = parser.getCommand();
        finished = processCommand(command);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    System.out.println("Thank you for playing.  Good bye.");
  }

  /**
   * Print out the opening message for the player.
   */
  private void printWelcome() {
    System.out.println();
    System.out.println("Welcome to Zork!");
    System.out.println("Zork is a new, incredibly boring adventure game.");
    System.out.println("Type 'help' if you need help.");
    System.out.println();
    System.out.println(currentRoom.longDescription());
  }

  /**
   * Given a command, process (that is: execute) the command. If this command ends
   * the game, true is returned, otherwise false is returned.
   */
  private boolean processCommand(Command command) {
    if (command.isUnknown()) {
      System.out.println("I don't know what you mean...");
      return false;
    }

    //NO COMMANDS ARE COMPLETED BECAUSE ITEMS INVENTORY ARRAY LIST IS NOT ACCESSABLE
    String commandWord = command.getCommandWord();
    if (commandWord.equals("help"))
      printHelp();
    else if (commandWord.equals("go"))
      goRoom(command);
    else if (commandWord.equals("quit")) {
      if (command.hasSecondWord())
        System.out.println("Quit what?");
      else
        return true; // signal that we want to quit
    } else if (commandWord.equals("eat")) {
      System.out.println("Do you really think you should be eating at a time like this?");
    }else if(commandWord.equals("take")){
      takeItem(command);
    }else if(commandWord.equals("look")){
      if(!command.hasSecondWord()){
        Scanner thingy = new Scanner(System.in);
        System.out.println("Look at what?");
        String thingToSee = thingy.nextLine();
        System.out.println(thingToSee.getDescription());
      else if((command.getSecondWord())== item.name)
        System.out.println();
      }
    }else if(commandWord.equals("open")){
      openItem(command);
    }else if(commandWord.equals("close")){
      closeItem(command);
    }else if(commandWord.equals("drop")){
      throwItem(command);
    }else if(commandWord.equals("use")){
      useItem(command);
    }else if(commandWord.equals("attack")){
      attackGod(command);
    }else if(commandWord.equals("jump")){
      jumpOut(command);
    }
    return false;
  }

  // implementations of user commands:

  /**
   * Print out some help information. Here we print some stupid, cryptic message
   * and a list of the command words.
   */
  private void printHelp() {
    System.out.println("You are lost. You are alone. You wander");
    System.out.println("around at Monash Uni, Peninsula Campus.");
    System.out.println();
    System.out.println("Your command words are:");
    parser.showCommands();
  }

  /**
   * Try to go to one direction. If there is an exit, enter the new room,
   * otherwise print an error message.
   */
  private void goRoom(Command command) {
    if (!command.hasSecondWord()) {
      // if there is no second word, we don't know where to go...
      System.out.println("Go where?");
      return;
    }

    String direction = command.getSecondWord();

    // Try to leave current room.
    Room nextRoom = currentRoom.nextRoom(direction);

    if (nextRoom == null)
      System.out.println("There is no door!");
    else {
      currentRoom = nextRoom;
      System.out.println(currentRoom.longDescription());
    }
  }
//allows you to pick up an abject- AK
  private void takeItem(Command command) {
    if(!command.hasSecondWord()){
      System.out.println("Take What?");
      return;
    }

    String thing = command.getSecondWord();
//attempts to ad an item to player's inventory
    for(Item item:itemMap.){
      if(Item item.name == thing){
        playerInventory.addItem(item);
      }
    }
  }
//Allows player to open an item
  private void openItem(Command command){
    if(!command.hasSecondWord()){
      System.out.println("Open What?");
    }
    //takes in the item, uses an if else statement to see if it is openable
    //will check to see if the item is locked, if it is, it will ask what to open it with. If it is not locked, status will be set to open
    //once a key is used, the item's status will be set to open/unlocked
  }
//Allows player to close an item
  private void closeItem(Command command){
    if(!command.hasSecondWord()){
      System.out.println("Close What?");
    }
    //takes in the item, uses an if else statement to see if it is closeable, if it is, will change the status to close

  }
//Removes an item from the player's inventory
  private void dropItem(Command command){
    if(!command.hasSecondWord()){
      System.out.println("Drop What?");
    }
    //uses the removeItem function to remove the object from the personal inventory array list
  } 
//Can be used with a god or to open something locked.
  private void useItem(Command command){
    if(!command.hasSecondWord()){
      System.out.println("Use What?");
    }
    //the item will be used in the way it is meant
  }

  //Specific use of useItem function, only used against gods
  private void attackGod(Command command){
    System.out.print("Using what?");
    command.useItem;
    }
    //supposed to call the useItem function to "attack" the god
  }

  //only used at the end of the game to escape the crumbling
  private void jumpOut(Command command){
    if () //not in 6.2
      System.out.println("You cannot jump anywhere");
    else //call description for window
  }

  /* Can be used for testing to print all items in every room.
   * Must also uncomment toString() of Key, Item, God classes.
   * Must also uncomment printInventory() of Room class.
   */ 
//  private void printRoomMap()
//  {
//    System.out.println("\n\nPrinting the inventory of each room...");

//    for (String roomKey: roomMap.keySet())
//    {
//      String key = roomKey.toString();
//      Room rm = roomMap.get(roomKey);
//      System.out.println("\nRoom: " + key);
//      rm.printInventory();
//    }
//  }

