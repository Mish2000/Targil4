import java.util.Random;
import java.util.Scanner;

public class Battle {

    private final String[] PLAYER_NAME = {"First Player: ", "Second Player: "};
    private Pokemon[] players;
    private static final int[] ACTIVE_INDEX = {0, 1};
    private static final int[] PASSIVE_INDEX = {1, 0};
    private static final String[] STANDBY_TITLES = {" hit points added to ", " attack points added to .", "Tripled attack power next turn "};
    private static final int[] STANDBY_HIT_POINTS_BOUNDS = {5, 31};
    private static final int[] STANDBY_ATTACK_POINTS_BOUNDS = {0, 31};
    private static final int ATTACK_FACTOR = 3;
    private static final int[] NEXT_TURN_HIT_ADD = {0, 5};
    private static final int[] NEXT_TURN_ENERGY_ADD = {0, 5};

    //Time complexity is O(n).
    public Battle(Pokemon[] players) {
        this.players = new Pokemon[players.length];
        System.out.println("****** Battle ******");
        for (int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
            System.out.println(PLAYER_NAME[i] + this.players[i].toString());
        }
    }

    //Time complexity is O(n^2).
    public boolean oneLoop() {
        for (int i = 0; i < 2; i++) {
            int stepMode;
            players[i].nextTurn(NEXT_TURN_HIT_ADD, NEXT_TURN_ENERGY_ADD);
            do {
                printStatus("=== Status before new step ===");
                System.out.println("Now active " + getPlayerTitle(ACTIVE_INDEX[i]) + ".\n" +
                        "Insert one of the following  step options number:");
                System.out.println("1. attack\n2. standby\n3. evaluation\n4. special action\n");
                do {
                    stepMode = checkDigAnswer(1, 4);
                    if (stepMode < 0)
                        System.out.println("Invalid step mode. Try again");
                } while (stepMode < 0);
            } while (!isOneStep(ACTIVE_INDEX[i], PASSIVE_INDEX[i], stepMode));
            boolean loserFound = searchForLoser(ACTIVE_INDEX[i], PASSIVE_INDEX[i]);
            if (loserFound) {
                printStatus("*** Game over ***");
                return false;
            }
            printStatus("--- Status after step ---");
        }
        return true;
    }

    //Time complexity is O(n).
    private void printStatus(String title) {
        System.out.println(title);
        for (int j = 0; j < 2; j++) {
            int hit = players[j].getHitPoints();
            if (hit < 0)
                hit = 0;
            System.out.println(getPlayerTitle(j) + ", level " + players[j].getCurrentLevel() +
                    " ,Hit max points " + hit +
                    " , Attack max points " + players[j].getAttackPoints() + ".");
        }
        System.out.println("---------------");
    }

    //Time complexity is O(n).
    private boolean searchForLoser(int i1, int i2) {
        int[] y = {i1, i2};
        boolean result = false;
        for (int i = 0; i < 2; i++) {
            int hit = players[y[i]].getHitPoints();
            if (hit < 0) {
                result = true;
                System.out.println(PLAYER_NAME[y[i]] + " lost");
            }
        }
        return result;
    }

    //Time complexity is O(1)(no loops).
    private boolean isOneStep(int i1, int i2, int stepMode) {
        switch (stepMode) {
            case 1 -> {
                if (attackStep(i1, i2))
                    return true;
                System.out.println("Mission failed");
                return false;
            }
            case 2 -> {
                standbyStep(i1);
                return true;
            }
            case 3 -> {
                return evaluationStep(i1);
            }
            case 4 -> {
                return specialActionStep(i1, i2);
            }
            default -> {
                return true;
            }
        }
    }

    //Time complexity is O(1)(no loops).
    protected boolean specialActionStep(int i1, int i2) {
        boolean needNextAction = players[i1].specialAction();
        System.out.println(getPlayerTitle(i1) + ".");
        if (needNextAction) {
            int points = players[i1].getSpecialEnemyDamage();
            players[i2].setSelfHitDamage(points);
        }
        return players[i1].isActionSuccess();
    }

    //Time complexity is O(1)(no loops).
    private boolean evaluationStep(int iActive) {
        boolean isEvaluation = players[iActive].evaluationPokemon();
        String string = getPlayerTitle(iActive);
        if (isEvaluation)
            System.out.println(string + " successful evaluation.");
        else
            System.out.println(string + " evaluation impossible ");
        return isEvaluation;
    }

    //Time complexity is O(1)(no loops).
    private void standbyStep(int iActive) {
        Random random = new Random();
        int stepOption = random.nextInt(0, STANDBY_TITLES.length);
        switch (stepOption) {
            case 0 -> {
                int hit = random.nextInt(STANDBY_HIT_POINTS_BOUNDS[0], STANDBY_HIT_POINTS_BOUNDS[1]);
                players[iActive].setAddHitPoints(hit);
                System.out.println(hit + STANDBY_TITLES[0] + PLAYER_NAME[iActive] + ".");
            }
            case 1 -> {
                int attackPoints = random.nextInt(STANDBY_ATTACK_POINTS_BOUNDS[0], STANDBY_ATTACK_POINTS_BOUNDS[1]);
                players[iActive].setAddAttackPoints(attackPoints);
                System.out.println(attackPoints + STANDBY_TITLES[1] + PLAYER_NAME[iActive] + ".");
            }
            case 2 -> {
                players[iActive].setAttackFactor(ATTACK_FACTOR);
                System.out.println("To " + PLAYER_NAME[iActive] + "tripled attack power next turn");
            }
        }
    }

    //Time complexity is O(n).
    private boolean attackStep(int i1, int i2) {
        Attack[] attacks = players[i1].getAttacks();
        System.out.println("Attack power factor: " + players[i1].getAttackFactor() + ". Choose attack number from possible attacks list:");
        int i;
        for (i = 0; i < attacks.length; i++) {
            System.out.println((i + 1) + ". " + attacks[i]);
        }
        System.out.println((i + 1) + ". " + players[i1].getKickAttackTitle());

        int attackNumber;
        do {
            attackNumber = checkDigAnswer(1, attacks.length + 1);
            if (attackNumber < 0)
                System.out.println("Invalid attack number. Try again");
        } while (attackNumber < 0);

        int enemyDamage;

        if (attackNumber == attacks.length + 1) {
            enemyDamage = players[i1].getKickPoints();
            players[i2].kick();
        } else {
            boolean successStart = players[i1].attackStart(attackNumber - 1);
            if (!successStart) return false;
            enemyDamage = players[i1].getEnemyDamage();
            players[i2].setSelfHitDamage(enemyDamage);
        }
        int attackHitPoints = players[i1].getAttackSelfHitDamage();
        players[i1].setSelfHitDamage(attackHitPoints);
        System.out.println("Attacker self hit points damage: " + attackHitPoints + ".");
        System.out.println("Attacked " + players[i2].getName() + " hit damage " + enemyDamage);
        return true;
    }

    //Time complexity is O(1)(no loops).
    private int checkDigAnswer(int minA, int maxA) {
        Scanner scanner = new Scanner(System.in);
        boolean isInt = scanner.hasNextInt();
        int answer;
        if (!isInt) {
            scanner.nextLine();
            return -1;
        }
        answer = scanner.nextInt();
        if (answer < minA || answer > maxA)
            answer = -1;
        return answer;
    }

    //Time complexity is O(1)(no loops).
    private String getPlayerTitle(int playerIndex) {
        return PLAYER_NAME[ACTIVE_INDEX[playerIndex]] + players[ACTIVE_INDEX[playerIndex]].getTypeName() +
                " - " + players[playerIndex].getName();
    }

}