/**
 * This class handles the overall game logic, responsible for initializing game components, managing
 * the game loop, and tracking the game state. It handles menu level player interactions, and oversee win/loss conditions.
 *
 */

import java.util.*;

public class Game {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private lovWorld world;
    private Market market;
    private boolean isGameOver;

    public Game() {
        this.heroes = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.market = new Market();
        this.isGameOver = false;
        setupHeroes(); // Initialize for the game
        spawnMonsters();
        this.world = new lovWorld(8, 8);
        world.initializeHeroesAndMonsters(heroes, monsters);
    }

    public void start() {
        System.out.println("Welcome to Legends of Valors!");

        // Main game loop with user-driven map movement
        while (!isGameOver) {
            world.updateBoard(heroes, monsters);// Update the board to reflect the positions of heroes and monsters
            world.displayMap();// Display the current state of the map
            startTurnBasedSystem(heroes, monsters, world);// Implement the turn-based system for heroes and monsters
            // Check if the game ended during the turn-based actions
            if (isGameOver) {
                System.out.println("Game Over. Thank you for playing!");
                break;
            }

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

    public void startTurnBasedSystem(List<Hero> heroes, List<Monster> monsters, lovWorld world) {
        int round = 1;

        while (!isGameOver) {
            System.out.println("\n--- Round " + round + " ---");

            // Heroes' Turn
            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    System.out.println("\nHero " + hero.getHeroIdentifier() + "'s turn:");
                    boolean inBattle = isHeroInBattle(hero, monsters);

                    // Define actions based on mode
                    List<String> actions = inBattle
                            ? Arrays.asList("Move", "Attack", "Use Potion", "Change Weapon/Armor", "Cast Spell")
                            : Arrays.asList("Move", "Use Potion", "Change Weapon/Armor", "Teleport", "Recall", "Shop");

                    // Display Available Actions
                    System.out.println("Available actions for " + hero.getName() + " (" + (inBattle ? "Battle Mode" : "Exploration Mode") + "):");
                    for (int i = 0; i < actions.size(); i++) {
                        System.out.println((i + 1) + ". " + actions.get(i));
                    }

                    // Handle Hero Action
                    boolean validAction = false;
                    while (!validAction) {
                        int action = InputHandler.getInstance().getIntInput("Enter the action number: ");

                        // Validate action index
                        if (action < 1 || action > actions.size()) {
                            System.out.println("Invalid action. Please enter a valid number.");
                            continue;
                        }

                        // Map action to functionality
                        String selectedAction = actions.get(action - 1);
                        switch (selectedAction) {
                            case "Move":
                                String direction = InputHandler.getInstance().getStringInput("Enter direction (w = up, a = left, s = down, d = right): ");
                                if (world.moveHero(hero, direction)) {
                                    validAction = true;
                                    world.updateBoard(heroes, monsters);
                                    world.displayMap();
                                }
                                break;

                            case "Attack":
                                if (inBattle) {
                                    Monster target = world.selectTargetMonster(hero, monsters);
                                    if (target != null) {
                                        int damage = hero.calculateDamage();
                                        hero.attack(target);
                                        System.out.println(hero.getName() + " attacked " + target.getName() + " for " + damage + " damage.");
                                        if (!target.isAlive()) {
                                            System.out.println(target.getName() + " has been defeated!");
                                        }
                                        validAction = true;
                                    } else {
                                        System.out.println("No target within attack range.");
                                    }
                                } else {
                                    System.out.println("Invalid action. Attack is only available in Battle Mode.");
                                }
                                break;

                            case "Use Potion":
                                world.usePotion(hero);
                                validAction = true;
                                break;

                            case "Change Weapon/Armor":
                                hero.getInventory().equipItem();
                                validAction = true;
                                break;

                            case "Cast Spell":
                                if (inBattle) {
                                    world.castSpell(hero);
                                    validAction = true;
                                } else {
                                    System.out.println("Invalid action. Casting spells is only available in Battle Mode.");
                                }
                                break;

                            case "Teleport":
                                System.out.println("\nChoose a hero you want to teleport to:");

                                List<Hero> teleportableHeroes = new ArrayList<>();
                                for (Hero target : heroes) {
                                    if (target != hero) {
                                        teleportableHeroes.add(target);
                                    }
                                }

                                for (int i = 0; i < teleportableHeroes.size(); i++) {
                                    System.out.println((i + 1) + ". " + teleportableHeroes.get(i).getName()+" (" + teleportableHeroes.get(i).getHeroIdentifier() + ")");
                                }

                                int heroIndex = InputHandler.getInstance().getIntInput("Enter the hero's number: ") - 1;
                                while (heroIndex < 0 || heroIndex >= teleportableHeroes.size()) {
                                    System.out.println("Invalid selection. Please try again.");
                                    heroIndex = InputHandler.getInstance().getIntInput("Enter the hero's number: ") - 1;
                                }

                                // Perform the teleport
                                Hero targetHero = teleportableHeroes.get(heroIndex);
                                if (world.teleportHero(hero, targetHero)) {
                                    validAction = true;
                                    world.updateBoard(heroes, monsters);
                                    world.displayMap();
                                }
                                break;

                            case "Recall":
                                if (world.recallHero(hero)) {
                                    validAction = true;
                                    world.updateBoard(heroes, monsters);
                                    world.displayMap();
                                }
                                break;
                            case "Shop":
                                if (world.isInNexus(hero)) {
                                    lovNexuesMarket(hero);
                                } else {
                                    System.out.println(hero.getName() + " is not in the nexus.");
                                }
                                break;

                            default:
                                System.out.println("Invalid action. Please enter a valid number.");
                                break;
                        }
                    }
                } else {
                    System.out.println(hero.getName() + " is not alive and cannot take a turn.");
                }
            }

            // Monsters' Turn
            System.out.println("\n--- Monsters' Turn ---");
            for (Monster monster : monsters) {
                if (monster.isAlive()) {
                    System.out.println(monster.getName() + "'s turn:");
                    world.moveMonster(monster, "s");
                    world.updateBoard(heroes, monsters);
                    world.displayMap();
                }
            }

            // Check win/loss conditions after each round
            if (world.checkWinCondition(heroes, monsters)) {
                isGameOver = true;
                break;
            }

            round++;
        }
    }


