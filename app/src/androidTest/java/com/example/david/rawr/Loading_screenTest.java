package com.example.david.rawr;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.example.david.rawr.MainActivities.Loading_screen;
import com.example.david.rawr.Tasks.ValidateUser;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;


/**
 * Created by alfredo on 18/05/15.
 */



/*@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)*/
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Loading_screen.class)
public class Loading_screenTest extends ActivityUnitTestCase<Loading_screen>{


    private Intent intent;
    private ValidateUser mockedValidateUser;
    private Loading_screen loading_screenActivity;

    public Loading_screenTest() {
        super(Loading_screen.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //mockedValidateUser = PowerMockito.mock(ValidateUser.class);
        //PowerMockito.whenNew(ValidateUser.class).withAnyArguments().thenReturn(mockedValidateUser);
        //loading_screenActivity = getActivity();
        //loading_screenActivity = new Loading_screen();

        intent = new Intent(getInstrumentation().getTargetContext(), Loading_screen.class);
        intent.putExtra("username", "pedro");
        intent.putExtra("password", "p");
        intent.putExtra("serviceType", "logIn");
        //mockedValidateUser = Mockito.mock(ValidateUser.class);

        //Mockito.when(mockedValidateUser.execute()).;

        /*intent = new Intent(getInstrumentation()
                .getTargetContext(), Loading_screen.class);

        startActivity(intent, null, null);
        */

    }

    @Test
    public void testLogIn() throws Exception {
        /*startActivity(intent, null, null);
        loading_screenActivity = getActivity();
        assertNotNull(loading_screenActivity);
*/
        ArrayList<String> data = new ArrayList<>();
        data.add("0");
        data.add("pedro");
        data.add("p");
        loading_screenActivity.validateFinish(data);

        //final Intent launchIntent = getStartedActivityIntent();
        assertEquals("pedddro", data.get(1));
        //assertNull("Intent was null", launchIntent);
        //assertTrue(isFinishCalled());
       /* loading_screenActivity = getActivity();
        ArrayList<String> data = new ArrayList<>();
        PowerMockito.doNothing().when(mockedValidateUser.execute());
        loading_screenActivity.validateFinish(data);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
        /*/
    }

    @Test
    public void test2() throws Exception {
        assertTrue("no paso", false);
    }

    @Test
    public void test3() throws Exception {
        assertTrue(true);
    }
}
