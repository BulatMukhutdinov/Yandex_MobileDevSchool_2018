package ru.mukhutdinov.bulat.yandextestapp;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import ru.mukhutdinov.bulat.yandextestapp.data.network.ApiImpl;
import ru.mukhutdinov.bulat.yandextestapp.presentation.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ru.mukhutdinov.bulat.yandextestapp.Utils.getStringFromFile;
import static ru.mukhutdinov.bulat.yandextestapp.Utils.waitFor;
import static ru.mukhutdinov.bulat.yandextestapp.Utils.withListSize;

@RunWith(AndroidJUnit4.class)
public class ListFragmentInstrumentedTest extends InstrumentationTestCase {

    private final static int RESPONSE_PHOTOS_AMOUNT = 3;
    private final static int LOADING_ITEM = 1;
    private final static String ERROR_TEXT_400 = "[ERROR 400] \"page\" is out of valid range.";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        server = new MockWebServer();
        server.start();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        ApiImpl.init(server.url("/").toString());
    }

    @Test
    public void onOkResponse_AllPhotosAreDisplayed() throws Exception {
        String fileName = "photos_200_ok_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.photos)).check(matches(withListSize(RESPONSE_PHOTOS_AMOUNT + LOADING_ITEM)));
    }

    @Test
    public void onOkResponseOnClick_TitlesAreDisplayedCorrectly() throws Exception {
        String fileName = "photos_200_ok_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
        onView(withId(R.id.photos))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("poppy, flower, klatschmohn")).check(matches(isDisplayed()));
    }

    @Test
    public void onErrorResponse_ErrorTextAndRetryButtonAreDisplayed() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(ERROR_TEXT_400));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(withText(ERROR_TEXT_400)).check(matches(isDisplayed()));
        onView(withText(R.string.retry)).check(matches(isDisplayed()));
    }

    /*
     * Caution! Very long running test to check time out. Intentionally commented.
     * Uncomment and run separately to check timeout behavior
     */
    //@Test
    public void onRequestTimeOut_ErrorTextAndRetryButtonAreDisplayed() throws Exception {
        String fileName = "photos_200_ok_response.json";

        server.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);

        onView(isRoot()).perform(waitFor(TimeUnit.SECONDS.toMillis(31)));

        onView(withText(R.string.network_error)).check(matches(isDisplayed()));
        onView(withText(R.string.retry)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        server.shutdown();
    }
}