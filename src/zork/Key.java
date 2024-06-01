package zork;

/*
 * This class represents a key that can be
 * used to unlock rooms or other items. 
 */
public class Key extends Item {

  private String keyId; //Indicated the unique key id.

  /* Constructor */
  public Key(String keyId, String keyName, int weight, String description) {
    super(weight, keyName, false, description);
    this.keyId = keyId;
  }

  /* Returns the unique key id */
  public String getKeyId() {
    return keyId;
  }

  //public String toString()
 //{
  // return "KEY- " + this.getName() + " " + this.getDescription() + " " + this.getKeyId() + " " + this.getWeight() + " " + this.isOpenable();
 //}
}
