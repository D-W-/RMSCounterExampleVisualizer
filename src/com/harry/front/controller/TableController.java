package com.harry.front.controller;

/**
 * RMS related tools, parse formatted data into RMS input and run RMS, then visualize output.
 * Created by Han Wang at 12/21/17.
 * Copyright (C) 2015-2017  Han Wang
 */

import com.harry.back.cmd.Crawler;
import com.harry.back.core.InputGenerator;
import com.harry.back.core.Transformer;
import com.harry.front.browser.WebViewSample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import com.harry.front.data.Task;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class TableController implements Initializable {

    public TextField interruptCycle;
    public TextField scheduling;
    public TextField switching;
//    public TextField taskNumber;

    private Integer interruptCycleTime;
    private Integer schedulingIntTime;
    private Integer switchingTime;
    private List<Task> tasks = new LinkedList<>();

    @FXML
    private TableView<TaskTableData> table;

    @FXML
    private TextField cycleTimeTextfield;

    @FXML
    private TextField runningTimeTextfield;

    private ObservableList<TaskTableData> data = FXCollections.observableArrayList();

    @FXML
    private TableColumn<TaskTableData, String> cycleTimeColumn;

    @FXML
    private TableColumn<TaskTableData, String> runningTimeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.setItems(data);
//        populate(retrieveData());
        setupCycleTimeColumn();
        setupRunningTimeColumn();
        setTableEditable();
    }

    private void setupCycleTimeColumn() {
//        updates the cycleTime field on the TaskTableData to the committed value
        cycleTimeColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
            ((TaskTableData) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow())).setCycleTime(value);
            table.refresh();
        });
    }

    private void setupRunningTimeColumn() {
//        updates the cycleTime field on the TaskTableData to the committed value
        runningTimeColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
            ((TaskTableData) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow())).setRunningTime(value);
            table.refresh();
        });
    }

    private void setTableEditable() {
        table.setEditable(true);
        // allows the individual cells to be selected
        table.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        table.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                editFocusedCell();
            } else if (event.getCode() == KeyCode.RIGHT
                    || event.getCode() == KeyCode.TAB) {
                table.getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                // work around due to
                // TableView.getSelectionModel().selectPrevious() due to a bug
                // stopping it from working on
                // the first column in the last row of the table
                selectPrevious();
                event.consume();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void editFocusedCell() {
        final TablePosition<TaskTableData, ?> focusedCell = table
                .focusModelProperty().get().focusedCellProperty().get();
        table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @SuppressWarnings("unchecked")
    private void selectPrevious() {
        if (table.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition<TaskTableData, ?> pos = table.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                table.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < table.getItems().size()) {
                // wrap to end of previous row
                table.getSelectionModel().select(pos.getRow() - 1,
                        table.getVisibleLeafColumn(
                                table.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = table.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                table.getSelectionModel().select(table.getItems().size() - 1);
            } else if (focusIndex > 0) {
                table.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private TableColumn<TaskTableData, ?> getTableColumn(
            final TableColumn<TaskTableData, ?> column, int offset) {
        int columnIndex = table.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return table.getVisibleLeafColumn(newColumnIndex);
    }

    public void submit(final ActionEvent actionEvent) {
        final String cycleTime = cycleTimeTextfield.getText();
        final String runningTime = runningTimeTextfield.getText();
        data.add(new TaskTableData(cycleTime, runningTime));
    }

    private int formalizeData(String data) {
        Scanner scanner = new Scanner(data);
        int time = 0;
        Double timeDouble = 0.0;
        if (scanner.hasNext())
            timeDouble = scanner.nextDouble();
        else
            throw new RuntimeException();
        if (scanner.hasNext()) {
            String unit = scanner.next().toLowerCase();
            if (unit.equals("ms")) {
                timeDouble *= 1000;
                time = timeDouble.intValue();
            }
            else if (unit.equals("us")) {
                time = timeDouble.intValue();
            }
            else
                throw new RuntimeException();
        }
        else {
            time = timeDouble.intValue();
        }
        return time;
    }

    private boolean allFieldsValid() {
//        check empty
        if (interruptCycle.getText().isEmpty() || scheduling.getText().isEmpty() || switching.getText().isEmpty() || data.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty data");
            alert.showAndWait();
            return false;
        }
//        parse data]
        try {
            interruptCycleTime = formalizeData(interruptCycle.getText());
            schedulingIntTime = formalizeData(scheduling.getText());
            switchingTime = formalizeData(switching.getText());

            tasks.clear();
            int cycleTime = 0, runningTime = 0;
            for (TaskTableData task : data) {
                cycleTime = formalizeData(task.getCycleTime());
                runningTime = formalizeData(task.getRunningTime());
                tasks.add(new Task(cycleTime, runningTime));
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Data parse error: Supported time units: ms, us");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    public void run(final ActionEvent actionEvent) {
        if(allFieldsValid()) {
//            System.out.println(tasks);

//            run backend
//            generate test cases
            InputGenerator inputGenerator = new InputGenerator();
            inputGenerator.setValues(interruptCycleTime, schedulingIntTime, switchingTime, tasks);
            inputGenerator.generate();
//            run crawler
            boolean result = new Crawler().crawl("test-case.maude");
            Alert.AlertType type = result ? Alert.AlertType.INFORMATION: Alert.AlertType.CONFIRMATION;
            Alert alert = new Alert(type);
            String s = result ? "True" : "False";
            alert.setContentText(s);
            Optional<ButtonType> buttonResult = alert.showAndWait();

            if (type == Alert.AlertType.CONFIRMATION && (buttonResult.isPresent()) && (buttonResult.get() == ButtonType.OK)) {
                new Transformer().transform();
                WebViewSample.run();
            }

        }
    }



}
