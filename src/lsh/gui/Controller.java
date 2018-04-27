package lsh.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import lsh.backend.Core;
import lsh.backend.Database;
import lsh.backend.Utils;

import java.util.ArrayList;

public class Controller {

    public ChoiceBox solution_box;
    public ProgressBar pBar;

    public void initSolutionBox() {
        ArrayList<String> solution_urls = new ArrayList<>();

        for (Database.Solution solution : Database.solution_list) {
            solution_urls.add(solution.name);
        }

        solution_box.setItems(FXCollections.observableArrayList(solution_urls));
        solution_box.getSelectionModel().select(0);

        Utils.setVisualizing(false);

    }

    @FXML
    public void initialize() {
        //Initialize Backend
        lsh.backend.Main.init();

        //Initialize Solution Box
        initSolutionBox();
    }

    public void runSolution(MouseEvent mouseEvent) {

        String selected_solution = (String)solution_box.getSelectionModel().getSelectedItem();

        for(int i = 0; i < Database.solution_list.size(); ++i) {
            if(selected_solution.compareTo(Database.solution_list.get(i).name) == 0) {
                new FixThread(i,pBar).start();
            }
        }

    }
}
