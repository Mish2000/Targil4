import java.util.Random;
public class Game {
    private static final String[] POKEMON_NAMES = {"Pikachu", "Blitzle", "Electabuzz",
            "Charmander", "Salandit", "Moltress"};
    private static int FIRE = 2;
    private static int ELECTRIC = 1;
    private static final int[] POKEMON_TYPES = {ELECTRIC,ELECTRIC,ELECTRIC,FIRE,FIRE,FIRE};
    private static final int[] POKEMON_EVOLUTIONS_NUMBER = {3,2,2,3,2,1};
    private static final String[] EVOLUTIONS_NAMES = {"Pichu", "Pikachu", "Riachu", "Blizle", "Zebstrika", "Electabuzz", "Electivire",
            "Charmander", "Charmeleon", "Charizard", "Salandit", "Salazzle", "Moltres"};
    private static final int[][] POKEMONS_EVOLUTIONS_PARAMETERS ={{1,40,30}, {2,50,40},{3,160,80}, //level, hit points, attack points
            {1,90,35}, {2,100,50},{1,30,100}, {2,35,120},
            {1,80,40}, {2,90,60}, {3,130,80},{1,100,60}, {2,160,80},
            {1,120,60}};

    private static final int[] EVOLUTIONS_ATTACKS_NUMBER = {1,1,1,1,1,1,1,1,1,1,1,1,2};
    private static final String[] EVOLUTIONS_ATTACKS_NAMES = {"Quick Attack", "Electro Ball", "Electric Surfer",
        "Flop", "Zap Kick", "Thunder", "Thunder Punch",
            "Scratch", "Flame Tail", "Fiery Blast", "Live Coal", "Fire Claws", "Assisting Heater", "Fire Wing"};
    private static final int[][]  EVOLUTIONS_ATTACKS_PARAMETERS = {{5,10,10},{10,30,40},{60,20,120}, //price, enemy damage (min,max)
            {20,20,25}, {30,30,35},{60,40,50}, {80,50,120},
            {15,25,30}, {40,30,50}, {50,50,50},{10,0,25}, {25,0,50},
            {30,10,60}, {30,30,30}};
    private Pokemon[] allPokemons;
    //Time complexity is O(1)(no loops).
    public Game(){
        buildPokemons();
    }

    //Time complexity is O(1)(no loops).
    public  String toString() {
        String out = "";
        out = out + "All Pokemons number: " + allPokemons.length + "\n";
        for (int i=0; i<allPokemons.length; i++)
        out = out + allPokemons[i].toString();
        return out;
    }
    //Time complexity is O(n*k*m),while n=POKEMON_NAMES.length,
    // k=POKEMON_EVOLUTIONS_NUMBER and m=EVOLUTIONS_ATTACKS_NUMBER.
    //The first loop does not affect the complexity of the function because it
    //is a very small number of operations comparing to the other loops.
    public Pokemon[] getTwoPlayers() {
        Pokemon[] playerPokemons = new Pokemon[2];
        Random random = new Random();
        int playerIndex;
        for (int i = 0; i<2; i++) {
            playerIndex = random.nextInt(0, POKEMON_NAMES.length);
            int PokemonType = allPokemons[playerIndex].getType();
            if (PokemonType == ELECTRIC)
                playerPokemons[i] = new ElectricPokemon(allPokemons[playerIndex]);
            else playerPokemons[i] = new FirePokemon(allPokemons[playerIndex]);
            playerPokemons[i].gameStart();
        }
        return playerPokemons;
    }
    //Time complexity is O(n*k*m),while n=POKEMON_NAMES.length,
    // k=POKEMON_EVOLUTIONS_NUMBER and m=EVOLUTIONS_ATTACKS_NUMBER.
    //The first loop does not affect the complexity of the function because it
    //is a very small number of operations comparing to the other loops.
    private void buildPokemons() {
        int electPokemonsNumber = 0;

        for (int i=0; i<POKEMON_TYPES.length; i++) {
            if (POKEMON_TYPES[i] == ELECTRIC)
                electPokemonsNumber++;
        }
        int firePokemonNumber = POKEMON_TYPES.length - electPokemonsNumber;
        allPokemons = new Pokemon[POKEMON_NAMES.length];

        int evolveIndex = 0;
        int modesIndex = 0;
        int allPokemonsIndex = 0;

        for (int i=0; i<POKEMON_NAMES.length; i++) {
            Evolution[] evolutions = new Evolution[POKEMON_EVOLUTIONS_NUMBER[i]];
            for (int j = 0; j < POKEMON_EVOLUTIONS_NUMBER[i]; j++, evolveIndex++) {
                int modesNumber = EVOLUTIONS_ATTACKS_NUMBER[evolveIndex];
                Attack[] attacks = new Attack[modesNumber];
                for (int k = 0; k < modesNumber; k++, modesIndex++) {
                    attacks[k] = new Attack(EVOLUTIONS_ATTACKS_NAMES[modesIndex], EVOLUTIONS_ATTACKS_PARAMETERS[modesIndex][0], EVOLUTIONS_ATTACKS_PARAMETERS[modesIndex][1],
                            EVOLUTIONS_ATTACKS_PARAMETERS[modesIndex][2]);
                }
                evolutions[j] = new Evolution(EVOLUTIONS_NAMES[evolveIndex], POKEMONS_EVOLUTIONS_PARAMETERS[evolveIndex][0],
                        POKEMONS_EVOLUTIONS_PARAMETERS[evolveIndex][1], POKEMONS_EVOLUTIONS_PARAMETERS[evolveIndex][2],  attacks);
            }
            if (POKEMON_TYPES[i] == FIRE)
                    allPokemons[allPokemonsIndex++] = new FirePokemon(evolutions[0].getName(), POKEMON_TYPES[i], evolutions[0].getLevel(),
                            evolutions[0].getHitPoints(), evolutions[0].getAttackPoints(), evolutions[0].getAttacks(),
                            evolutions);
            else
                    allPokemons[allPokemonsIndex++] = new ElectricPokemon(evolutions[0].getName(), POKEMON_TYPES[i], evolutions[0].getLevel(),
                            evolutions[0].getHitPoints(), evolutions[0].getAttackPoints(),
                            evolutions[0].getAttacks(),
                            evolutions);
        }
    }
}
