/**
 * Represents a marketplace where players can buy and sell items. This class provide methods to display items for sale,
 * resells, and manage player interactions with the market.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Market extends Space {
    private List<Item> availableItems;

    public Market() {
        loadAvailableItems();
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    // Load items from all categories into availableItems list
    private void loadAvailableItems() {
        availableItems = new ArrayList<>(Arrays.asList(
                ItemFactory.createItem("Weapon", 1),
                ItemFactory.createItem("Armor", 1),
                ItemFactory.createItem("Potion", 1),
                ItemFactory.createItem("Spell", 1)
                // Add more items as needed
        ));
    }

    public void enterMarket(Hero hero) {
        InputHandler inputHandler = InputHandler.getInstance();
        boolean inMarket = true;

        while (inMarket) {
            System.out.println("\nMerchant: Everything's for sale, my friend. Everything. If I had a brother, I'd sell him in a second.");

            System.out.println("\n1. Buy an item");
            System.out.println("2. Sell an item");
            System.out.println("3. Exit market");

            int choice = inputHandler.getIntInput("Enter choice: ");

            switch (choice) {

                case 1:
                    if (availableItems.isEmpty()) {
                        System.out.println("Sold Out！");
                        break;
                    }
                    buyItem(hero, inputHandler);
                    break;
                case 2:
                    sellItem(hero, inputHandler);
                    break;
                case 3:
                    System.out.println("\nDo come back...");
                    System.out.println(hero.getName() + " has left the market.");
                    inMarket = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void viewItemsForSale() {
        System.out.println("\nItems available for sale:");
        int index = 1;
        for (Item item : availableItems) {
            System.out.println(index + ": " + item);
            index++;
        }
    }

    private void buyItem(Hero hero, InputHandler inputHandler) {
       viewItemsForSale();

        int index = InputHandler.getInstance().getIntInput("Enter the index of the item you want to buy: ");
        Item itemToBuy = null;
        if(index > 0 && index < availableItems.size() + 1) {
            itemToBuy = availableItems.get(index - 1);
        }else{
            System.out.println("Invalid choice. Please enter a number between 1 and " + availableItems.size());
        }

        if (itemToBuy == null) {
            System.out.println("Item not found in the market.");
            return;
        }

        if (hero.getLevel() < itemToBuy.getLevelRequirement()) {
            System.out.println("You do not meet the level requirement to buy this item.");
        } else if (hero.getGold() < itemToBuy.getCost()) {
            System.out.println("You do not have enough gold to buy this item.");
        } else {
            hero.deductGold(itemToBuy.getCost());
            hero.getInventory().addItem(itemToBuy);
            availableItems.remove(itemToBuy);
            System.out.println("You have successfully bought " + itemToBuy.getName() + "!");
            System.out.println("You now have " + hero.getGold() + "  golds.");
        }
    }

    private void sellItem(Hero hero, InputHandler inputHandler) {
        System.out.println("\nYour inventory:");
        List<Item> items = hero.getInventory().getItems();

        if (items.isEmpty()) {
            System.out.println("You don't have any items to sell.");
            return;
        }

        // Display the inventory items
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getName());
        }

        Item itemToSell = null;
        int index = -1;

        while (itemToSell == null) {
            index = inputHandler.getIntInput("Enter the index of the item you want to sell: ") - 1;
            if (index >= 0 && index < items.size()) {
                itemToSell = items.get(index);
            } else {
                System.out.println("Invalid index. Please enter a number between 1 and " + items.size() + ".");
            }
        }

        int sellPrice = itemToSell.getCost() / 2;
        hero.addGold(sellPrice);
        hero.getInventory().useItem(itemToSell.getName());
        System.out.println("You have sold " + itemToSell.getName() + " for " + sellPrice + " gold.");
    }
}