    // Check if a hero is in battle, i.e., if there are any monsters in attack range
    private boolean isHeroInBattle(Hero hero, List<Monster> monsters) {
        int heroRow = hero.getHeroRow();
        int heroCol = hero.getHeroCol();

        for (Monster monster : monsters) {
            int monsterRow = monster.getMonsterRow();
            int monsterCol = monster.getMonsterCol();

            // Check if a monster is in an adjacent space
            if (Math.abs(heroRow - monsterRow) <= 1 && Math.abs(heroCol - monsterCol) <= 1) {
                return true; // Hero is in battle
            }
        }
        return false; // No monsters nearby, hero is not in battle
    }

    private boolean checkWinCondition(List<Hero> heroes, List<Monster> monsters) {
        // Check if any monster has reached the heroes' Nexus
        for (Monster monster : monsters) {
            if (monster.getMonsterRow() == 7) {
                System.out.println("Monsters have reached the heroes' Nexus. Heroes lose!");
                isGameOver = true; // Set the flag to true to end the game
                return true;
            }
        }

        // Check if any hero has reached the monsters' Nexus
        for (Hero hero : heroes) {
            if (hero.getHeroRow() == 0) {
                System.out.println("Heroes have reached the monsters' Nexus. Heroes win!");
                isGameOver = true; // Set the flag to true to end the game
                return true;
            }
        }

        return false;
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
                    this.world = new lovWorld(8, 8);
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
        int partySize = 3;  // The player selects exactly 3 heroes for the game

        System.out.println("Choose your party of 3 heroes:");

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

            // Add a check to avoid adding duplicate heroes
            boolean isDuplicate = heroes.stream().anyMatch(existingHero -> existingHero.getName().equalsIgnoreCase(hero.getName()));
            if (isDuplicate) {
                System.out.println("Hero already chosen! Please select a different hero.");
                i--; // decrement to retry the selection
                continue;
            }

            // Assign an identifier to the hero (H1, H2, H3)
            hero.setHeroIdentifier("H" + (i + 1));
            System.out.println("Debug: Hero identifier assigned as: " + hero.getHeroIdentifier());

            // Add the hero to the party
            heroes.add(hero);
            System.out.println(hero.getName() + " has joined your party as " + hero.getHeroIdentifier() + ".");
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
        int counter = 1; // Counter for assigning identifiers

        // Assign a monster for each lane (assuming 3 heroes, therefore 3 lanes)
        for (int i = 0; i < heroes.size(); i++) {
            // Create a monster for the lane corresponding to each hero
            Monster monster = MonsterFactory.createMonster(heroes.get(i).getLevel());

            // Assign identifier M1, M2, etc.
            monster.setMonsterIdentifier("M" + counter++);

            // Set initial location for each monster in their respective lane
            monster.setMonsterRow(0); // Monsters start at row 0 (top Nexus)
            monster.setMonsterCol(i * 2 + 1); // Monsters start in the right cell of each lane's Nexus (e.g., col 1, col 4, col 7)

            // Add monster to the list
            monsters.add(monster);

            // Log to confirm spawning
            System.out.println(monster.getMonsterIdentifier() + " (" + monster.getName() + ") Lv: " + monster.getLevel() + " has spawned.");
        }
    }


    private void lovNexuesMarket(Hero hero) {
        System.out.println("\n" + hero.getName() + " is entering the market with " + hero.getGold() + " gold.");
        market.enterMarket(hero);
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