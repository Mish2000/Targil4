import java.util.Random;

public class Attack {
    private double attackCost;
    private int enemyDamageMin;
    private int enemyDamageMax;
    private String attackName;

    //Time complexity is O(1).
    public Attack(String attackName, int attackCost, int enemyDamageMin, int enemyDamageMax) {
        this.attackName = attackName;
        this.enemyDamageMax = enemyDamageMax;
        this.enemyDamageMin = enemyDamageMin;
        this.attackCost = attackCost;
    }

    //Time complexity is O(1).
    public int getEnemyDamage() {
        Random random = new Random();
        return random.nextInt(enemyDamageMin, enemyDamageMax + 1);
    }

    //Time complexity is O(1).
    public int getAttackCost() {
        return (int) attackCost;
    }

    //Time complexity is O(1).
    public String toString() {
        String out = "Attack name: " + attackName + ", Enemy damage bounds: (" + enemyDamageMin + " , " + enemyDamageMax + "), ";
        out = out + "Attack self cost: " + attackCost + ".\n";
        return out;
    }

}
