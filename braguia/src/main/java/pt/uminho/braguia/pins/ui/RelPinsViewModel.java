package pt.uminho.braguia.pins.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.pins.data.PinRepository;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.RelPin;

@HiltViewModel
public class RelPinsViewModel extends ViewModel {
    private LiveData<List<RelPin>> relPins;

    @Inject
    public RelPinsViewModel(PinRepository repository) {
        relPins = repository.getRelPins();
    }

    public LiveData<List<RelPin>> getRelPins() {
        return relPins;
    }
}