public class Evolution {
    private String name;
    private int level;
    private int hitPoints;
    private int attackPoints;
    private Attack[] attacks;

    public Evolution (String name, int level, int hitPoints, int attackPoints, Attack[] attacks) {
        this.name = name;
        this.level = level;
        this.hitPoints = hitPoints;
        this.attackPoints = attackPoints;
        this.attacks = new Attack[attacks.length];
        for (int i = 0; i <attacks.length; i++)
            this.attacks[i] = attacks[i];
    }
    public String getName() {
        return name;
    }
    public int getLevel() {
        return level;
    }

    public int getHitPoints() {

        return hitPoints;
    }
    public int getAttackPoints() {

        return attackPoints;
    }
    public Attack[] getAttacks() {

        return attacks;
    }
    public String toString() {
        String out = "Evolution Name " + name + ", pokemon level " + level +
                ", hit points max " + hitPoints + " ,attack points max " + attackPoints +
                ". Attacks number " + attacks.length + ".\n";
        for (int i = 0; i < attacks.length; i++)
            out = out + attacks[i];
        return out;
    }
}
