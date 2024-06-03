package zork;

/* This class represents a single item in the game. */
public class Item extends OpenableObject {
  private int weight;             // Weight of the item.
  private String name;            // Name of the item.
  private String description;     // Description of the item.
  private boolean isOpenable;     // Can the item be opened.
  private Inventory inventory;    // Other items held within this item.
  
  /* Constructor */
  public Item(int weight, String name, boolean isOpenable, String description) {
    this.weight = weight;
    this.name = name;
    this.isOpenable = isOpenable;
    this.description = description;
    /* If the item has the capacity to hold extra items internally, set up an inventory. */
    if (isOpenable)
    inventory = new Inventory(100);
  }

  /* Attempts to open the object. If the item cannot be opened a message
   * will be printed. If the item can be opened, the 'isOpen' status
   * will be set to true. 
   */
  public void open() {
    if (!isOpenable)
    {
      System.out.println("The " + name + " cannot be opened.");
    }
    else
    {
      this.setOpen(true);
    }
  }

  /* Returns the weight of the item. */
  public int getWeight() {
    return weight;
  }

  /* Sets the weight of the item. */
  public void setWeight(int weight) {
    this.weight = weight;
  }

  /* Returns the name of the item. */
  public String getName() {
    return name;
  }

  /* Sets the name of the item. */
  public void setName(String name) {
    this.name = name;
  }

  /* Determines if this is an openable item. 
   * Returns true, if this is an openable item.
   * Returns false, otherwise.
  */
  public boolean isOpenable() {
    return isOpenable;
  }

  /* Sets if the item is capable of being opened. */
  public void setOpenable(boolean isOpenable) {
    this.isOpenable = isOpenable;
  }

  /* Returns the description of the item. */
  public String getDescription()
  {
    return this.description;
  }

  /* Inserts an item into the present item. */
  public boolean addItem(Item item) {
    if (isOpenable)
      return inventory.addItem(item);
    else 
      return false;
}

/* Removes an item from the current item. */
public Item removeItem(Item item){
  if(isOpenable)
    return inventory.removeItem(item);
  else
    System.out.println("You cannot take the " + item.name + " from the " + name);
 
  return null;
}

/* Can be used for testing to print Item variables. */ 
//public String toString()
//{
//  return "ITEM- " + this.name + " " + this.description + " " + this.weight + " " + this.isOpenable + " " + this.isLocked() + " " + this.getKeyId() + " " + this.isOpen();
//}
 
}