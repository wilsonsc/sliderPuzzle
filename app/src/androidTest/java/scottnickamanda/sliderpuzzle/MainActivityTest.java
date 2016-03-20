package scottnickamanda.sliderpuzzle;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Tests MainActivity
 *
 * Created by Amanda Buhr on 3/19/16.
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {

        super.setUp();
        activity = getActivity();
    }

    public void testActivityExists() {
        assertNotNull(activity);
    }
}
