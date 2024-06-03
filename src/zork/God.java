package zork;

/* 
 * This class represents the gods of the game.
 * Some gods can be killed. Those that cannot
 * must be bribed to distract them. 
 */
public class God extends Item{
    private boolean isDefeated; //Tracks if the god has been defeated.
    private boolean killable;   //Indicates if a god can be killed.
    private String bribeItem;   //Hold the item name that can bribe/distract the god, null otherwise.

    /* Constructor */
    public God(String name, boolean k, String description, int weight, String bribe){
        super(weight, name, false, description);
        this.killable = k;
        this.bribeItem = bribe;
        this.isDefeated = false;
    }

    /* Will return if the god has been defeated.
     * Returns true, if the god has been defeated.
     * Return false, otherwise.
     */
    public boolean defeatedStatus()
    {
        return isDefeated;
    }

    /* 
     * Executes an attack attempt on a god.
     * Returns a string indicating if the god
     * has been defeated or not.
     */
    public String attack(){
        if(killable){
            isDefeated = true;
            return "You have defeated" + this.getName() + "you can now go to the next exit";
        }
        else{
            return "This god is not killable. Find another way to defeat them.";
        }
    }

    /*
     * An attempt is made to bribe/distract the god with
     * the item name passed in 'bribeAttempt'.
     * Returns true, if the attempt was successful, false
     * otherwise.
     */
    public boolean bribe(String bribeAttempt){
        boolean isBribeSuccessful = false;
        if((!killable) && (bribeAttempt.equals(this.bribeItem))){
            isBribeSuccessful = true;
        }
        return isBribeSuccessful;
    }

    /*
     * An attempt is made to bribe/distract the god with
     * the item object passed in 'bribeAttempt'.
     * Returns true, if the attempt was successful, false
     * otherwise.
     */
    public boolean bribe(Item bribeAttempt){
        boolean isBribeSuccessful = false;
        if((!killable) && ((bribeAttempt.getName()).equals(this.bribeItem))){
            isBribeSuccessful = true;
        }
        return isBribeSuccessful;
    }

    /* Can be used for testing to print God variables. */ 
    //public String toString()
    //{
    //  return "GOD- " + this.getName() + " " + this.getDescription() + " " + this.getWeight() + " " + this.killable + " " + this.bribeItem + " " + this.defeatedStatus();
    //}
    
}
