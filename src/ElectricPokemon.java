public class ElectricPokemon extends Pokemon {
    private static final double TURN_CHARGING = 0.05;  //5%
    private static final double MIN_ENERGY_POINTS_PERCENT = 0.2;  //20%

    private int dischargedPokemonLevel ;
    private boolean fullRefreshFlag;
    private double energyPusher;
    private boolean actionSuccessFlag;
    public ElectricPokemon(String name, int type, int currentLevel, int hitPoints, int attackPoints, Attack[] attacks, Evolution[] evolutions) {
        super(name, type, currentLevel, hitPoints, attackPoints, attacks, evolutions);
        fullRefreshFlag = true;
        energyPusher = 0;
        dischargedPokemonLevel =(int)( hitPoints * MIN_ENERGY_POINTS_PERCENT);
    }

    public ElectricPokemon(Pokemon inPokemon) {
        super(inPokemon);
        fullRefreshFlag = true;
        energyPusher = 0;
        dischargedPokemonLevel = (int)(hitPoints * MIN_ENERGY_POINTS_PERCENT);
    }

    public boolean setSelfHitDamage( int selfDamagePoints) {
        boolean status = super.setSelfHitDamage(selfDamagePoints);
        if (hitPoints <  dischargedPokemonLevel)
            energyPusher = 0;
        return status;
    }
    public int getEnemyDamage() {
        int energy = super.getEnemyDamage();
        energy = energy + (int)(energy*energyPusher);
        return energy;
    }

    public boolean evaluationPokemon() {
        boolean status = super.evaluationPokemon();
        if (status) {
            fullRefreshFlag = true;
            energyPusher = 0;
            dischargedPokemonLevel = (int) (hitPoints * MIN_ENERGY_POINTS_PERCENT);
        }
        return status;
    }

    private void pointsRefresh() {
        attackPoints = maxAttackPoints;
        hitPoints = maxHitPoints;
    }
    public int getAttackSelfHitDamage() {
        return 0;
    }
    public  String getTypeName() {
        return "Electric_pokemon";
    }

    public void nextTurn(int[] hitAdd, int[] energyAdd) {
        energyPusher = energyPusher + TURN_CHARGING;
        super.nextTurn(hitAdd,  energyAdd);
    }


    public boolean specialAction() {
        if (fullRefreshFlag)  {
            pointsRefresh();
            System.out.println(("Set max values: hit points " + hitPoints + ", attack points " + attackPoints + "."));
            fullRefreshFlag = false;
            actionSuccessFlag = true;
        }
        else {
            System.out.println(("Maximum points are not set. You have already used this feature."));
            actionSuccessFlag = false;
        }
        return false;
    }
    public int getSpecialEnemyDamage(){
        return 0;
    }
    public boolean isActionSuccess() {
        return actionSuccessFlag;
    }

}