public class Evolution {
    private String name;
    private int level;
    private int hitPoints;
    private int attackPoints;
    private Attack[] attacks;

    //Time complexity is O(1)(no loops).
    public Evolution(String name, int level, int hitPoints, int attackPoints, Attack[] attacks) {
        this.name = name;
        this.level = level;
        this.hitPoints = hitPoints;
        this.attackPoints = attackPoints;
        this.attacks = new Attack[attacks.length];
        for (int i = 0; i < attacks.length; i++)
            this.attacks[i] = attacks[i];
    }

    //Time complexity is O(1)(no loops).
    public String getName() {
        return name;
    }

    //Time complexity is O(1)(no loops).
    public int getLevel() {
        return level;
    }
    //Time complexity is O(1)(no loops).

    public int getHitPoints() {

        return hitPoints;
    }
    //Time complexity is O(1)(no loops).

    public int getAttackPoints() {

        return attackPoints;
    }
    //Time complexity is O(1)(no loops).
    public Attack[] getAttacks() {

        return attacks;
    }
    //Time complexity is O(n).
    public String toString() {
        String out = "Evolution Name " + name + ", pokemon level " + level +
                ", hit points max " + hitPoints + " ,attack points max " + attackPoints +
                ". Attacks number " + attacks.length + ".\n";
        for (int i = 0; i < attacks.length; i++)
            out = out + attacks[i];
        return out;
    }
}
