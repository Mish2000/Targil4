import java.util.Random;

public  abstract class Pokemon {
    protected static final int  KICK_POINTS =  2;
    protected static final double START_LOST_POINTS_PERCENT = 0.75; //75%
    protected static final int DEFAULT_ATTACK_FACTOR = 1;
    protected static int[] EVOLUTION_HIT_POINTS_PRICE = {20,25};
    protected static int[] EVOLUTION_ATTACK_POINTS_PRICE = {30,40};

    protected String name;
    protected int type;
    protected int currentLevel;
    protected int hitPoints;
    protected  int attackPoints;
    protected int maxHitPoints;
    protected int maxAttackPoints;
    protected int attackFactor;
    protected Evolution[] evolutions;
    protected int actualEvolutionIndex;
    protected Attack[] attacks;
    protected int actualAttackIndex;

    @Override
    public String toString() {
        String out = getTypeName() + ".";
        out = out + "Name: " + name +", current evolution level " + currentLevel +", hit points " +
                hitPoints + ", attack points  " + attackPoints + "\n";
        if (evolutions.length != 0) {
            out = out + "Evolutions number " + evolutions.length + ", All evolutions:\n";
            for (int i = 0; i < evolutions.length; i++)
                out = out + (i + 1) + ". " + evolutions[i].toString() + "\n";
        }
        return out;
    }

    public Pokemon(String name, int type, int currentLevel, int hitPoints, int energyPoints, Attack[] attacks, Evolution[] evolutions) {
        this.name = name;
        this.type = type;
        this.currentLevel = currentLevel;
        this.hitPoints = hitPoints;
        this.attackPoints = energyPoints;
        storeMaxPoints();
        attackFactor = DEFAULT_ATTACK_FACTOR;
        this.attacks = new Attack[attacks.length];
        for (int i=0; i<attacks.length; i++)
            this.attacks[i] = attacks[i];
        actualAttackIndex = 0;
        actualEvolutionIndex = 0;
        if (evolutions==null)
            this.evolutions = new Evolution[0];
        else {
            this.evolutions = new Evolution[evolutions.length];
            for (int i = 0; i < evolutions.length; i++)
                this.evolutions[i] = evolutions[i];
        }
    }

    public Pokemon(Pokemon pokemon) {
        this.name = pokemon.getName();
        this.type = pokemon.getType();
        this.currentLevel = pokemon.getCurrentLevel();
        this.hitPoints = pokemon.getHitPoints();
        this.attackPoints = pokemon.getAttackPoints();
        attackFactor = DEFAULT_ATTACK_FACTOR;
        Attack[] inAttack = pokemon.getAttacks();
        storeMaxPoints();
        this.attacks = new Attack[inAttack.length];
        for (int i=0; i<inAttack.length; i++)
            this.attacks[i] = inAttack[i];
        actualAttackIndex = 0;
//        specialActionFlag = true;
        Evolution[] inEvolutions = pokemon.getEvolutions();
        if (inEvolutions.length==0)
            this.evolutions = new Evolution[0];
        else {
            this.evolutions = new Evolution[inEvolutions.length];
            for (int i = 0; i < inEvolutions.length; i++)
                this.evolutions[i] = inEvolutions[i];
        }
    }

    private void storeMaxPoints() {
        this.maxAttackPoints = this.attackPoints;
        this.maxHitPoints = this.hitPoints;
    }

    public String getName() {
        return name;
    }
    public int getType() {
        return type;
    }

    public abstract String getTypeName();
    public Evolution[] getEvolutions() {
        return evolutions;
    }

