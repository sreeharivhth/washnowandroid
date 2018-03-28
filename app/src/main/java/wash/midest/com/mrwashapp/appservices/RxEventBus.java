package wash.midest.com.mrwashapp.appservices;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Sreehari.KV on 3/27/2018.
 */

public class RxEventBus {

    public RxEventBus(){}

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
