package csumissu.fakewechat.v2.model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import csumissu.fakewechat.v2.bean.Status;
import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.dagger.Local;
import csumissu.fakewechat.v2.dagger.Remote;
import csumissu.fakewechat.v2.model.local.WeiboLocalDataSource;
import rx.Observable;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Singleton
public class WeiboRepository implements WeiboDataSource {

    private final WeiboDataSource mRemoteDataSource;
    private final WeiboLocalDataSource mLocalDataSource;

    @Inject
    WeiboRepository(@Remote WeiboDataSource remoteDataSource,
                    @Local WeiboDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = (WeiboLocalDataSource) localDataSource;
    }

    @Override
    public Observable<User> getOwner() {
        Observable<User> disk = mLocalDataSource.getOwner();
        Observable<User> network = mRemoteDataSource.getOwner()
                .doOnNext(mLocalDataSource::saveOwner);
        return Observable.concat(disk, network).first();
    }

    @Override
    public Observable<List<User>> getFriends() {
        Observable<List<User>> disk = mLocalDataSource.getFriends();
        Observable<List<User>> network = mRemoteDataSource.getFriends()
                .doOnNext(mLocalDataSource::saveFriends);
        return Observable.concat(disk, network).first();
    }

    @Override
    public Observable<List<Status>> getAllStatuses() {
        Observable<List<Status>> disk = mLocalDataSource.getAllStatuses();
        Observable<List<Status>> network = mRemoteDataSource.getAllStatuses()
                .doOnNext(mLocalDataSource::saveStatuses);
        return Observable.concat(disk, network).first();
    }

    @Override
    public Observable<List<Status>> getStatuses(int pageNum) {
        return mLocalDataSource.getStatuses(pageNum);
    }
}
