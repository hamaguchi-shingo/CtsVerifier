/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.hardware.cts;

import android.hardware.Sensor;
import android.hardware.cts.helpers.SensorTestStateNotSupportedException;
import android.hardware.cts.helpers.TestSensorEnvironment;
import android.hardware.cts.helpers.reporting.ISensorTestNode;
import android.hardware.cts.helpers.sensoroperations.SensorOperation;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Test Case class that handles gracefully sensors that are not available in the device.
 */
public abstract class SensorTestCase extends AndroidTestCase {
    // TODO: consolidate all log tags
    protected static final String LOG_TAG = "TestRunner";

    /**
     * By default tests need to run in a {@link TestSensorEnvironment} that assumes each sensor is
     * running with a load of several listeners, requesting data at different rates.
     *
     * In a better world the component acting as builder of {@link SensorOperation} would compute
     * this value based on the tests composed.
     *
     * Ideally, each {@link Sensor} object would expose this information to clients.
     */
    private volatile boolean mEmulateSensorUnderLoad = true;

    /**
     * By default the test class is the root of the test hierarchy.
     */
    private volatile ISensorTestNode mCurrentTestNode = new TestClassNode(getClass());

    protected SensorTestCase() {}

    @Override
    public void runBare() throws Throwable {
        try {
            super.runBare();
        } catch (SensorTestStateNotSupportedException e) {
            // the sensor state is not supported in the device, log a warning and skip the test
            Log.w(LOG_TAG, e.getMessage());
        }
    }

    public void setEmulateSensorUnderLoad(boolean value) {
        mEmulateSensorUnderLoad = value;
    }

    protected boolean shouldEmulateSensorUnderLoad() {
        return mEmulateSensorUnderLoad;
    }

    public void setCurrentTestNode(ISensorTestNode value) {
        mCurrentTestNode = value;
    }

    protected ISensorTestNode getCurrentTestNode() {
        return mCurrentTestNode;
    }

    private class TestClassNode implements ISensorTestNode {
        private final Class<?> mTestClass;

        public TestClassNode(Class<?> testClass) {
            mTestClass = testClass;
        }

        @Override
        public String getName() {
            return mTestClass.getSimpleName();
        }
    }
}
