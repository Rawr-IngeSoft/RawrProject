package com.example.david.rawr;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.david.rawr.MainActivities.Loading_screen;
import com.example.david.rawr.Tasks.ValidateUser;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
 @SmallTest
/**
 * Created by alfredo on 18/05/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Loading_screen.class)
public class Loading_screenTest extends ActivityUnitTestCase<Loading_screen> {


    private Intent intent;
    private ValidateUser mockedValidateUser;
    private Loading_screen loading_screenActivity;

    public Loading_screenTest() {
        super(Loading_screen.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockedValidateUser = PowerMockito.mock(ValidateUser.class);
        PowerMockito.whenNew(ValidateUser.class).withAnyArguments().thenReturn(mockedValidateUser);
        Loading_screen loading_screen = new Loading_screen();
        intent = new Intent(getInstrumentation()
                .getTargetContext(), Loading_screen.class);
        intent.putExtra("username", "pedro");
        intent.putExtra("password", "p");
        intent.putExtra("serviceType", "logIn");
        startActivity(intent, null, null);


    }

    @SmallTest
    public void testLogIn() throws Exception {

        loading_screenActivity = getActivity();
        ArrayList<String> data = new ArrayList<>();
        PowerMockito.doNothing().when(mockedValidateUser.execute());
        loading_screenActivity.validateFinish(data);

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);

    }
}
