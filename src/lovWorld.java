public class lovWorld extends World {

    public lovWorld(int row, int col) {
        super(row, col);
    }

    private boolean setHeroLocation(Hero hero, int targetRow, int targetCol) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        hero.setHeroRow(targetRow);
        hero.setHeroCol(targetCol);
        return true;
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
            setHeroLocation(hero, newRow, newCol);
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

        // Check that teleporting is between different lanes
        if (teleportingHeroRow == targetRow) {
            System.out.println("Teleport failed: Must teleport to a different lane.");
            return false;
        }

        // Define possible adjacent positions around the target hero
        int[][] adjacentPositions = {
                {targetRow - 1, targetCol}, // Above
                {targetRow + 1, targetCol}, // Below
                {targetRow, targetCol - 1}, // Left
                {targetRow, targetCol + 1}  // Right
        };

        // Attempt teleport to any valid adjacent position
        for (int[] pos : adjacentPositions) {
            int newRow = pos[0];
            int newCol = pos[1];

            if (isValidMove(newRow, newCol)) {
                setHeroLocation(teleportingHero, newRow, newCol);
                System.out.println("Teleport successful.");
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
        return true;
    }
}