package pt.uminho.braguia.pins.data;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import pt.uminho.braguia.network.CacheControl;
import pt.uminho.braguia.pins.data.db.PinEntity;
import pt.uminho.braguia.pins.data.db.PinMediaEntity;
import pt.uminho.braguia.pins.data.db.RelPinEntity;
import pt.uminho.braguia.pins.domain.Pin;
import pt.uminho.braguia.pins.domain.PinMedia;
import pt.uminho.braguia.pins.domain.RelPin;
import pt.uminho.braguia.preference.SharedPreferencesModule;
import pt.uminho.braguia.trail.domain.Trail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinRepository {

    private final PinLocalDatasource localDatasource;
    private final PinRemoteDatasource remoteDatasource;
    private final RelPinLocalDatasource relPinLocalDatasource;
    private final PinMediaLocalDatasource mediaLocalDatasource;
    private final CacheControl cacheControl;

    private MediatorLiveData<List<Pin>> pins = new MediatorLiveData<>();
    private MediatorLiveData<List<RelPin>> relPins = new MediatorLiveData<>();
    private MediatorLiveData<List<PinMedia>> pinsMedia = new MediatorLiveData<>();

    private MediatorLiveData<Pin> pin = new MediatorLiveData<>();

    public PinRepository(PinLocalDatasource localDatasource,
                         PinRemoteDatasource remoteDatasource,
                            RelPinLocalDatasource relPinLocalDatasource,
                         PinMediaLocalDatasource mediaLocalDatasource,
                         CacheControl cacheControl) {
        this.localDatasource = localDatasource;
        this.remoteDatasource = remoteDatasource;
        this.relPinLocalDatasource = relPinLocalDatasource;
        this.mediaLocalDatasource = mediaLocalDatasource;
        this.cacheControl = cacheControl;
    }

    public LiveData<List<Pin>> getPins() {
        return getPins(false);
    }

    public LiveData<List<Pin>> getPins(boolean forceRefresh) {
        pins.addSource(localDatasource.getPins(), localPins -> {
            localPins = localPins != null ? localPins : new ArrayList<>();
            pins.postValue(localPins.stream().map(PinEntity::toDomain).collect(Collectors.toList()));
            if (localPins.isEmpty() || this.cacheControl.isExpired() || forceRefresh) {
                fetchRemoteDatasource(forceRefresh);
            }
        });

        return pins;
    }

    public LiveData<Pin> getPinById(Long id) {
        return getPinById(id, false);
    }

    public LiveData<Pin> getPinById(Long id, boolean forceRefresh) {
        fetchRemoteDatasource(id, forceRefresh);
        return pin;
    }

    public LiveData<List<PinMedia>> getPinsMedia() {
        return getPinsMedia(false);
    }

    public LiveData<List<PinMedia>> getPinsMedia(boolean forceRefresh) {
        pinsMedia.addSource(mediaLocalDatasource.getMedia(), localPinsMedia -> {
            localPinsMedia = localPinsMedia != null ? localPinsMedia : new ArrayList<>();
            pinsMedia.postValue(localPinsMedia.stream().map(PinMediaEntity::toDomain).collect(Collectors.toList()));
            if (localPinsMedia.isEmpty() || this.cacheControl.isExpired() || forceRefresh) {
                fetchRemoteDatasource(forceRefresh);
            }
        });
        return pinsMedia;
    }

    public LiveData<List<RelPin>> getRelPins() {
        return getRelPins(false);
    }

    public LiveData<List<RelPin>> getRelPins(boolean forceRefresh) {
        relPins.addSource(relPinLocalDatasource.getRelPins(), localRelPins -> {
            localRelPins = localRelPins != null ? localRelPins : new ArrayList<>();
            relPins.postValue(localRelPins.stream().map(RelPinEntity::toDomain).collect(Collectors.toList()));
            if (localRelPins.isEmpty() || this.cacheControl.isExpired() || forceRefresh) {
                fetchRemoteDatasource(forceRefresh);
            }
        });
        return relPins;
    }

    private void updateCache(@NonNull List<Pin> pinList, boolean forceRefresh) {
        PinRepository.InsertTask task = new PinRepository.InsertTask();
        task.execute(pinList.toArray(new Pin[0]));
        cacheControl.refresh();
        if (forceRefresh) {
            pins.postValue(pinList);
        }
    }

    private void fetchRemoteDatasource(boolean forceRefresh) {
        remoteDatasource.getPins().enqueue(new Callback<List<Pin>>() {
            @Override
            public void onResponse(Call<List<Pin>> call, Response<List<Pin>> response) {
                List<Pin> pinList = response.isSuccessful() ? response.body() : new ArrayList<>();
                updateCache(pinList, forceRefresh);
            }

            @Override
            public void onFailure(Call<List<Pin>> call, Throwable t) {
                updateCache(new ArrayList<>(), forceRefresh);
            }
        });
    }

    private void fetchRemoteDatasource(Long id, boolean forceRefresh) {
        if(id == null) {
            return;
        }
        remoteDatasource.getPin(id.toString()).enqueue(new Callback<Pin>() {
            @Override
            public void onResponse(Call<Pin> call, Response<Pin> response) {
                //List<Trail> trailList = response.isSuccessful() ? Arrays.asList(response.body()) : new ArrayList<>();
                //updateCache(trailList, forceRefresh);
                pin.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Pin> call, Throwable t) {
                pin.setValue(null);
            }
        });
    }

    private class InsertTask extends AsyncTask<Pin, Void, Void> {
        @Override
        protected Void doInBackground(Pin... pins) {
            localDatasource.deleteAll();
            relPinLocalDatasource.deleteAll();
            mediaLocalDatasource.deleteAll();
            for (Pin pin : pins) {
                localDatasource.insert(PinEntity.fromDomain(pin));
                for (PinMedia media : pin.getPinMedia()) {
                    mediaLocalDatasource.insert(PinMediaEntity.fromDomain(media));
                }
                for (RelPin relPin : pin.getRelPins()) {
                    relPinLocalDatasource.insert(RelPinEntity.fromDomain(relPin));
                }
            }
            return null;
        }
    }

}
