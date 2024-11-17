/**
 * This class handles the overall game logic, responsible for initializing game components, managing
 * the game loop, and tracking the game state. It handles menu level player interactions, and oversee win/loss conditions.
 *
 */

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private World world;
    private Market market;
    private boolean isGameOver;

    public Game() {
        this.heroes = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.world = new World(8, 8);
        this.market = new Market();
        this.isGameOver = false;
    }

    public void start() {
        System.out.println("                                                               .---.\n" +
                "                                                              /  .  \\\n" +
                "                                                             |\\_/|   |\n" +
                "                                                             |   |  /|\n" +
                "  .----------------------------------------------------------------' |\n" +
                " /  .-.                                                              |\n" +
                "|  /   \\                                                             |\n" +
                "| |\\_.  |                        _                                   |\n" +
                "|\\|  | /|   /\\/\\   ___  _ __  ___| |_ ___ _ __                       |\n" +
                "| `---' |  /    \\ / _ \\| '_ \\/ __| __/ _ \\ '__|                      |\n" +
                "|       | / /\\/\\ \\ (_) | | | \\__ \\ ||  __/ |                         |\n" +
                "|       | \\/    \\/\\___/|_| |_|___/\\__\\___|_|                         |\n" +
                "|       |                                                            |\n" +
                "|       |   __ _ _ __   __| |   /\\  /\\___ _ __ ___   ___  ___        |\n" +
                "|       |  / _` | '_ \\ / _` |  / /_/ / _ \\ '__/ _ \\ / _ \\/ __|       |\n" +
                "|       | | (_| | | | | (_| | / __  /  __/ | | (_) |  __/\\__ \\       |\n" +
                "|       |  \\__,_|_| |_|\\__,_| \\/ /_/ \\___|_|  \\___/ \\___||___/       |\n" +
                "|       |                                                            | \n" +
                "|       |                                                           /\n" +
                "|       |----------------------------------------------------------'\n" +
                "\\       |\n" +
                " \\     /\n" +
                "  `---'");

        setupHeroes(); // Initialize heroes for the game

        // Main game loop with user-driven map movement
        while (!isGameOver) {
            world.displayMap(); // Display current map state
            System.out.println("Use W/A/S/D for movement, I for info, M for market, and Q to quit.");
            String command = InputHandler.getInstance().getCommand().toLowerCase();

            switch (command) {
                case "w":
                case "a":
                case "s":
                case "d":
                    if(!attemptMove(command)){ break; }

                    if (world.isInMarketSpace()) {
                        System.out.println("You see a marketplace. Press M to enter it and enter anything else to quit.");
                        command = InputHandler.getInstance().getCommand().toLowerCase();
                        if (command.equals("m")) {
                            enterMarket();
                        } else {
                            System.out.println("You left the marketplace.");
                        }
                        break;
                    } else if (world.isInCommonSpace()) {
                        boolean chanceOfBattle = Math.random() < 0.5; // 50% chance for battle
                        if (chanceOfBattle) {
                            System.out.println("You encountered monsters!");
                            explore();
                        } else {
                            System.out.println("Your teams spot no sign of monsters in this area...");
                        }
                        break;
                    }
                    break;
                case "q":
                    isGameOver = true;
                    System.out.println("Thank you for playing!");
                    break;
                case "i":
                    showHeroStats();
                    break;
                default:
                    System.out.println("Invalid command. Use W/A/S/D for movement, I for info, M for market, and Q to quit.");
                    break;
            }
        }
    }

    private boolean attemptMove(String direction) {
        // Attempt the move in the specified direction
        if (!world.moveParty(direction)) {
            // Check if the hero is surrounded by inaccessible spaces
            boolean choosed = false;
            while (!choosed) {
                System.out.println("Are you surrounded by inaccessible spaces? Choosing yes will move you into a new map. y/n");
                String choice = InputHandler.getInstance().getCommand().toLowerCase();

                if (choice.equals("y")) {
                    System.out.println("Your heroes were sucked into a portal to another dimension, where more adventures await...(New Map)");
                    this.world = new World(8, 8);
                    world.displayMap();
                    choosed = true;
                } else if (choice.equals("n")) {
                    choosed = true;
                } else {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            }
            return false;
        }return true;
    }

    private void setupHeroes() {
        InputHandler inputHandler = InputHandler.getInstance();
        int partySize = 0;
        boolean valid = false;

        while (!valid) {
            partySize = inputHandler.getIntInput("Choose your party size (1-3): ");
            if (partySize >= 1 && partySize <= 3) {
                valid = true;
            } else {
                System.out.println("Invalid input. Please enter a valid party size (1-3).");
            }
        }

        for (int i = 0; i < partySize; i++) {
            System.out.println("Choose a class for hero " + (i + 1) + ":");
            System.out.println("1. Warrior\n2. Sorcerer\n3. Paladin");

            int classChoice = inputHandler.getIntInput("Enter your choice: ");
            Hero hero;

            switch (classChoice) {
                case 1:
                    hero = HeroFactory.createHero("Warrior");
                    break;
                case 2:
                    hero = HeroFactory.createHero("Sorcerer");
                    break;
                case 3:
                    hero = HeroFactory.createHero("Paladin");
                    break;
                default:
                    System.out.println("Invalid choice, selecting default hero (Warrior).");
                    hero = HeroFactory.createHero("Warrior");
            }

            heroes.add(hero);
            System.out.println(hero.getName() + " has joined your party.");
        }
    }

    private void explore() {
        spawnMonsters();
        Battle battle = new Battle(heroes, monsters);
        boolean heroesWon = battle.startBattle();

        if (!heroesWon) {
            isGameOver = true;
            System.out.println("All of your heroes have been defeated. Game over.");
        }
    }

    private void spawnMonsters() {
        monsters.clear();
        for (Hero hero : heroes) {
            Monster monster = MonsterFactory.createMonster(hero.getLevel());
            monsters.add(monster);
            System.out.println(monster.getName() + " Lv: " + monster.level + " has spawned.");
        }
    }

    private void enterMarket() {
        List<Hero> availableHeroes = new ArrayList<>(heroes);

        while (!availableHeroes.isEmpty()) {
            System.out.println("Choose a hero to enter the market:");
            for (int i = 0; i < availableHeroes.size(); i++) {
                System.out.println((i + 1) + ". " + availableHeroes.get(i).getName() + " (Gold: " + availableHeroes.get(i).getGold() + ")");
            }

            int choice = InputHandler.getInstance().getIntInput("Enter the hero's number to enter market: ") - 1;
            if (choice >= 0 && choice < availableHeroes.size()) {
                Hero selectedHero = availableHeroes.get(choice);
                System.out.println("\n" + selectedHero.getName() + " is entering the market with " + selectedHero.getGold() + " gold.");
                market.enterMarket(selectedHero);

                availableHeroes.remove(choice);  // Remove hero after market visit

                if (!availableHeroes.isEmpty()) {
                    System.out.println("Do other heroes want to shop as well? (y/n): ");
                    String continueShopping = InputHandler.getInstance().getCommand().toLowerCase();
                    if (!continueShopping.equals("y")) {
                        break;
                    }
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showHeroStats() {
        // Display heroes with indices for selection
        System.out.println("\nChoose a hero:");
        for (int i = 0; i < heroes.size(); i++) {
            System.out.println((i + 1) + ". " + heroes.get(i).getName());
        }

        int heroIndex = InputHandler.getInstance().getIntInput("Enter the hero's number: ") - 1;
        while (heroIndex < 0 || heroIndex >= heroes.size()) {
            System.out.println("Invalid selection. Please try again.");
            heroIndex = InputHandler.getInstance().getIntInput("Enter the hero's number: ") - 1;
        }

        Hero selectedHero = heroes.get(heroIndex);

        // Choose to view stats or inventory
        System.out.println("\n1. View Hero Stats\n2. View Inventory");
        String command = InputHandler.getInstance().getCommand().toLowerCase();

        switch (command) {
            case "1":
            case "view hero stats":
                displayHeroStats(selectedHero);
                break;
            case "2":
            case "view inventory":
                displayInventory(selectedHero);
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    private void displayHeroStats(Hero hero) {
        System.out.println(hero);
        hero.printExperienceProgress();
    }

    private void displayInventory(Hero hero) {
        System.out.println("\n" + hero.getName() + "'s Inventory:");
        hero.getInventory().showItems();

        System.out.println("\nWould you like to equip or unequip an item?");
        System.out.println("1. Equip Item\n2. Unequip Item\n3. Exit");
        int choice = InputHandler.getInstance().getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                hero.getInventory().equipItem();
                break;
            case 2:
                hero.getInventory().unequipItem();
                break;
            case 3:
                System.out.println("Exiting inventory view.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

}