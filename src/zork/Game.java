package zork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
 
public class Game {

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
    //  printRoomMap();
      currentRoom = roomMap.get("1.1");
    } catch (Exception e) {
      e.printStackTrace();
    }
    parser = new Parser();
  }
  /*
  private void printRoomMap()
  {
    System.out.println("\n\nPrinting the inventory of each room...");

    for (String roomKey: roomMap.keySet())
    {
      String key = roomKey.toString();
      Room value = roomMap.get(roomKey);
      System.out.println("\nRoom: " + key);
      value.printInventory();
    }
  }
*/

  private void initGods(String fileName) throws Exception {
    Path path = Path.of(fileName);
    String jsonString = Files.readString(path);
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);
  
    JSONArray jsonItems = (JSONArray) json.get("gods");

    
    for (Object godObj : jsonItems) {
      
      String godName = (String) ((JSONObject) godObj).get("name");
      String godId = (String) ((JSONObject) godObj).get("id");
      String description = (String) ((JSONObject) godObj).get("description");
      int godWeight = Integer.parseInt((String) ((JSONObject) godObj).get("weight"));
      boolean isOpenable = Boolean.parseBoolean((String) ((JSONObject) godObj).get("isOpenable"));

      String loc_id = (String) ((JSONObject) godObj).get("roomId");
      boolean isKillable = Boolean.parseBoolean((String) ((JSONObject) godObj).get("isKillable"));
      String bribeItem = (String) ((JSONObject) godObj).get("bribeItem");

      God god = new God(godName, isKillable, description, godWeight, bribeItem);

        roomMap.get(loc_id).addItem(god);

      godMap.put(godId, god);
      
    }
    //System.out.println("\nPrinting all Gods from Map...");
    //System.out.println(godMap.toString());
  }


  private void initItems(String fileName) throws Exception {
    Path path = Path.of(fileName);
    String jsonString = Files.readString(path);
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);
  
    JSONArray jsonItems = (JSONArray) json.get("items");

    //System.out.println("\nProcessing items (initItems())...");
    
    for (Object itemObj : jsonItems) {
      
      String itemName = (String) ((JSONObject) itemObj).get("name");
      String itemId = (String) ((JSONObject) itemObj).get("id");
      String description = (String) ((JSONObject) itemObj).get("description");
      int itemWeight = Integer.parseInt((String) ((JSONObject) itemObj).get("weight"));
      boolean isOpenable = Boolean.parseBoolean((String) ((JSONObject) itemObj).get("isOpenable"));
      String keyId = (String) ((JSONObject) itemObj).get("keyId");

      String loc_id = (String) ((JSONObject) itemObj).get("roomId");
      boolean isKey = Boolean.parseBoolean((String) ((JSONObject) itemObj).get("isKey"));

      Item item;

      if (isKey)
      {
        //System.out.println("\nkey object detected!");
        item = new Key(keyId, itemName, itemWeight, description);
       
      }
      else
      {
        //System.out.println("\nitem object detected!");
        item = new Item(itemWeight, itemName, isOpenable, description);
      }

      if (loc_id != null){
        //System.out.println("Item belongs in a room, adding to indicated room. Item ID = " + itemId + "  name = " + itemName + "  Location ID = " + loc_id + "  Item Weight = " + itemWeight + "  Is Openable = " + isOpenable);
        roomMap.get(loc_id).addItem(item);
      }else{
        loc_id = (String) ((JSONObject) itemObj).get("itemId");
        //System.out.println("Item belongs inside another item, adding inside item. Item ID = " + itemId + "  name = " + itemName + "  Location ID = " + loc_id + "  Item Weight = " + itemWeight + "  Is Openable = " + isOpenable);
        itemMap.get(loc_id).addItem(item);
      }

      itemMap.put(itemId, item);
      
    }
    //System.out.println("\n...End of processing items (initItems())...");

    //System.out.println("\nPrinting all Items from Map...");
    //System.out.println(itemMap.toString());
  }
  private void initRooms(String fileName) throws Exception {
    Path path = Path.of(fileName);
    String jsonString = Files.readString(path);
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);

    JSONArray jsonRooms = (JSONArray) json.get("rooms");

    for (Object roomObj : jsonRooms) {
      Room room = new Room();
      String roomName = (String) ((JSONObject) roomObj).get("name");
      String roomId = (String) ((JSONObject) roomObj).get("id");
      String roomDescription = (String) ((JSONObject) roomObj).get("description");
      room.setDescription(roomDescription);
      room.setRoomName(roomName);

      JSONArray jsonExits = (JSONArray) ((JSONObject) roomObj).get("exits");
      ArrayList<Exit> exits = new ArrayList<Exit>();
      for (Object exitObj : jsonExits) {
        String direction = (String) ((JSONObject) exitObj).get("direction");
        String adjacentRoom = (String) ((JSONObject) exitObj).get("adjacentRoom");
        String keyId = (String) ((JSONObject) exitObj).get("keyId");
        Boolean isLocked = (Boolean) ((JSONObject) exitObj).get("isLocked");
        Boolean isOpen = (Boolean) ((JSONObject) exitObj).get("isOpen");
        Exit exit = new Exit(direction, adjacentRoom, isLocked, keyId, isOpen);
        exits.add(exit);
      }
      room.setExits(exits);
      roomMap.put(roomId, room);

      //System.out.println("Room ID = " + roomId + "  name = " + roomName);
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
}