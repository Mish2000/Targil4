public class Main {
    public static void main(String[] args) {
        Game game = new Game();
       // System.out.println(game.toString());
        Pokemon[] players = game.getTwoPlayers();
        Battle battle = new Battle(players);
        boolean notWinner;
        do {
             notWinner = battle.oneLoop();
        }while (notWinner);
     }
}