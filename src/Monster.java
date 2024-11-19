import java.util.Random;

/**
 * Represents the Monster character type, with unique attributes. This class also contain the attributes effect under
 * different kinds of spells
 */

public class Monster extends Character implements Attackable<Hero> {
    private int baseDamage;
    private int defense;
    private int dodgeChance;

    /*
    For Legends and Valor
     */
    private int MonsterRow;
    private int MonsterCol;

    public Monster(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level);
        this.baseDamage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    public void setAttributes(String name, int level, int baseDamage, int defense, int dodgeChance) {
        this.name = name;
        this.level = level;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    // Getters
    public int getHealth() { return healthPoints; }
    public int getCurrentHealth() { return healthPoints; }
    public int getBaseDamage() { return baseDamage; }
    public int getBaseDefense() { return defense; }
    public double getDodgeChance() { return dodgeChance * 0.01; }  // Convert dodge chance to percentage


    public void applySpellEffect(String spellType) {
        double reductionAmount;

        switch (spellType.toLowerCase()) {
            case "ice":
                reductionAmount = baseDamage * 0.1;
                baseDamage = Math.max(0, (int)(baseDamage - reductionAmount));
                System.out.println(name + "'s base damage reduced by " + (int)reductionAmount + ". New damage: " + baseDamage);
                break;

            case "fire":
                reductionAmount = defense * 0.1;
                defense = Math.max(0, (int)(defense - reductionAmount));
                System.out.println(name + "'s defense reduced by " + (int)reductionAmount + ". New defense: " + defense);
                break;

            case "lightning":
                reductionAmount = dodgeChance * 0.1;
                dodgeChance = Math.max(0, (int)(dodgeChance - reductionAmount));
                System.out.println(name + "'s dodge chance reduced by " + (int)reductionAmount + "%.");
                break;

            default:
                System.out.println("Unknown spell type: no effect applied.");
        }
    }


    public void attack(Hero target){
        Random random = new Random();
        if (target != null) {
            double dodgeChance = target.getCurrentAgility() * 0.002;
            double reducedDamage = getBaseDamage() * 0.05 - target.getInventory().useArmor();
            double damage = Math.max(0, reducedDamage);

            // If hero fails to dodge
            if ((random.nextInt(100) + 1) > dodgeChance) {
                // Apply final damage
                int finalDamage = (int) Math.ceil(damage);
                target.takeDamage(finalDamage);

                System.out.println(getName() + " attacked " + target.getName() + " for " + finalDamage + " damage.");
                System.out.println(target.getName() + "'s remaining HP: " + target.getCurrentHealth());

                // Check if hero has fainted
                if (!target.isAlive()) {
                    System.out.println(target.getName() + " fainted!");
                }
            } else {
                System.out.println(target.getName() + " dodged the attack from " + getName() + "!");
            }
        }
    }
     /*
    For Legends and Valor
     */
    public int getMonsterRow() { return MonsterRow; }
    public int getMonsterCol() { return MonsterCol; }
    public void setMonsterRow(int monsterRow) { MonsterRow = monsterRow; }
    public void setMonsterCol(int monsterCol) { MonsterCol = monsterCol; }

    private void attackAdjacent(Hero target){
        double distance = Math.sqrt(Math.pow(target.getHeroRow() - this.getMonsterRow(), 2) + Math.pow(target.getHeroCol() - this.getMonsterCol(), 2));

        if (distance <= Math.sqrt(2)) {
            //monster attacks
        }
    }

    private void move(lovWorld world){
        int[] down = {getMonsterRow() + 1, getMonsterCol()};
        int[] left = {getMonsterRow(), getMonsterCol() - 1};
        int[] right = {getMonsterRow(), getMonsterCol() + 1};

        if(world.isValidMove(down[0], down[1])){
            setMonsterCol(down[0]);
        } else if (world.isValidMove(left[0], left[1])) {
            setMonsterRow(left[1]);
        } else if (world.isValidMove(right[0], right[1])) {
            setMonsterRow(right[1]);
        }
    }

}