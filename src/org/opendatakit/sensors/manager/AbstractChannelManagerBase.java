/*
 * Copyright (C) 2013 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.sensors.manager;

import java.util.List;

import org.opendatakit.sensors.CommunicationChannelType;
import org.opendatakit.sensors.ODKSensor;
import org.opendatakit.sensors.SensorStateMachine;
import org.opendatakit.sensors.SensorStatus;
import org.opendatakit.sensors.SensorWorkStatus;

import android.content.Context;

/**
 * 
 * @author wbrunette@gmail.com
 * @author rohitchaudhri@gmail.com
 * 
 */
public abstract class AbstractChannelManagerBase implements ChannelManager {
	
	protected Context mContext;
	protected ODKSensorManager mSensorManager;
	protected CommunicationChannelType mChannelType;
	
	public AbstractChannelManagerBase(Context context, CommunicationChannelType channelType) {
		this.mContext = context;
		this.mChannelType = channelType;
	}
	
	public abstract void initializeSensors();
	
	public abstract List<DiscoverableDevice> getDiscoverableSensor();
	
	public void setSensorManager(ODKSensorManager sensorManager) {
		this.mSensorManager = sensorManager;
	}
	
	public SensorStateMachine getSensorStatus(String id) {
		DetailedSensorState state = mSensorManager.querySensorState(id);
		SensorStateMachine sensorState = new SensorStateMachine();
		if (state == DetailedSensorState.CONNECTED) {
			sensorState.status = SensorStatus.CONNECTED;
		} else if (state == DetailedSensorState.DISCONNECTED) {
			sensorState.status = SensorStatus.DISCONNECTED;
			sensorState.workStatus = SensorWorkStatus.BUSY;
		} else if (state == DetailedSensorState.READY){
			sensorState.status = SensorStatus.READY;
		} else if (state == DetailedSensorState.SLEEP) {
			sensorState.status = SensorStatus.SLEEP;
		}
		return sensorState;
	}
	
	public List<ODKSensor> getRegisteredSensorList(CommunicationChannelType commType) {
		return mSensorManager.getRegisteredSensors(commType);
	}
	
	public CommunicationChannelType getCommChannelType() {
		return mChannelType;
	}
}