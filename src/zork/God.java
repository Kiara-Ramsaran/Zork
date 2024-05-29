package zork;

public class God{
    private boolean isDefeated = false;
    private String name;
    private boolean killable;
    private String description;
    public God(String n, boolean k, String des){
        this.name = n;
        this.killable = k;
        this.description = des;
    }
    public String attack(){
        if(killable){
            isDefeated = true;
            System.out.print("You have defeated" + name + "you can now go to the next exit");
        }
        else{
            System.out.print("This god is not killable. Find another way to defeat them.");
        }
    }
    public void bribe(){
        if(!defeatable){

        }
        
    }
}
