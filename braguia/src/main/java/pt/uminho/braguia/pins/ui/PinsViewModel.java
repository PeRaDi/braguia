package pt.uminho.braguia.pins.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.pins.data.PinRepository;
import pt.uminho.braguia.pins.domain.Pin;

@HiltViewModel
public class PinsViewModel extends ViewModel {
    private LiveData<List<Pin>> pins;

    @Inject
    public PinsViewModel(PinRepository repository) {
        pins = repository.getPins();
    }

    public LiveData<List<Pin>> getPins() {
        return pins;
    }
}