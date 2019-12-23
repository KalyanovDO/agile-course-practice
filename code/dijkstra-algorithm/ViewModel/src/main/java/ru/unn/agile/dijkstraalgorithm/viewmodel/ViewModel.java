package ru.unn.agile.dijkstraalgorithm.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.unn.agile.dijkstraalgorithm.model.DijkstraGraph;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ViewModel {

    private DijkstraGraph graph;

    private static final Pattern VERTEX_INPUT_ALLOWED_SYMBOLS = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern WEIGHT_INPUT_ALLOWED_SYMBOLS = Pattern.compile("^[1-9][0-9]*$");
    private final SimpleBooleanProperty addingNewEdgeDisabled = new SimpleBooleanProperty();


    private final StringProperty vertex1 = new SimpleStringProperty();
    private final StringProperty vertex2 = new SimpleStringProperty();
    private final StringProperty weight = new SimpleStringProperty();

    private StringProperty vertexFrom = new SimpleStringProperty();
    private StringProperty vertexTo = new SimpleStringProperty();
    private StringProperty resultPath = new SimpleStringProperty();


    private final ObservableList<EdgeViewModel> edgeList = FXCollections.observableArrayList();
    private final ObservableList<String> vertexList = FXCollections.observableArrayList();

    private ILogger logger;
    private final StringProperty logs = new SimpleStringProperty();

    public final void setLogger(final ILogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger parameter can't be null");
        }
        this.logger = logger;
    }

    public ViewModel() {
        init();
    }

    public ViewModel(final ILogger logger) {
        setLogger(logger);
        init();
    }

    private void init() {
        clearFormInput();

        BooleanBinding canCalculateBoolBinding = new BooleanBinding() {
            {
                super.bind(vertex1, vertex2, weight);
            }
            @Override
            protected boolean computeValue() {
                return (isVertex1InputCorrect()
                        && isVertex2InputCorrect()
                        && isWeightInputCorrect());
            }
        };
        addingNewEdgeDisabled.bind(canCalculateBoolBinding.not());
    }

    public void addEdge() {
        EdgeViewModel newEdge = new EdgeViewModel(vertex1.get(), vertex2.get(), weight.get());
        edgeList.add(newEdge);
        logForAddedEdge();

        clearFormInput();
    }

    private void clearFormInput() {
        vertex1.set("");
        vertex2.set("");
        weight.set("");
    }

    public boolean isVertex1InputCorrect() {
        String exprText = vertex1.get();
        return (VERTEX_INPUT_ALLOWED_SYMBOLS.matcher(exprText).matches()
                && areVertexInputsNotEqual());
    }

    public boolean isVertex2InputCorrect() {
        String exprText = vertex2.get();
        return (VERTEX_INPUT_ALLOWED_SYMBOLS.matcher(exprText).matches()
                && areVertexInputsNotEqual());
    }

    private boolean areVertexInputsNotEqual() {
        return !vertex1.get().equals(vertex2.get());
    }

    public boolean isWeightInputCorrect() {
        String exprText = weight.get();
        return (WEIGHT_INPUT_ALLOWED_SYMBOLS.matcher(exprText).matches());
    }

    public void createGraph() {
        StringBuilder message = new StringBuilder(LogMessages.CREATE_GRAPH_WAS_PRESSED);
        if (edgeList.isEmpty()) {
            message.append(" unsuccessfully. List of edges is empty");
            logger.log(message.toString());
            updateLogs();
            return;
        }

        List<DijkstraGraph.Edge> list = edgeList.stream()
                .map(EdgeViewModel::getEdge)
                .collect(Collectors.toList());

        graph = new DijkstraGraph(list);
        updateVertexList();

        message.append(" successfully.");
        logger.log(message.toString());
        updateLogs();
    }

    private void updateVertexList() {
        vertexList.clear();
        vertexList.addAll(graph.getVertexList());
    }

    public void calculatePath() {
        String toPath = getVertexTo();
        String fromPath = getVertexFrom();
        StringBuilder messageCalculate = new StringBuilder(LogMessages.CALCULATE_WAS_PRESSED);
        if (toPath == null || fromPath == null) {
            messageCalculate.append("unsuccessfully. Check From and To vertexes.");
            logger.log(messageCalculate.toString());
            updateLogs();
            return;
        }

        graph.calculate(fromPath);
        messageCalculate.append("successfully.");
        logger.log(messageCalculate.toString());
        updateLogs();

        resultPath.setValue(graph.getPath(toPath));
    }

    public StringProperty vertex1Property() {
        return vertex1;
    }
    public StringProperty vertex2Property() {
        return vertex2;
    }
    public StringProperty weightProperty() {
        return weight;
    }
    private String getVertexFrom() {
        return vertexFrom.get();
    }
    public StringProperty vertexFromProperty() {
        return vertexFrom;
    }
    private String getVertexTo() {
        return vertexTo.get();
    }
    public StringProperty vertexToProperty() {
        return vertexTo;
    }
    public StringProperty resultPathProperty() {
        return resultPath;
    }
    public SimpleBooleanProperty addingNewEdgeDisabledProperty() {
        return addingNewEdgeDisabled;
    }
    public ObservableList<EdgeViewModel> getEdgeList() {
        return edgeList;
    }
    public ObservableList<String> getVertexList() {
        return vertexList;
    }
    public StringProperty logsProperty() {
        return logs;
    }
    public final String getLogs() {
        return logs.get();
    }
    public final List<String> getLogList() {
        return logger.getLog();
    }

    private void updateLogs() {
        List<String> fullLog = logger.getLog();
        StringBuilder record = new StringBuilder(new String(""));
        for (String log : fullLog) {
            record.append(log).append("\n");
        }
        logs.set(record.toString());
    }

    private void logForAddedEdge() {
        StringBuilder message = new StringBuilder(LogMessages.ADD_EDGE_WAS_PRESSED);
        message.append("[")
                .append(vertex1.get())
                .append(",")
                .append(vertex2.get())
                .append(",")
                .append(weight.get())
                .append("]");
        logger.log(message.toString());
        updateLogs();
    }

    public void onExpressionTextFieldFocusChanged() {
        StringBuilder message = new StringBuilder(LogMessages.EDITING_INPUT);
        message.append("[")
                .append(vertex1.get())
                .append(",")
                .append(vertex2.get())
                .append(",")
                .append(weight.get())
                .append("]. ");

        if (isVertex1InputCorrect()
                && isVertex2InputCorrect()
                && isWeightInputCorrect()) {
            message.append(LogMessages.CORRECT_INPUT);
        } else {
            message.append(LogMessages.INCORRECT_INPUT);
        }

        logger.log(message.toString());
        updateLogs();
    }

    public void onExpressionComboBoxFocusChanged() {
        StringBuilder message = new StringBuilder(LogMessages.EDITING_INPUT);
        message.append("[from ")
                .append(vertexFrom.get())
                .append(" to ")
                .append(vertexTo.get())
                .append("]. ");

        logger.log(message.toString());
        updateLogs();
    }

    final class LogMessages {
        public static final String ADD_EDGE_WAS_PRESSED = "Edge was added: ";
        public static final String CREATE_GRAPH_WAS_PRESSED = "Graph was created";
        public static final String CALCULATE_WAS_PRESSED = "Calculation completed ";
        public static final String EDITING_INPUT = "Updated input: ";
        public static final String INCORRECT_INPUT = "Incorrect input. ";
        public static final String CORRECT_INPUT = "Correct input. ";

        private LogMessages() { }
    }
}
