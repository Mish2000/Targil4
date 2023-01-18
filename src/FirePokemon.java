import java.util.Random;

public class FirePokemon extends Pokemon {
    protected static final int[] SELF_HIT_POINTS_DAMAGE_BOUNDS = {3, 10};
    protected static final int[] SELF_HIT_POINTS_DAMAGE_PROBABILITY = {1, 4}; //25%

    private int doubleEnemyDamage;

    //Time complexity is O(1)(no loops).
    public FirePokemon(String name, int type, int currentLevel, int hitPoints, int attackPoints, Attack[] attacks, Evolution[] evolutions) {
        super(name, type, currentLevel, hitPoints, attackPoints, attacks, evolutions);
    }

    //Time complexity is O(1)(no loops).
    public FirePokemon(Pokemon inPokemon) {
        super(inPokemon);
    }

    //Time complexity is O(1)(no loops).
    public String getTypeName() {
        return "Fire_pokemon";
    }

    //Time complexity is O(1)(no loops).
    public int getAttackSelfHitDamage() {
        Random random = new Random();
        int selfDamage = 0;
        int probability = random.nextInt(SELF_HIT_POINTS_DAMAGE_PROBABILITY[0],
                SELF_HIT_POINTS_DAMAGE_PROBABILITY[1] + 1);
        if (probability == 1)
            selfDamage = random.nextInt(SELF_HIT_POINTS_DAMAGE_BOUNDS[0], SELF_HIT_POINTS_DAMAGE_BOUNDS[1] + 1);
        return selfDamage;
    }

    //Time complexity is O(1)(no loops).
    public int getSpecialEnemyDamage() {
        return doubleEnemyDamage;
    }

    //Time complexity is O(n).
    public boolean specialAction() {
        Random random = new Random();
        int attackIndex;
        doubleEnemyDamage = 0;
        for (int i = 0; i < 2; i++) {
            attackIndex = random.nextInt(0, attacks.length);
            doubleEnemyDamage = doubleEnemyDamage + attacks[attackIndex].getEnemyDamage();
        }
        attackPoints = 0;
        hitPoints = hitPoints / 2;
        System.out.println("Fire pokemon performed a double attack, enemy damage is " + doubleEnemyDamage);
        return true;
    }

    //Time complexity is O(1)(no loops).
    public boolean isActionSuccess() {
        return true;
    }
}