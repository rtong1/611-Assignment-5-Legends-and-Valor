import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class lovWorld extends World {
    private List<Hero> heroes;
    private List<Monster> monsters;

    public lovWorld(int row, int col) {
        super(row, col);
        initializeSpaces();
        System.out.println("lovWorld initialized with custom board setup!");
    }

    @Override
    public void displayMap() {
        System.out.println("╔════╦════╦════╦════╦════╦════╦════╦════╗");

        for (int i = 0; i < grid.length; i++) {
            System.out.print("║");
            for (int j = 0; j < grid[i].length; j++) {
                Space space = grid[i][j];
                if (space != null && space.getOccupant() != null) {
                    System.out.print(" " + space.getOccupant().getSymbol());
                    if (space.getOccupant().getSymbol().length() < 2) {
                        System.out.print("  ║");
                    } else {
                        System.out.print(" ║");
                    }
                } else if (space != null) {
                    System.out.print(" " + space.getSymbol());
                    if (space.getSymbol().length() < 2) {
                        System.out.print("  ║");
                    } else {
                        System.out.print(" ║");
                    }

                } else {
                    System.out.print("   ║");  // Empty space
                }
            }
            System.out.println();
            if (i < grid.length - 1) {
                System.out.println("╠════╬════╬════╬════╬════╬════╬════╬════╣");
            }
        }

        System.out.println("╚════╩════╩════╩════╩════╩════╩════╩════╝");
    }



    protected void initializeSpaces() {
        // Create a 8x8 grid
        grid = new Space[8][8];

        // Set Nexus spaces for heroes and monsters
        for (int j = 0; j < 8; j++) {
            grid[0][j] = new NexusSpace("Monster Nexus");  // Top row for monsters
            grid[7][j] = new NexusSpace("Hero Nexus");     // Bottom row for heroes
        }

        // Set lanes and walls
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 2 || j == 5) {
                    grid[i][j] = new InaccessibleSpace();  // Walls separating lanes
                } else {
                    grid[i][j] = createRandomTerrainSpace();  // Randomly assign terrain in lanes
                }
            }
        }

        // Update specific cells to be inaccessible as per user request
        grid[0][2] = new InaccessibleSpace(); // Third cell from the left in the top row
        grid[0][5] = new InaccessibleSpace(); // Sixth cell from the left in the top row
        if (grid[0][5].getOccupant() != null) {
            grid[0][5].setOccupant(null); // Clear the previous occupant if necessary
        }
        grid[7][2] = new InaccessibleSpace(); // Third cell from the left in the bottom row
        grid[7][5] = new InaccessibleSpace(); // Sixth cell from the left in the bottom row
        if (grid[7][5].getOccupant() != null) {
            grid[7][5].setOccupant(null); // Clear the previous occupant if necessary
        }

    }

    public void initializeHeroesAndMonsters(List<Hero> heroes, List<Monster> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;

        // Setting up monsters in the top row: M1, M1, inaccessible, M2, M2, inaccessible, M3, M3
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            monster.setMonsterIdentifier("M" + (i + 1));

            // Set initial monster positions, occupying two consecutive cells
            int col = i * 3; // Each monster takes two spaces, with inaccessible space between groups
            setMonsterLocation(monster, 0, col);       // First space for the monster
            setMonsterLocation(monster, 0, col + 1);   // Second space for the monster

            // Update the grid to reflect the monsters' positions
            grid[0][col].setOccupant(monster);
            grid[0][col + 1].setOccupant(monster);
        }

        // Setting up heroes in the bottom row: H1, H1, inaccessible, H2, H2, inaccessible, H3, H3
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            hero.setHeroIdentifier("H" + (i + 1));

            // Set initial hero positions, occupying two consecutive cells
            int col = i * 3; // Each hero takes two spaces, with inaccessible space between groups
            setHeroLocation(hero, 7, col);       // First space for the hero
            setHeroLocation(hero, 7, col + 1);   // Second space for the hero

            hero.setNexus(7, col + 1);
            // Update the grid to reflect the heroes' positions
            grid[7][col].setOccupant(hero);
            grid[7][col + 1].setOccupant(hero);
        }

        // Set inaccessible spaces between the heroes and monsters
        grid[0][2] = new InaccessibleSpace(); // Third cell from the left in the top row
        grid[0][5] = new InaccessibleSpace(); // Sixth cell from the left in the top row
        grid[7][2] = new InaccessibleSpace(); // Third cell from the left in the bottom row
        grid[7][5] = new InaccessibleSpace(); // Sixth cell from the left in the bottom row
    }





    // Helper method to create random terrain spaces (Bush, Cave, Koulou, Plain)
    private Space createRandomTerrainSpace() {
        int rand = new Random().nextInt(100);
        if (rand < 20) {
            return new BushSpace();
        } else if (rand < 40) {
            return new CaveSpace();
        } else if (rand < 60) {
            return new KoulouSpace();
        } else {
            return new PlainSpace();
        }
    }

    private void removeTerrainEffect(Hero hero, Space space) {
        if (space instanceof BushSpace) {
            hero.increaseDexterity(-10);  // Remove dexterity boost from Bush space
            System.out.println(hero.getName() + " loses a dexterity boost from the Bush.");
        } else if (space instanceof CaveSpace) {
            hero.increaseAgility(-10);  // Remove agility boost from Cave space
            System.out.println(hero.getName() + " loses an agility boost from the Cave.");
        } else if (space instanceof KoulouSpace) {
            hero.increaseStrength(-10);  // Remove strength boost from Koulou space
            System.out.println(hero.getName() + " loses a strength boost from the Koulou.");
        }
    }



    private boolean setHeroLocation(Hero hero, int targetRow, int targetCol) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        hero.setHeroRow(targetRow);
        hero.setHeroCol(targetCol);
        grid[targetRow][targetCol].setOccupant(hero);
        return true;
    }

    private boolean setMonsterLocation(Monster monster, int targetRow, int targetCol) {
        if (monster == null) {
            System.out.println("Invalid Monster");
            return false;
        }

        monster.setMonsterRow(targetRow);
        monster.setMonsterCol(targetCol);
        grid[targetRow][targetCol].setOccupant(monster);
        return true;
    }

    public void castSpell(Hero hero) {
        List<Item> spells = hero.getInventory().getItems().stream()
                .filter(item -> item instanceof Spell)
                .collect(Collectors.toList());

        if (spells.isEmpty()) {
            System.out.println("No spells in inventory.");
            return;
        }

        System.out.println("Select a spell to cast:");
        for (int i = 0; i < spells.size(); i++) {
            System.out.println((i + 1) + ". " + spells.get(i).getName());
        }

        int choice = InputHandler.getInstance().getIntInput("Enter the index of the spell you'd like to cast: ");
        if (choice > 0 && choice <= spells.size()) {
            Spell spell = (Spell) spells.get(choice - 1);
            if (hero.getCurrentMana() >= spell.getManaCost()) {
                Monster target = selectTargetMonster(hero, monsters);
                if (target != null) {
                    spell.use(hero);
                    target.takeDamage(spell.getDamage());
                    System.out.println(hero.getName() + " cast " + spell.getName() + " on " + target.getName() + " dealing " + spell.getDamage() + " damage.");
                    if (!target.isAlive()) {
                        System.out.println(target.getName() + " has been defeated!");
                    }
                }
            } else {
                System.out.println("Not enough mana.");
            }
        } else {
            System.out.println("Invalid spell selection.");
        }
    }


    public void usePotion(Hero hero) {
        List<Item> potions = hero.getInventory().getItems().stream()
                .filter(item -> item instanceof Potion)
                .collect(Collectors.toList());

        if (potions.isEmpty()) {
            System.out.println("No potions available in inventory.");
            return;
        }

        System.out.println("Select a potion to use:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ". " + potions.get(i).getName());
        }

        int choice = InputHandler.getInstance().getIntInput("Enter the index of the potion you'd like to use: ");
        if (choice > 0 && choice <= potions.size()) {
            Potion selectedPotion = (Potion) potions.get(choice - 1);
            selectedPotion.use(hero);
            System.out.println(hero.getName() + " used " + selectedPotion.getName() + ".");
        } else {
            System.out.println("Invalid potion selection.");
        }
    }



    public Monster selectTargetMonster(Hero hero, List<Monster> monsters) {
        System.out.println("Select a monster to attack:");

        // Filter and display only alive monsters within range
        List<Monster> monstersInRange = getMonstersInRange(hero, monsters);

        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters are within attack range.");
            return null;
        }

        for (int i = 0; i < monstersInRange.size(); i++) {
            Monster monster = monstersInRange.get(i);
            System.out.println((i + 1) + ". " + monster.getName() + " (HP: " + monster.getCurrentHealth() + ")");
        }

        int choice = InputHandler.getInstance().getIntInput("Enter the index of the monster you'd like to attack: ");
        if (choice > 0 && choice <= monstersInRange.size()) {
            Monster target = monstersInRange.get(choice - 1);
            if (target.isAlive()) {
                return target;
            }
        }
        System.out.println("Invalid selection or target is already defeated.");
        return null;
    }





    public boolean moveHero(Hero hero, String direction) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        int currentRow = hero.getHeroRow();
        int currentCol = hero.getHeroCol();

        int[] newPosition = calculateNewPosition(currentRow, currentCol, direction);
        if (newPosition == null) {
            System.out.println("Invalid direction.");
            return false; // Invalid direction
        }

        int newRow = newPosition[0];
        int newCol = newPosition[1];

        if (isValidMove(newRow, newCol)) {
            // Remove current terrain effect before moving
            removeTerrainEffect(hero, grid[currentRow][currentCol]);

            // Set new location for the hero
            setHeroLocation(hero, newRow, newCol);
            grid[newRow][newCol].setOccupant(hero);

            // Apply terrain effect after moving
            applyTerrainEffect(hero, grid[newRow][newCol]);

            System.out.println("Moved " + hero.getName() + " " + direction + ".");
            return true;
        } else {
            System.out.println("Move failed: Invalid position.");
            return false;
        }
    }

    public boolean teleportHero(Hero teleportingHero, Hero targetHero) {
        if (teleportingHero == null || targetHero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        int targetRow = targetHero.getHeroRow();
        int targetCol = targetHero.getHeroCol();
        int teleportingHeroRow = teleportingHero.getHeroRow();
        int teleportingHeroCol = teleportingHero.getHeroCol();

        if (Math.abs(teleportingHeroCol - targetCol) <= 1) {
            System.out.println("Teleport failed: Must teleport to a different lane.");
            return false;
        }

        // Remove terrain effect from current space
        removeTerrainEffect(teleportingHero, grid[teleportingHeroRow][teleportingHeroCol]);


        // Define possible adjacent positions around the target hero
        int[][] adjacentPositions = {
                {targetRow + 1, targetCol}, // Below
                {targetRow, targetCol - 1}, // Left
                {targetRow, targetCol + 1}  // Right
        };

        // Attempt teleport to any valid adjacent position
        for (int[] pos : adjacentPositions) {
            int newRow = pos[0];
            int newCol = pos[1];

            if (isValidMoveNoPrint(newRow, newCol) && (newRow!=teleportingHeroRow || newCol!=teleportingHeroCol)) {
                setHeroLocation(teleportingHero, newRow, newCol);
                System.out.println("Teleport successful.");
                applyTerrainEffect(teleportingHero, grid[newRow][newCol]); // Apply terrain effect after teleporting
                return true;
            }
        }

        System.out.println("Teleport failed: No valid adjacent space.");
        return false;
    }

    public boolean recallHero(Hero hero) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        int[] nexus = hero.getNexus();
        if (!setHeroLocation(hero, nexus[0], nexus[1])) {
            System.out.println("Recall failed.");
            return false;
        }
        System.out.println("Recalling " + hero.getName());
        applyTerrainEffect(hero, grid[nexus[0]][nexus[1]]); // Apply terrain effect after recall
        return true;
    }


    // Apply terrain effects to the hero when they move to a new space
    private void applyTerrainEffect(Hero hero, Space space) {
        if (space instanceof BushSpace) {
            hero.increaseDexterity(10);  // Increase dexterity by 10 in Bush space
            System.out.println(hero.getName() + " gains a dexterity boost in the Bush.");
        } else if (space instanceof CaveSpace) {
            hero.increaseAgility(10);  // Increase agility by 10 in Cave space
            System.out.println(hero.getName() + " gains an agility boost in the Cave.");
        } else if (space instanceof KoulouSpace) {
            hero.increaseStrength(10);  // Increase strength by 10 in Koulou space
            System.out.println(hero.getName() + " gains a strength boost in the Koulou.");
        }
    }

    public List<Monster> getMonstersInRange(Hero hero, List<Monster> monsters) {
        List<Monster> monstersInRange = new ArrayList<>();
        int heroRow = hero.getHeroRow();
        int heroCol = hero.getHeroCol();

        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                int monsterRow = monster.getMonsterRow();
                int monsterCol = monster.getMonsterCol();

                // Check if monster is adjacent to the hero
                if (Math.abs(heroRow - monsterRow) <= 1 && Math.abs(heroCol - monsterCol) <= 1) {
                    monstersInRange.add(monster);
                }
            }
        }
        return monstersInRange;
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

    // Simple move monster logic
    public boolean moveMonster(Monster monster, String direction) {
        if (monster == null) {
            System.out.println("Invalid Monster");
            return false;
        }

        int currentRow = monster.getMonsterRow();
        int currentCol = monster.getMonsterCol();

        int[] newPosition = calculateNewPosition(currentRow, currentCol, direction);
        if (newPosition == null) {
            System.out.println("Invalid direction.");
            return false; // Invalid direction
        }

        int newRow = newPosition[0];
        int newCol = newPosition[1];

        if (isValidMove(newRow, newCol)) {
            monster.setMonsterRow(newRow);
            monster.setMonsterCol(newCol);
            System.out.println("Moved " + monster.getName() + " " + direction + ".");
            return true;
        } else {
            System.out.println("Move failed: Invalid position.");
            return false;
        }
    }

    // Check win/loss conditions
    public boolean checkWinCondition(List<Hero> heroes, List<Monster> monsters) {
        // Check if any monster has reached the heroes' Nexus
        for (Monster monster : monsters) {
            if (monster.getMonsterRow() == 7) {
                System.out.println("Monsters have reached the heroes' Nexus. Heroes lose!");
                return true;
            }
        }

        // Check if any hero has reached the monsters' Nexus
        for (Hero hero : heroes) {
            if (hero.getHeroRow() == 0) {
                System.out.println("Heroes have reached the monsters' Nexus. Heroes win!");
                return true;
            }
        }

        return false;
    }

    public void updateBoard(List<Hero> heroes, List<Monster> monsters) {
        // Clear all occupants first
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // Do not clear Inaccessible spaces
                if (!(grid[i][j] instanceof InaccessibleSpace)) {
                    grid[i][j].setOccupant(null);
                }
            }
        }

        // Set hero locations
        for (Hero hero : heroes) {
            int row = hero.getHeroRow();
            int col = hero.getHeroCol();
            if (!(grid[row][col] instanceof InaccessibleSpace)) {
                grid[row][col].setOccupant(hero);
            }
        }

        // Set monster locations
        for (Monster monster : monsters) {
            int row = monster.getMonsterRow();
            int col = monster.getMonsterCol();
            if (!(grid[row][col] instanceof InaccessibleSpace)) {
                grid[row][col].setOccupant(monster);
            }
        }
    }



    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    public boolean isInNexus(Hero hero){
        int row = hero.getHeroRow();
        int col = hero.getHeroCol();
        return (row == hero.getNexus()[0] && col == hero.getNexus()[1] );
    }

}



// Define the specific terrain space classes
class NexusSpace extends Space {
    private String type;

    public NexusSpace(String type) {
        this.type = type;
    }

    @Override
    public String getSymbol() {
        return type.equals("Hero Nexus") ? "HN" : "MN";
    }
}

class BushSpace extends Space {
    @Override
    public String getSymbol() {
        return "B";
    }
}

class CaveSpace extends Space {
    @Override
    public String getSymbol() {
        return "C";
    }
}

class KoulouSpace extends Space {
    @Override
    public String getSymbol() {
        return "K";
    }
}

class PlainSpace extends Space {
    @Override
    public String getSymbol() {
        return "P";
    }
}