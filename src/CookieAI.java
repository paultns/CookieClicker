public class CookieAI {

    public static void main(String[] args) throws InterruptedException {


        CookieClicker cC = new CookieClicker();

        // set number of unlocked buildings
        cC.setBuildings(10);
        // set number of loops between building buys
        cC.setCycleLength(10);

        cC.setUp();

        cC.getGoal();

        while (true)
            cC.run();


    }
}


// create method to generate number of buildings
// find way to set goal for building with no units
