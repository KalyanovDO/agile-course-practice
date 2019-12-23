package ru.unn.agile.dijkstraalgorithm.view;

import javafx.beans.property.Property;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.unn.agile.dijkstraalgorithm.infrastructure.TextLogger;
import ru.unn.agile.dijkstraalgorithm.viewmodel.EdgeViewModel;
import ru.unn.agile.dijkstraalgorithm.viewmodel.ViewModel;


public class Dijkstra {

    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    private final String vertexInputTooltip = "Only letters allowed";
    private final String weightInputTooltip = "Only positive integer numbers allowed";

    @FXML
    private ViewModel viewModel;

    @FXML
    private TextField vertex1TextField;
    @FXML
    private TextField vertex2TextField;
    @FXML
    private TextField weightTextField;
    @FXML
    private Button addEdgeButton;

    @FXML
    private TableView<EdgeViewModel> tableEdges;
    @FXML
    private TableColumn<EdgeViewModel, String> vertex1Column;
    @FXML
    private TableColumn<EdgeViewModel, String> vertex2Column;
    @FXML
    private TableColumn<EdgeViewModel, Integer> weightColumn;

    @FXML
    private Button createGraphButton;
    @FXML
    private Button calculatePathButton;

    @FXML
    private ComboBox<String> fromComboBox;
    @FXML
    private ComboBox<String> toComboBox;
    @FXML
    private TextArea resultPathTextArea;

    @FXML
    void initialize() {
        viewModel.setLogger(new TextLogger("./TextLogger-lab3.log"));
        initAddEdgeForm();
        initTableView();
        initControlPanel();
    }

    private void initAddEdgeForm() {
        initTextField(vertex1TextField, vertexInputTooltip, viewModel.vertex1Property());
        initTextField(vertex2TextField, vertexInputTooltip, viewModel.vertex2Property());
        initTextField(weightTextField, weightInputTooltip, viewModel.weightProperty());

        viewModel.vertex1Property().addListener(obs ->
                setErrorBorder(vertex1TextField, !viewModel.isVertex1InputCorrect()));
        viewModel.vertex2Property().addListener(obs ->
                setErrorBorder(vertex2TextField, !viewModel.isVertex2InputCorrect()));
        viewModel.weightProperty().addListener(obs ->
                setErrorBorder(weightTextField, !viewModel.isWeightInputCorrect()));

        addEdgeButton.disableProperty()
                .bindBidirectional(viewModel.addingNewEdgeDisabledProperty());
        addEdgeButton.setOnAction(e -> viewModel.addEdge());
    }

    private void initTableView() {
        tableEdges.setItems(viewModel.getEdgeList());

        vertex1Column.setCellValueFactory(
                new PropertyValueFactory<EdgeViewModel, String>("vertex1"));
        vertex2Column.setCellValueFactory(
                new PropertyValueFactory<EdgeViewModel, String>("vertex2"));
        weightColumn.setCellValueFactory(
                new PropertyValueFactory<EdgeViewModel, Integer>("weight"));
    }

    private void initControlPanel() {
        createGraphButton.setOnAction(e -> viewModel.createGraph());
        calculatePathButton.setOnAction(e -> viewModel.calculatePath());

        fromComboBox.valueProperty().bindBidirectional(viewModel.vertexFromProperty());
        fromComboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                viewModel.onExpressionComboBoxFocusChanged();
            }
        });
        toComboBox.valueProperty().bindBidirectional(viewModel.vertexToProperty());
        toComboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                viewModel.onExpressionComboBoxFocusChanged();
            }
        });

        resultPathTextArea.textProperty().bindBidirectional(viewModel.resultPathProperty());
    }

    private void initTextField(final TextField textField,
                                final String tooltip,
                                final Property<String> property) {
        bindTextFieldProperty(textField, property);
        setTextFieldTooltip(textField, tooltip);
        addListenerForRemovingSpaces(textField);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                viewModel.onExpressionTextFieldFocusChanged();
            }
        });
    }

    private void setTextFieldTooltip(final TextField textField, final String tooltip) {
        textField.tooltipProperty().setValue(new Tooltip(tooltip));
    }

    private void bindTextFieldProperty(final TextField textField,
                                       final Property<String> property) {
        textField.textProperty().bindBidirectional(property);
    }

    private void addListenerForRemovingSpaces(final TextField textField) {
        textField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        textField.setText(newValue.replaceAll("\\s+", ""))
        );
    }

    private void setErrorBorder(final TextField textField, final boolean active) {
        textField.pseudoClassStateChanged(errorClass, active);
    }
}