    public void kick() {
        hitPoints = hitPoints - KICK_POINTS;
    }
    public void nextTurn(int[] hitAdd, int[] attackPointsAdd) {        Random random = new Random();
        int points = random.nextInt(hitAdd[0], hitAdd[1]);
        setAddHitPoints(points);
        points = random.nextInt(attackPointsAdd[0],attackPointsAdd[1]);
        setAddAttackPoints(points);

    }
    public abstract boolean isActionSuccess();
    public static String getKickAttackTitle() {
        return "Kick attack. Enemy damage: " + KICK_POINTS + ". No self attack points cost. ";
    }
    public int getKickPoints(){
        return KICK_POINTS;
    }
    public abstract boolean specialAction();
    public abstract int getSpecialEnemyDamage();
    public void gameStart() {
        attackPoints =(int) (attackPoints * START_LOST_POINTS_PERCENT);
    }
    public abstract int getAttackSelfHitDamage();

    public boolean evaluationPokemon() {
        if (evolutions.length < 2) {
            return false;
        }
        else {
            for (int i = 1; i< evolutions.length; i++) {
                if (evolutions[i].getLevel() - this.currentLevel == 1) {
                    if ((this.currentLevel == 1) || (this.currentLevel == 2)) {
                        this.name = evolutions[i].getName();
                        this.currentLevel = evolutions[i].getLevel();
                        this.hitPoints = evolutions[i].getHitPoints();
                        this.attackPoints = evolutions[i].getAttackPoints();
                        storeMaxPoints();
                        this.hitPoints =  this.hitPoints - EVOLUTION_HIT_POINTS_PRICE[this.currentLevel - 1];
                        this.attackPoints = this.attackPoints - EVOLUTION_ATTACK_POINTS_PRICE[this.currentLevel - 1];
                        evolutionAttacksMerge(evolutions[i].getAttacks());
                        return true;
                    }
                }
            }
            return false;
        }
    }
    private void evolutionAttacksMerge(Attack[] evolutionAttacks) {
        int newAttacksLength = this.attacks.length + evolutionAttacks.length;
        Attack[] newAttacks = new Attack[newAttacksLength];
        int i ,j , k;
        for (j = 0; j < this.attacks.length; j++)
            newAttacks[j] = this.attacks[j];
        for (k = 0 ; k < evolutionAttacks.length; k++, j++)
            newAttacks[j] = evolutionAttacks[k];
        this.attacks = new Attack[newAttacksLength];
        for (i = 0; i < newAttacksLength; i++)
            this.attacks[i] = newAttacks[i];
    }

    public boolean attackStart(int attackIndex) {
        int attackLostPoints = attacks[attackIndex].getAttackCost();
        if (attackPoints - attackLostPoints >= 0){
            attackPoints = attackPoints - attackLostPoints;
            actualAttackIndex = attackIndex;
            return true;
        }
        System.out.println("Not enough self energy : need " + attackLostPoints + ", but available " + attackPoints + ".");
        return false;
    }

    public int getEnemyDamage() {
        int energy = attacks[actualAttackIndex].getEnemyDamage() * attackFactor;
        attackFactor = 1;
        return energy;
    }

    public boolean setSelfHitDamage( int selfDamagePoints) {
        hitPoints = hitPoints - selfDamagePoints;
        if (hitPoints > 0 )
            return true;
        else return false;
    }
    public void setAddHitPoints(int hitPoints) {
        this.hitPoints = this.hitPoints + hitPoints;
        if (this.hitPoints > maxHitPoints)
            this.hitPoints = maxHitPoints;

    }

    public void setAddAttackPoints(int attackPoints) {
        this.attackPoints = this.attackPoints + attackPoints;
        if (this.attackPoints > maxAttackPoints)
            this.attackPoints = maxAttackPoints;

    }
    public int getHitPoints() {
        return hitPoints;
    }

    public int getAttackPoints() {
        return attackPoints;
    }
    public int getCurrentLevel() {
        return currentLevel;
    }
    public Attack[] getAttacks() {
        return attacks;
    }
    public void setAttackFactor(int attackFactor) {
        this.attackFactor = attackFactor;
    }
    public int getAttackFactor() {
        return attackFactor;
    }
}