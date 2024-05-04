package pt.uminho.braguia.pins.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.pins.data.PinRepository;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;

@HiltViewModel
public class PinMediasViewModel extends ViewModel {
    private LiveData<List<PinMedia>> pinsMedia;

    @Inject
    public PinMediasViewModel(PinRepository repository) {
        pinsMedia = repository.getPinsMedia();
    }

    public LiveData<List<PinMedia>> getPinsMedia() {
        return pinsMedia;
    }
}