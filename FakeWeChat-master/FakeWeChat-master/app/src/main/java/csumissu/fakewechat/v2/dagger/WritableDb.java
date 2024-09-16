package csumissu.fakewechat.v2.dagger;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sunyaxi
 * @date 2016/11/17.
 */
@Qualifier
@Retention(RUNTIME)
public @interface WritableDb {
}
