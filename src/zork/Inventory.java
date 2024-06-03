package zork;

import java.util.ArrayList;

public class Inventory {
  private ArrayList<Item> items;  //Actual collection of all items in inventory.
  private int maxWeight;          //Max weight allowed. 
  private int currentWeight;      //Current total weight of all items.

  /* Constructor with indicated max weight allowed. */
  public Inventory(int maxWeight) {
    this.items = new ArrayList<Item>();
    this.maxWeight = maxWeight;
    this.currentWeight = 0;
  }

  /* Returns the total max weight allowed for the inventory. */
  public int getMaxWeight() {
    return maxWeight;
  }

  /* Returns the total current weight of all items in the inventory. */
  public int getCurrentWeight() {
    return currentWeight;
  }

  /* Adds an item to the inventory. 
   * Returns true, if the item was added to the inventory.
   * Returns false, if the item cannot be added due to total
   * weight exceeding max weight.
   */
  public boolean addItem(Item item) {
    if (item.getWeight() + currentWeight <= maxWeight)
      return items.add(item);
    else {
      System.out.println("There is no room to add the item.");
      return false;
    }
  }

  /* Removes an item from the inventory.
   * Returns the item that was removed, if found.
   * Returns null, if the item was not found in the inventory.
   */
  public Item removeItem(Item item) {
    //throw new UnsupportedOperationException("Unimplemented method 'removeItem'");
    
    /* Checks if the item passed in exists in the inventory. */
    if (items.contains(item))
    {
      int indexToRemove = -1;

      /* Searches for and removes the item from inventory. */
      for(int i=0; (i<items.size()) && (indexToRemove == -1);i++)
      {
        if (items.get(i) == item)
        {
          items.remove(i);
          return item;
        }
      }
    }
    return null;
  }

  /* Returns an arraylist of all items in the inventory */
  public ArrayList<Item> getInventoryItems(){
    return items;
  }

  /* Can be used for testing to print all items in the current inventory. */ 
  //public void printAllItems(){
  //  System.out.println("Printing all inventory items...");
  //  for(Item i:items)
  //  {
  //    System.out.println(i.toString());
  //  }
  //}

}
