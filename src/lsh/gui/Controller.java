package lsh.gui;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import lsh.backend.Core;
import lsh.backend.Database;
import lsh.backend.Utils;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

public class Controller {

    public ChoiceBox solution_box;
    public ProgressBar pBar;
    public ImageView Logo;


    public void initSolutionBox() {
        ArrayList<String> solution_urls = new ArrayList<>();

        for (Database.Solution solution : Database.solution_list) {
            solution_urls.add(solution.name);
        }

        solution_box.setItems(FXCollections.observableArrayList(solution_urls));
        solution_box.getSelectionModel().select(0);

        Utils.setVisualizing(false);
    }

    private Image getImage(String filePath) {
        try {
            return new Image(new File(filePath).toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void initialize() {
        //Initialize Backend
        lsh.backend.Main.init();

        //Initialize Solution Box
        initSolutionBox();

        //Initialize Logo
        Logo.setImage(getImage("../../../../resources/lshelper_full.png"));
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
