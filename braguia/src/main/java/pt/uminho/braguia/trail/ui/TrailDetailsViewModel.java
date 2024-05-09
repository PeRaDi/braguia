package pt.uminho.braguia.trail.ui;

import androidx.compose.runtime.snapshots.Snapshot;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.trail.data.TrailRepository;
import pt.uminho.braguia.trail.domain.Edge;
import pt.uminho.braguia.trail.domain.Trail;

@HiltViewModel
public class TrailDetailsViewModel extends ViewModel {

    public enum TrailStatus {
        INITIAL,
        STARTED,
        STOPED
    }

    private final MutableLiveData<Long> trailIdData = new MutableLiveData<>();
    private LiveData<Trail> trailData;
    private LiveData<List<PinMedia>> mediasData;
    private LiveData<List<Pin>> pinsData;
    private MutableLiveData<TrailStatus> statusLiveData = new MutableLiveData<>(TrailStatus.INITIAL);

    // Called cold pins because it is not LiveData
    private List<Pin> coldPins = new ArrayList<>();

    @Inject
    public TrailDetailsViewModel(TrailRepository repository) {
        trailData = Transformations.switchMap(trailIdData, id -> {
            if (id == null) {
                return new LiveData<Trail>() {
                };
            }
            return repository.getTrailById(id);
        });

        pinsData = Transformations.switchMap(trailData, trail -> {
            List<Pin> pins = Optional.ofNullable(trail.getEdges())
                    .map(edges -> edges.stream()
                            .flatMap(e -> e.getPins().stream())
                            .distinct()
                            .collect(Collectors.toList())
                    ).orElseGet(() -> new ArrayList<>());
            coldPins = Collections.synchronizedList(pins);
            return new MediatorLiveData<>(coldPins);
        });

        mediasData = Transformations.switchMap(pinsData, pinsList -> {
            List<PinMedia> mediaList = Optional.ofNullable(pinsList)
                    .map(pins -> pins.stream()
                            .flatMap(pin -> pin.getPinMedia().stream())
                            .distinct()
                            .collect(Collectors.toList())
                    ).orElseGet(() -> new ArrayList<>());
            return new MediatorLiveData<>(mediaList);
        });
    }

    public LiveData<Trail> getTrail(Long id) {
        trailIdData.setValue(id);
        return trailData;
    }

    public LiveData<Trail> getTrail() {
        return trailData;
    }


    public LiveData<List<PinMedia>> getMedias() {
        return this.mediasData;
    }

    public LiveData<List<Pin>> getPins() {
        return this.pinsData;
    }

    public List<Pin> getColdPins() {
        return coldPins;
    }

    public LiveData<TrailStatus> getStatus() {
        return statusLiveData;
    }

    public void switchRouteStatus() {
        if (statusLiveData.getValue().equals(TrailStatus.INITIAL) || statusLiveData.getValue().equals(TrailStatus.STOPED)) {
            startRoute();
        } else {
            stopRoute();
        }
    }

    public void startRoute() {
        statusLiveData.setValue(TrailStatus.STARTED);
    }

    public void stopRoute() {
        statusLiveData.setValue(TrailStatus.STOPED);
    }
}