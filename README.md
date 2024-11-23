# CS611-Assignment 5 Legends of Valor

## Monster and Hero

---

- **Name**: Ronghao Tong Haoran Zhang
- **Email**: ronghaot@bu.edu hz0820@bu.edu
- **Student ID**: U72020135 U95845630

---

## Files

| Filename                 | Description                                                                                                                                   |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| `Attackable.java`        | An Interface for all attackable characters.                                                                                                   |
| `Buyable.java`           | An Interface for all buyable items.                                                                                                           |
| `Consumable.java`        | An Interface for all consumable items.                                                                                                        |
| `lovWorld.java`          | A child class for World class, represent a world of Legends of Valor.                                                                         |
| `BushSpace.java`         | A child class for Space, a kind of terrain in LOV game that increase heroes' dexterity.                                                       |
| `CaveSpace.java`         | A child class for Space, a kind of terrain in LOV game that increase heroes' agility.                                                         |
| `KoulouSpace.java`       | A child class for Space, a kind of terrain in LOV game that increase heroes' strength.                                                        |
| `NexusSpace.java`        | A child class for Space, the goal for opponents, respawn and shopping space for heroes.                                                       |
| `PlainSpace.java`        | A child class for Space, a kind of regular terrain in LOV game that is neutral for all characters.                                            |                                                                                                                                              |
| `Armor.java`             | Represents an armor item in the game, providing damage reduction to heroes by reducing incoming damage.                                       |
| `Battle.java`            | Manages the combat interactions between characters, including heroes and monsters.                                                            |
| `Character.java`         | An abstract base class representing common attributes and behaviors for all characters in the game, such as health, level, and attributes.    |
| `CommonSpace.java`       | Represents a common, area in the game world where players might move triggering battle events.                                                |
| `Game.java`              | This class handles the overall game logic, responsible for initializing game components, managing the game loop, and tracking the game state. |
| `Hero.java`              | Defines the Hero character type with specific attributes such as health, mana, and other attributes.                                          |
| `Herofactory.java`       | A factory class for creating instances of Hero characters. Using the Factory design pattern.                                                  |
| `InaccessibleSapce.java` | Represents an area in the game world that cannot be entered by characters.                                                                    |
| `InputHandler.java`      | Singleton input Handler that handles all user's input.                                                                                        |
| `Inventory.java`         | Manages the collection of items a hero possesses, such as weapons, armor, spell, and potions.                                                 |
| `Item.java`              | An abstract class representing generic items in the game, serving as the base class for all item types (e.g., Weapon, Armor, Potion).         |
| `ItemFactory.java`       | A factory class responsible for creating instances of various items from data files.                                                          |
| `Main.java`              | A concise main class.                                                                                                                         |
| `Market.java`            | Represents a marketplace where players can buy and sell items.                                                                                |
| `Monster.java`           | Represents the Monster character type, with unique attributes.                                                                                |
| `MosterFactory.java`     | A factory class to create instances of Monster characters.                                                                                    |
| `Potion.java`            | Represents a potion item that can restore health, mana, or grant other temporary boosts to characters.                                        |
| `Space.java`             | An abstract class that represents a grid within the game world.                                                                               |
| `Spell.java`             | Represents a spell that characters can cast in battle, providing magical effects.                                                             |
| `Weapon.java`            | Defines a weapon item, providing damage increase to characters who equip it.                                                                  |
| `World.java`             | Manages the overall game world, containing different spaces and areas where gameplay occurs.                                                  |

---

## Notes

1. **Design Pattern**: Use factory classes to generate objects like heroes, items, and monsters.
2. **Singleton InputHandler**: Uses a singleton class to handle user inputs.
3. **Customized Hero**: User can choose to customize their own hero with default attributes.
4. **Target Selection**: User can choose which enemy he wants to attack.
5. **Stuck Proof**: User can move to a new map if they are blocked by inaccessible spaces.
---

## How to Compile and Run
File Structures:
MyProject/
└── src/
├── Main.java
└── data/
├──  Armory.txt
└──...txt

1. Navigate to the directory `"src"` after unzipping the files.
2. Run the following commands:
   ```bash
   javac *.java
   java Main
   ```

## Input/Output Example

