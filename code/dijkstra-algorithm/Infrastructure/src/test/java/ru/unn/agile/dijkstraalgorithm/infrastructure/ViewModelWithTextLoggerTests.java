package ru.unn.agile.dijkstraalgorithm.infrastructure;

import ru.unn.agile.dijkstraalgorithm.viewmodel.ViewModel;
import ru.unn.agile.dijkstraalgorithm.viewmodel.ViewModelTest;

public class ViewModelWithTextLoggerTests extends ViewModelTest {
    @Override
    public void setUp() {
        TextLogger realLogger =
            new TextLogger("./ViewModel_with_TxtLogger_Tests-lab3.log");
        super.setExternalViewModel(new ViewModel(realLogger));
    }
}
