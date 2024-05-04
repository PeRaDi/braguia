package pt.uminho.braguia.trail.ui;

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

        mediasData = Transformations.switchMap(edgesData, list -> {
            List<PinMedia> mediaList = Optional.ofNullable(list)
                    .map(edges -> edges.stream()
                            .flatMap(e -> e.getPins().stream())
                            .flatMap(pin -> pin.getPinMedia().stream())
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
}