```
Choose your party of 3 heroes:
Choose a class for hero 1:
1. Warrior
2. Sorcerer
3. Paladin
Enter your choice: 1
Would you like to choose a pre-defined hero or create a custom one?
1. Choose a hero from the list
2. Create a custom hero
1
Available Warrior heroes:
1. Name: Gaerdal_Ironhand, Level=1, HP=100/100, MP=100/100, STR=700, DEX=500, AGI=600
2. Name: Sehanine_Monnbow, Level=1, HP=100/100, MP=600/600, STR=700, DEX=800, AGI=500
3. Name: Muamman_Duathall, Level=1, HP=100/100, MP=300/300, STR=900, DEX=500, AGI=750
4. Name: Flandal_Steelskin, Level=1, HP=100/100, MP=200/200, STR=750, DEX=650, AGI=700
5. Name: Undefeated_Yoj, Level=1, HP=100/100, MP=400/400, STR=800, DEX=400, AGI=700
6. Name: Eunoia_Cyn, Level=1, HP=100/100, MP=400/400, STR=700, DEX=800, AGI=600
Enter the index of the hero you'd like to choose: 1
Debug: Hero identifier assigned as: H1
Gaerdal_Ironhand has joined your party as H1.
Choose a class for hero 2:
1. Warrior
2. Sorcerer
3. Paladin
Enter your choice: 2
Would you like to choose a pre-defined hero or create a custom one?
1. Choose a hero from the list
2. Create a custom hero
2
Enter a name for your hero: A Sorcerer
Debug: Hero identifier assigned as: H2
A Sorcerer has joined your party as H2.
Choose a class for hero 3:
1. Warrior
2. Sorcerer
3. Paladin
Enter your choice: 3
Would you like to choose a pre-defined hero or create a custom one?
1. Choose a hero from the list
2. Create a custom hero
1
Available Paladin heroes:
1. Name: Parzival, Level=1, HP=100/100, MP=300/300, STR=750, DEX=650, AGI=700
2. Name: Sehanine_Moonbow, Level=1, HP=100/100, MP=300/300, STR=750, DEX=700, AGI=700
3. Name: Skoraeus_Stonebones, Level=1, HP=100/100, MP=250/250, STR=650, DEX=600, AGI=350
4. Name: Garl_Glittergold, Level=1, HP=100/100, MP=100/100, STR=600, DEX=500, AGI=400
5. Name: Amaryllis_Astra, Level=1, HP=100/100, MP=500/500, STR=500, DEX=500, AGI=500
6. Name: Caliber_Heist, Level=1, HP=100/100, MP=400/400, STR=400, DEX=400, AGI=400
Enter the index of the hero you'd like to choose: 2
Debug: Hero identifier assigned as: H3
Sehanine_Moonbow has joined your party as H3.
M1 (Natsunomeryu) Lv: 1 has spawned.
M2 (Casper) Lv: 1 has spawned.
M3 (Casper) Lv: 1 has spawned.
lovWorld initialized with custom board setup!
Welcome to Legends of Valors!
╔════╦════╦════╦════╦════╦════╦════╦════╗
║ MN ║ M1 ║ ☠  ║ MN ║ M2 ║ ☠  ║ MN ║ M3 ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ B  ║ K  ║ ☠  ║ B  ║ C  ║ ☠  ║ C  ║ C  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ C  ║ K  ║ ☠  ║ P  ║ P  ║ ☠  ║ P  ║ P  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ B  ║ C  ║ ☠  ║ P  ║ K  ║ ☠  ║ B  ║ C  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ C  ║ P  ║ ☠  ║ B  ║ P  ║ ☠  ║ C  ║ P  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ B  ║ C  ║ ☠  ║ P  ║ K  ║ ☠  ║ P  ║ P  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ C  ║ C  ║ ☠  ║ P  ║ B  ║ ☠  ║ P  ║ C  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ HN ║ H1 ║ ☠  ║ HN ║ H2 ║ ☠  ║ HN ║ H3 ║
╚════╩════╩════╩════╩════╩════╩════╩════╝

--- Round 1 ---

Hero H1's turn:
Available actions for Gaerdal_Ironhand (Exploration Mode):
1. Move
2. Use Potion
3. Change Weapon/Armor
4. Teleport
5. Recall
6. Shop
Enter the action number: 1
Enter direction (w = up, a = left, s = down, d = right): w
Gaerdal_Ironhand gains 10 agility
Gaerdal_Ironhand gains an agility boost in the Cave.
Moved Gaerdal_Ironhand w.
╔════╦════╦════╦════╦════╦════╦════╦════╗
║ MN ║ M1 ║ ☠  ║ MN ║ M2 ║ ☠  ║ MN ║ M3 ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ B  ║ K  ║ ☠  ║ B  ║ C  ║ ☠  ║ C  ║ C  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ C  ║ K  ║ ☠  ║ P  ║ P  ║ ☠  ║ P  ║ P  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ B  ║ C  ║ ☠  ║ P  ║ K  ║ ☠  ║ B  ║ C  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ C  ║ P  ║ ☠  ║ B  ║ P  ║ ☠  ║ C  ║ P  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ B  ║ C  ║ ☠  ║ P  ║ K  ║ ☠  ║ P  ║ P  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ C  ║ H1 ║ ☠  ║ P  ║ B  ║ ☠  ║ P  ║ C  ║
╠════╬════╬════╬════╬════╬════╬════╬════╣
║ HN ║ HN ║ ☠  ║ HN ║ H2 ║ ☠  ║ HN ║ H3 ║
╚════╩════╩════╩════╩════╩════╩════╩════╝

```
