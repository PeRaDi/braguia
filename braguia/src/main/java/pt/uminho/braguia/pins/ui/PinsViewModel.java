package pt.uminho.braguia.pins.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.pins.data.PinRepository;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.pins.domain.RelPin;

@HiltViewModel
public class PinsViewModel extends ViewModel {
    private LiveData<List<Pin>> pins;
    private LiveData<List<PinMedia>> pinsMedia;
    private LiveData<List<RelPin>> relPins;

    @Inject
    public PinsViewModel(PinRepository repository) {
        pins = repository.getPins();
        pinsMedia = repository.getPinsMedia();
        relPins = repository.getRelPins();
    }

    public LiveData<List<Pin>> getPins() {
        return pins;
    }

    public LiveData<List<PinMedia>> getPinsMedia() {
        return pinsMedia;
    }

    public LiveData<List<RelPin>> getRelPins() {
        return relPins;
    }
}