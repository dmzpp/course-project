package main.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.AuthManager;
import main.dao.DBChampionshipDAO;
import main.dao.DBDisciplineDAO;
import main.dao.DBUserDAO;
import main.models.Discipline;
import main.models.User;
import main.services.ChampionshipService;
import main.services.DisciplineService;
import main.services.UserService;
import tableModels.VoluunterCompetence;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


public class DistributionCompetenciesController {
    @FXML
    private ComboBox<String> competenciesComboBox;

    @FXML
    private TableView<VoluunterCompetence> voluunterTable;

    @FXML
    private TableColumn<VoluunterCompetence, CheckBox> checkColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> nameColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> sexColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> ageColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> regionColumn;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<String> secondCompetenciesComboBox;

    @FXML
    private Button secondSearchButton;

    @FXML
    private TableView<VoluunterCompetence> secondVoluunterTable;

    @FXML
    private TableColumn<VoluunterCompetence, CheckBox> secondCheckColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> secondNameColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> secondSexColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> secondAgeColumn;

    @FXML
    private TableColumn<VoluunterCompetence, String> secondRegionColumn;



    private final UserService<DBUserDAO> userService = new UserService<>(DBUserDAO::new);
    @FXML
    private Button toLeftButton;

    @FXML
    private Button toRightButton;


    void fillComboBox(ComboBox<String> comboBox, List<Discipline> disciplines) {
        disciplines.add(new Discipline("Нераспределенные", null, null, null));
        for(var discipline: disciplines) {
            comboBox.getItems().add(discipline.getName());
        }
        comboBox.setValue(disciplines.get(disciplines.size() - 1).getName());
    }

    void fillTable(TableView<VoluunterCompetence> tableView, List<User> volunteers) {
        for(var voluunter : volunteers) {
            var sex = voluunter.isMale()? "male": "female";
            var id = voluunter.getId();
            tableView.getItems().add(new VoluunterCompetence(
                    voluunter.getFirstName(),
                    voluunter.getBirthdayDate().toString(),
                    sex, voluunter.getRegion().getName(), voluunter.getId()));
        }
    }


    @FXML
    public void initialize() {
        checkColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, CheckBox>("check"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("age"));
        sexColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("sex"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("region"));

        secondNameColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("name"));
        secondAgeColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("age"));
        secondSexColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("sex"));
        secondRegionColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, String>("region"));
        secondCheckColumn.setCellValueFactory(new PropertyValueFactory<VoluunterCompetence, CheckBox>("check"));


        var disciplineService = new DisciplineService<>(DBDisciplineDAO::new);
        var service = new ChampionshipService<DBChampionshipDAO>(DBChampionshipDAO::new);

        var allDisciplines = disciplineService.findAll();

        fillComboBox(competenciesComboBox, allDisciplines);
        fillComboBox(secondCompetenciesComboBox, allDisciplines);

        searchButton.setOnAction(actionEvent -> {

            voluunterTable.getItems().clear();

            Discipline currentDiscipline = null;
            var selected = competenciesComboBox.getValue();
            if(!selected.equals("Нераспределенные")){
                currentDiscipline = disciplineService.findByName(competenciesComboBox.getValue());
            }

            var currentUser = AuthManager.Current.getUser();
            var volunteers = service.findAllByRole("Volunteer",
                    currentUser.getChampionship());

            var result = new LinkedList<User>();

            for(var volunteer : volunteers){
                if(currentDiscipline != null && volunteer.getDiscipline() != null){
                    if(volunteer.getDiscipline().getId() == currentDiscipline.getId()){
                        result.add(volunteer);
                    }
                }
                else if(volunteer.getDiscipline() == currentDiscipline){
                    result.add(volunteer);
                }
            }

            fillTable(voluunterTable , result);


//            for(var voluunter : volunteers) {
//                var sex = voluunter.isMale()? "male": "female";
//                voluunterTable.getItems().add(new VoluunterCompetence(
//                        voluunter.getFirstName(), voluunter.getBirthdayDate().toString(), sex, voluunter.getRegion().getName()));
//            }
        });

        secondSearchButton.setOnAction(actionEvent -> {

            secondVoluunterTable.getItems().clear();

            Discipline currentDiscipline = null;
            var selected = secondCompetenciesComboBox.getValue();
            if(!selected.equals("Нераспределенные")){
                currentDiscipline = disciplineService.findByName(secondCompetenciesComboBox.getValue());
            }

            var currentUser = AuthManager.Current.getUser();
            var volunteers = service.findAllByRole("Volunteer",
                    currentUser.getChampionship());

            var result = new LinkedList<User>();

            for(var volunteer : volunteers){
                if(currentDiscipline != null  && volunteer.getDiscipline() != null){
                    if(volunteer.getDiscipline().getId() == currentDiscipline.getId()){
                        result.add(volunteer);
                    }
                }
                else if(volunteer.getDiscipline() == currentDiscipline){
                    result.add(volunteer);
                }
            }

            fillTable(secondVoluunterTable , result);
        });

        toLeftButton.setOnMouseClicked(e -> {

            // если вторая таблица не инициализирована, то инициализируем её
            if(voluunterTable.getItems().size() == 0){
                searchButton.fire();
            }

            // проходим по всем элементам правой таблицы и выбираем те элементы, которые выбраны
            var volunteersInRightTable = secondVoluunterTable.getItems();

            var changedVolunteers = new LinkedList<VoluunterCompetence>();

            for (var volunteerRow : volunteersInRightTable){
                if(volunteerRow.getCheck().isSelected()){
                    changedVolunteers.add(volunteerRow);
                }
            }

            // перекидываем их в соседнию таблицу
            voluunterTable.getItems().addAll(changedVolunteers);

            // изменяем у каждого его направление
            var selected = competenciesComboBox.getValue();
            Discipline currentDiscipline = null;
            if(!selected.equals("Нераспределенные")){
                currentDiscipline = disciplineService.findByName(selected);
            }

            for (var volunteer : changedVolunteers){
                userService.updateDiscipline(volunteer.getId(), currentDiscipline);
            }


            secondVoluunterTable.getItems().removeAll(changedVolunteers);
        });

        toRightButton.setOnMouseClicked(e -> {

            // если вторая таблица не инициализирована, то инициализируем её
            if(secondVoluunterTable.getItems().size() == 0){
                secondSearchButton.fire();
            }

            // проходим по всем элементам правой таблицы и выбираем те элементы, которые выбраны
            var volunteersInLeftTable = voluunterTable.getItems();

            var changedVolunteers = new LinkedList<VoluunterCompetence>();

            for (var volunteerRow : volunteersInLeftTable){
                if(volunteerRow.getCheck().isSelected()){
                    changedVolunteers.add(volunteerRow);
                }
            }

            // перекидываем их в соседнию таблицу
            secondVoluunterTable.getItems().addAll(changedVolunteers);

            // изменяем у каждого его направление
            var selected = secondCompetenciesComboBox.getValue();
            Discipline currentDiscipline = null;
            if(!selected.equals("Нераспределенные")){
                currentDiscipline = disciplineService.findByName(selected);
            }

            for (var volunteer : changedVolunteers){
                userService.updateDiscipline(volunteer.getId(), currentDiscipline);
            }


            voluunterTable.getItems().removeAll(changedVolunteers);
        });

    }
}
