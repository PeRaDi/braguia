package pt.uminho.braguia.trail.ui;

import androidx.compose.runtime.snapshots.Snapshot;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
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

    private final MutableLiveData<Long> trailIdData = new MutableLiveData<>();
    private LiveData<Trail> trailData;
    private LiveData<List<Edge>> edgesData;
    private LiveData<List<PinMedia>> mediasData;
    private LiveData<List<Pin>> pinsData;

    @Inject
    public TrailDetailsViewModel(TrailRepository repository) {
        trailData = Transformations.switchMap(trailIdData, id -> {
            if (id == null) {
                return new LiveData<Trail>() {
                };
            }
            return repository.getTrailById(id);
        });

        edgesData = Transformations.switchMap(trailData, trail -> {
            List<Edge> edges = trail == null ? new ArrayList<>() : trail.getEdges();
            return new MutableLiveData<>(edges == null ? new ArrayList<>() : edges);
        });

        pinsData = Transformations.switchMap(edgesData, edgesList -> {
            List<Pin> pins = Optional.ofNullable(edgesList)
                    .map(edges -> edges.stream()
                            .flatMap(e -> e.getPins().stream())
                            .distinct()
                            .collect(Collectors.toList())
                    ).orElseGet(() -> new ArrayList<>());
            return new MediatorLiveData<>(pins);
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


    public LiveData<List<Edge>> getEdges() {
        return edgesData;
    }

    public LiveData<List<PinMedia>> getMedias() {
        return this.mediasData;
    }

    public LiveData<List<Pin>> getPins() {
        return this.pinsData;
    }
}