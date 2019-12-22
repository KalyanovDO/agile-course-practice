package ru.unn.agile.dijkstraalgorithm.infrastructure;

import ru.unn.agile.dijkstraalgorithm.viewmodel.ViewModel;
import ru.unn.agile.dijkstraalgorithm.viewmodel.ViewModelTest;

public class ViewModelWithTxtLoggerTests extends ViewModelTest {
    @Override
    public void setUp() {
        TxtLogger realLogger =
            new TxtLogger("./ViewModel_with_TxtLogger_Tests-lab3.log");
        super.setExternalViewModel(new ViewModel(realLogger));
    }
}
