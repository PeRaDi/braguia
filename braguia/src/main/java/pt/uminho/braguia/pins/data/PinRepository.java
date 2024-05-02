package pt.uminho.braguia.pins.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pt.uminho.braguia.network.CacheControl;
import pt.uminho.braguia.pins.data.db.PinEntity;
import pt.uminho.braguia.pins.domain.Pin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinRepository {

    private final PinLocalDatasource localDatasource;
    private final PinRemoteDatasource remoteDatasource;
    private final CacheControl cacheControl;

    private MediatorLiveData<List<Pin>> pins = new MediatorLiveData<>();

    public PinRepository(PinLocalDatasource localDatasource,
                         PinRemoteDatasource remoteDatasource,
                         CacheControl cacheControl) {
        this.localDatasource = localDatasource;
        this.remoteDatasource = remoteDatasource;
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

    private class InsertTask extends AsyncTask<Pin, Void, Void> {
        @Override
        protected Void doInBackground(Pin... pins) {
            localDatasource.deleteAll();
            localDatasource.insert(Arrays.stream(pins).map(PinEntity::fromDomain).toArray(PinEntity[]::new));
            return null;
        }
    }

}
