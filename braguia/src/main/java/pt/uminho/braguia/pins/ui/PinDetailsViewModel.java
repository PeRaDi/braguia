package pt.uminho.braguia.pins.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.pins.data.PinRepository;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;

@HiltViewModel
public class PinDetailsViewModel extends ViewModel {

    private final MutableLiveData<Long> pinIdData = new MutableLiveData<>();
    private LiveData<Pin> pinData;
    private LiveData<List<PinMedia>> mediasData;

    @Inject
    public PinDetailsViewModel(PinRepository repository) {
        pinData = Transformations.switchMap(pinIdData, id -> {
            if (id == null) {
                return new LiveData<Pin>() {
                };
            }
            return repository.getPinById(id);
        });

        mediasData = Transformations.switchMap(pinData, pin -> {
            List<PinMedia> mediaList = Optional.ofNullable(pin)
                    .map(p -> new ArrayList<>(p.getPinMedia())
                    ).orElseGet(ArrayList::new);
            return new MediatorLiveData<>(mediaList);
        });
    }

    public LiveData<Pin> getPin(Long id) {
        pinIdData.setValue(id);
        return pinData;
    }

    public LiveData<Pin> getPin() {
        return pinData;
    }

    public LiveData<List<PinMedia>> getMedias() {
        return this.mediasData;
    }
}