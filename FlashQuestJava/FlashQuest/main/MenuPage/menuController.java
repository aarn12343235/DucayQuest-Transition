package MenuPage;

import Backend.Controller.FlashQuestController;
import CreateFolderPage.createFolderPage;
import QuestPage.quest;
import javafx.stage.Stage;

public class menuController {
    private final Stage stage;
    private final FlashQuestController flashQuestController;

    public menuController(Stage stage, FlashQuestController flashQuestController) {
        this.stage = stage;
        this.flashQuestController = flashQuestController;
    }

    public void clickMenuButton() {
        transitionToMenu();
    }

    public void clickSmithFolderButton() {
        transitionToCreateFolderPage();
    }

    public void clickQuestButton() {
        transitionToQuestPage();
    }

    public void clickUserButton() {

    }

    public void clickStartButton() {

    }

    public void clickViewButton() {

    }

    private void transitionToMenu() {
        menu Menu = new menu(stage, flashQuestController);
        Menu.show();
    }

    private void transitionToCreateFolderPage() {
        createFolderPage Smithfolder = new createFolderPage(stage, flashQuestController);
        Smithfolder.show();
    }

    private void transitionToQuestPage() {
        quest Quest = new quest(stage, flashQuestController);
        Quest.show();
    }
}
