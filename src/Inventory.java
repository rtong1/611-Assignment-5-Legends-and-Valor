/**
 *  Manages the collection of items a hero possesses, such as weapons, armor, spell, and potions. It provides
 *  methods to add, remove, equip, unequip and use items within the inventory.
 */

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;
    private List<Weapon> equippedWeapons;
    private Armor equippedArmor;

    public Inventory() {
        this.items = new ArrayList<>();
        this.equippedWeapons = new ArrayList<>();
        this.equippedArmor = null;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }


    public void equipItem() {
        List<Item> equippableItems = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Weapon || item instanceof Armor) {
                equippableItems.add(item);
            }
        }

        if (equippableItems.isEmpty()) {
            System.out.println("No weapons or armor available to equip.");
            return;
        }

        System.out.println("Available equitable items:");
        for (int i = 0; i < equippableItems.size(); i++) {
            System.out.println((i + 1) + ". " + equippableItems.get(i).getName());
        }

        Item itemToEquip = null;
        while (itemToEquip == null) {
            int index = InputHandler.getInstance().getIntInput("Enter the index of the item you want to equip: ") - 1;
            if (index >= 0 && index < equippableItems.size()) {
                itemToEquip = equippableItems.get(index);
            } else {
                System.out.println("Invalid selection. Please choose a valid index.");
            }
        }

        if (itemToEquip instanceof Weapon) {
            equipWeapon((Weapon) itemToEquip);
        } else if (itemToEquip instanceof Armor) {
            equipArmor((Armor) itemToEquip);
        }
    }

    public void unequipItem() {
        List<Item> unequippableItems = new ArrayList<>(equippedWeapons);
        if (equippedArmor != null) {
            unequippableItems.add(equippedArmor);
        }

        if (unequippableItems.isEmpty()) {
            System.out.println("No items currently equipped.");
            return;
        }

        System.out.println("Currently equipped items:");
        for (int i = 0; i < unequippableItems.size(); i++) {
            System.out.println((i + 1) + ". " + unequippableItems.get(i).getName());
        }

        Item itemToUnequip = null;
        while (itemToUnequip == null) {
            int index = InputHandler.getInstance().getIntInput("Enter the index of the item you want to unequip: ") - 1;
            if (index >= 0 && index < unequippableItems.size()) {
                itemToUnequip = unequippableItems.get(index);
            } else {
                System.out.println("Invalid selection. Please choose a valid index.");
            }
        }

        if (itemToUnequip instanceof Weapon) {
            unequipWeapon((Weapon) itemToUnequip);
        } else if (itemToUnequip instanceof Armor) {
            unequipArmor();
        }
    }

    private void equipWeapon(Weapon weapon) {
        if (weapon.getRequiredHands() == 2) {
            equippedWeapons.clear();
            equippedWeapons.add(weapon);
            items.remove(weapon);
            System.out.println(weapon.getName() + " is now equipped as a two-handed weapon.");
        } else {
            if (equippedWeapons.size() < 2) {
                equippedWeapons.add(weapon);
                items.remove(weapon);
                System.out.println(weapon.getName() + " is now equipped as a one-handed weapon.");
            } else {
                System.out.println("Already holding two single-handed weapons.");
            }
        }
    }

    private void equipArmor(Armor armor) {
        if (equippedArmor != null) {
            System.out.println("An armor is already equipped.");
        } else {
            equippedArmor = armor;
            items.remove(armor);
            System.out.println(armor.getName() + " is now equipped as armor.");
        }
    }

    private void unequipWeapon(Weapon weapon) {
        equippedWeapons.remove(weapon);
        items.add(weapon);
        System.out.println(weapon.getName() + " has been unequipped.");
    }

    private void unequipArmor() {
        if (equippedArmor != null) {
            items.add(equippedArmor);
            System.out.println(equippedArmor.getName() + " has been unequipped.");
            equippedArmor = null;
        }
    }

    public int useWeapon() {
        if (equippedWeapons.isEmpty()) {
            return 0;
        } else if (equippedWeapons.size() == 1) {
            Weapon weapon = equippedWeapons.get(0);
            // 50% more damage for double handed weapon
            return weapon.getRequiredHands() == 2 ? (int) (weapon.getDamage() * 1.5) : weapon.getDamage();
        } else {
            // Sum damage of two one-handed weapons
            return equippedWeapons.get(0).getDamage() + equippedWeapons.get(1).getDamage();
        }
    }

    public int useArmor() {
        return equippedArmor != null ? equippedArmor.getDamageReduction() : 0;
    }

    public void useItem(String itemName) {
        items.removeIf(item -> item.getName().equals(itemName));
    }


    public void showItems() {
        int index = 1;
        for (Item item : items) {
            System.out.println(index + ". " + item.getName());
            index++;
        }
    }

    public Item getItemByName(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public Item getCurrentArmor() {
        return equippedArmor;
    }

    public List<Weapon> getCurrentWeapon() {
        return equippedWeapons;
    }


}