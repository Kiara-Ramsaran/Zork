package zork;

public class NotKillableGods extends Item{
    private boolean isAlive;
    private String defeated;

    public NotKillableGods(String name, String defeated, boolean isKillabale, String description){
        super(999999999, name, false, description);
    
    }
}
